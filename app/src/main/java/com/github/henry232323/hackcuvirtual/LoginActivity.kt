package com.github.henry232323.hackcuvirtual

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val msg = intent.getBooleanExtra(EXTRA_MESSAGE, false)

        val login_title: TextView = findViewById(R.id.login_title)
        val text:String = if(msg) "Already logged in" else "Not yet logged in";
        login_title.setText(text)
    }
}