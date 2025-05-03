import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerButton(
    datePickerState : DatePickerState,
    timePickerState : TimePickerState,
    selectedDateTime: Date
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }


    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }



    Column(
        modifier = Modifier.padding(end = 10.dp, start = 10.dp,top = 2.dp, bottom = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Кнопка для выбора даты и времени
        Surface(
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dateFormat.format(selectedDateTime),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable { showDatePicker = true }
                )
                Text(
                    text = timeFormat.format(selectedDateTime),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { showTimePicker = true }
                )
            }
        }

        // Диалог выбора даты
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            showTimePicker = true
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancel")
                    }
                },
                text = {
                    DatePicker(
                        state = datePickerState
                    )
                }
            )
        }

        // Диалог выбора времени
        if (showTimePicker) {
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = { showTimePicker = false }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showTimePicker = false }
                    ) {
                        Text("Cancel")
                    }
                },
                text = {
                    TimePicker(
                        state = timePickerState
                    )
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DateTimePickerButtonPreview(){
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }


    // Состояние DatePicker
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.timeInMillis
    )

    val timePickerState = rememberTimePickerState(
        initialHour = selectedDate.get(Calendar.HOUR_OF_DAY),  // Берем часы из selectedDate
        initialMinute = selectedDate.get(Calendar.MINUTE)     // Берем минуты из selectedDate
    )
    DateTimePickerButton(
        datePickerState,
        timePickerState,
        Date()
    )
}