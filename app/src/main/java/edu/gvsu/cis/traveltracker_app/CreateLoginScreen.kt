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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme

@Composable
fun CreateLoginScreen(modifier: Modifier = Modifier, onCreateLogin: () -> Unit, onBack: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }



    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.LightGray),
        // These properties will center all children of the Column
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
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.padding(top = 40.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.padding(top = 20.dp)
        )

        OutlinedTextField(
            value = repassword,
            onValueChange = { repassword = it },
            label = { Text("Re-Insert Password") },
            singleLine = true,
            modifier = Modifier.padding(top = 20.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email= it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.padding(top = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { onCreateLogin() },
                modifier = Modifier.weight(1f).padding(top = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(2, 38, 88) // Sets the background color

                )
            ) {
                Text("Create Login")
            }
            Button(
                onClick = { onBack()},
                modifier = Modifier.weight(1f).padding(top = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(2, 38, 88) // Sets the background color
                )
            ) {
                Text("Back")
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