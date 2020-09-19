package com.github.henry232323.hackcuvirtual

import android.net.http.HttpResponseCache.install
import android.os.Bundle
import android.os.PersistableBundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import java.net.URI;
import java.net.URISyntaxException;
import io.reactivex.Flowable


interface WebsocketClient {
    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>
    @Send
    fun sendSubscribe(subscribe: Subscribe)
    @Receive
    fun observeTicker(): Flowable<Ticker>
}

class Messenger : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory("wss://ws-feed.gdax.com"))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

        val service = scarletInstance.create<WebsocketClient>()
    }
    /*

    Example Code:
    private fun processMessage(content) {
        val textView: TextView = findViewById(R.id.animalSound)
        textView.setText(message)
    }

    Add the new message (using its user) to the messaging textview
     */


}