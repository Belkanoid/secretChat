package com.belkanoid.secretchat.ui.screens.buttons

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.belkanoid.secretchat.R

@Preview
@Composable
fun NewMessageButton(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = R.drawable.ic_create),
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(1.dp),
        modifier = modifier,
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(corner = CornerSize(20.dp)),
    ) {
        Icon(painter = painter, contentDescription = null)
    }
}