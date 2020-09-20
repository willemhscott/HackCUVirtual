package com.github.henry232323.hackcuvirtual

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    companion object{
        var current_user = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_title: TextView = findViewById(R.id.login_title)
        login_title.text = "Login"

        //storing username and password from edit text boxes
        val bLogin = findViewById<Button>(R.id.btnLogin)


        val activity = this

        bLogin.setOnClickListener {

            val iUsername = findViewById<EditText>(R.id.etUsername).text.toString()
            val iPassword = findViewById<EditText>(R.id.etPassword).text.toString()

            Messenger.instance.getToken(iUsername, iPassword, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        login_title.text = "Login Failed"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        login_title.text = "Login Successful!"
                    }

                    val intent = Intent(activity, MainActivity::class.java)
                    current_user = iUsername
                    Messenger.instance.start(application)
                    startActivity(intent)
                }
            })
        }

    }

}