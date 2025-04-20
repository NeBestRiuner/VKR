package org.example.project.View.Box

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.example.project.Model.InfiniteScrollState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import org.example.project.Model.Line
import org.example.project.Model.PostRectangle
import org.example.project.Model.User
import org.example.project.View.Card.AddEmployeeToPostCard

@Composable
fun HierarchyBox(postRectangleList: SnapshotStateList<PostRectangle>,
                 selectedEmployee: MutableState<User>,
                 employeeFreeList: SnapshotStateList<User>,
                 selectedPostRectangle: MutableState<PostRectangle>,
                 onGetUserList: ()->Unit,
                 lineList: SnapshotStateList<Line>
) {
    var showAddPostCard = remember { mutableStateOf(false) }
    var isArrowed = remember { mutableStateOf(false) }
    var secondDot = remember { mutableStateOf(0) }
    var firstDotRectangle =  remember { mutableStateOf(PostRectangle(lineList = emptyList<Line>().toMutableStateList())) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,){
            Text("Иерархия бухгалтерии")
            Row(Modifier.fillMaxWidth()){
                PostDispenser(postRectangleList = postRectangleList, isArrowed = isArrowed,
                    secondDot = secondDot, firstDotRectangle = firstDotRectangle,
                    lineList = lineList)
                LinkDispenser(isArrowed)
            }
            InfiniteCanvas {
                if (postRectangleList.isNotEmpty()) {
                    postRectangleList.forEach { postRectangle ->
                        postRectangle.PostRectangleCompose(
                            onAddCard = {
                                showAddPostCard.value = true
                                onGetUserList.invoke()
                            },
                            selectedPost = selectedPostRectangle
                        )
                    }
                }
                if(lineList.isNotEmpty()){
                    lineList.forEach{
                        line ->
                        Canvas(
                            modifier = Modifier.fillMaxSize().background(Color.Transparent)
                        ) {
                            drawLine(
                                color = Color.Blue,
                                start = Offset(line.firstX.value,line.firstY.value),
                                end = Offset(line.secondX.value,line.secondY.value),
                                strokeWidth = 4f // Толщина линии в пикселях
                            )
                        }
                    }
                }
            }
        }
        if(showAddPostCard.value){
            AddEmployeeToPostCard(
                onDismiss = {showAddPostCard.value = false},
                employeeFreeList = employeeFreeList,
                selectedPostRectangle = selectedPostRectangle,
                selectedUser = selectedEmployee
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun HierarchyBoxPreview(){
    HierarchyBox(
        postRectangleList = emptyList<PostRectangle>().toMutableStateList(),
        employeeFreeList = emptyList<User>().toMutableStateList(),
        selectedEmployee = mutableStateOf(User("","")),
        selectedPostRectangle = mutableStateOf(PostRectangle(
                lineList = emptyList<Line>().toMutableStateList()
            )
        ),
        onGetUserList = {},
        lineList =  emptyList<Line>().toMutableStateList()
    )
}

@Composable
fun PostDispenser(postRectangleList: SnapshotStateList<PostRectangle>,
                  isArrowed: MutableState<Boolean>, secondDot: MutableState<Int>,
                  firstDotRectangle: MutableState<PostRectangle>,
                  lineList: SnapshotStateList<Line>){
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)){
        Text("Должность",modifier = Modifier.padding(3.dp))
        OutlinedCard(
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent),
            border = BorderStroke(1.dp,Color.Black),
            modifier = Modifier.size(100.dp,50.dp).padding(7.dp),
            onClick = {
                postRectangleList.add(
                    PostRectangle(
                        isArrowed = isArrowed,
                        secondDot = secondDot,
                        firstDotRectangle = firstDotRectangle,
                        lineList = lineList
                    )
                )
            }

        ){

        }
    }
}
@Composable
fun LinkDispenser(isArrowed: MutableState<Boolean>){
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)){
        Text("Связь",modifier = Modifier.padding(3.dp))
        Canvas(modifier = Modifier.size(60.dp).clickable {
            isArrowed.value = true
        }) {
            // Рисуем стрелку
            val path = Path().apply {
                moveTo(size.width * 0.2f, size.height * 0.5f)
                lineTo(size.width * 0.8f, size.height * 0.5f)
                lineTo(size.width * 0.7f, size.height * 0.4f)
                moveTo(size.width * 0.8f, size.height * 0.5f)
                lineTo(size.width * 0.7f, size.height * 0.6f)
            }

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}
@Composable
fun InfiniteCanvas(
    content: @Composable BoxScope.() -> Unit
) {
    val scrollState = remember { InfiniteScrollState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    scrollState.onScroll(dragAmount)
                }
            }.clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = scrollState.offsetX
                    translationY = scrollState.offsetY
                }
        ) {
            content()
        }
    }
}