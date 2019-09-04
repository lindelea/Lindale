package org.lindelin.lindale.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import org.lindelin.lindale.R
import org.lindelin.lindale.models.OAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginButtonHasTapped(view: View) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        OAuth.login(email, password) {
            it?.let {
                println(it.accessToken)
            }
        }
    }
}
