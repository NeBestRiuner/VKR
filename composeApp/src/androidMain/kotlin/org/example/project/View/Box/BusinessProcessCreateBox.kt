package org.example.project.View.Box

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun BusinessProcessCreateBox(
    leaveBPCreate: ()->Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(){
            Row(verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                IconButton(onClick = leaveBPCreate,Modifier.padding(start = 10.dp)){
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, "Кнопка назад")
                }
            }
        }
    }
}
@Composable
@Preview
fun BusinessProcessCreateBoxPreview(){
    BusinessProcessCreateBox(
        leaveBPCreate = {}
    )
}