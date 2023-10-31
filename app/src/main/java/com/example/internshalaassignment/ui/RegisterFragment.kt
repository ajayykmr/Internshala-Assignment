package com.example.internshalaassignment.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.internshalaassignment.R
import com.example.internshalaassignment.models.LocalDatabase
import com.example.internshalaassignment.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    private var sharedPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private lateinit var database: LocalDatabase
    private var registeredUsers: MutableSet<String> = mutableSetOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        editor = sharedPref?.edit()
        database = LocalDatabase.getDatabase(requireContext())

        //setupViews
        etUsername = view.findViewById(R.id.et_user_id)
        etPassword = view.findViewById(R.id.et_password)
        btnRegister = view.findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val pass = etPassword.text.toString()

            if (username.isNotEmpty() && pass.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val allUsers = async { database.dao().getUsers() }.await()
                    for (user in allUsers) {
                        registeredUsers.add(user.username)
                        findNavController().popBackStack()
                    }

                    if (registeredUsers.contains(username)) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "This user is already registered",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        database.dao().upsertUser(User(username, pass))
                        editor?.apply {
                            putBoolean("isUserLoggedIn", true)
                            putString("currentUser", username)
                            apply()
                        }

                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Please enter a valid username and password",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

}