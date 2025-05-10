package org.example.project.ViewModel

import androidx.compose.runtime.toMutableStateList
import org.example.project.Model.BusinessProcess

class BusinessProcessViewModel {
    var businessProcessList = emptyList<BusinessProcess>().toMutableStateList()



}