package com.example.adminrealpawster

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

class EditBooking : AppCompatActivity() {

    private lateinit var icNumberEditText: EditText
    private lateinit var petNEditText: EditText
    private lateinit var petTEditText: EditText
    private lateinit var petDEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton : Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_booking)

        icNumberEditText = findViewById(R.id.icNumber)
        petNEditText = findViewById(R.id.petN)
        petTEditText = findViewById(R.id.petT)
        petDEditText = findViewById(R.id.petD)
        updateButton = findViewById(R.id.btnReg)
        deleteButton = findViewById(R.id.btnDelete)

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Book")

        // Set click listeners
        deleteButton.setOnClickListener {
            val i = Intent(this@EditBooking, MainActivity8::class.java)
            startActivity(i)
        }

        // Get the IC number from the intent extras
        val icNumber = intent.getStringExtra("icNumber") ?: ""
        icNumberEditText.setText(icNumber)

        // Fetch existing data from Firebase
        databaseReference.child(icNumber).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val book = dataSnapshot.getValue(Book::class.java)
                    book?.let {
                        petNEditText.setText(it.petN ?: "")
                        petTEditText.setText(it.petT ?: "")
                        petDEditText.setText(it.petD ?: "")
                    }
                } else {
                    Toast.makeText(this@EditBooking, "No data found for the provided IC number", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EditBooking, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Update button click listener
        updateButton.setOnClickListener {
            val icNumber = icNumberEditText.text.toString().trim()
            val petN = petNEditText.text.toString().trim()
            val petT = petTEditText.text.toString().trim()
            val petD = petDEditText.text.toString().trim()

            if (icNumber.isEmpty() || petN.isEmpty() || petT.isEmpty() || petD.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save the updated data
                saveData(icNumber, petN, petT, petD)
            }
        }
    }

    private fun saveData(icNumber: String, petN: String, petT: String, petD: String) {
        val book = Book(icNumber, petN, petT, petD)

        // Update data in the database
        databaseReference.child(icNumber).setValue(book)
            .addOnSuccessListener {
                Toast.makeText(this@EditBooking, "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@EditBooking, "Failed to update data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
