package com.example.adminrealpawster

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HotelList : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var hotelArrayList: ArrayList<Hotel>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_list)

        userRecyclerView = findViewById(R.id.hotelList)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        hotelArrayList = ArrayList()
        getHotelData()
    }

    private fun getHotelData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Hotel")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (hotelSnapshot in snapshot.children) {
                        val hotel = hotelSnapshot.getValue(Hotel::class.java)
                        hotel?.let {
                            hotelArrayList.add(it)
                        }
                    }
                    userRecyclerView.adapter = HotelAdapter(hotelArrayList)
                } else {
                    Log.d("HotelList", "No books found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HotelList", "Failed to retrieve book data: ${error.message}")
            }
        })
    }
}
