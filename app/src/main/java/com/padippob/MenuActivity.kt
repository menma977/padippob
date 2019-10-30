package com.padippob

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.padippob.controller.BalanceController
import com.padippob.model.UserSession

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        validationSession(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_menu,
                R.id.navigation_logout
            )
        )

        val buttonLogout = this.findViewById<View>(R.id.navigation_logout)
        buttonLogout.setOnClickListener {
            val userSession = UserSession(this)
            userSession.saveString("username", "")
            userSession.saveString("password", "")
            userSession.saveString("code", "")
            userSession.saveInteger("balance", 0)
            userSession.saveString("ktp", "")
            userSession.saveString("selfAndKTP", "")
            val goTo = Intent(this, MainActivity::class.java)
            startActivity(goTo)
            finish()
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun validationSession(context: Context) {
        try {
            val userSession = UserSession(context)
            if (userSession.getString("code").isNullOrEmpty()) {
                userSession.saveString("username", "")
                userSession.saveString("password", "")
                userSession.saveString("code", "")
                userSession.saveInteger("balance", 0)
                userSession.saveString("ktp", "")
                userSession.saveString("selfAndKTP", "")
                val goTo = Intent(this, MainActivity::class.java)
                startActivity(goTo)
                finish()
            }

            val balanceController = BalanceController(
                userSession.getString("username").toString(),
                userSession.getString("code").toString()
            ).execute()
            val responseBalance = balanceController.get()
            if (responseBalance["Status"].toString() == "1") {
                userSession.saveString("username", "")
                userSession.saveString("password", "")
                userSession.saveString("code", "")
                userSession.saveInteger("balance", 0)
                userSession.saveString("ktp", "")
                userSession.saveString("selfAndKTP", "")
                val goTo = Intent(this, MainActivity::class.java)
                startActivity(goTo)
                finish()
            } else {
                userSession.saveInteger("balance", responseBalance["Saldo"].toString().replace(".", "").toInt())
            }
        } catch (e : Exception) {
            val userSession = UserSession(context)
            userSession.saveString("username", "")
            userSession.saveString("password", "")
            userSession.saveString("code", "")
            userSession.saveInteger("balance", 0)
            userSession.saveString("ktp", "")
            userSession.saveString("selfAndKTP", "")
            val goTo = Intent(this, MainActivity::class.java)
            startActivity(goTo)
            finish()
        }
    }
}