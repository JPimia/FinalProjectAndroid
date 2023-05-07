package fi.tuni.finalprojectandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fi.tuni.finalprojectandroid.ui.theme.FinalProjectAndroidTheme
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var users by remember { mutableStateOf(emptyList<User>()) }
            LaunchedEffect(key1 = Unit) {
                val fetchedUsers = getUsers()
                users = fetchedUsers
            }
            UserList(users = users)
        }
    }

}

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

@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            Text("First Name: ${user.firstName}")
            Text("Last Name: ${user.lastName}")
            Text("Email: ${user.email}")
            Text("Phone: ${user.phone}")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinalProjectAndroidTheme {
        Greeting("Android")
    }
}