package pro.borna.androidplayground.features.login.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Auth(
    private val firebaseAuth: FirebaseAuth
) {

    val user: User?
        get() = getCurrentUser()

    @OptIn(ExperimentalCoroutinesApi::class)
    val userFlow: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            sendBlocking(getCurrentUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    suspend fun createUser(email: String, password: String) {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(task.exception!!.wrap())
                    }
                }
        }
    }

    suspend fun signIn() {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(task.exception!!.wrap())
                    }
                }
        }
    }

    suspend fun signIn(email: String, password: String) {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(task.exception!!.wrap())
                    }
                }
        }
    }

    fun signOut() {
        return firebaseAuth.signOut()
    }

    suspend fun deleteUser() {
        return suspendCoroutine { continuation ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Unit)
                        } else {
                            continuation.resumeWithException(task.exception!!.wrap())
                        }
                    }
            } else {
                continuation.resumeWithException(InvalidUserException("User is not signed in"))
            }
        }
    }

    private fun getCurrentUser(): User? {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            val email = user.email
            if (email != null && email.isNotBlank()) {
                User.Known(name = user.displayName, email = email)
            } else {
                User.Anonymous
            }
        } else {
            null
        }
    }
}