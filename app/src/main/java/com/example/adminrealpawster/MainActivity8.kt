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

class MainActivity8 : AppCompatActivity() {

    private lateinit var icNumberSpinner: Spinner
    private lateinit var removeButton: Button
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main8)

        icNumberSpinner = findViewById(R.id.spinnerIcNumber)
        removeButton = findViewById(R.id.button)

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("Book")

        // Fetch IC numbers from Firebase and populate the Spinner
        fetchIcNumbers()

        // Set click listener for the remove button
        removeButton.setOnClickListener {
            // Get the selected IC number from the Spinner
            val icNumber = icNumberSpinner.selectedItem?.toString()

            // Remove the selected booking from Firebase if an IC number is selected
            icNumber?.let { removeBooking(it) }
        }
    }

    private fun fetchIcNumbers() {
        val icNumberList = mutableListOf<String>()
        // Fetch IC numbers from Firebase database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val icNumber = snapshot.child("icNumber").getValue(String::class.java)
                    icNumber?.let { icNumberList.add(it) }
                }
                // Populate the Spinner with IC numbers
                val adapter = ArrayAdapter<String>(this@MainActivity8, android.R.layout.simple_spinner_item, icNumberList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                icNumberSpinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                showErrorToast("Database error: ${databaseError.message}")
            }
        })
    }

    private fun removeBooking(icNumber: String) {
        // Query the database to find the booking with the selected IC number
        databaseReference.orderByChild("icNumber").equalTo(icNumber).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming there's only one matching entry
                    for (snapshot in dataSnapshot.children) {
                        // Remove the booking entry
                        snapshot.ref.removeValue()
                            .addOnSuccessListener {
                                // Show success message
                                Toast.makeText(this@MainActivity8, "Booking removed successfully", Toast.LENGTH_SHORT).show()
                                // Additional UI updates or actions can be added here if needed
                            }
                            .addOnFailureListener {
                                // Show error message if removal fails
                                showErrorToast("Failed to remove booking: ${it.message}")
                            }
                    }
                } else {
                    // Show message if no booking found for the provided IC number
                    showErrorToast("No booking found for the provided IC number")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                showErrorToast("Database error: ${databaseError.message}")
            }
        })
    }

    private fun showErrorToast(message: String) {
        // Display error toast message
        Toast.makeText(this@MainActivity8, message, Toast.LENGTH_SHORT).show()
    }
}
