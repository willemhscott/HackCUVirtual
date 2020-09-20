package com.github.henry232323.hackcuvirtual

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject


class UserViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        val likeButton: Button = findViewById(R.id.btnLike)
        val dislikeButton: Button = findViewById(R.id.btnDislike)

        likeButton.setOnClickListener {
            getUserData( "zach" )
        }

    }

    fun getUserData(username:String) {
        Messenger.instance.requestInfo(username, this)
    }

    fun loadProfile( data: JSONObject ) {
        println( "Here is some data:\n$$data")
    }

}