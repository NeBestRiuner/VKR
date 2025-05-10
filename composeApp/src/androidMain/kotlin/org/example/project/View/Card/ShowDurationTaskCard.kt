package org.example.project.View.Card

import android.annotation.SuppressLint
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
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import org.example.project.Model.PostRectangle
import org.example.project.Model.User


@Composable
fun ShowDurationTaskCard(
    selectedCycleType: MutableState<String>,
    selectedCycleDuration: MutableState<String>,
    showDurationCard: MutableState<Boolean>
){
    var textNumber = remember { mutableStateOf("") }
    // Затемнение фона
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = {
                showDurationCard.value = false
            })
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
                IconButton(onClick = {
                    showDurationCard.value = false
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Стрелка назад"
                    )
                }

            }
            Row{
                IconToggleButton(
                    modifier = Modifier.padding(10.dp).weight(1f),
                    checked = selectedCycleType.value == "periodic",
                    onCheckedChange = {
                        selectedCycleType.value = "periodic"
                    }
                ) {
                    Text("Периодичность")
                }
                IconToggleButton(
                    modifier = Modifier.padding(10.dp).weight(1f),
                    checked = selectedCycleType.value == "dom",
                    onCheckedChange = {
                        selectedCycleType.value = "dom"
                    }
                ) {
                    Text("День месяца")
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            )
            {

                if(selectedCycleType.value == "periodic"){
                    Text(
                        text = "Выберите периодичность",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    TextField(
                        modifier = Modifier.padding(10.dp),
                        value = textNumber.value,
                        onValueChange = {
                                newNumber ->
                            textNumber.value = newNumber
                        },
                        label = {
                            Text("Число дней")
                        }
                    )
                }
                if(selectedCycleType.value == "dom"){
                    Text(
                        text = "Выберите день месяца",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    TextField(
                        modifier = Modifier.padding(10.dp),
                        value = textNumber.value,
                        onValueChange = {
                                newNumber ->
                            textNumber.value = newNumber
                        },
                        label = {
                            Text("")
                        }
                    )
                }

                Button(
                    modifier = Modifier.padding(top = 30.dp),
                    onClick = {
                        selectedCycleDuration.value = textNumber.value
                        showDurationCard.value = false
                    }
                ){
                    Text("Принять")
                }
            }

        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun ShowDurationTaskCardPreview(){
    ShowDurationTaskCard(
        selectedCycleDuration = remember {
            mutableStateOf("")
        },
        selectedCycleType = remember {
            mutableStateOf("dom")
        },
        showDurationCard = remember {
            mutableStateOf(true)
        }
    )
}