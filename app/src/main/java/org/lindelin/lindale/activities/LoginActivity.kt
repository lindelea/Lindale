package org.lindelin.lindale.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import org.lindelin.lindale.Application
import org.lindelin.lindale.R
import org.lindelin.lindale.models.OAuth
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

class LoginActivity : AppCompatActivity() {

    private lateinit var app: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as Application
        app.init()

        when (Preferences(this).getString(Keys.ACCESS_TOKEN)) {
            null -> initView()
            else -> openHome()
        }
    }

    fun loginButtonHasTapped(view: View) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        OAuth.login(this, email, password) {
            if (it) {
                openHome()
            }
        }
    }

    private fun initView() {
        setContentView(R.layout.activity_login)
    }

    private fun openHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
