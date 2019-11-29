package com.example.feelingsdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewEmail = findViewById<View>(R.id.email)
        val viewPassword = findViewById<View>(R.id.password)
        val viewLoginForm = findViewById<View>(R.id.email_login_form)
        val viewProgress = findViewById<View>(R.id.login_progress)

        val viewSignUp = findViewById<View>(R.id.sign_up_link)
        viewSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        val viewForgotPW = findViewById<View>(R.id.forgot_password)
        viewForgotPW.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        val buttonLogin = findViewById<View>(R.id.log_in_button)
        buttonLogin.setOnClickListener {

        }
    }
}
