package com.example.adminrealpawster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userList : ArrayList<Customer>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]

        holder.email.text = currentitem.email
        holder.name.text = currentitem.name
        holder.password.text = currentitem.customerPassword
        holder.phone.text = currentitem.phoneNumber

    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.tvname)
        val email : TextView = itemView.findViewById(R.id.tvemail)
        val phone : TextView = itemView.findViewById(R.id.tvphone)
        val password : TextView = itemView.findViewById(R.id.tvpassword)

    }

}