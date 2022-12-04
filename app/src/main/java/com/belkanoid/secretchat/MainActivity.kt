package com.belkanoid.secretchat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.belkanoid.secretchat.presentation.MessengerApplication
import com.belkanoid.secretchat.presentation.viewModel.MainViewModel
import com.belkanoid.secretchat.presentation.viewModel.ViewModelFactory
import com.belkanoid.secretchat.ui.theme.SecretChatTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val mainViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val component by lazy {
        (application as MessengerApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContent {
            mainViewModel.messages.observe(this) {
                Log.d("MESS on ac", it.toString())
            }
            mainViewModel.needToCreateUser.observe(this) {
                Log.d("MESS on ac", "need to create user!")
            }
            SecretChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SecretChatTheme {
        Greeting("Android")
    }
}