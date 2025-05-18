package org.example.project.View.Item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import org.example.project.Model.MessageWithUser
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyMessageItem(
    modifier: Modifier = Modifier,
    message: MessageWithUser
){
    Row(
        modifier.padding(5.dp),
        horizontalArrangement = Arrangement.End
    ){
        Text(
            modifier = Modifier.padding(2.dp).weight(1f),
            text = message.text
        )
        Column(
            modifier = Modifier.padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Icon(
               imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "Аватарка пользователя"
            )
            Text(message.user)
        }
    }
}

@Preview
@Composable
fun MyMessageItemPreview(){
    MyMessageItem(
        message = MessageWithUser(
            text = "Я новое сообщение",
            file = ByteArray(0),
            createDate = "10 May 2025 08:28",
            user = "Admin"
        )
    )
}