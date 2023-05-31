package fi.tuni.finalprojectandroid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

/**
 * Composable function representing the user list.
 *
 * @param userPagingSource The paging source for users.
 * @param users The list of users.
 * @param showAddUserDialog Callback function for showing the add user dialog.
 * @param setUsers Callback function for updating the list of users.
 */
@Composable
fun UserList (userPagingSource: UserPagingSource, users: List<User>, showAddUserDialog: () -> Unit, setUsers: (List<User>) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var sortedByName by remember { mutableStateOf(true) }
    var sortedByAge by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    //val focusRequester = remember { FocusRequester() }
    //val focusManager = LocalFocusManager.current

    val mainBackground = Color(android.graphics.Color.parseColor("#242424"))
    val surfaceColor = Color(android.graphics.Color.parseColor("#4b4b4b"))
    val buttonColor = Color(android.graphics.Color.parseColor("#4d5e6a"))
    val buttonTextColor = Color(android.graphics.Color.parseColor("#e3d184"))

    // PAGING
    /*val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val pageSize = 20
    val loadSize = remember { mutableStateOf(pageSize) }
    val currentPage = remember { mutableStateOf(1) }
    val isLoading = remember { mutableStateOf(false) }
    val users = remember { mutableStateListOf<User>() }

    LaunchedEffect(currentPage.value) {
        val newUsers = userPagingSource.getUsers(pageSize, currentPage.value)
        users.addAll(newUsers)
        isLoading.value = false
    }

    val loadMore = {
        if (!isLoading.value) {
            isLoading.value = true
            currentPage.value++
            scope.launch {
                val newUsers = userPagingSource.getUsers(pageSize, currentPage.value)
                users.addAll(newUsers)
                isLoading.value = false
            }
        }
    }*/


    Column(modifier = Modifier.background(mainBackground)) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp,
            color = surfaceColor
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Button(onClick = { showAddUserDialog() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
                )
                {
                    Text("Add User", color = buttonTextColor)
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
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                    ) {
                        Text(text = "Sort By Name", color = buttonTextColor)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { sortedByAge = true
                            sortedByName = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
                    ) {
                        Text(text = "Sort By Age", color = buttonTextColor)
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

