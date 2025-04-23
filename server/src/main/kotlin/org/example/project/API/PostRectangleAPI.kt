package org.example.project.API

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable

data class PostRectangleAPI(
    val uId: Int,
    val positionX: Float,
    val positionY: Float,
    val sizeWidth: Float,
    val sizeHeight: Float,
    val text: String,
    val employeeList: List<UserAPI>,
    @EncodeDefault
    var leaderPostRectangleAPI: PostRectangleAPI? = null,
    var centerOffsetX: Float,
    var centerOffsetY: Float,
){
    fun printPRAPI(){
            println()
            println(uId)
            println(text)
            if(leaderPostRectangleAPI!=null){
                println(leaderPostRectangleAPI!!.uId)
            }else {
                println(leaderPostRectangleAPI)
            }
            println(employeeList)
            println()
    }
}
