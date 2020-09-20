package com.github.henry232323.hackcuvirtual

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val login: Button = findViewById(R.id.login)
//        val autoLogin: Button = findViewById(R.id.auto_login)
        val userView: Button = findViewById(R.id.user_view)
        val matches: Button = findViewById(R.id.matches)
        val profileView: Button = findViewById(R.id.profile_view)
        val messenger: Button = findViewById(R.id.messenger)

        var intent = Intent( this, LoginActivity::class.java).apply { putExtra(EXTRA_MESSAGE, false) }

//        login.setOnClickListener { startActivity( Intent( this, LoginActivity::class.java).apply { putExtra(EXTRA_MESSAGE, false) } ) }
//        autoLogin.setOnClickListener { startActivity( Intent( this, LoginActivity::class.java).apply { putExtra(EXTRA_MESSAGE, true) } ) }
        userView.setOnClickListener { startActivity( Intent( this, UserViewActivity::class.java ) ) }
        matches.setOnClickListener { startActivity( Intent( this, MatchesActivity::class.java ) ) }
        profileView.setOnClickListener { startActivity( Intent( this, ProfileViewActivity::class.java ) ) }
        messenger.setOnClickListener { startActivity( Intent( this, ProfileViewActivity::class.java ) ) }


    }

}
