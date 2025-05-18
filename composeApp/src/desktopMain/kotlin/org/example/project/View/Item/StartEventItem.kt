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
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StartEventItem(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.padding(top = 30.dp, bottom = 10.dp, end = 10.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Иконка события начала"
        )
        Text(
            text = "Начальное событие"
        )
    }
}

@Preview
@Composable
fun StartEventItemPreview(){
    StartEventItem(

    )
}