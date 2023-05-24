package fi.tuni.finalprojectandroid

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun UserList(users: List<User>, showAddUserDialog: () -> Unit, setUsers: (List<User>) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var sortedByName by remember { mutableStateOf(true) }
    var sortedByAge by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.background(Color.LightGray)) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp,
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Button(onClick = { showAddUserDialog() },
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 8.dp))
                {
                    Text("Add User")
                }
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(text = "Search by Name or Age") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Row {
                    Button(
                        onClick = { sortedByName = true
                            sortedByAge = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Sort By Name")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { sortedByAge = true
                            sortedByName = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Sort By Age")
                    }
                }
            }

        }

        LazyColumn {
            var updatedUsers = emptyList<User>()
            if(sortedByName) {
                updatedUsers = users.sortedBy { it.firstName }

                items(updatedUsers.filter { it.firstName.contains(searchText, ignoreCase = true) ||
                    it.age.toString().contains(searchText, ignoreCase = true) } ) { user ->
                    UserItem(
                        user = user,
                        onEditUser = { selectedUser = it },
                        users = users,
                        setUsers = setUsers
                    )
                }
            } else if (sortedByAge) {
                updatedUsers = users.sortedBy { it.age }

                items(updatedUsers.filter { it.age.toString().contains(searchText, ignoreCase = true) ||
                    it.firstName.contains(searchText, ignoreCase = true) } ) { user ->
                    UserItem(
                        user = user,
                        onEditUser = { selectedUser = it },
                        users = users,
                        setUsers = setUsers
                    )
                }
            } else {
                items(users) { user ->
                    UserItem(
                        user = user,
                        onEditUser = { selectedUser = it },
                        users = users,
                        setUsers = setUsers
                    )
                }
            }
        }
        selectedUser?.let { user ->
            EditUserDialog(
                user = user,
                onEditUser = { editedUser ->
                    setUsers(users.map { if (it.id == editedUser.id) editedUser else it })
                },
                onDismiss = { selectedUser = null }
            )
        }
    }
}

