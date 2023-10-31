package com.example.internshalaassignment

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private var sharedPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //nav-controller and bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView.setOnItemSelectedListener { true }
        bottomNavigationView.setupWithNavController(navController)

        //shared Preferences
        sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        editor = sharedPref?.edit()

        //setup custom action-bar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //if user is logged in
        if (sharedPref!!.getBoolean("isUserLoggedIn", false)){
            val view = bottomNavigationView.findViewById<View>(R.id.dashboardFragment)
            view.performClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //appbar menu
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            //appbar-menu
            R.id.miLogout -> {
                editor?.apply {
                    putBoolean("isUserLoggedIn", false)
                    putString("currentUser", "")
                    apply()
                }
                navController.navigate(R.id.workshopListFragment)
                navController.clearBackStack(R.id.workshopListFragment)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}
