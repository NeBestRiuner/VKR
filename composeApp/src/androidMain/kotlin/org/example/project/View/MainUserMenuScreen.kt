package org.example.project.View


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MainUserMenuScreen(modifier: Modifier = Modifier, onCreateDepartment: ()->Unit){

        Row(
            modifier.fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, color = Color.Black),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceAround,
        ){
                Image(imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Иконка профиля",
                    modifier = modifier.padding(top=10.dp, bottom = 10.dp))
                Text(text="Логин/Почта",
                    modifier = modifier.padding(top=12.dp, bottom = 10.dp),
                    fontSize = 16.sp)
                Image(imageVector = Icons.Filled.Close,
                    contentDescription = "Выйти из аккаунта",
                    modifier = modifier.padding(top=10.dp, bottom = 10.dp))
        }
        Column(modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            TextField(value = "",
                onValueChange = {},
                label={Text("Поиск")},
                modifier = modifier.padding(10.dp))
            Text("Список бухгалтерий",
                modifier = modifier.padding(10.dp),
                fontWeight = FontWeight(700),
                fontSize = 20.sp
            )
            LazyColumn(modifier = modifier.border(1.dp, color = Color.Black)
                .size(width = 300.dp, height = 500.dp)) {

            }
            Button(onClick = {
                onCreateDepartment()
            },
                modifier = modifier.padding(15.dp)
            ){
                Text("Создать бухгалтерию")
            }

    }
}