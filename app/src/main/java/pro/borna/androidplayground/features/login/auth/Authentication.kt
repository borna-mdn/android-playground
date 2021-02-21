package pro.borna.androidplayground.features.login.auth

import kotlinx.coroutines.flow.Flow


interface Authentication {

    val user: User?

    val userFlow: Flow<User?>

    suspend fun createUser(email: String, password: String)

    suspend fun signIn()

    suspend fun signIn(email: String, password: String)

    fun signOut()

    suspend fun deleteUser()
}