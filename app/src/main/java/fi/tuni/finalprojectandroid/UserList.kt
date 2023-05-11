package fi.tuni.finalprojectandroid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserList(users: List<User>, showAddUserDialog: () -> Unit, setUsers: (List<User>) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    Column {
        Button(onClick = { showAddUserDialog() }, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            Text("Add User")
        }
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
                UserItem(user = user, users = users, setUsers = setUsers)
            }
        }
    }
}