package com.example.internshalaassignment.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internshalaassignment.R
import com.example.internshalaassignment.adapters.WorkshopsListRecyclerViewAdapter
import com.example.internshalaassignment.models.Workshop
import com.example.internshalaassignment.models.LocalDatabase
import com.example.internshalaassignment.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class WorkshopListFragment : Fragment(R.layout.fragment_workshop_list) {


    private var sharedPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private lateinit var database: LocalDatabase

    private lateinit var rvWorkshopList: RecyclerView

    private lateinit var allWorkshops: List<Workshop>
    private val appliedWorkshopIDs: MutableList<Int> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialisation
        database = LocalDatabase.getDatabase(requireContext())
        sharedPref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        editor = sharedPref?.edit()
        rvWorkshopList = view.findViewById(R.id.rv_workshops_list)


        //setup and fetch workshops from database
        lifecycleScope.launch(Dispatchers.IO) {
            setupDatabase()
            allWorkshops = database.dao().getWorkshops()

            if (sharedPref!=null && sharedPref!!.getBoolean("isUserLoggedIn", false))
            {
                //if user is logged in
                val username = sharedPref!!.getString("currentUser", "")!!
                val user = database.dao().getUser(username)!!

                appliedWorkshopIDs.addAll(user.appliedWorkshopIDs)
            }

            lifecycleScope.launch (Dispatchers.Main){
                setupRecyclerView(allWorkshops, appliedWorkshopIDs)
            }
        }
    }

    private fun onClickApply(position: Int) {

        lifecycleScope.launch {
            if (sharedPref != null && sharedPref!!.getBoolean("isUserLoggedIn", false)) {
                //user is logged in
                allWorkshops[position].registeredSeats++
                database.dao().upsertWorkshop(allWorkshops[position])

                appliedWorkshopIDs.add(allWorkshops[position].workshopID)

                val username = sharedPref!!.getString("currentUser", "")!!
                val user = database.dao().getUser(username)!!

                database.dao().upsertUser(User(user.username, user.password, appliedWorkshopIDs))
                rvWorkshopList.adapter?.notifyItemChanged(position)
            } else {
                //user is not logged in
                findNavController().navigate(R.id.action_workshopListFragment_to_loginFragment)
            }
        }
        rvWorkshopList.adapter?.notifyItemChanged(position)
    }


    private fun setupRecyclerView(
        allWorkshops: List<Workshop>,
        appliedWorkshopIDs: MutableList<Int>
    ) {
        rvWorkshopList.adapter =
            context?.let {
                WorkshopsListRecyclerViewAdapter(
                    allWorkshops,
                    it,
                    appliedWorkshopIDs,
                    ::onClickApply
                )
            }
        rvWorkshopList.layoutManager = LinearLayoutManager(context)
    }

    private suspend fun setupDatabase() {
        val dao = database.dao()

        if (database.dao().getWorkshops().isEmpty()) {
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 1",
                    "Jaipur",
                    5,
                    0,
                    LocalDate.parse("2023-11-01")
                )
            )
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 2",
                    "Delhi",
                    1,
                    0,
                    LocalDate.parse("2023-11-05")
                )
            )
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 3",
                    "Lucknow",
                    10,
                    0,
                    LocalDate.parse("2023-11-10")
                )
            )
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 4",
                    "Indore",
                    50,
                    0,
                    LocalDate.parse("2023-11-15")
                )
            )
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 5",
                    "Indore",
                    50,
                    0,
                    LocalDate.parse("2023-11-17")
                )
            )
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 6",
                    "Indore",
                    50,
                    0,
                    LocalDate.parse("2023-11-19")
                )
            )
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 7",
                    "Indore",
                    50,
                    0,
                    LocalDate.parse("2023-11-21")
                )
            )
            dao.upsertWorkshop(
                Workshop(
                    "Workshop 8",
                    "Indore",
                    50,
                    0,
                    LocalDate.parse("2023-11-23")
                )
            )
        }
    }
}