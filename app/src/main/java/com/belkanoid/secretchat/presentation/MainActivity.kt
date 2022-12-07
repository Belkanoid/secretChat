package com.belkanoid.secretchat.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.belkanoid.secretchat.ui.navigation.MainNavGraph
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {


    val component by lazy { (application as MessengerApplication).component }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            var userName by remember { mutableStateOf("") }
                            component.getInteractor().userDefined.observe(
                                LocalLifecycleOwner.current) {
                                runBlocking {
                                    val user = component.getInteractor().getUser()
                                    userName = "${user.name}#${user.id}"
                                    Log.d("MESS on topbar", userName)
                                }
                            }
                            Text(
                                text = userName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                textAlign = TextAlign.Start
                            )
                        },
                        backgroundColor = Color(0xFF15202b),

                    )
                },
                backgroundColor = Color(0xFF263544)
            ) {
                MainNavGraph()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        component.getInteractor().refreshQueueJob?.cancel()
    }
}
