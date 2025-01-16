package com.example.inspireface_example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inspireface_example.R
import com.example.inspireface_example.bean.FaceManagerItem

class FaceManagerAdapter(var item: List<FaceManagerItem>) : RecyclerView.Adapter<FaceManagerAdapter.FaceManagerViewHolder>()  {


    class FaceManagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaceManagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.manager_item, parent, false)
        return FaceManagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: FaceManagerViewHolder, position: Int) {

    }
}