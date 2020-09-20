package com.github.henry232323.hackcuvirtual

import android.R.attr
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray


class MatchesActivity : AppCompatActivity() {

    fun profileData(data: JSONArray){
        for (i in 0 until data.length()) {
            val user = data.getJSONObject(i)
            val age = user.getInt("age")
            val gender = user.getString("gender")
            val favorites = user.getString("favorites")
            val allergens = user.getString("allergens")
            val covid = user.getString("covid")
        }
    }
    fun matchData(data: JSONArray): Int {
        var num_matches = data.length()
        return num_matches
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches)
        Messenger.instance.requestMatchInfo(LoginActivity.current_user, this)

    }
}