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

class MainActivity9 : AppCompatActivity() {

    private lateinit var icNumberSpinner: Spinner
    private lateinit var removeButton: Button
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main9)

        icNumberSpinner = findViewById(R.id.spinnerIcNumber1)
        removeButton = findViewById(R.id.button)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Hotel")

        // Fetch email addresses from Firebase and populate the Spinner
        fetchIcNumbers()

        removeButton.setOnClickListener {
            // Get the selected email from the Spinner
            val icNumber = icNumberSpinner.selectedItem?.toString()

            // Remove the selected user from Firebase if an email is selected
            icNumber?.let { removeBook(it) }
        }
    }

    private fun fetchIcNumbers() {
        val icNumberList = mutableListOf<String>()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val icNumber = snapshot.child("icNumber").getValue(String::class.java)
                    icNumber?.let { icNumberList.add(it) }
                }
                // Populate the Spinner with email addresses
                val adapter = ArrayAdapter<String>(this@MainActivity9, android.R.layout.simple_spinner_item, icNumberList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                icNumberSpinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@MainActivity9, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeBook(icNumber: String) {
        databaseReference.orderByChild("icNumber").equalTo(icNumber).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming there's only one matching entry
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(this@MainActivity9, "Booking removed successfully", Toast.LENGTH_SHORT).show()
                                // If needed, you can add code here to update UI or perform any other actions after removing the user
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@MainActivity9, "Failed to remove booking: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    showErrorToast("No user found for the provided ic number")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showErrorToast("Database error: ${databaseError.message}")
            }
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this@MainActivity9, message, Toast.LENGTH_SHORT).show()
    }
}
