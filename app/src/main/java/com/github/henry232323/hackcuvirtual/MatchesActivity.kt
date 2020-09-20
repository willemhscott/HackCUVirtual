package com.github.henry232323.hackcuvirtual

import android.content.ReceiverCallNotAllowedException
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray


class MatchesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches)
        Messenger.instance.requestMatchInfo(LoginActivity.current_user, this)
    }

    fun createCards(num_matches: Int): ArrayList<CardView> {
        var newCards = ArrayList<CardView>()
        for(i in 0 until num_matches){
            val newCard = CardView(this)
            newCard.layoutParams.width = 298
            newCard.layoutParams.height = 122
            newCard.setBackgroundColor(Color.parseColor("#BD6138"))
            newCards.add(newCard)
        }
        return newCards
    }

    fun profileData(data: JSONArray){
        //just getting all the data from the JSONArray
        for (i in 0 until data.length()) {
            val user = data.getJSONObject(i)
            val age = user.getInt("age")
            val gender = user.getString("gender")
            val favorites = user.getJSONArray("favorites")
            val allergens = user.getJSONArray("allergens")
            val covid = user.getBoolean("covid")
        }
        createCards(data.length())
    }
}