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

class MainActivity7 : AppCompatActivity() {

    private lateinit var icNumberEditText: EditText
    private lateinit var petQEditText: EditText
    private lateinit var petTEditText: EditText
    private lateinit var petDEditText: EditText
    private lateinit var petOEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton : Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)

        icNumberEditText = findViewById(R.id.icNum)
        petQEditText = findViewById(R.id.petQ)
        petTEditText = findViewById(R.id.petT)
        petDEditText = findViewById(R.id.petD)
        petOEditText = findViewById(R.id.petO)
        updateButton = findViewById(R.id.btnReg)
        deleteButton = findViewById(R.id.btnDelete)

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Hotel")

        // Set click listeners
        deleteButton.setOnClickListener {
            val i = Intent(this@MainActivity7, MainActivity9::class.java)
            startActivity(i)
        }

        // Get the IC number from the intent extras
        val icNumber = intent.getStringExtra("icNumber") ?: ""
        icNumberEditText.setText(icNumber)

        // Fetch existing data from Firebase
        databaseReference.child(icNumber).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val hotel = dataSnapshot.getValue(Hotel::class.java)
                    hotel?.let {
                        petQEditText.setText(it.petQ ?: "")
                        petTEditText.setText(it.petT ?: "")
                        petDEditText.setText(it.petD ?: "")
                        petOEditText.setText(it.petO ?: "")
                    }
                } else {
                    Toast.makeText(this@MainActivity7, "No data found for the provided IC number", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity7, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Update button click listener
        updateButton.setOnClickListener {
            val icNumber = icNumberEditText.text.toString().trim()
            val petQ = petQEditText.text.toString().trim()
            val petT = petTEditText.text.toString().trim()
            val petD = petDEditText.text.toString().trim()
            val petO = petOEditText.text.toString().trim()

            if (icNumber.isEmpty() || petQ.isEmpty() || petT.isEmpty() || petD.isEmpty() || petO.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save the updated data
                saveData(icNumber, petQ, petT, petD, petO)
            }
        }
    }

    private fun saveData(icNumber: String, petQ: String, petT: String, petD: String, petO: String) {
        val hotel = Hotel(icNumber, petQ, petT, petD, petO)

        // Update data in the database
        databaseReference.child(icNumber).setValue(hotel)
            .addOnSuccessListener {
                Toast.makeText(this@MainActivity7, "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@MainActivity7, "Failed to update data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
