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
import org.example.project.API.AccountsDepartmentAPI
import org.example.project.API.CreateUserResponse
import org.example.project.API.GetDepartmentListResponse
import org.example.project.API.GetProfileInfo
import org.example.project.API.GetProfileInfoResponse
import org.example.project.API.LoginUserResponse
import org.example.project.API.UpdateProfileInfoRequest
import org.example.project.API.UserAPI
import org.example.project.model.AccountsDepartment
import org.example.project.model.CreateDepartmentResponse
import org.example.project.model.ProfileInfo
import org.example.project.model.User
import org.example.project.tables.AccountsDepartmentTable
import org.example.project.tables.AccountsEmployeeTable
import org.example.project.tables.UserTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant.now

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json() // Использует kotlinx.serialization
    }
    configureSecurity()
    routing {

        get("/HelloWorld/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/get-all-users/") {
            configureDatabase()
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
            configureDatabase()

            if(!userExist(user)){
                transaction {
                    UserTable.insert {
                        it[login] = user.login
                        it[password] = user.password
                    }
                }
                call.respond(CreateUserResponse("success", user))
            }
            call.respond(CreateUserResponse("error",null))
        }
        post("/get-user/"){
            val user = call.receive<UserAPI>()
            configureDatabase()
            if(userPasswordCheck(user)){
                val token = generateToken(user.login)
                call.respond(LoginUserResponse("success",token))
            }else{
                call.respond(LoginUserResponse("error","null"))
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
                    configureDatabase()
                    transaction {
                        val nowTime = now() // получить текущее время
                        AccountsDepartmentTable.insert {
                            it[accountsName] = accountsDepartment.name
                            it[createDate] = nowTime
                            it[authorLogin] = user.login
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
                                authorLogin = row[AccountsDepartmentTable.authorLogin]
                            )
                        }//получить id бухгалтерии
                        val uId: Int = queryUser[0].id!!
                        val accountsDepartmentId: Int = queryDepartment[0].id!!
                        AccountsEmployeeTable.insert {
                            it[departmentId] = accountsDepartmentId
                            it[userId] = uId
                            it[dateOfEmployment] = nowTime
                        }
                        //создать бухгалтера в бухгалтерии
                    }
                    call.respond("success")
                }
            }
            get("/get-accounts-departments/"){


                val principal = call.principal<JWTPrincipal>()
                val login = principal?.payload?.getClaim("login")?.asString()
                if(principal!=null) {
                    val userJSON:String? = call.request.queryParameters["user"]
                    val userLogin = userJSON?.substringAfter("login=")
                        ?.substringBefore(",")?.trim()
                    configureDatabase()
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
                                        authorLogin = row[AccountsDepartmentTable.authorLogin]
                                    )
                        }
                    }

                    call.respond(GetDepartmentListResponse("success",result))

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
                                        phoneNumber = ""
                                    )
                                )
                        }
                        result = resultsql[0]
                    }
                    call.respond(GetProfileInfoResponse("success",result))
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
                                    it[phone] = newPhoneNumber.toLong()
                                }
                            }
                            // нет валидации логина
                            updateProfileInfoRequest.user.login.let{
                                    newLogin -> it[login] = newLogin
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
                                    phoneNumber = ""
                                )
                            )
                        }
                    }
                }
                call.respond(GetProfileInfoResponse("success", result))
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

