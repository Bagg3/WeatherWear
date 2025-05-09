package com.mad.weatherwear.screens.authentication

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthenticationService {

private val auth = FirebaseAuth.getInstance()

    suspend fun signUp(email: Email, password: Password): AuthResult {
        return try {
          val result =  auth.createUserWithEmailAndPassword(email.value, password.value).await().user ?:
            return AuthResult(Status.ERROR, null)

            val userEmail = result.email?.let { Email(it) } ?: return AuthResult(Status.ERROR, null)

            val user = User(result.uid, userEmail)
            AuthResult(Status.OK, user)
        } catch (e: Exception) {
            AuthResult(Status.ERROR, null)
        }
    }

    suspend fun signIn(email: Email, password: Password): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email.value, password.value).await().user ?:
                return AuthResult(Status.ERROR, null)

            val userEmail = result.email?.let { Email(it) } ?: return AuthResult(Status.ERROR, null)

            val user = User(result.uid, userEmail)
            AuthResult(Status.OK, user)
        } catch (e: Exception) {
            AuthResult(Status.ERROR, null)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
