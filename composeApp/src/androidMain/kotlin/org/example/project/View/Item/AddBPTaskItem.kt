package org.example.project.View.Item

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddBPTaskItem(
    modifier: Modifier = Modifier,
    onItemAdd: ()->Unit
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = onItemAdd
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Кнопка добавления задания бизнес-процесса"
            )
        }
        Text(
            text = "Добавить БП"
        )
    }
}

@Preview
@Composable
fun AddBPTaskItemPreview(){
    AddBPTaskItem(
        onItemAdd = {}
    )
}
