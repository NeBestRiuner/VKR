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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import org.example.project.Model.PostRectangle
import org.example.project.Model.User
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun AddEmployeeToPostCard(
    onDismiss: ()->Unit,
    employeeFreeList: SnapshotStateList<User>,
    selectedPostRectangle: MutableState<PostRectangle>,
    selectedUser: MutableState<User>
){
    var mExpanded by remember { mutableStateOf(false) }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
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

                // Create an Outlined Text Field
                // with icon and not expanded
                OutlinedTextField(
                    value = selectedUser.value.login,
                    onValueChange = { selectedUser.value = User(it,"") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // This value is used to assign to
                            // the DropDown the same width
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    label = {Text("Бухгалтеры")},
                    trailingIcon = {
                        Icon(icon,"Текстовая строка/список с бухгалтерами",
                            Modifier.clickable { mExpanded = !mExpanded })
                    }
                )

                // Create a drop-down menu with list of cities,
                // when clicked, set the Text Field text as the city selected
                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
                        .weight(1f)

                ) {
                    employeeFreeList.forEach { label ->
                        DropdownMenuItem(onClick = {
                            selectedUser.value = User(label.login, "")
                            mExpanded = false
                        }, text = {
                            Text(text = label.login)
                        }
                        )
                    }
                }
                Button(onClick = {
                    selectedPostRectangle.value.employeeList.add(selectedUser.value)
                    //удалить пользователя из списка
                    onDismiss.invoke()
                },
                    modifier = Modifier.padding(10.dp)
                ){
                    Text("Добавить бухгалтера")
                }
            }

        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun AddEmployeeToPostCardPreview(){
    val empFL = emptyList<User>().toMutableStateList()
    empFL.add(User("Admin",""))
    AddEmployeeToPostCard(
        onDismiss = {},
        employeeFreeList = empFL,
        selectedPostRectangle = mutableStateOf(PostRectangle()),
        selectedUser = mutableStateOf(User("",""))
    )
}