package com.belkanoid.secretchat.ui.screens.ConversationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.belkanoid.secretchat.domain.entity.Message
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


data class ConversationDataStyle(
    val message: Message,
    val companion: Boolean,
)


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConversationScreen(
    companionId: Long,
    conversationMessages: List<Message>,
    onSendMessage: (text: String) -> Unit,
) {

    var userMessage by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(conversationMessages.size) {
        coroutineScope.launch {
            listState.animateScrollToItem(conversationMessages.size -1)
        }
    }


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val input = ConstrainedLayoutReference(id = "1")
        val list = ConstrainedLayoutReference("2")
        val verticalChain = createVerticalChain(list, input, chainStyle = ChainStyle.SpreadInside)

        LazyColumn(
            state = listState,
            modifier = Modifier
                .constrainAs(list) {
                    verticalChain.top
                }
                .fillMaxHeight(0.93f)
        ) {
            items(count = conversationMessages.size,
                itemContent = { index ->
                    val messageData = ConversationDataStyle(
                        message = conversationMessages[index],
                        companion = conversationMessages[index].sender == companionId
                    )
                    ConversationItem(messageData = messageData)
                }
            )
        }

        Row(
            modifier = Modifier
                .constrainAs(input) {
                    verticalChain.bottom
                }
                .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
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
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                modifier = Modifier
                    .background(Color(0xFF263544))
                    .height(50.dp)
                    .weight(1f)
                    .fillMaxWidth(0.85f),
            )
            Button(
                onClick = {
                    onSendMessage(userMessage)
                    userMessage = ""
                    keyboardController?.hide()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(0.15f)
            ) {
                Text(text = ">")
            }
        }
    }

}


@Composable
private fun ConversationItem(
    messageData: ConversationDataStyle
) {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.US);
    val dateString = formatter.format(Date(messageData.message.timestamp))

    Box(
        modifier = Modifier
            .background(
                if (messageData.companion) Color(0xFF263544) else Color(0xFF384486)
            )
            .padding(start = 5.dp, end = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(
                text = messageData.message.message,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Visible,
                fontSize = 18.sp,
                color = Color(0xFFE45F05)
            )
            Text(
                text = dateString,
                color = Color.White.copy(0.65f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start

            )
        }

    }

}

@Preview
@Composable
private fun Preview() {
    ConversationScreen(1, listOf(Message(1, 1, 1, "", 0, false))) {

    }
}