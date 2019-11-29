package com.example.feelingsdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private var userEmail: EditText? = null
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val buttonReset = findViewById<View>(R.id.reset_password)
        buttonReset.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        userEmail = findViewById(R.id.email)

        val email = userEmail!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_SHORT).show()
            return
        } else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "Email does not exist in database", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
