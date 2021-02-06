package pro.borna.androidplayground.features.login.auth

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class InvalidCredentialsException(cause: FirebaseAuthInvalidCredentialsException) : Exception(cause)

class InvalidUserException : Exception {

    constructor(cause: FirebaseAuthInvalidUserException) : super(cause)

    constructor(message: String) : super(message)
}

class UserCollisionException(cause: FirebaseAuthUserCollisionException) : Exception(cause)

class WeakPasswordException(cause: FirebaseAuthWeakPasswordException) : Exception(cause)

internal fun Exception.wrap(): Exception {
    return when (this) {
        is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException(this)
        is FirebaseAuthInvalidUserException -> InvalidUserException(this)
        is FirebaseAuthUserCollisionException -> UserCollisionException(this)
        is FirebaseAuthWeakPasswordException -> WeakPasswordException(this)
        else -> this
    }
}
