package com.belkanoid.secretchat.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
fun ErrorDialog(
    onDismissRequest: () -> Unit,
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
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
                Text(
                    text = "Неправильное id Получателя",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    fontSize = 18.sp,
                    color = Color(0xFFFF6700)
                )

                Text(
                    text = "Чтобы отправить сообщение нужно правильно ввести id получателя, если не знаете его, то спросите его у вашего собеседника",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Visible,
                    maxLines = 3,
                    fontSize = 14.sp,
                    color = Color(0xFFE45F05)
                )

                FullWidthButton(text = "Хорошо") {
                    onDismissRequest()
                }
            }
        }

    }
}

@Preview
@Composable
private fun Preview() {
    ErrorDialog {

    }
}
