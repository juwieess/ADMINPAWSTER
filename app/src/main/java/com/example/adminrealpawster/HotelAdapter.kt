package com.example.adminrealpawster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HotelAdapter(private val userList: ArrayList<Hotel>) : RecyclerView.Adapter<HotelAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hotel_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.icNumber?.text = currentItem.icNumber
        holder.petQ?.text = currentItem.petQ
        holder.petT?.text = currentItem.petT
        holder.petD?.text = currentItem.petD
        holder.petO?.text = currentItem.petO
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icNumber: TextView? = itemView.findViewById(R.id.tvnumber)
        val petQ: TextView? = itemView.findViewById(R.id.tvquantity)
        val petT: TextView? = itemView.findViewById(R.id.tvtype)
        val petD: TextView? = itemView.findViewById(R.id.tvdate)
        val petO: TextView? = itemView.findViewById(R.id.tvoptional)
    }
}