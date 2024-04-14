package com.example.adminrealpawster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity4 : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        nameEditText = findViewById(R.id.eTName)
        passwordEditText = findViewById(R.id.eTPassword)
        phoneEditText = findViewById(R.id.eTPhone)
        updateButton = findViewById(R.id.btnReg)
        deleteButton = findViewById(R.id.btnDelete)

        // Get the name from the intent extras
        val name = intent.getStringExtra("CustomerName") ?: ""

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer")

        // Set click listeners
        deleteButton.setOnClickListener {
            val i = Intent(this@MainActivity4, MainActivity6::class.java)
            startActivity(i)
        }

        // Fetch existing data from Firebase based on name
        databaseReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming there's only one matching entry
                    for (snapshot in dataSnapshot.children) {
                        val customer = snapshot.getValue(Customer::class.java)
                        customer?.let {
                            // Set the retrieved data to EditText fields
                            nameEditText.setText(it.name ?: "")
                            passwordEditText.setText(it.customerPassword ?: "")
                            phoneEditText.setText(it.phoneNumber ?: "")
                        }
                    }
                } else {
                    showErrorToast("No data found for the provided name")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showErrorToast("Database error: ${databaseError.message}")
            }
        })

        // Update button click listener
        updateButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val customerPassword = passwordEditText.text.toString().trim()
            val phoneNumber = phoneEditText.text.toString().trim()

            if (name.isEmpty() || customerPassword.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Perform update based on name
                updateData(name, customerPassword, phoneNumber)
            }
        }
    }

    private fun updateData(name: String, customerPassword: String, phoneNumber: String) {
        val updatedData = mapOf(
            "customerPassword" to customerPassword,
            "phoneNumber" to phoneNumber
        )

        // Update data based on name
        databaseReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming there's only one matching entry
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.updateChildren(updatedData)
                            .addOnSuccessListener {
                                Toast.makeText(this@MainActivity4, "Data updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@MainActivity4, "Failed to update data: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    showErrorToast("No data found for the provided name")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showErrorToast("Database error: ${databaseError.message}")
            }
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this@MainActivity4, message, Toast.LENGTH_SHORT).show()
    }
}
