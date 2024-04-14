package com.example.adminrealpawster

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity6 : AppCompatActivity() {

    private lateinit var userEmailSpinner: Spinner
    private lateinit var removeButton: Button
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        userEmailSpinner = findViewById(R.id.spinnerUserEmail)
        removeButton = findViewById(R.id.button)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Customer")

        // Fetch email addresses from Firebase and populate the Spinner
        fetchUserEmails()

        removeButton.setOnClickListener {
            // Get the selected email from the Spinner
            val email = userEmailSpinner.selectedItem?.toString()

            // Remove the selected user from Firebase if an email is selected
            email?.let { removeUser(it) }
        }
    }

    private fun fetchUserEmails() {
        val userEmailsList = mutableListOf<String>()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val email = snapshot.child("email").getValue(String::class.java)
                    email?.let { userEmailsList.add(it) }
                }
                // Populate the Spinner with email addresses
                val adapter = ArrayAdapter<String>(this@MainActivity6, android.R.layout.simple_spinner_item, userEmailsList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                userEmailSpinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@MainActivity6, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeUser(email: String) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming there's only one matching entry
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(this@MainActivity6, "User removed successfully", Toast.LENGTH_SHORT).show()
                                // If needed, you can add code here to update UI or perform any other actions after removing the user
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@MainActivity6, "Failed to remove user: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    showErrorToast("No user found for the provided email")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showErrorToast("Database error: ${databaseError.message}")
            }
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this@MainActivity6, message, Toast.LENGTH_SHORT).show()
    }
}
