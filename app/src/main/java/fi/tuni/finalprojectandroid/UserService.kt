package fi.tuni.finalprojectandroid

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://dummyjson.com/users")
        .build()
    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()
    val gson = Gson()
    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
    val jsonArray = jsonObject.getAsJsonArray("users")
    return@withContext gson.fromJson(jsonArray, object : TypeToken<List<User>>() {}.type)
}