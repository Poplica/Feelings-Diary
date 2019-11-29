package com.example.feelingsdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var viewEmail: TextView? = null
    private var viewPassword: EditText? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewEmail = findViewById<View>(R.id.email) as TextView
        viewPassword = findViewById<View>(R.id.password) as EditText
        mAuth = FirebaseAuth.getInstance()

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
            loginUserAccount()
        }
    }

    private fun loginUserAccount() {
        val email: String = viewEmail!!.text.toString()
        val password: String = viewPassword!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG).show()

                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.putExtra(UserMail, email)
                intent.putExtra(UserID, mAuth!!.currentUser!!.uid)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,"Login failed! Please try again later", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val UserMail = "com.example.tesla.myhomelibrary.UMail"
        const val UserID = "com.example.tesla.myhomelibrary.UID"
    }
}
