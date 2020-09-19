package com.github.henry232323.hackcuvirtual

class UserCard( username: String, displayName: String, gender: String, favorites: List<String>, allergens: List<String>, covid: Boolean ) {
    val uName = username
    val dName = displayName
    val gender = gender
    val favs = favorites
    val alrgs = allergens
    val cov19 = covid

    fun displayInfo() {
        println("Username: $uName\n DisplayName: $dName")
    }
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