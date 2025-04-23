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
import kotlinx.serialization.encodeToByteArray
import org.example.project.API.AccountsDepartmentAPI
import org.example.project.API.CreateAccountDepartmentResponse
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
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.net.URLDecoder
import java.time.Instant.now
import kotlinx.serialization.protobuf.ProtoBuf

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
                        val protobuf = ProtoBuf{}
                        val postRectanglesBinary = protobuf.encodeToByteArray(sendHierarchyRequest.postRectangleAPIList)
                        val lineListBinary = protobuf.encodeToByteArray(sendHierarchyRequest.lineList)
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

