package com.github.henry232323.hackcuvirtual

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject


class UserViewActivity : AppCompatActivity() {
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        val likeButton: Button = findViewById(R.id.btnLike)
        val dislikeButton: Button = findViewById(R.id.btnDislike)

        getUserData(LoginActivity.current_user)

        likeButton.setOnClickListener {
            getUserData(LoginActivity.current_user)
        }

        dislikeButton.setOnClickListener {
            getUserData(LoginActivity.current_user)
        }

    }

    fun getUserData(username: String) {
        Messenger.instance.getPotentialMatches(username) { arr: JSONArray ->
            if (counter == arr.length()) {
                counter = 0
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                loadProfile(arr[counter] as JSONObject)
                counter += 1
            }
        }
    }

    fun loadProfile(data: JSONObject) {
        val user = UserCard(
            data.getString("username"),
            data.getString("display_name"),
            data.getInt("age"),
            data.getString("gender"),
            data.getJSONArray("favorites"),
            data.getJSONArray("allergens"),
//            data.getBoolean("covid"))//,
            data.getString("photo")
        )

        val displayName: TextView = findViewById(R.id.name_display)
        val displayAge: TextView = findViewById(R.id.age_display)
        val displayGender: TextView = findViewById(R.id.gender_display)
        val displayFavorites: TextView = findViewById(R.id.favorites_display)
        val displayAllergies: TextView = findViewById(R.id.allergen_display)
        val displayImage: ImageView = findViewById(R.id.imageView)

        displayName.text = user.dName
        displayAge.text =  user.age.toString() + " years old"
        displayGender.text = user.gender
        displayFavorites.text = user.getFavorites()
        displayAllergies.text = user.getAllergies()
        runOnUiThread { Glide.with(this).load(user.photo).into(displayImage) }

    }

}