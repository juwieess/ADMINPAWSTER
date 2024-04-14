package com.example.adminrealpawster

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

class MainActivity5 : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var bookArrayList: ArrayList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booklist)

        userRecyclerView = findViewById(R.id.MainActivity5)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        bookArrayList = ArrayList()
        getBookData()
    }

    private fun getBookData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Book")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (bookSnapshot in snapshot.children) {
                        val book = bookSnapshot.getValue(Book::class.java)
                        book?.let {
                            bookArrayList.add(it)
                        }
                    }
                    userRecyclerView.adapter = BookAdapter(bookArrayList)
                } else {
                    Log.d("MainActivity5", "No books found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity5", "Failed to retrieve book data: ${error.message}")
            }
        })
    }
}
