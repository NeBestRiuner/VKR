package org.example.project.Model

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt


data class PostRectangle(
    var uId: Int? = null,
    var position: MutableState<Offset> = mutableStateOf(Offset(0f,0f)),
    var size: DpSize = DpSize(100.dp, 60.dp),
    val color: Color = Color(Color.White.value),
    var text: MutableState<String> = mutableStateOf("Должность"),
    var employeeList: SnapshotStateList<User> = emptyList<User>().toMutableStateList(),
    var leaderPostRectangle: PostRectangle? = null,
    var isArrowed: MutableState<Boolean> = mutableStateOf(false),
    var secondDot : MutableState<Int> = mutableStateOf(0),
    var firstDotRectangle: MutableState<PostRectangle>? = null,
    var centerOffsetX: MutableState<Float> = mutableStateOf(130f),
    var centerOffsetY: MutableState<Float> = mutableStateOf(90f),
    var lineList: SnapshotStateList<Line> = emptyList<Line>().toMutableStateList()
){
    init{
        if(uId == null) uId = this.hashCode()
    }

    @Composable
    fun PostRectangleCompose(
            onAddCard: ()->Unit,
            selectedPost: MutableState<PostRectangle>
        ){
        var isEditing by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        position.value.x.roundToInt(), position.value.y.roundToInt()
                    )
                }
                .background(color)
                .size(size.width, size.height)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        position.value += dragAmount
                        centerOffsetX.value += dragAmount.x
                        centerOffsetY.value +=dragAmount.y
                    }
                }.clickable {
                    if(isArrowed.value){
                        if(secondDot.value % 2 == 0 ){
                            firstDotRectangle!!.value = this
                        }else{
                            leaderPostRectangle = firstDotRectangle!!.value
                            lineList.add(
                                Line(
                                    firstPostRectangle = leaderPostRectangle!!,
                                    secondPostRectangle = this
                                )
                            )
                            isArrowed.value = false
                        }
                        secondDot.value++
                    }
                }
        ){
            Column(Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){
               // Text(text.value, textAlign = TextAlign.Center)
                // Текст или поле ввода (в зависимости от режима)
                if (isEditing) {
                    TextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        singleLine = true,
                        modifier = Modifier
                            .onKeyEvent { event ->
                                if (event.key == Key.Enter) {
                                    isEditing = false
                                    true
                                } else {
                                    false
                                }
                            }
                            .size(size.width - 10.dp, size.height)
                            .padding(5.dp),
                        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 8.sp)
                    )
                } else {
                    Text(
                        text = text.value,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { /* Одиночный клик не нужен */ }
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = { // Двойной клик переключает в режим редактирования
                                        isEditing = true
                                    }
                                )
                            }
                    )
                }
                Row(Modifier.fillMaxWidth()){
                    if (employeeList.isNotEmpty()) {
                        employeeList.forEach { employee ->
                            EmployeesAvatar(employee)
                        }
                    }
                     IconButton(onClick = {
                         selectedPost.value = this@PostRectangle
                         onAddCard.invoke()
                         if(employeeList.size > 1){
                             size = DpSize((((employeeList.size+1)*50).dp), size.height)
                                centerOffsetX.value+= 70
                         }
                     }){
                         Icon(imageVector = Icons.Filled.AddCircle,
                             contentDescription = "Кнопка добавить пользователя")
                     }
                }
            }
        }
    }
    @Composable
    fun EmployeesAvatar(employee: User){
        Column(Modifier.width(40.dp).padding(horizontal = 2.dp)  ,horizontalAlignment = Alignment.CenterHorizontally){
            Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = "Аватар бухгалтера")
            Text( text = employee.login, style = TextStyle(fontSize = 8.sp))
        }
    }

}

@Preview
@Composable
fun PostRectangleComposePreview(){
    PostRectangle(lineList = emptyList<Line>().toMutableStateList()).PostRectangleCompose(
        {},
        selectedPost = mutableStateOf(PostRectangle(lineList = emptyList<Line>().toMutableStateList())),
    )
}