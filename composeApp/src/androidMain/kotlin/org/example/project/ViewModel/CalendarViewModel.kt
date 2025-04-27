package org.example.project.ViewModel

import androidx.compose.runtime.toMutableStateList
import org.example.project.Model.Task

class CalendarViewModel {
    var taskList =  emptyList<Task>().toMutableStateList()

}