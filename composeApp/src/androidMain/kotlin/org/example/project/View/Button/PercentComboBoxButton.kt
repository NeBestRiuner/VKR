package org.example.project.View.Button

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import org.example.project.Model.TaskWithID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PercentComboBoxButton(
    modifier: Modifier = Modifier,
    task: TaskWithID
) {
    val options = listOf("0", "10", "20", "30","40","60","70","80","90","100")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(task.percent) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        // Поле ввода (выглядит как TextField, но только для выбора)
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOption,
            onValueChange = {},
            label = { Text("Выберите вариант") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )

        // Выпадающий список
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        task.percent = option
                        expanded = false
                    }
                )
            }
        }
    }
}