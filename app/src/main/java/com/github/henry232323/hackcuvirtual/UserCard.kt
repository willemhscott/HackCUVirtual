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
