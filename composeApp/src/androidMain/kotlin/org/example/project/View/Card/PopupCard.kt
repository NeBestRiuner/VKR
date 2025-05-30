package org.example.project.View.Card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun PopupCard(
    onDismiss: () -> Unit,
    onCreateDepartment: (String) -> Unit,
    onGetDepartmentList: () -> Unit
) {
    val name = remember{ mutableStateOf("") }
    // Затемнение фона
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss)
    ) {
        // Сама карточка
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .width(300.dp)
                .clickable(enabled = false) { } // предотвращает закрытие при клике на карточку
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ){
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Стрелка назад"
                    )
                }

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().height(600.dp)
            )
            {
                TextField(value = name.value, onValueChange = {newName->name.value=newName}, label= {
                    Text("Название бухгалтерии")
                })
                Button(modifier = Modifier.padding(top = 60.dp), onClick = {
                    onCreateDepartment.invoke(name.value)
                    onDismiss.invoke()
                },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        containerColor = Color(40,100,206),
                        disabledContentColor = Color(0,75,174),
                        disabledContainerColor = Color(192,220,253)
                    )
                ){
                    Text("Создать бухгалтерию")
                }
            }
        }
    }
}
