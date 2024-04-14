package com.example.adminrealpawster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Initialize the components
    private lateinit var btnLogin: Button
    private lateinit var eTE: EditText
    private lateinit var eTP: EditText

    // Declare Firebase Authentication
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance()

        // Declare the components
        btnLogin = findViewById(R.id.btnLogin)
        eTE = findViewById(R.id.eTEmail)
        eTP = findViewById(R.id.eTPassword)

        // Click listener for Login button
        btnLogin.setOnClickListener {
            val email = eTE.text.toString()
            val password = eTP.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                btnLogin(email, password)
            } else {
                Toast.makeText(this@MainActivity, "All fields are mandatory", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun btnLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val customerId = mAuth.currentUser
                    Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT)
                        .show()
                    // Navigate to the home page (MainActivity4)
                    val intent = Intent(this@MainActivity, MainActivity2::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this@MainActivity, "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
