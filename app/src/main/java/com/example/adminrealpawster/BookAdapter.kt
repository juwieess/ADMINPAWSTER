package com.example.adminrealpawster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter (private val userList: ArrayList<Book>) : RecyclerView.Adapter<BookAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.icNumber.text = currentItem.icNumber
        holder.petN.text = currentItem.petN
        holder.petT.text = currentItem.petT
        holder.petD.text = currentItem.petD
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icNumber: TextView = itemView.findViewById(R.id.tvic)
        val petN: TextView = itemView.findViewById(R.id.tvname)
        val petT: TextView = itemView.findViewById(R.id.tvtype)
        val petD: TextView = itemView.findViewById(R.id.tvdate)
    }
}
