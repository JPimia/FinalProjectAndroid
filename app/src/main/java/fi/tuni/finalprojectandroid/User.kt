package fi.tuni.finalprojectandroid

import android.media.Image
import java.util.UUID

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val email: String,
    val phone: String,
    val image: String
)
