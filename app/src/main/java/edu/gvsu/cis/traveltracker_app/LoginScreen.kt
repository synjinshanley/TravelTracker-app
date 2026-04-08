package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onCreateNewAccount: () -> Unit, onGuestLogin: () -> Unit, onLogin: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.LightGray),
        // These properties will center all children of the Column
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Login",
            modifier = modifier
                .padding(top = 50.dp, bottom = 50.dp),
            color = Color(2,38,88),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )


        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.padding(top = 30.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "Continue as guest",
            modifier = Modifier
                .padding(top = 80.dp)
                .clickable {
                    onGuestLogin()
                },
            color = Color(2, 38, 88),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Create new account",
            modifier = Modifier
                .padding(top = 20.dp)
                .clickable {
                    onCreateNewAccount()
                },
            color = Color(2, 38, 88),
            fontWeight = FontWeight.Bold
        )

        Button(onClick = { onLogin()},

            modifier = Modifier
                .fillMaxWidth(0.33f)
                .padding(top = 80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(2, 38, 88))) {
            Text("Login")
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