package com.github.henry232323.hackcuvirtual

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.MessageAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
//import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter

data class Authentication(
    val type: String = "authenticate",
    val username: String,
    val password: String
)

data class Message(
    val type: String = "message",
    val from: String,
    val to: String,
    val timestamp: Long = System.currentTimeMillis(),
    val content: String
)

interface WebsocketClient {
    @Send
    fun sendAuth(auth: Authentication)

    @Send
    fun sendMessage(message: Message)

    @Receive
    fun observeMessage(): Flowable<Message>

    @Receive
    fun observeOnConnectionOpenedEvent(): Flowable<WebSocket.Event.OnConnectionOpened<*>>

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>
}

const val WEBSOCK_URL = "wss://3.17.77.33"

class Messenger {

    lateinit var authentication: Authentication
    lateinit var messenger: WebsocketClient
    lateinit var application: Application

    @SuppressLint("CheckResult")
    public fun start(application: Application, username: String, password: String) {
        this.application = application

        authentication = Authentication("authenticate", username, password)
        val BACKOFF_STRATEGY = ExponentialWithJitterBackoffStrategy(1000, 60000)
        val scarletInst = Scarlet.Builder()
            .backoffStrategy(BACKOFF_STRATEGY)
//            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .lifecycle(createAppForegroundAndUserLoggedInLifecycle())
            .build()


        val messenger = scarletInst.create<WebsocketClient>()


        messenger.observeMessage().subscribe { message -> processMessage(message) }
        messenger.observeOnConnectionOpenedEvent().subscribe { onConnectionOpen() }
    }

    fun createAppForegroundAndUserLoggedInLifecycle(): Lifecycle {
        return AndroidLifecycle.ofApplicationForeground(application)
//            .combineWith(loggedInLifecycle)
    }

    private fun onConnectionOpen() {
        messenger.sendAuth(authentication)
    }

    private fun processMessage(message: Message) {
        print(message.content)
        //  val textView: TextView = findViewById(R.id.animalSound)
        //  textView.setText(message)
    }

}