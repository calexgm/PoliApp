package com.example.poliapp

import PoliSQLiteOpenHelper
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.poliapp.databinding.ActivityMainBinding
import com.example.poliapp.db.contracts.ProfileContract

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //creamos la db

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_inicio, R.id.nav_profile, R.id.nav_photos, R.id.nav_videos, R.id.nav_web,
                R.id.nav_buttons
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        loadInformationFromDB()

        return true
    }

    private fun loadInformationFromDB() {
        val dbHelper = PoliSQLiteOpenHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            ProfileContract.ProfileEntry.TABLE_NAME,
            arrayOf(
                ProfileContract.ProfileEntry.COLUMN_NAMES,
                ProfileContract.ProfileEntry.COLUMN_OCCUPATION,
                ProfileContract.ProfileEntry.COLUMN_IMAGE
            ),
            null,
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {

            val names = cursor.getString(cursor.getColumnIndexOrThrow(ProfileContract.ProfileEntry.COLUMN_NAMES))
            val occupation = cursor.getString(cursor.getColumnIndexOrThrow(ProfileContract.ProfileEntry.COLUMN_OCCUPATION))
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(ProfileContract.ProfileEntry.COLUMN_IMAGE))

            val menuImageProfile = findViewById<ImageView>(R.id.menuImageProfile)
            findViewById<TextView>(R.id.menuTextName)?.text = names
            findViewById<TextView>(R.id.menuTextOccupation)?.text = occupation
            if (!imagePath.isNullOrEmpty()) {
                val imageUri = Uri.parse(imagePath)
                menuImageProfile?.setImageURI(imageUri)
            } else {
                menuImageProfile?.setImageResource(R.drawable.perfil)
            }
        }

        cursor.close()
        db.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}