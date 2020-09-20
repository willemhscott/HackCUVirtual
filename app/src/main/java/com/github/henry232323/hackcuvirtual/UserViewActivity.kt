package com.github.henry232323.hackcuvirtual

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class UserViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        val likeButton: Button = findViewById(R.id.btnLike)
        val dislikeButton: Button = findViewById(R.id.btnDislike)

        getUserData( LoginActivity.current_user )

        likeButton.setOnClickListener {
            getUserData( LoginActivity.current_user )
        }

    }

    fun getUserData(username:String) {
        Messenger.instance.getPotentialMatches(username, { arr: JSONArray -> loadProfile( arr[0] as JSONObject ) } )
    }

    fun loadProfile( data: JSONObject ) {
        val user = UserCard( data.getString("username"),
            data.getString("displayname"),
            data.getInt("age"),
            data.getString("gender"),
            data.getJSONArray("favorites") as List<String>,
            data.getJSONArray( "allergens") as List<String>,
            data.getBoolean("covid") )

        val displayName: TextView = findViewById(R.id.name_display)
        val displayAge: TextView = findViewById(R.id.age_display)
        val displayGender: TextView = findViewById(R.id.gender_display)
        val displayFavorites: TextView = findViewById(R.id.favorites_display)
        val displayAllergies: TextView = findViewById(R.id.allergen_display)
        val displayCovid: TextView = findViewById(R.id.covid_display)

        displayName.setText(user.dName)
        displayAge.setText(user.age)
        displayGender.setText(user.gender)
        displayFavorites.setText(user.getFavorites())
        displayAllergies.setText(user.getAllergies())
        displayCovid.setText(user.getCovidResults())
    }

}