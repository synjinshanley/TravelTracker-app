package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun ProfileScreen(modifier: Modifier = Modifier, onChangeProfile: () -> Unit, onHome: () -> Unit) {

    var username by remember { mutableStateOf("Lauren_Applegate") }
    var address by remember { mutableStateOf("1234 Laker Ln, Allendale MI, 49401") }
    var email by remember { mutableStateOf("applegla@mail.gvsu.edu") }



    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally){

        Text(
            text = "Profile",
            modifier = modifier
                .padding(top = 50.dp, bottom = 20.dp),
            color = Color(2,38,88),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.profile3),
            contentDescription = null
        )

        Text ("Username", modifier = Modifier.padding(top = 20.dp), fontSize = 23.sp, fontWeight = FontWeight.Bold)

        Text ("$username", modifier = Modifier.padding(top = 20.dp), fontSize = 20.sp)

        Text ("Password", modifier = Modifier.padding(top = 20.dp), fontSize = 23.sp, fontWeight = FontWeight.Bold)

        Text ("*********", modifier = Modifier.padding(top = 20.dp), fontSize = 20.sp)

        Text ("Email", modifier = Modifier.padding(top = 20.dp), fontSize = 23.sp, fontWeight = FontWeight.Bold)

        Text ("$email", modifier = Modifier.padding(top = 20.dp), fontSize = 20.sp)

        Text ("Address", modifier = Modifier.padding(top = 20.dp), fontSize = 23.sp, fontWeight = FontWeight.Bold)

        Text ("$address", modifier = Modifier.padding(top = 20.dp), fontSize = 20.sp)

        Text(
            text = "Change Profile",
            modifier = Modifier
                .padding(top = 50.dp)
                .clickable { onChangeProfile() },
            color = Color(2, 38, 88),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = { onHome() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(2, 38, 88)
                )
            ) {
                Text("Home")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TravelTrakerappTheme {
        ProfileScreen(onChangeProfile = {}, onHome = {})
    }
}