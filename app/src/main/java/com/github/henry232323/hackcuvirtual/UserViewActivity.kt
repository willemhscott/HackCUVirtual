package com.github.henry232323.hackcuvirtual

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class UserViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        val yayButton: Button = findViewById(R.id.yay)
        val nayButton: Button = findViewById(R.id.nay)

        yayButton.setOnClickListener {
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