package fi.tuni.finalprojectandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    var searchText by remember { mutableStateOf("") }
    Column {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(text = "search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        LazyColumn {
            items(users.filter {
                it.firstName.contains(searchText, ignoreCase = true) ||
                it.lastName.contains(searchText, ignoreCase = true) ||
                it.email.contains(searchText, ignoreCase = true) ||
                it.phone.contains(searchText, ignoreCase = true) ||
                it.age.toString().contains(searchText, ignoreCase = true)
            }) { user ->
                UserItem(user = user)
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color.Gray)
            .clickable { /* Handle user click */ }
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            "Name: ${user.firstName} ${user.lastName}",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(5.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            "Age: ${user.age}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(5.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            "Email: ${user.email}",
            style = MaterialTheme.typography.body1,
            color = Color.Gray,
            modifier = Modifier.padding(5.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            "Phone: ${user.phone}",
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
            modifier = Modifier.padding(5.dp)
        )
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