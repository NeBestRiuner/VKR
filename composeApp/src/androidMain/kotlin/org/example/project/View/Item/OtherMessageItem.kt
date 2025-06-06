package org.example.project.View.Item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.Model.MessageWithUser


@Composable
fun OtherMessageItem(
    modifier: Modifier = Modifier,
    message: MessageWithUser
){
    Row(
        modifier.padding(5.dp),
        horizontalArrangement = Arrangement.Start
    ){
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
        Text(
            modifier = Modifier.padding(2.dp).weight(1f),
            text = message.text
        )
    }
}

@Preview
@Composable
fun OtherMessageItemPreview(){
    OtherMessageItem(
        message = MessageWithUser(
            text = "Я новое сообщение",
            file = ByteArray(0),
            createDate = "10 May 2025 08:28",
            user = "Admin"
        )
    )
}