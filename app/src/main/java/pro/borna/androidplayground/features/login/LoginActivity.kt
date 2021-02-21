package pro.borna.androidplayground.features.login

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pro.borna.androidplayground.R
import pro.borna.androidplayground.features.login.auth.Authentication
import pro.borna.androidplayground.features.login.auth.AuthenticationImpl


class LoginActivity : AppCompatActivity(R.layout.layout_login) {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var signInButton: Button
    private lateinit var signInAnonymouslyButton: Button

    private lateinit var authentication: Authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        signInButton = findViewById(R.id.sign_in_button)
        signInAnonymouslyButton = findViewById(R.id.sign_in_anonymously_button)

        authentication = AuthenticationImpl(FirebaseAuth.getInstance())

        GlobalScope.launch {
            authentication.userFlow.collect {
                Log.d("USER", "$it")
            }
        }

        signInButton.setOnClickListener {
            GlobalScope.launch {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                authentication.signIn(email, password)
            }
        }
        signInAnonymouslyButton.setOnClickListener {
            GlobalScope.launch {
                authentication.signIn()
            }
        }
    }

}