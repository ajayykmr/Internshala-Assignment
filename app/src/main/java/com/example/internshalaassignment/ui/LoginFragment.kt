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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var etUserID: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private var sharedPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private lateinit var database: LocalDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //initialisation
        sharedPref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        editor = sharedPref?.edit()
        database = LocalDatabase.getDatabase(requireContext())
        etUserID = view.findViewById(R.id.et_user_id)
        etPassword = view.findViewById(R.id.et_password)
        btnLogin = view.findViewById(R.id.btn_login)
        btnRegister = view.findViewById(R.id.btn_register)


        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnLogin.setOnClickListener {
            val username = etUserID.text.toString()
            val pass = etPassword.text.toString()

            if (username.isNotEmpty() && pass.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val user = async { database.dao().getUser(username) }.await()

                    if (user != null) {
                        if (user.password == pass) {
                            editor?.apply {
                                putString("currentUser", username)
                                putBoolean("isUserLoggedIn", true)
                                apply()
                            }
                            lifecycleScope.launch(Dispatchers.Main)
                            {
                                Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                        } else {
                            lifecycleScope.launch {
                                Toast.makeText(context, "Wrong PassWord", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "This user is not present", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Pleast enter a valid username and passeword",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


}