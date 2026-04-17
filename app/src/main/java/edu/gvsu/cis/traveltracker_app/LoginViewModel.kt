package edu.gvsu.cis.traveltracker_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class LoginState(
    val error: String? = null,
    val inProgress: Boolean = false
)

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val isGuest: Boolean = false
)
class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _uid = MutableStateFlow<String?>(null)
    val uid = _uid.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    init {
        val current = auth.currentUser
        if (current != null) {
            _uid.value = current.uid
            loadProfile(current.uid)
        }
    }
    suspend fun signIn(email: String, password: String): String? {
        _loginState.update { it.copy(inProgress = true, error = null) }
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid
            _loginState.update { it.copy(inProgress = false) }
            _uid.value = uid
            if (uid != null) loadProfile(uid)
            uid
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
                db.collection("users").document(uid).set(userData).await()
                _userProfile.value = UserProfile(uid = uid, name = name, email = email)
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
        _userProfile.value = null
        _loginState.update { LoginState() }
    }

    fun continueAsGuest() {
        auth.signOut()          
        _uid.value = null
        _userProfile.value = UserProfile(isGuest = true)
        _loginState.update { LoginState() }
    }

    private fun loadProfile(uid: String) {
        viewModelScope.launch {
            try {
                val doc = db.collection("users").document(uid).get().await()
                _userProfile.value = UserProfile(
                    uid = uid,
                    name = doc.getString("name")  ?: "",
                    email = doc.getString("email") ?: auth.currentUser?.email ?: ""
                )
            } catch (e: Exception) {
                // If Firestore fails, fall back to what Firebase Auth knows
                _userProfile.value = UserProfile(
                    uid   = uid,
                    email = auth.currentUser?.email ?: ""
                )
            }
        }
    }
}