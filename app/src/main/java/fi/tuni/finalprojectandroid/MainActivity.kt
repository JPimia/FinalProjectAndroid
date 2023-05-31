package fi.tuni.finalprojectandroid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import fi.tuni.finalprojectandroid.ui.theme.FinalProjectAndroidTheme

/**
 * The main activity of the Android application.
 */
class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var context = LocalContext.current
            var showDialog by remember { mutableStateOf(false) }
            var (users, setUsers) = remember { mutableStateOf(listOf<User>()) }

            /**
             * Shows the add user dialog.
             */
            fun showAddUserDialog() {
                showDialog = true
            }

            val userPagingSource = remember {
                UserPagingSource(pageSize = 20) { pageSize, page ->
                    getUsers(pageSize, page)
                }
            }

            LaunchedEffect(key1 = Unit) {
                val fetchedUsers = getUsers(99,1)
                val sortedUsers = fetchedUsers.sortedBy { it.id }
                setUsers(sortedUsers)
            }
            if(showDialog) {
                AddUserDialog(
                    onAddUser = { user ->
                        Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show()
                        showDialog = false
                    },
                    onDismiss = {showDialog = false },
                    sortedUsers = users, setUsers = setUsers
                )
            }
            if(!users.isNullOrEmpty()) {
                UserList(
                    userPagingSource = userPagingSource,
                    users = users,
                    showAddUserDialog = {showAddUserDialog()},
                    setUsers = setUsers)
            }
        }
    }
}

/**
 * Composable function representing a user item.
 *
 * @param user The user object to display.
 * @param onEditUser Callback function for editing a user.
 * @param users The list of users.
 * @param setUsers Callback function for updating the list of users.
 */
@Composable
fun UserItem(user: User, onEditUser: (User) -> Unit, users: List<User>, setUsers: (List<User>) -> Unit) {

    val nameColor = Color(android.graphics.Color.parseColor("#e3d184"))
    val surfaceColor = Color(android.graphics.Color.parseColor("#4b4b4b"))

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(8.dp),
        elevation = 8.dp,
        color = surfaceColor
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = user.image),
                contentDescription = "User Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(
                    "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.h6,
                    color = nameColor,
                )
                Text(
                    "Age: ${user.age}",
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
                Text(
                    "Email: ${user.email}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Text(
                    "Phone: ${user.phone}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
            Column() {
                IconButton(
                    onClick = { onEditUser(user) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton(
                    onClick = { deleteUser(user, users = users, setUsers = setUsers) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

/**
 * Composable function representing the edit user dialog.
 *
 * @param user The user object to edit.
 * @param onEditUser Callback function for saving the edited user.
 * @param onDismiss Callback function for dismissing the dialog.
 */
@Composable
fun EditUserDialog(user: User, onEditUser: (User) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var age by remember { mutableStateOf(user.age.toString()) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var editedUser by remember { mutableStateOf(user.copy()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp),
            elevation = 8.dp,
            color = Color.LightGray
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            ) {
                Text(
                    text = "Edit User",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            age = newValue
                        }
                    },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = {
                            if(firstName.isBlank() || lastName.isBlank() || age.isBlank() || email.isBlank() || phone.isBlank()) {
                                Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                            } else {
                                val newEditedUser = editedUser
                                newEditedUser.firstName = firstName
                                newEditedUser.lastName = lastName
                                newEditedUser.age = age.toInt()
                                newEditedUser.email = email
                                newEditedUser.phone = phone
                                onEditUser(newEditedUser)
                                onDismiss()
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

/**
 * Composable function representing a greeting.
 *
 * @param name The name to greet.
 */
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

/**
 * Preview function for the composable functions.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinalProjectAndroidTheme {
        Greeting("Android")
    }
}