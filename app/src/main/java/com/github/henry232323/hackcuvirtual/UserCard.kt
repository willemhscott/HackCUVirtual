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