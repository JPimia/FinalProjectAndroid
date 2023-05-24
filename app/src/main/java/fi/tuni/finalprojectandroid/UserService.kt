package fi.tuni.finalprojectandroid

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.toImmutableList
import java.io.IOException

suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://dummyjson.com/users?limit=100")
        .build()
    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    val gson = Gson()
    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
    val jsonArray = jsonObject.getAsJsonArray("users")
    return@withContext gson.fromJson(jsonArray, object : TypeToken<List<User>>() {}.type)
}

fun AddUser(user: User, users: List<User>, setUsers: (List<User>) -> Unit) {
    val url = "https://dummyjson.com/users"
    val jsonMediaType = "application/json; charset=uft-8".toMediaTypeOrNull()

    val maxId = users.map { it.id.toIntOrNull() ?: 0 }.maxOrNull() ?: 0

    var newId = (maxId + 1).toString()

    val requestBody = """
        {
            "firstName": "${user.firstName}",
            "lastName": "${user.lastName}",
            "age": ${user.age},
            "email": "${user.email}",
            "phone": "${user.phone}"
        }""".trimIndent().toRequestBody(jsonMediaType)

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            Log.d("MyTag", users.toString())
            val updatedUsers = users.toMutableList()
            val newUser = user.copy(id = newId)
            updatedUsers.add(newUser)
            setUsers(updatedUsers)
        }

        override fun onFailure(call: Call, e: IOException) {
            Log.d("MyTag", "User added failed")
        }
    })
}

fun deleteUser(user: User, users: List<User>, setUsers: (List<User>) -> Unit) {
    /*val url = "https://dummyjson.com/users/${user.id}"

    val request = Request.Builder()
        .url(url)
        .delete()
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {*/
            val updatedUsers = users.toMutableList()
            updatedUsers.remove(user)

            for (i in updatedUsers.indices) {
                updatedUsers[i] = updatedUsers[i].copy(id = (i + 1).toString())
            }
            setUsers(updatedUsers)
        /*}
        override fun onFailure(call: Call, e: IOException) {
            Log.d("MyTag", "Deleting failed")
        }
    })*/
}