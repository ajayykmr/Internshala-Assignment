package com.example.internshalaassignment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.internshalaassignment.R
import com.example.internshalaassignment.models.Workshop

class AppliedWorkshopListRecyclerViewAdapter(
    private val workshops: List<Workshop>
) : RecyclerView.Adapter<AppliedWorkshopListRecyclerViewAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWorkshopName: TextView = itemView.findViewById(R.id.tv_workshop_name)
        val tvWorkshopLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvWorkshopDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_applied_workshops_item_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return workshops.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            tvWorkshopName.text = workshops[position].workshopName
            tvWorkshopLocation.text = workshops[position].location
            tvWorkshopDate.text = workshops[position].workshopDate.toString()
        }
    }

}