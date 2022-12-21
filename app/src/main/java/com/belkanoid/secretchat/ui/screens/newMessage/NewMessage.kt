package com.belkanoid.secretchat.ui.screens.newMessage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.belkanoid.secretchat.domain.entity.User
import com.belkanoid.secretchat.ui.dialogs.ErrorDialog
import com.belkanoid.secretchat.ui.screens.buttons.FullWidthButton
import java.lang.Exception

@Composable
fun NewMessage(
    receiverId: String = "",
    checkReceiver: (receiverId: Long) -> User,
    onSendMessage: (receiver: Long, message: String) -> Unit,
) {

    var receiverID by remember { mutableStateOf(receiverId) }
    var userMessage by remember { mutableStateOf("") }

    var receiverName by remember { mutableStateOf("Введите id пользователя") }
    var errorReceiverId by remember { mutableStateOf(false)}

    var showErrorDialog by remember { mutableStateOf(false)}

    if (showErrorDialog) {
        ErrorDialog {
            showErrorDialog = false
        }
    }

    LaunchedEffect(key1 = receiverID) {
        if (receiverID.isNotBlank()) {
            val receiver = checkReceiver(receiverID.toLong())
            receiverName = receiver.name
            if (receiver.id == -1L) {
                errorReceiverId = true
            }
        } else {
            receiverName = "Введите id пользователя"
            errorReceiverId = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = receiverID,
                onValueChange = {
                    receiverID = it
                    errorReceiverId = false
                },
                isError = errorReceiverId,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(
                        text = receiverName,
                        color = Color(0xFFE45F05)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = userMessage,
                onValueChange = {
                    userMessage = it
                },
                label = {
                    Text(
                        text = "Введите сообщение",
                        color = Color(0xFFE45F05)
                    )
                },
                maxLines = 20,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                modifier = Modifier.fillMaxWidth()

            )
        }

        FullWidthButton(text = "Отправить", modifier = Modifier.align(Alignment.BottomCenter)) {
            if (receiverID.isNotBlank() && errorReceiverId == false) {
                onSendMessage(receiverID.toLong(), userMessage)
            } else {
                showErrorDialog = true
            }
        }
    }



}


@Preview
@Composable
private fun Preview() {
    NewMessage("", { _ -> User(1, "", "", "") }) { _, _ ->
    }
}