package com.github.henry232323.hackcuvirtual

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_title: TextView = findViewById(R.id.login_title)
        login_title.setText("Login")

        //storing username and password from edit text boxes
        val bLogin = findViewById<Button>(R.id.btnLogin)


        bLogin.setOnClickListener{
            val iUsername = findViewById<EditText>(R.id.etUsername).getText().toString()
            val iPassword = findViewById<EditText>(R.id.etPassword).getText().toString()

            Messenger.instance.getToken(iUsername, iPassword, object: Callback {
                override fun onFailure(call: Call, e: IOException){
                    login_title.setText("Login Failed")
                }

                override fun onResponse(call: Call, response: Response) {
                    login_title.setText("Login Successful!")
                    val intent = Intent(this, Messages::class.java)
                    Messenger.instance.start(application)
                    startActivity(intent)
                }
            })
        }

    }
}