package com.adoyo.pushnotifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adoyo.pushnotifications.ui.theme.PushNotificationsTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ChatViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        enableEdgeToEdge()
        setContent {
            PushNotificationsTheme {
                val state = viewModel.state

                if (state.isEnteringToken) {
                    EnterTokenDialog(
                        token = state.remoteToken,
                        onTokenChange = viewModel::onRemoteTokenChange,
                        onSubmitToken = viewModel::onSubmitToken
                    )
                } else {
                    ChatScreen(
                        messageText = state.messageText,
                        onMessageChange = viewModel::onMessageChange,
                        onMessageSend = {
                            viewModel.sendMessage(isBroadcast = false)
                        }, onMessageBroadcast = {
                            viewModel.sendMessage(isBroadcast = true)
                        })
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }

    }
}

