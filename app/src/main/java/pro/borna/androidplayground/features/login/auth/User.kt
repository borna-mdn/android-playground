package pro.borna.androidplayground.features.login.auth


sealed class User {

    object Anonymous : User()

    data class Known(
        val name: String?,
        val email: String
    ) : User()
}