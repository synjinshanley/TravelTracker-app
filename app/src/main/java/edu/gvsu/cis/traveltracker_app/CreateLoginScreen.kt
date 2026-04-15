package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme
import kotlinx.coroutines.launch

@Composable
fun CreateLoginScreen(modifier: Modifier = Modifier, onCreateLogin: () -> Unit, onBack: () -> Unit, loginViewModel: LoginViewModel = viewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val loginState by loginViewModel.loginState.collectAsState()
    val scope = rememberCoroutineScope()
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.LightGray),

        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Create Login",
            modifier = modifier
                .padding(top = 50.dp, bottom = 30.dp),
            color = Color(2,38,88),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.profile3),
            contentDescription = null
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.padding(top = 40.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.padding(top = 40.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.padding(top = 20.dp)
        )

        OutlinedTextField(
            value = repassword,
            onValueChange = { repassword = it },
            label = { Text("Re-Enter Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.padding(top = 20.dp)
        )

        val errorMessage = passwordError ?: loginState.error
        if (errorMessage != null) {
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 12.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (loginState.inProgress) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 80.dp))
            } else {
                Button(
                    onClick = {
                        if (password != repassword) {
                            passwordError = "Passwords do not match"
                        } else if (password.length < 6) {
                            passwordError = "Password must be at least 6 characters"

                        } else {
                            passwordError = null
                            scope.launch {
                                val uid = loginViewModel.signUp(email, password, name)
                                if (uid != null) onCreateLogin()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).padding(top = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88)

                    )
                ) {
                    Text("Create Login")
                }
                Button(
                    onClick = { onBack() },
                    modifier = Modifier.weight(1f).padding(top = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88)
                    )
                ) {
                    Text("Back")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateLoginScreenPreview() {
    TravelTrakerappTheme {
        CreateLoginScreen(onCreateLogin = {}, onBack = {})
    }
}