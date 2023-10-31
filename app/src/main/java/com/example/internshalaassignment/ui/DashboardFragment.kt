package com.example.internshalaassignment.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internshalaassignment.AppliedWorkshopListRecyclerViewAdapter
import com.example.internshalaassignment.R
import com.example.internshalaassignment.models.Workshop
import com.example.internshalaassignment.models.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var rvAppliedWorkshops: RecyclerView

    private var sharedPref: SharedPreferences? = null
    private lateinit var database: LocalDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialisation
        database = LocalDatabase.getDatabase(requireContext())
        sharedPref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        rvAppliedWorkshops = view.findViewById(R.id.rv_applied_workshops)

        setupDashboard()
    }


    private fun setupDashboard() {

        if (sharedPref != null && sharedPref!!.getBoolean("isUserLoggedIn", false)) {
            //user is logged in
            val username = sharedPref!!.getString("currentUser", "")!!

            lifecycleScope.launch(Dispatchers.IO) {
                val allWorkshops =  database.dao().getWorkshops()
                val user =  database.dao().getUser(username) !!

                val appliedWorkshops: MutableList<Workshop> = mutableListOf()

                for (workshop in allWorkshops) {
                    if (user.appliedWorkshopIDs.contains(workshop.workshopID)) {
                        appliedWorkshops.add(workshop)
                    }
                }

                lifecycleScope.launch(Dispatchers.Main) {
                    if (appliedWorkshops.isNotEmpty()) {
                        setupRecyclerView(appliedWorkshops)
                    } else {
                        Toast.makeText(context, "You have not applied to any workshop yet", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "Please Login First", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }

    }

    private fun setupRecyclerView(appliedWorkshops: MutableList<Workshop>) {
        rvAppliedWorkshops.adapter = AppliedWorkshopListRecyclerViewAdapter(appliedWorkshops,)
        rvAppliedWorkshops.layoutManager = LinearLayoutManager(context)
    }

}


