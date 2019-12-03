package com.example.feelingsdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private var userEmail: EditText? = null
    private var userPassword: EditText? = null
    private var userConfirmPassword: EditText? = null
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val buttonSignUp = findViewById<View>(R.id.email_sign_in_button) as Button
        buttonSignUp.setOnClickListener {
            registerNewUser()
        }
    }

    private fun registerNewUser() {
        userEmail = findViewById<View>(R.id.email) as EditText
        userPassword = findViewById<View>(R.id.password) as EditText
        userConfirmPassword = findViewById<View>(R.id.confirm_password) as EditText

        val email = userEmail!!.text.toString()
        val password = userPassword!!.text.toString()
        val confirmPassword = userConfirmPassword!!.text.toString()

        // empty field checks
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter an email", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(applicationContext, "Please enter password again!", Toast.LENGTH_SHORT).show()
            return
        }

        // if matching password fields, then create the account
        if (TextUtils.equals(password, confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,"Registration successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    } else {
                        Toast.makeText(applicationContext,"Registration failed! Please try again later", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }
    }
}
