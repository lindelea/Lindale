package org.lindelin.lindale.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import org.lindelin.lindale.R
import org.lindelin.lindale.models.OAuth
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (Preferences(this).getString(Keys.ACCESS_TOKEN)) {
            null -> initView()
            else -> openHome()
        }
    }

    fun loginButtonHasTapped(view: View) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        OAuth.login(email, password) {
            it?.let {
                it.save(this)
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
