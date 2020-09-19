package com.github.henry232323.hackcuvirtual

//import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter

import android.annotation.SuppressLint
import android.app.Application
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


data class Authentication(
    val type: String = "authenticate",
    val token: String
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
const val API_BASE = "http://3.17.77.33"

class Messenger {

    lateinit var authentication: Authentication
    lateinit var messenger: WebsocketClient
    lateinit var application: Application

    fun getToken(username: String, password: String) {
        val client = OkHttpClient()

        val body = JSONObject()
        body.put("username", username)
        body.put("password", password)

        val request: Request = Request.Builder()
            .url("$API_BASE/login")
            .post(RequestBody.create(MediaType.get("application/json"), body.toString()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    if (response.header("X-Authentication") == null) throw IOException("Malformed response, missing auth header")

                    authentication = Authentication("authenticate", response.header("X-Authentication")!!)
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    fun start(application: Application, token: String) {
        this.application = application

        authentication = Authentication("authenticate", token)
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

    fun requestInfo( username: String ) {
        // Make request to http://3.17.77.33/getprofile/usernamegoeshere

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("http://3.17.77.33/users/$username")
            .addHeader("Authorization", authentication.token)
            .get()
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // do something if it fails
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    // do something with response here
                }
            }
        })
    }

}