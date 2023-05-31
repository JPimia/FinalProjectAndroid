package fi.tuni.finalprojectandroid

/**
 * Data class representing a user.
 *
 * @param id The ID of the user.
 * @param firstName The first name of the user.
 * @param lastName The last name of the user.
 * @param age The age of the user.
 * @param email The email of the user.
 * @param phone The phone number of the user.
 * @param image The image URL of the user.
 */
data class User(
    val id: String,
    var firstName: String,
    var lastName: String,
    var age: Int,
    var email: String,
    var phone: String,
    val image: String
)
