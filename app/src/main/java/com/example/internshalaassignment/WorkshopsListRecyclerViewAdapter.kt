package com.example.internshalaassignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.internshalaassignment.models.Workshop

class WorkshopsListRecyclerViewAdapter(
    private val workshops: List<Workshop>,
    private val context: Context,
    private val appliedWorkshopIDs: MutableList<Int>,
    private val onClickApply: (Int) -> Unit,
) :
    RecyclerView.Adapter<WorkshopsListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWorkshopName: TextView = itemView.findViewById(R.id.tv_workshop_name)
        val tvWorkshopLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvWorkshopDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvVacantSeats: TextView = itemView.findViewById(R.id.tv_vacant_seats)
        val btnApply: Button = itemView.findViewById(R.id.btn_apply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_workshop_list_item_view, parent, false)
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
            tvVacantSeats.text =
                (workshops[position].totalSeats - workshops[position].registeredSeats).toString()

            if (tvVacantSeats.text.toString()=="0"){
                setButtonNoSeats(btnApply)
            } else if (appliedWorkshopIDs.contains(workshops[position].workshopID)) {
                setButtonApplied(btnApply)
            } else {
                setButtonNormal(btnApply, position)
            }
        }
    }

    private fun setButtonNoSeats(btnApply: Button){
        btnApply.apply {
            text = "No Seats Left"
            setBackgroundColor(context.getColor(R.color.secondary))
            setOnClickListener {}
        }
    }
    private fun setButtonApplied(btnApply: Button) {
        btnApply.apply {
            text = "Applied"
            setBackgroundColor(context.getColor(R.color.secondary))
            setOnClickListener {}
        }
    }

    private fun setButtonNormal(btnApply: Button, position: Int) {
        btnApply.apply {
            text = "Apply"
            setBackgroundColor(context.getColor(R.color.secondary))
            setOnClickListener {
                onClickApply(position)
            }

        }
    }

}

