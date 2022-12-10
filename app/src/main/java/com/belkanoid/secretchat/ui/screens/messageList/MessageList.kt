package com.belkanoid.secretchat.ui.screens.messageList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.User
import com.belkanoid.secretchat.presentation.Injector
import com.belkanoid.secretchat.ui.screens.buttons.NewMessageButton
import com.belkanoid.secretchat.ui.screens.buttons.ReplayButton
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

data class MessageDataStyle(
    val message: Message,
    val sender: User,
)

@Composable
fun MessageList(
    messages: List<Message>,
    users: List<User>,
    onReplayButton: (userId: String) -> Unit,
    onNewMessage: () -> Unit,
    onOpenMessageClick: (senderId: Long) -> Unit,
) {
    Box() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(count = messages.size,
                itemContent = { index ->
                    val messageData = MessageDataStyle(
                        message = messages[index],
                        sender = users.find { messages[index].sender == it.id } ?: User(
                            -1L,
                            "error"
                        )
                    )
                    MessageListItem(
                        messageData = messageData,
                        onReplayButton = { onReplayButton(messageData.sender.id.toString()) }
                    ) {
                        onOpenMessageClick(messageData.sender.id)
                    }
                }
            )
        }
        NewMessageButton(
            onClick = onNewMessage,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(80.dp)
                .padding(10.dp)
        )
    }
}

@Composable
private fun MessageListItem(
    messageData: MessageDataStyle,
    onReplayButton: () -> Unit,
    onClick: () -> Unit,
) {

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US);
    val dateString = formatter.format(Date(messageData.message.timestamp))

    Box(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = messageData.sender.name + "#${messageData.sender.id}",
                fontSize = 16.sp,
                color = Color.White.copy(0.8f)
            )
            Text(
                text = messageData.message.message,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(0.9f),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 18.sp,
                color = Color(0xFFE45F05)
            )
            Text(
                text = dateString,
                color = Color.White.copy(0.65f),
                fontWeight = FontWeight.Medium

            )
        }

        ReplayButton(
            onClick = onReplayButton,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp)
        )
    }

}


@Preview
@Composable
private fun Preview1() {
    MessageList(
        listOf(
            Message(0, 0, 0, "absadfsdfsdfasdddd", 82233213123L, false),
            Message(0, 0, 0, "absadfsdfsdfasdddd", 82233213123L, false),
            Message(0, 0, 0, "absadfsdfsdfasdddd", 82233213123L, false),
            Message(0, 0, 0, "absadfsdfsdfasdddd", 82233213123L, false),
            Message(0, 0, 0, "absadfsdfsdfasdddd", 82233213123L, false),
            Message(0, 0, 0, "absadfsdfsdfasdddd", 82233213123L, false),
        ),
        listOf(
            User(0, "test"),
            User(0, "test"),
            User(0, "test"),
            User(0, "test"),
            User(0, "test"),
            User(0, "test")
        ),
        onNewMessage = {},
        onReplayButton = {},
        onOpenMessageClick = {}
    )
}

@Preview
@Composable
private fun Preview2() {
    MessageListItem(
        messageData = MessageDataStyle(
            Message(0, 0, 0, "absadfsdfsdfasdddd", 82233213123L, false),
            User(0, "test")
        ),
        onReplayButton = {},
        onClick = {}
    )
}