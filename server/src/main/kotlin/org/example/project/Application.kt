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
import org.example.project.API.CreateUserResponse
import org.example.project.API.LoginUserResponse
import org.example.project.API.UserAPI
import org.example.project.model.AccountsDepartment
import org.example.project.model.CreateDepartmentResponse
import org.example.project.model.User
import org.example.project.tables.AccountsDepartmentTable
import org.example.project.tables.AccountsDepartmentTable.accountsName
import org.example.project.tables.AccountsEmployeeTable
import org.example.project.tables.UserTable
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant.now
import java.time.LocalDateTime

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
                            (accountsName eq accountsDepartment.name).and(
                                AccountsDepartmentTable.createDate eq nowTime
                            ).and(
                                AccountsDepartmentTable.authorLogin eq accountsDepartment.authorLogin
                            )
                        }.map { row ->
                            AccountsDepartment(
                                id = row[AccountsDepartmentTable.id],
                                name = row[accountsName],
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
                    val user = call.receive<UserAPI>()
                    configureDatabase()
                    transaction {

                    }
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

