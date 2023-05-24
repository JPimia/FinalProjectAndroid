package fi.tuni.finalprojectandroid

import android.media.Image
import java.util.UUID

data class User(
    val id: String,
    var firstName: String,
    var lastName: String,
    var age: Int,
    var email: String,
    var phone: String,
    val image: String
)
