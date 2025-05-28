package org.example.project

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.API.AccountsDepartmentAPI
import org.example.project.API.CreateAccountDepartmentResponse
import org.example.project.API.CreateBusinessProcessRequest
import org.example.project.API.CreateBusinessProcessResponse
import org.example.project.API.CreateInviteCodeResponse
import org.example.project.API.CreateUserResponse
import org.example.project.API.EnterDepartmentTokenResponse
import org.example.project.API.GetDepartmentListResponse
import org.example.project.API.GetPermissionResponse
import org.example.project.API.GetProfileInfo
import org.example.project.API.GetProfileInfoResponse
import org.example.project.API.GetUsersListResponse
import org.example.project.API.LoginUserResponse
import org.example.project.API.PermissionAPI
import org.example.project.API.SendHierarchyRequest
import org.example.project.API.SendHierarchyResponse
import org.example.project.API.UpdateProfileInfoRequest
import org.example.project.API.UserAPI
import org.example.project.model.AccountsDepartment
import org.example.project.model.CreateDepartmentResponse
import org.example.project.model.IdNumDepartment
import org.example.project.model.Permission
import org.example.project.model.ProfileInfo
import org.example.project.model.User
import org.example.project.tables.AccountsDepartmentTable
import org.example.project.tables.AccountsEmployeeTable
import org.example.project.tables.EmployeePermissionTable
import org.example.project.tables.HierarchyTable
import org.example.project.tables.PermissionTable
import org.example.project.tables.UserTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.net.URLDecoder
import java.time.Instant.now
import org.example.project.API.CreateTaskRequest
import org.example.project.API.CreateTaskResponse
import org.example.project.API.DeleteAccountantRequest
import org.example.project.API.DeleteAccountantResponse
import org.example.project.API.DeleteBPRequest
import org.example.project.API.DeleteBPResponse
import org.example.project.API.DeleteBPTaskRequest
import org.example.project.API.DeleteBPTaskResponse
import org.example.project.API.DeleteTaskRequest
import org.example.project.API.DeleteTaskResponse
import org.example.project.API.GetBusinessProcessResponse
import org.example.project.API.GetHierarchyResponse
import org.example.project.API.GetMessageListRequest
import org.example.project.API.GetMessageListResponse
import org.example.project.API.GetTaskListResponse
import org.example.project.API.LineAPI
import org.example.project.API.PostRectangleAPI
import org.example.project.API.RunBusinessProcessRequest
import org.example.project.API.RunBusinessProcessResponse
import org.example.project.API.SendMessageRequest
import org.example.project.API.UpdateBusinessProcessRequest
import org.example.project.API.UpdateBusinessProcessResponse
import org.example.project.API.UpdateTaskRequest
import org.example.project.API.UpdateTaskResponse
import org.example.project.model.BPTask
import org.example.project.model.BusinessProcess
import org.example.project.model.Bytes
import org.example.project.model.FullTask
import org.example.project.model.MessageWithUser
import org.example.project.model.Task
import org.example.project.model.TaskWithId
import org.example.project.tables.BusinessProcessCreatedTaskTable
import org.example.project.tables.BusinessProcessTable
import org.example.project.tables.BusinessProcessTaskTable
import org.example.project.tables.EmployeeTaskTable
import org.example.project.tables.TaskMessageTable
import org.example.project.tables.TaskTable
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.or
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale


fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json() // Использует kotlinx.serialization
    }
    configureSecurity()
    configureDatabase()
    startBackgroundTask()
    routing {

        get("/HelloWorld/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/get-all-users/") {

            val users = transaction {
                UserTable.selectAll().map {
                    it[UserTable.login]
                    it[UserTable.password]
                }
            }
            call.respondText("Users: $users")
        }
        post("/create-user/") {
            val user = call.receive<UserAPI>() // Автоматическая десериализация JSON


            if(!userExist(user)){
                transaction {
                    UserTable.insert {
                        it[login] = user.login
                        it[password] = user.password
                    }
                }
                call.respond(CreateUserResponse("200","Успешно выполнен запрос", user))
            }
            call.respond(CreateUserResponse("401","Токен не прошёл валидацию",null))
        }
        post("/get-user/"){
            val user = call.receive<UserAPI>()

            if(userPasswordCheck(user)){
                val token = generateToken(user.login)
                call.respond(LoginUserResponse("200","Успешно выполнен запрос",token))
            }else{
                call.respond(LoginUserResponse("401","Ошибка, неверный пароль","null"))
            }
        }
        authenticate("auth-jwt") {
            post("/create-accounts-department/") {


                val principal = call.principal<JWTPrincipal>()
                val login = principal?.payload?.getClaim("login")?.asString()
                if(principal!=null) {
                    val createDepartmentResponse = call.receive<CreateDepartmentResponse>()
                    val accountsDepartment = createDepartmentResponse.accountsDepartment
                    //id будет null, так его при создании у пользователя нет
                    val user = createDepartmentResponse.user

                    var inserted = false

                    transaction {
                        val nowTime = now() // получить текущее время
                        if(AccountsDepartmentTable.selectAll().where{
                                (AccountsDepartmentTable.accountsName eq accountsDepartment.name).and(
                                    AccountsDepartmentTable.authorLogin eq accountsDepartment.authorLogin
                                )
                            }.count()>0
                            ){
                            inserted = false
                        }else {

                            AccountsDepartmentTable.insert {
                                it[accountsName] = accountsDepartment.name
                                it[createDate] = nowTime
                                it[authorLogin] = user.login
                                it[employeesNumber] = 1
                            }//создать бухгалтерию
                            val queryUser = UserTable.selectAll().where {
                                UserTable.login eq user.login
                            }.map { row ->
                                User(
                                    id = row[UserTable.id],
                                    login = row[UserTable.login],
                                    password = row[UserTable.password]
                                )
                            }//получить id автора
                            val queryDepartment = AccountsDepartmentTable.selectAll().where {
                                (AccountsDepartmentTable.accountsName eq accountsDepartment.name).and(
                                    AccountsDepartmentTable.createDate eq nowTime
                                ).and(
                                    AccountsDepartmentTable.authorLogin eq accountsDepartment.authorLogin
                                )
                            }.map { row ->
                                AccountsDepartment(
                                    id = row[AccountsDepartmentTable.id],
                                    name = row[AccountsDepartmentTable.accountsName],
                                    createDate = row[AccountsDepartmentTable.createDate].toString(),
                                    authorLogin = row[AccountsDepartmentTable.authorLogin],
                                    employeesNumber = row[AccountsDepartmentTable.employeesNumber]
                                )
                            }//получить id бухгалтерии
                            val uId: Int = queryUser[0].id!!
                            val accountsDepartmentId: Int = queryDepartment[0].id!!
                            val employeeIdAuthor = AccountsEmployeeTable.insertAndGetId {
                                it[departmentId] = accountsDepartmentId
                                it[userId] = uId
                                it[dateOfEmployment] = nowTime
                            }
                            val permissionList = PermissionTable.selectAll().map{
                                row -> Permission(
                                    row[PermissionTable.permissionId],
                                    row[PermissionTable.permissionName],
                                    row[PermissionTable.description]
                                )
                            }
                            for(permission in permissionList){
                                EmployeePermissionTable.insert {
                                    it[permissionId] = permission.id
                                    it[employeeId] = employeeIdAuthor.value
                                }
                            }
                            inserted = true
                        }
                        //создать бухгалтера в бухгалтерии
                    }
                    if(inserted) {
                        call.respond(
                            CreateAccountDepartmentResponse(
                                "200",
                                "Успешно создана бухгалтерия"
                            )
                        )
                    }else{
                        call.respond(
                            CreateAccountDepartmentResponse(
                                "401",
                                "Бухгалтерия не была создана"
                            )
                        )
                    }
                }
            }
            get("/get-accounts-departments/"){


                val principal = call.principal<JWTPrincipal>()
                val login = principal?.payload?.getClaim("login")?.asString()
                if(principal!=null) {
                    val userJSON:String? = call.request.queryParameters["user"]
                    val userLogin = userJSON?.substringAfter("login=")
                        ?.substringBefore(",")?.trim()

                    // что делать, если одинаковые названия атрибутов у User и Department
                    val result = transaction {
                        val query = UserTable.join(AccountsEmployeeTable,JoinType.INNER){
                            (UserTable.id eq AccountsEmployeeTable.userId).and(
                                UserTable.login eq userLogin.toString()
                            )
                        }.join(AccountsDepartmentTable,JoinType.INNER){
                            AccountsDepartmentTable.id eq AccountsEmployeeTable.departmentId
                        }
                        query.selectAll().map {
                                row ->
                                    val dateStr = row[AccountsDepartmentTable.createDate].toString()
                                    AccountsDepartmentAPI(
                                        name = row[AccountsDepartmentTable.accountsName],
                                        createDate = dateStr.substringBefore("T")+"\n"+
                                                dateStr.substringAfter("T").substringBefore("."),
                                        authorLogin = row[AccountsDepartmentTable.authorLogin],
                                        employeesNumber = row[AccountsDepartmentTable.employeesNumber]
                                    )
                        }
                    }

                    call.respond(GetDepartmentListResponse("200","Запрос выполнен успешно",result))

                }
            }
            get("/get-user-profile-info/"){

                val principal = call.principal<JWTPrincipal>()
                val login = principal?.payload?.getClaim("login")?.asString()
                val userJSON:String? = call.request.queryParameters["user"]
                val userLogin = userJSON?.substringAfter("login=")
                    ?.substringBefore(",")?.trim()
                if(principal!=null) {
                    var result:GetProfileInfo? = null
                    transaction {
                        val resultsql = UserTable.selectAll().where{
                            UserTable.login eq userLogin.toString()
                        }.map{
                            row ->
                                GetProfileInfo(user = UserAPI(
                                    login = row[UserTable.login],
                                    password = ""
                                ),
                                    profileInfo = ProfileInfo(
                                        name = row[UserTable.firstname],
                                        surname = row[UserTable.lastname],
                                        patronymic = row[UserTable.patronymic],
                                        phoneNumber = row[UserTable.phone]
                                    )
                                )
                        }
                        result = resultsql[0]
                    }
                    call.respond(GetProfileInfoResponse("200","Запрос успешно выполнен",result))
                }
            }
            post("/update-user-profile-info/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val updateProfileInfoRequest = call.receive<UpdateProfileInfoRequest>()
                var result:GetProfileInfo? = null
                if(principal!=null && updateProfileInfoRequest.user.login == loginSecret) {
                    transaction {
                        UserTable.update({UserTable.login eq updateProfileInfoRequest.user.login})
                        {
                            updateProfileInfoRequest.profileInfo.name.let{
                                    newName -> it[firstname] = newName.toString()
                            }
                            updateProfileInfoRequest.profileInfo.surname.let {
                                    newSurname -> it[lastname] = newSurname.toString()
                            }
                            updateProfileInfoRequest.profileInfo.patronymic.let{
                                    newPatronymic -> it[patronymic] = newPatronymic.toString()
                            }
                            updateProfileInfoRequest.profileInfo.phoneNumber.let{
                                    newPhoneNumber ->
                                if (newPhoneNumber != null && newPhoneNumber != "") {
                                    it[phone] = newPhoneNumber
                                }
                            }
                            // нет валидации логина
                            if(!userExist(UserAPI(updateProfileInfoRequest.user.login,""))){
                                updateProfileInfoRequest.user.login.let{
                                        newLogin -> it[login] = newLogin
                                }
                            }

                            // нет валидации пароля
                            if(updateProfileInfoRequest.user.password!=""){
                                updateProfileInfoRequest.user.password.let{
                                        newPassword -> it[password] = newPassword
                                }
                            }
                        }
                        UserTable.selectAll().where{
                            UserTable.login eq loginSecret
                        }.map{
                            row ->
                            result = GetProfileInfo(user = UserAPI(
                                login = row[UserTable.login],
                                password = row[UserTable.password]
                            ),
                                profileInfo = ProfileInfo(
                                    name = row[UserTable.firstname],
                                    surname = row[UserTable.lastname],
                                    patronymic = row[UserTable.patronymic],
                                    phoneNumber = row[UserTable.phone]
                                )
                            )
                        }
                    }
                }
                call.respond(GetProfileInfoResponse("200","Запрос успешно выполнен", result))
            }
            get("/get-users-list/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val accountDepartmentJSON:String? = call.request.queryParameters["accountDepartment"]
                val selectedAccountDepartment = parseAccountsDepartmentFromQuery(
                    accountDepartmentJSON.toString()
                )
                var result = emptyList<UserAPI>()
                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq selectedAccountDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq selectedAccountDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }
                if(AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                        UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().count() > 0
                    ){
                        result = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map {
                        row ->
                            UserAPI(row[UserTable.login],"")
                    }

                }else{
                    // выводить ошибку, что юзер не принадлежит бухгалтерии
                }
            }
                call.respond(GetUsersListResponse("200",
                    "Запрос выполненил успешно",result
                ))
            }
            get("/get-permission/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val userPermissionJSON:String? = call.request.queryParameters["user"]
                val accountDepartmentJSON: String? = call.request.queryParameters["accountDepartment"]
                val selectedAccountDepartment = parseAccountsDepartmentFromQuery(
                    accountDepartmentJSON.toString()
                )
                val selectedUser = parseUserFromQuery(
                    userPermissionJSON.toString()
                )
                var result = emptyList<PermissionAPI>()
                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq selectedAccountDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq selectedAccountDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }
                    if(AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                            (UserTable.id eq AccountsEmployeeTable.userId).and(
                                UserTable.login eq loginSecret.toString()).and(
                                AccountsEmployeeTable.departmentId eq departmentIdList[0]
                            )
                        }.selectAll().count() > 0
                    ){
                        val selectedEmployeeId = AccountsEmployeeTable.join(UserTable, JoinType.INNER){
                            (UserTable.id eq AccountsEmployeeTable.userId).and(
                                AccountsEmployeeTable.departmentId eq departmentIdList[0]).and(
                                UserTable.login eq selectedUser.login
                            )
                        }.selectAll().map{
                                row ->
                            row[AccountsEmployeeTable.id]

                        }
                        //println("Выбранный id у пользователя"+selectedEmployeeId[0])
                        val resultSQL = PermissionTable.join(EmployeePermissionTable,JoinType.INNER){
                            (PermissionTable.permissionId eq EmployeePermissionTable.permissionId).and(
                                EmployeePermissionTable.employeeId eq selectedEmployeeId[0].value
                            )
                        }.selectAll().map{
                                row->
                            PermissionAPI(
                                row[PermissionTable.permissionName],
                                row[PermissionTable.description]
                            )
                        }
                        result = resultSQL

                    }else{
                        // если запрашивающий пользователь реален, но не связан с этой бухгалтерией
                    }
                }
                call.respond(GetPermissionResponse("200","Запрос выполнен успешно",result))
            }
            post("/create-invite-code/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val selectedAccountDepartment = call.receive<AccountsDepartment>()
                var inviteToken = "null"
                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq selectedAccountDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq selectedAccountDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }
                    if(AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                            (UserTable.id eq AccountsEmployeeTable.userId).and(
                                UserTable.login eq loginSecret.toString()).and(
                                AccountsEmployeeTable.departmentId eq departmentIdList[0]
                            )
                        }.selectAll().count() > 0
                    ){
                        inviteToken = AccountingSecurity.encode(
                            selectedAccountDepartment.name,
                            selectedAccountDepartment.authorLogin
                        )

                    }else{
                        // нельзя создать код не участнику бухгалтерии
                    }
                }
                if(inviteToken!="null"){
                    call.respond(CreateInviteCodeResponse("200","Успешно выполнен запрос",inviteToken))
                }else{
                    call.respond(CreateInviteCodeResponse("401","Не смог создать код приглашения",inviteToken))
                }
            }
            post("/enter-department/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val enterToken = call.receive<String>()

                val decryptedInfo = AccountingSecurity.decode(enterToken)
                val selectedAccountDepartment = AccountingSecurity.decodeToAccountDepartment(decryptedInfo)

                if(selectedAccountDepartment!= null){
                    var result = false
                    transaction {
                        val departmentIdNumList = AccountsDepartmentTable.selectAll().where {
                            (AccountsDepartmentTable.accountsName eq selectedAccountDepartment.name).and(
                                AccountsDepartmentTable.authorLogin eq selectedAccountDepartment.authorLogin
                            )
                        }.map { row ->
                            IdNumDepartment(
                            row[AccountsDepartmentTable.id],
                            row[AccountsDepartmentTable.employeesNumber])
                        }
                        println("Число сотрудников"+departmentIdNumList[0].employeeNumber)
                        val queryUserId = UserTable.selectAll().where {
                            UserTable.login eq loginSecret.toString()
                        }.map { row ->
                               row[UserTable.id]
                        }
                        if(AccountsEmployeeTable.selectAll().where{
                                (AccountsEmployeeTable.departmentId eq departmentIdNumList[0].id).and(
                                    AccountsEmployeeTable.userId eq (queryUserId[0])
                                )
                            }.count() == 0L
                        ){
                            AccountsEmployeeTable.insert {
                                it[departmentId] = departmentIdNumList[0].id
                                it[userId] = queryUserId[0]
                                it[dateOfEmployment] =  now()
                            }
                            AccountsDepartmentTable.update(where = {
                                AccountsDepartmentTable.id eq departmentIdNumList[0].id
                            },body = {
                                row->
                                    row[employeesNumber] = departmentIdNumList[0].employeeNumber+1
                            })
                        }
                        result = true
                    }
                    if(result==true){
                        call.respond(EnterDepartmentTokenResponse("200","Запрос выполнен успешно"))
                    }else{
                        call.respond(EnterDepartmentTokenResponse("403","Ошибка не сервере"))
                    }
                }else{
                    call.respond(EnterDepartmentTokenResponse("401","Токен не прошёл проверку"))
                }
            }
            post("/send-hierarchy/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val sendHierarchyRequest = call.receive<SendHierarchyRequest>()
                var result:String? = null
                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sendHierarchyRequest.accountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sendHierarchyRequest.accountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }
                    if(AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                            (UserTable.id eq AccountsEmployeeTable.userId).and(
                                UserTable.login eq loginSecret.toString()).and(
                                AccountsEmployeeTable.departmentId eq departmentIdList[0]
                            )
                        }.selectAll().count() > 0
                    ){
                        result="200"
                        if(HierarchyTable.selectAll().where(
                                HierarchyTable.departmentId eq departmentIdList[0]
                            ).count()>0
                            ){
                            HierarchyTable.deleteWhere {
                                HierarchyTable.departmentId eq departmentIdList[0]
                            }
                        }
                        val json = Json { prettyPrint = true }
                        val postRectanglesJson = json.encodeToString(sendHierarchyRequest.postRectangleAPIList)
                        println("JSON:\n$postRectanglesJson")
                        val postRectanglesBinary = postRectanglesJson.toByteArray()
                        println("\nByteArray: ${postRectanglesBinary.contentToString()}")
                        println("Size: ${postRectanglesBinary.size} bytes")
                        val lineListJson = json.encodeToString(sendHierarchyRequest.lineList)
                        println("JSON:\n$lineListJson")
                        val lineListBinary = lineListJson.toByteArray()
                        println("\nByteArray: ${lineListBinary.contentToString()}")
                        println("Size: ${lineListBinary.size} bytes")
                        HierarchyTable.insert {
                            it[departmentId] = departmentIdList[0]
                            it[postRectangles] = postRectanglesBinary
                            it[lines] = lineListBinary
                        }
                    }else{
                        // не участник бухгалтерии
                    }
                }
                for(prapi in sendHierarchyRequest.postRectangleAPIList){
                    prapi.printPRAPI()
                }
                if(result!=null){
                    call.respond(SendHierarchyResponse(result.toString(),"Успешно выполнен запрос"))
                }else{
                    call.respond(SendHierarchyResponse("500","Server error"))
                }
            }
            get("/get-hierarchy/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val accountDepartmentJSON: String? = call.request.queryParameters["accountDepartment"]
                var result:String? = null
                val selectedAccountDepartment = parseAccountsDepartmentFromQuery(
                    accountDepartmentJSON.toString()
                )
                var postRectangleList:List<PostRectangleAPI>? = null
                var lineList:List<LineAPI>? = null

                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq selectedAccountDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq selectedAccountDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }
                    if(AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                            (UserTable.id eq AccountsEmployeeTable.userId).and(
                                UserTable.login eq loginSecret.toString()).and(
                                AccountsEmployeeTable.departmentId eq departmentIdList[0]
                            )
                        }.selectAll().count() > 0
                    ){
                        if(HierarchyTable.selectAll().where{
                            HierarchyTable.departmentId eq departmentIdList[0]
                            }.count()>0)
                        {
                            val pairBytes =
                                HierarchyTable.selectAll().where{
                                    HierarchyTable.departmentId eq departmentIdList[0]
                                }.map{
                                    row ->
                                        Bytes(
                                            row[HierarchyTable.postRectangles],
                                            row[HierarchyTable.lines]
                                        )
                                }
                            val postRectanglesJson = String(pairBytes[0].postRectangleBytes)
                            postRectangleList = Json.decodeFromString<List<PostRectangleAPI>>(postRectanglesJson)
                            val lineJson = String(pairBytes[0].lineListBytes)
                            lineList = Json.decodeFromString<List<LineAPI>>(lineJson)
                            for(pr in postRectangleList!!){
                                pr.printPRAPI()
                            }
                            for(line in lineList!!){
                                println(line.firstUID.toString() + " " + line.secondUID.toString())
                            }
                            result = "true"
                        }
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="true"){
                    call.respond(
                        GetHierarchyResponse("200","Успех", postRectangleList, lineList)
                    )
                }else{
                    call.respond(
                        GetHierarchyResponse("500","Ошибка на сервере", null,null)
                    )
                }
            }
            post("/create-task/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val createTaskRequest = call.receive<CreateTaskRequest>()

                val sentAccountsDepartment = createTaskRequest.accountsDepartment
                val sentTask = createTaskRequest.task
                var result:String? = null


                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                        row ->
                        row[AccountsEmployeeTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){

                        val taskId = TaskTable.insertAndGetId {
                            it[TaskTable.name] = sentTask.name
                            it[TaskTable.description] = sentTask.description
                            it[TaskTable.file] = sentTask.file
                            it[TaskTable.beginTime] = sentTask.beginTime
                            it[TaskTable.endTime] = sentTask.endTime
                            it[TaskTable.percent] = sentTask.percent.toFloat()
                            it[TaskTable.priority] = sentTask.priority.toInt()
                            it[TaskTable.creatorId] = creatorIdList[0].value
                            it[TaskTable.completed] = sentTask.completed
                            it[TaskTable.cycleDuration] = sentTask.cycleDuration
                            it[TaskTable.cycleType] = sentTask.cycleType
                        }

                        val responsibleEmployeeIdList = emptyList<Int>().toMutableList()
                        for(employee in sentTask.responsiblePersons){
                            AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                                (UserTable.id eq AccountsEmployeeTable.userId).and(
                                    UserTable.login eq employee.login).and(
                                    AccountsEmployeeTable.departmentId eq departmentIdList[0]
                                )
                            }.selectAll().map{
                                    row ->
                                    responsibleEmployeeIdList.add(row[AccountsEmployeeTable.id].value)
                            }
                        }
                        for(id in responsibleEmployeeIdList){
                            EmployeeTaskTable.insert{
                                it[EmployeeTaskTable.employeeId] = id
                                it[EmployeeTaskTable.taskId] = taskId.value
                            }
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result!=null){
                    call.respond(CreateTaskResponse("200","Запрос выполнен успешно"))
                }else {
                    call.respond(CreateTaskResponse("500","Ошибка добавления в БД на сервере"))
                }

            }
            get("/get-tasks/") {
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val accountDepartmentJSON: String? = call.request.queryParameters["accountDepartment"]
                val selectedAccountDepartment = parseAccountsDepartmentFromQuery(
                    accountDepartmentJSON.toString()
                )
                val userJSON:String? = call.request.queryParameters["user"]
                val selectedUser = parseUserFromQuery(
                    userJSON.toString()
                )

                var result:String? = null
                var taskListG = emptyList<TaskWithId>().toMutableList()
                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq selectedAccountDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq selectedAccountDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }// получение id бухгалтерии
                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq selectedUser.login).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }// id бухгалтера, для кого ищём таски
                    var taskIdHashSet = HashSet<Int>()
                    var taskList = emptyList<TaskWithId>().toMutableList()
                    if(creatorIdList.isNotEmpty()
                    ){
                        TaskTable.selectAll().where {
                            TaskTable.creatorId eq creatorIdList[0].value
                        }.map {
                            row ->
                                if(!taskIdHashSet.contains(row[TaskTable.id].value)){ // если такую задачу мы ещё не сохраняли
                                    taskIdHashSet.add(row[TaskTable.id].value)
                                    taskList.add(
                                        TaskWithId(
                                            id = row[TaskTable.id].value,
                                            name = row[TaskTable.name],
                                            description = row[TaskTable.description],
                                            beginTime = row[TaskTable.beginTime],
                                            endTime = row[TaskTable.endTime],
                                            priority = row[TaskTable.priority].toString(),
                                            percent = row[TaskTable.percent].toString(),
                                            file = row[TaskTable.file],
                                            responsiblePersons = EmployeeTaskTable.join(AccountsEmployeeTable, JoinType.INNER){
                                                (EmployeeTaskTable.employeeId eq AccountsEmployeeTable.id).and(
                                                    EmployeeTaskTable.taskId eq row[TaskTable.id].value
                                                )
                                            }.join(UserTable, JoinType.INNER){
                                                (AccountsEmployeeTable.userId eq UserTable.id)
                                            }.selectAll().map{
                                                lrow ->
                                                UserAPI(
                                                    lrow[UserTable.login],
                                                    ""
                                                )
                                            }.toMutableList(),
                                            creatorUser = selectedUser,
                                            completed = row[TaskTable.completed]
                                        )
                                    )
                                    println("Выполнили код по добавлению задачи, где ты создатель")
                                }
                        }// блок кода до - выбрали всех создателей
                        TaskTable.join(EmployeeTaskTable, joinType = JoinType.INNER){
                            (TaskTable.id eq EmployeeTaskTable.taskId).and(
                                EmployeeTaskTable.employeeId eq creatorIdList[0].value
                            )
                        }.selectAll().map{
                            row ->
                            println("Попытка добавить задачу, где ты исполнитель, но не создатель")
                                if(!taskIdHashSet.contains(row[TaskTable.id].value)){
                                    taskIdHashSet.add(row[TaskTable.id].value)
                                    taskList.add(
                                        TaskWithId(
                                            id = row[TaskTable.id].value,
                                            name = row[TaskTable.name],
                                            description = row[TaskTable.description],
                                            beginTime = row[TaskTable.beginTime],
                                            endTime = row[TaskTable.endTime],
                                            priority = row[TaskTable.priority].toString(),
                                            percent = row[TaskTable.percent].toString(),
                                            file = row[TaskTable.file],
                                            responsiblePersons = EmployeeTaskTable.join(AccountsEmployeeTable, JoinType.INNER){
                                                (EmployeeTaskTable.employeeId eq AccountsEmployeeTable.id).and(
                                                    EmployeeTaskTable.taskId eq row[TaskTable.id].value
                                                )
                                            }.join(UserTable, JoinType.INNER){
                                                (AccountsEmployeeTable.userId eq UserTable.id)
                                            }.selectAll().map{
                                                    lrow ->
                                                UserAPI(
                                                    lrow[UserTable.login],
                                                    ""
                                                )
                                            }.toMutableList(),
                                            creatorUser = UserAPI(
                                                TaskTable.join(AccountsEmployeeTable,JoinType.INNER){
                                                    (TaskTable.creatorId eq AccountsEmployeeTable.id).and(
                                                        TaskTable.id eq row[TaskTable.id].value
                                                    )
                                                }.join(UserTable,JoinType.INNER){
                                                    (UserTable.id eq AccountsEmployeeTable.userId)
                                                }.selectAll().map{
                                                    lrow->lrow[UserTable.login]
                                                }[0],
                                                ""
                                            ),
                                            completed = row[TaskTable.completed]
                                        )
                                    )
                                    println("Выполнили код по добавлению задачи, где ты исполнитель")
                                }
                        }
                        // блок кода - добавить все задачи, где ты исполнитель
                        taskListG = taskList
                        result = "202"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result!=null){
                    call.respond(GetTaskListResponse(status = "200", description = "Успешно выполнен запрос",
                       taskListG ))
                }else{
                    call.respond(GetTaskListResponse(status = "500", description = "Ошибка сервера",
                        emptyList<TaskWithId>()
                    ))
                }
            }
            post("/update-task/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val updateTaskRequest = call.receive<UpdateTaskRequest>()

                val task = updateTaskRequest.taskWithID
                val sentAccountsDepartment = updateTaskRequest.accountsDepartment

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        TaskTable.update(
                            {TaskTable.id eq task.id}
                        ){
                            it[beginTime] = task.beginTime
                            it[endTime] = task.endTime
                            it[description] = task.description
                            it[completed] = task.completed
                            it[name] = task.name
                            it[percent] = task.percent.toFloat()
                            it[priority] = task.priority.toInt()
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(UpdateTaskResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(UpdateTaskResponse("500","Не удалось обновить данные"))
                }
            }
            post("/send-message/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val sendMessageRequest = call.receive<SendMessageRequest>()

                val task = sendMessageRequest.taskWithID
                val sentAccountsDepartment = sendMessageRequest.accountsDepartment

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                        row ->
                            row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        TaskMessageTable.insert{
                            it[text] = sendMessageRequest.message.text
                            it[file] = sendMessageRequest.message.file
                            it[createDate] = sendMessageRequest.message.createDate
                            it[userId] = uId[0]
                            it[taskId] = task.id
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(UpdateTaskResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(UpdateTaskResponse("500","Не удалось обновить данные"))
                }
            }
            post("/get-message-list/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val getMessageListRequest = call.receive<GetMessageListRequest>()

                val task = getMessageListRequest.taskWithID
                val sentAccountsDepartment = getMessageListRequest.accountsDepartment

                var result:String? = null
                var messageList  = emptyList<MessageWithUser>()
                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        messageList = TaskMessageTable.selectAll().where{
                            (TaskMessageTable.taskId eq task.id)
                        }.map{
                            row ->
                                MessageWithUser(
                                    text = row[TaskMessageTable.text],
                                    file = row[TaskMessageTable.file],
                                    createDate = row[TaskMessageTable.createDate],
                                    user = UserTable.selectAll().where{
                                        UserTable.id eq row[TaskMessageTable.userId]
                                    }.map{
                                        rowL ->
                                            rowL[UserTable.login]
                                    }[0]
                                )
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(GetMessageListResponse("200","Запрос успешно выполнен",messageList))
                }else{
                    call.respond(GetMessageListResponse("500","Не удалось обновить данные",messageList))
                }
            }
            post("/create-business-process/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val createBusinessProcessRequest = call.receive<CreateBusinessProcessRequest>()

                val sentAccountsDepartment = createBusinessProcessRequest.accountsDepartment
                val businessProcess = createBusinessProcessRequest.businessProcess

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        BusinessProcessTable.insert{
                            it[name] = businessProcess.name
                            it[completed] = false
                            it[departmentId] = departmentIdList[0]
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(CreateBusinessProcessResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(CreateBusinessProcessResponse("500","Не удалось обновить данные"))
                }
            }
            get("/get-business-process-list/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()
                val accountDepartmentJSON: String? = call.request.queryParameters["accountDepartment"]
                val selectedAccountDepartment = parseAccountsDepartmentFromQuery(
                    accountDepartmentJSON.toString()
                )

                var result:String? = null
                var businessProcessListG = emptyList<BusinessProcess>().toMutableList()
                transaction {
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq selectedAccountDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq selectedAccountDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }
                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){ // юзер-создатель, тот для кого ищём таски
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }
                    var BPList = emptyList<BusinessProcess>()
                    if(creatorIdList.isNotEmpty()
                    ){
                        BPList = BusinessProcessTable.selectAll().where{
                            BusinessProcessTable.departmentId eq departmentIdList[0]
                        }.map{
                            row ->
                                BusinessProcess(
                                    id = row[BusinessProcessTable.id].value,
                                    name = row[BusinessProcessTable.name],
                                    completed = row[BusinessProcessTable.completed],
                                    bpTaskList = BusinessProcessTaskTable.selectAll().where{
                                        BusinessProcessTaskTable.bpId eq row[BusinessProcessTable.id].value
                                    }.map{
                                        bptRow ->
                                            BPTask(
                                                id = bptRow[BusinessProcessTaskTable.id].value,
                                                name = bptRow[BusinessProcessTaskTable.name],
                                                description = bptRow[BusinessProcessTaskTable.description],
                                                duration = bptRow[BusinessProcessTaskTable.duration].toString(),
                                                priority = bptRow[BusinessProcessTaskTable.priority].toString(),
                                                percent = bptRow[BusinessProcessTaskTable.percent].toString(),
                                                file = bptRow[BusinessProcessTaskTable.file],
                                                responsiblePost = bptRow[BusinessProcessTaskTable.responsiblePost],
                                                responsibleUser = emptyList<UserAPI>().toMutableList(),
                                                creatorUser = UserAPI("",""),
                                                completed = bptRow[BusinessProcessTaskTable.completed],
                                                position = bptRow[BusinessProcessTaskTable.position]
                                            )
                                    }.toMutableList()
                                )
                        }
                        businessProcessListG = BPList.toMutableList()
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result!=null){
                    for(bp in businessProcessListG){
                        println(bp.bpTaskList.size)
                    }
                    call.respond(GetBusinessProcessResponse(status = "200", description = "Успешно выполнен запрос",
                        businessProcessListG
                    ))
                }else{
                    call.respond(GetBusinessProcessResponse(status = "500", description = "Ошибка сервера",
                        businessProcessListG
                    ))
                }
            }
            post("/update-business-process/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val updateBusinessProcessRequest = call.receive<UpdateBusinessProcessRequest>()

                val sentAccountsDepartment = updateBusinessProcessRequest.accountsDepartment
                val businessProcess = updateBusinessProcessRequest.businessProcess

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        //обновили БП
                        BusinessProcessTable.update(
                            {BusinessProcessTable.id eq businessProcess.id}
                        ){
                            it[name] = businessProcess.name
                            it[completed] = businessProcess.completed
                        }
                        //создаём или обновляем задания
                        println(businessProcess.bpTaskList.size)
                        for(bpTask in businessProcess.bpTaskList){
                            if(BusinessProcessTaskTable.selectAll().where {
                                    BusinessProcessTaskTable.id eq bpTask.id
                                }.map { row ->
                                    row[BusinessProcessTaskTable.id]
                                }.isNotEmpty())
                            {
                                BusinessProcessTaskTable.update({
                                    BusinessProcessTaskTable.id eq bpTask.id
                                }) {
                                    it[description] = bpTask.description
                                    it[name] = bpTask.name
                                    it[duration] = bpTask.duration.toInt()
                                    it[priority] = bpTask.priority.toInt()
                                    it[file] = bpTask.file
                                    it[responsiblePost] = bpTask.responsiblePost
                                }
                            }else{
                                BusinessProcessTaskTable.insert{
                                    it[description] = bpTask.description
                                    it[name] = bpTask.name
                                    it[duration] = bpTask.duration.toInt()
                                    it[priority] = bpTask.priority.toInt()
                                    it[file] = bpTask.file
                                    it[completed] = false
                                    it[percent] = 0.0f
                                    it[position] = bpTask.position
                                    it[bpId] = businessProcess.id
                                    it[responsiblePost] = bpTask.responsiblePost
                                }
                            }
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(UpdateBusinessProcessResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(UpdateBusinessProcessResponse("500","Не удалось обновить данные"))
                }
            }
            post("/run-business-process/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val runBusinessProcessRequest = call.receive<RunBusinessProcessRequest>()

                val sentAccountsDepartment = runBusinessProcessRequest.accountsDepartment
                val businessProcess = runBusinessProcessRequest.businessProcess
                val postRectangleAPIList = runBusinessProcessRequest.postRectangleAPIList

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }
                    var currentTimeString = getCurrentTimeFormatted()
                    if(creatorIdList.isNotEmpty()){
                        val createdTaskList = emptyList<Int>().toMutableList()
                        println("размер bpTaskList - ${businessProcess.bpTaskList.size}")
                        for(bpTask in businessProcess.bpTaskList){
                            val finalTime = formatDateTime(
                                parseStringToLocalDateTime(currentTimeString)?.plusHours(bpTask.duration.toLong())
                                    ?: LocalDateTime.now())
                            val newTID = TaskTable.insertAndGetId {
                                it[name] = bpTask.name
                                it[description] = bpTask.description
                                it[creatorId] = creatorIdList[0].value
                                it[completed] = false
                                it[percent] = bpTask.percent.toFloat()
                                it[file] = bpTask.file
                                it[cycleType] = "none"
                                it[cycleDuration] = "0"
                                it[priority] = bpTask.priority.toInt()
                                it[beginTime] = currentTimeString
                                it[endTime] = finalTime
                            }.value
                            createdTaskList.add(
                                newTID
                            )
                            println("Размеры списка должностей - ${postRectangleAPIList.size}")
                            for(post in postRectangleAPIList){
                                if(post.text == bpTask.responsiblePost){
                                    bpTask.responsibleUser = post.employeeList.toMutableList()
                                }
                            }
                            for(employee in bpTask.responsibleUser){
                                EmployeeTaskTable.insert{
                                    it[taskId] = newTID
                                    it[employeeId] = UserTable.join(AccountsEmployeeTable, JoinType.INNER){
                                        (UserTable.login eq employee.login).and(
                                            AccountsEmployeeTable.userId eq UserTable.id
                                        ).and(AccountsEmployeeTable.departmentId eq departmentIdList[0])
                                    }.selectAll().map{
                                        row ->
                                            row[AccountsEmployeeTable.id]
                                    }[0].value
                                }
                            }
                            BusinessProcessCreatedTaskTable.insert{
                                it[taskId] = newTID
                                it[bpTaskId] = bpTask.id
                            }
                            currentTimeString = finalTime
                        }

                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(RunBusinessProcessResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(RunBusinessProcessResponse("500","Не удалось обновить данные"))
                }
            }
            post("/delete-task/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val deleteTaskRequest = call.receive<DeleteTaskRequest>()

                val sentAccountsDepartment = deleteTaskRequest.accountsDepartment
                val task = deleteTaskRequest.taskWithID

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        TaskTable.deleteWhere {
                            TaskTable.id eq task.id
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(DeleteTaskResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(DeleteTaskResponse("500","Не удалось обновить данные"))
                }
            }
            post("/delete-bptask/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val deleteBPTaskRequest = call.receive<DeleteBPTaskRequest>()

                val sentAccountsDepartment = deleteBPTaskRequest.accountsDepartment
                val bpTask = deleteBPTaskRequest.bpTask

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        BusinessProcessTaskTable.deleteWhere {
                            BusinessProcessTaskTable.id eq bpTask.id
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(DeleteBPTaskResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(DeleteBPTaskResponse("500","Не удалось обновить данные"))
                }
            }
            post("/delete-business-process/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val deleteBusinessProcess = call.receive<DeleteBPRequest>()

                val sentAccountsDepartment = deleteBusinessProcess.accountsDepartment
                val businessProcess = deleteBusinessProcess.businessProcess

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){
                        BusinessProcessTable.deleteWhere {
                            BusinessProcessTable.id eq businessProcess.id
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(DeleteBPResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(DeleteBPResponse("500","Не удалось обновить данные"))
                }
            }
            post("/delete-accountant/"){
                val principal = call.principal<JWTPrincipal>()
                val loginSecret = principal?.payload?.getClaim("login")?.asString()

                val deleteAccountant = call.receive<DeleteAccountantRequest>()

                val sentAccountsDepartment = deleteAccountant.accountsDepartment
                val businessProcess = deleteAccountant.user

                var result:String? = null

                transaction{
                    val departmentIdList = AccountsDepartmentTable.selectAll().where {
                        (AccountsDepartmentTable.accountsName eq sentAccountsDepartment.name).and(
                            AccountsDepartmentTable.authorLogin eq sentAccountsDepartment.authorLogin
                        )
                    }.map { row ->
                        row[AccountsDepartmentTable.id]
                    }

                    val creatorIdList = AccountsEmployeeTable.join(UserTable,JoinType.INNER){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            UserTable.login eq loginSecret.toString()).and(
                            AccountsEmployeeTable.departmentId eq departmentIdList[0]
                        )
                    }.selectAll().map{
                            row ->
                        row[AccountsEmployeeTable.id]
                    }

                    val uId = UserTable.selectAll().where {
                        UserTable.login eq loginSecret.toString()
                    }.map{
                            row ->
                        row[UserTable.id]
                    }

                    if(creatorIdList.isNotEmpty()){

                        val employeeId = UserTable.join(
                            AccountsEmployeeTable, JoinType.INNER
                        ){
                            AccountsEmployeeTable.userId eq UserTable.id
                        }.selectAll().map{
                            row->
                                row[AccountsEmployeeTable.id]
                        }

                        AccountsEmployeeTable.deleteWhere {
                            AccountsEmployeeTable.id eq employeeId[0]
                        }
                        result = "200"
                    }else{
                        // нет такого бухгалтера
                    }
                }
                if(result=="200"){
                    call.respond(DeleteAccountantResponse("200","Запрос успешно выполнен"))
                }else{
                    call.respond(DeleteAccountantResponse("500","Не удалось обновить данные"))
                }
            }
        }
    }
}

fun userExist(user: UserAPI):Boolean
{
    return transaction {
        UserTable.selectAll().where{
            UserTable.login eq user.login
        }.count() > 0
    }
}
fun userPasswordCheck(user: UserAPI):Boolean{
    return transaction {
        UserTable.selectAll().where{
            UserTable.login eq user.login

        }.andWhere {
            UserTable.password eq user.password
        }.count() > 0
    }
}

fun parseAccountsDepartmentFromQuery(query: String): AccountsDepartment {
    // Декодируем URL-encoded строку
    val decodedQuery = URLDecoder.decode(query, "UTF-8")

    // Проверяем, есть ли в строке готовая AccountsDepartment
    val regex = """AccountsDepartment\(id=(-?\d+),\s*name=([^,]+),\s*createDate=([^,]+),\s*authorLogin=([^,]+),\s*employeesNumber=(\d+)\)""".toRegex()
    val matchResult = regex.find(decodedQuery)

    return if (matchResult != null) {
        // Случай 1: Разбираем готовый объект из строки
        val (id, name, createDateStr, authorLogin, employeesNumberStr) = matchResult.destructured
        AccountsDepartment(
            id = id.toInt(),
            name = name.trim(),
            createDate = createDateStr.trim().replace("[\n]".toRegex(), " "),
            authorLogin = authorLogin.trim(),
            employeesNumber = employeesNumberStr.toInt()
        )
    } else {
        // Случай 2: Создаём новый объект из отдельных параметров
        val params = decodedQuery.split('&').associate {
            val (key, value) = it.split('=')
            key to value
        }
        AccountsDepartment(
            id = -1,  // Дефолтный ID для нового объекта
            name = params["name"] ?: "Unknown",
            createDate =  "1970-01-01 00:00:00",
            authorLogin = params["authorLogin"] ?: "Anonymous",
            employeesNumber = params["employeesNumber"]?.toIntOrNull() ?: 0
        )
    }
}

fun parseUserFromQuery(query: String): UserAPI {
    // Декодируем URL-encoded строку
    val decoded = URLDecoder.decode(query, "UTF-8")

    // Регулярное выражение для извлечения login и password
    val pattern = """User\(login=([^,]+),\s*password=([^)]*)\)""".toRegex()
    val matchResult = pattern.find(decoded) ?: throw IllegalArgumentException("Invalid user format")

    val (login, password) = matchResult.destructured

    return UserAPI(
        login = login.trim(),
        password = password.trim()
    )
}
fun getCurrentTimeFormatted(): String {
    val currentTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH)
    return currentTime.format(formatter)
}
fun formatDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH)
    return dateTime.format(formatter)
}
fun parseStringToLocalDateTime(dateTimeString: String): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH)
    return try {
        LocalDateTime.parse(dateTimeString, formatter)
    } catch (e: DateTimeParseException) {
        null // Если строка не соответствует формату
    }
}
fun startBackgroundTask() {
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            delay(10 * 60 * 1000) // 10 минут
            fetchDataFromDb()
        }
    }
}
// Функция для выполнения SELECT-запроса
fun fetchDataFromDb() {
    transaction {
        // Пример SELECT-запроса
        val taskList = TaskTable.selectAll().where{
            (TaskTable.cycleType eq "dom").or(
                TaskTable.cycleType eq "periodic"
            )
        }.map { row ->
            FullTask(
                id = row[TaskTable.id].value,
                name = row[TaskTable.name],
                description = row[TaskTable.description],
                beginTime = row[TaskTable.beginTime],
                endTime = row[TaskTable.endTime],
                priority = row[TaskTable.priority].toString(),
                percent = row[TaskTable.percent].toString(),
                file = row[TaskTable.file],
                completed = row[TaskTable.completed],
                responsiblePersons = EmployeeTaskTable.join(AccountsEmployeeTable, JoinType.INNER){
                    (EmployeeTaskTable.employeeId eq AccountsEmployeeTable.id).and(
                        EmployeeTaskTable.taskId eq row[TaskTable.id].value
                    )
                }.join(UserTable, JoinType.INNER){
                    (AccountsEmployeeTable.userId eq UserTable.id)
                }.selectAll().map{
                        lrow ->
                    UserAPI(
                        lrow[UserTable.login],
                        ""
                    )
                }.toMutableList(),
                creatorUser = UserAPI(
                    UserTable.join(
                        AccountsEmployeeTable,JoinType.INNER
                    ){
                        (UserTable.id eq AccountsEmployeeTable.userId).and(
                            AccountsEmployeeTable.id eq row[TaskTable.creatorId]
                        )
                    }.selectAll().map{
                            lrow ->
                        lrow[UserTable.login]
                    }[0]
                    ,""
                ),
                cycleDuration = row[TaskTable.cycleDuration],
                cycleType = row[TaskTable.cycleType]
            )
        }
        for(task in taskList){
            if(task.cycleType == "periodic"){ // каждые N дней

            }
            if(task.cycleType == "dom"){ // день месяца

            }
        }

    }
}
