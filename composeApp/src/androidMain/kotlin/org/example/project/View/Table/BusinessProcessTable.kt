package org.example.project.View.Table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BusinessProcessTable(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier.background(Color.LightGray).
            height(50.dp).drawWithContent {
                drawContent()
                val strokeWidth = 1.0f
                val width = size.width
                val height = size.height
                val borderColor = Color.Black
                // Верхняя граница
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(width, 0f),
                    strokeWidth = strokeWidth
                )

                // Левая граница
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, height),
                    strokeWidth = strokeWidth
                )

                // Правая граница
                drawLine(
                    color = borderColor,
                    start = Offset(width, 0f),
                    end = Offset(width, height),
                    strokeWidth = strokeWidth
                )
            },
            verticalAlignment = Alignment.CenterVertically
        ){
            BusinessProcessTableHeader("Название")
            VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
            BusinessProcessTableHeader("Редактировать")
            VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
            BusinessProcessTableHeader("Запустить")
        }
        LazyColumn(modifier = Modifier.weight(1f)) {

        }
    }

}

@Composable
fun BusinessProcessTableHeader(name:String){
    Box(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun BusinessProcessTablePreview(){
    BusinessProcessTable(

    )
}