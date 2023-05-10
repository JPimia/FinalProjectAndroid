package fi.tuni.finalprojectandroid

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.tuni.finalprojectandroid.ui.theme.FinalProjectAndroidTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var context = LocalContext.current
            var showDialog by remember { mutableStateOf(false) }
            var (users, setUsers) = remember { mutableStateOf(listOf<User>()) }

            fun showAddUserDialog() {
                showDialog = true
            }

            LaunchedEffect(key1 = Unit) {
                val fetchedUsers = getUsers()
                setUsers(fetchedUsers)
            }
            if(showDialog) {
                AddUserDialog(
                    onAddUser = { user ->
                    Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show()
                    showDialog = false
                    print("jeeejeeejeee")
                },
                onDismiss = {showDialog = false
                            print("mororo")},
                users, setUsers = setUsers

                )
            }
            UserList(users = users, showAddUserDialog = {showAddUserDialog()})
            print("asdasdasd")
            Log.d("MyTag", users.toString())

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