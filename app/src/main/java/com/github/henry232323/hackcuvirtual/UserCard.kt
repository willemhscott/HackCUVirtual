package com.github.henry232323.hackcuvirtual

import org.json.JSONArray

class UserCard(username: String, displayName: String, age: Int, gender: String, favorites: JSONArray, allergens: JSONArray, photo: String ) {
    val uName = username
    val dName = displayName
    val age = age
    val gender = gender
    val favs = favorites
    val alrgs = allergens
//    val cov19 = covid
    val photo = photo;

    fun displayInfo() {
        println("Username: $uName\n DisplayName: $dName")
    }

    fun getFavorites(): String {
        var ret = ""
        for (i in 0 until favs.length()) {
            ret = ret + favs.getString(i)
            if ( i < favs.length() - 1) {
                ret = ret + ", "
            } else {
                ret = ret + " "
            }
        }
        return ret
    }

    fun getAllergies(): String {
        var ret = ""
        for (i in 0 until alrgs.length()) {
            ret = ret + alrgs.getString(i)
            if ( i < alrgs.length() - 1) {
                ret = ret + ", "
            } else {
                ret = ret + " "
            }
        }
        return ret
    }
//
//    fun getCovidResults(): String {
//        if( cov19 ) return "I am covid positive"
//        else return "I tested negative for covid"
//    }
}


/*
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val populateButtonHS: Button = findViewById(R.id.populateButtonHS)
        val populateButtonEB: Button = findViewById(R.id.populateButtonEB)
        val populateButtonZE: Button = findViewById(R.id.populateButtonZE)

        val hs = UserCard( "willemhscott", "Henry Scott", "Male", listOf("Men"), listOf("Apples"), true )
        val eb = UserCard( "Bico", "Enri-C-not-QU-o", "Male", listOf("Ham"), listOf("Vegatbles"), false )
        val ze = UserCard( "zelkins18", "Zach Elkins", "Male", listOf("Sushi", "Grapes"), listOf("Peanuts"), false )

        populateButtonHS.setOnClickListener { populateData( hs ) }
        populateButtonEB.setOnClickListener { populateData( eb ) }
        populateButtonZE.setOnClickListener { populateData( ze ) }
    }

    private fun populateData( user: UserCard ) {

        val username: TextView = findViewById(R.id.username)
        val displayname: TextView = findViewById(R.id.displayname)
        val gender: TextView = findViewById(R.id.gender)
        val favorites: TextView = findViewById(R.id.favorites)
        val allergies: TextView = findViewById(R.id.allergies)
        val covid: TextView = findViewById(R.id.covid)

        username.setText(user.uName)
        displayname.setText(user.dName)
        gender.setText(user.gender)
        favorites.setText(user.favs.toString())
        allergies.setText(user.alrgs.toString())
        covid.setText(user.cov19.toString())

    }
}
*
* */