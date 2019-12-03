package com.example.feelingsdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {
    private var userPassword: EditText? = null
    private var userConfirmPassword: EditText? = null
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val buttonChangePassword = findViewById<View>(R.id.change_password_btn)
        buttonChangePassword.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        userPassword = findViewById<View>(R.id.password) as EditText
        userConfirmPassword = findViewById<View>(R.id.confirm_password) as EditText

        val password = userPassword!!.text.toString()
        val confirmPassword = userConfirmPassword!!.text.toString()

        // empty field checks
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(applicationContext, "Please enter password again!", Toast.LENGTH_SHORT).show()
            return
        }

        // check if new password fields match
        if (TextUtils.equals(password, confirmPassword)) {
            mAuth.currentUser!!.updatePassword(password)
            Toast.makeText(applicationContext,"Password successfully changed!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }
    }
}
