package com.mad.weatherwear.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SignUp(signUp: (email: Email, password: Password) -> Unit) {
    var email by remember { mutableStateOf("testmail@uni.au.dk") }
    var password by remember { mutableStateOf("Test1234") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row {
            Text("Email:")
            TextField(
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }

        Row {
            Text("Password:")
            TextField(
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Button(onClick = {
            if (!Email.validate(email) || !Password.validate(password)) {
                errorMessage = "Invalid email or password format"
            } else {
                signUp(Email(email), Password(password))
            }
        }) { Text("Sign Up") }
    }
}