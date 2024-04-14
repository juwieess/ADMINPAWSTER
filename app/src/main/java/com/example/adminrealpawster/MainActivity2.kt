package com.example.adminrealpawster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    private lateinit var book : ImageView
    private lateinit var user : ImageView
    private lateinit var edlete : Button
    private lateinit var groom : Button
    private lateinit var hotel : ImageView
    private lateinit var editHotel : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        book = findViewById(R.id.btnBook)
        user = findViewById(R.id.btnUser)
        edlete = findViewById(R.id.btnUplete)
        groom = findViewById(R.id.btnGroom)
        hotel = findViewById(R.id.btnHotel)
        editHotel = findViewById(R.id.btnEditHotel)

        // Set click listeners
        book.setOnClickListener {
            val i = Intent(this@MainActivity2, MainActivity5::class.java)
            startActivity(i)
        }

        user.setOnClickListener {
            val i = Intent(this@MainActivity2, UserlistActivity::class.java)
            startActivity(i)
        }

        edlete.setOnClickListener {
            val i = Intent(this@MainActivity2, MainActivity4::class.java)
            startActivity(i)
        }

        groom.setOnClickListener {
            val i = Intent(this@MainActivity2, EditBooking::class.java)
            startActivity(i)
        }

        hotel.setOnClickListener {
            val i = Intent(this@MainActivity2, HotelList::class.java)
            startActivity(i)
        }

        editHotel.setOnClickListener {
            val i = Intent(this@MainActivity2, MainActivity7::class.java)
            startActivity(i)
        }

        }
    }