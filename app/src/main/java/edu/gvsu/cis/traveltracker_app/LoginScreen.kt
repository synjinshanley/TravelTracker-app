package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onCreateNewAccount: () -> Unit,
    onGuestLogin: () -> Unit,
    onLogin: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by loginViewModel.loginState.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            modifier = modifier.padding(top = 50.dp, bottom = 50.dp),
            color = Color(2, 38, 88),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.padding(top = 30.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 20.dp)
        )

        if (loginState.error != null) {
            Text(
                text = loginState.error ?: "",
                color = Color.Red,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Text(
            text = "Continue as guest",
            modifier = Modifier
                .padding(top = 80.dp)
                .clickable {
                    loginViewModel.continueAsGuest()
                    onGuestLogin()
                },
            color = Color(2, 38, 88),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Create new account",
            modifier = Modifier
                .padding(top = 20.dp)
                .clickable { onCreateNewAccount() },
            color = Color(2, 38, 88),
            fontWeight = FontWeight.Bold
        )

        if (loginState.inProgress) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 80.dp))
        } else {
            Button(
                onClick = {
                    scope.launch {
                        val uid = loginViewModel.signIn(email, password)
                        if (uid != null) onLogin()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .padding(top = 80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(2, 38, 88))
            ) {
                Text("Login")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TravelTrakerappTheme {
        LoginScreen(onCreateNewAccount = {}, onGuestLogin = {}, onLogin = {})
    }
}