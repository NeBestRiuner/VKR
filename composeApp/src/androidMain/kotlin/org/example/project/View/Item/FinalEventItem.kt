package org.example.project.View.Item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FinalEventItem(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.padding(start = 10.dp, top = 10.dp, bottom = 20.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Иконка события финала"
        )
        Text(
            text = "Финальное событие"
        )
    }
}

@Preview
@Composable
fun FinalEventItemPreview(){
    FinalEventItem(

    )
}