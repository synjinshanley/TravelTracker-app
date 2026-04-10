package edu.gvsu.cis.traveltracker_app

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

data class LoginState(
    val error: String? = null,
    val inProgress: Boolean = false
)

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _uid = MutableStateFlow<String?>(null)
    val uid = _uid.asStateFlow()

    suspend fun signIn(email: String, password: String): String? {
        _loginState.update { it.copy(inProgress = true, error = null) }
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()

            _loginState.update { it.copy(inProgress = false) }
            _uid.value = result.user?.uid
            result.user?.uid
        } catch (e: Exception) {
            _loginState.update { it.copy(error = e.localizedMessage, inProgress = false) }
            null
        }
    }

    suspend fun signUp(email: String, password: String, name: String): String? {
        _loginState.update { it.copy(inProgress = true, error = null) }
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid

            if (uid != null) {
                val userData = mapOf(
                    "email" to email,
                    "name" to name,
                    "createdAt" to FieldValue.serverTimestamp()
                )

                db.collection("users")
                    .document(uid)
                    .set(userData)
                    .await()
            }

            _loginState.update { it.copy(inProgress = false) }
            _uid.value = uid
            uid
        } catch (e: Exception) {
            _loginState.update { it.copy(error = e.localizedMessage, inProgress = false) }
            null
        }
    }

    fun signOut() {
        auth.signOut()
        _uid.value = null
        _loginState.update { LoginState() }
    }
}