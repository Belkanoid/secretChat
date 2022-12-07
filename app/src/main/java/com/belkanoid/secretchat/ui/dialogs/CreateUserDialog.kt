package com.belkanoid.secretchat.ui.dialogs

import android.util.Log
import androidx.compose.material.*
import androidx.compose.runtime.*


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.belkanoid.secretchat.ui.screens.buttons.FullWidthButton

@Composable
fun CreateUserDialog(
    onDismissRequest: () -> Unit,
    onClick: (userName: String) -> Unit,
    ) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF15202b).copy(0.9f)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 30.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                 var userName by remember { mutableStateOf("")}

                Text(
                    text = "Чтобы начать общаться сперва представтесь!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    fontSize = 18.sp,
                    color = Color(0xFFFF6700)
                )

                TextField(
                    value = userName,
                    onValueChange = {
                        userName = it
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                    modifier = Modifier.fillMaxWidth(0.9f)
                        .padding(bottom = 20.dp)
                )

                FullWidthButton(text = "Продолжить") {
                    onClick(userName)
                    onDismissRequest()
                }
            }
        }

    }
}

@Preview
@Composable
private fun Preview() {
    CreateUserDialog({}) {

    }
}
