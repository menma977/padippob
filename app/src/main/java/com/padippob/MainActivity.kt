package com.padippob

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.padippob.controller.LoginController
import com.padippob.controller.ProgressBar
import com.padippob.model.UserSession
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doRequestPermission()

        val progressBar = ProgressBar(this)
        val userSession = UserSession(this)

        if (userSession.getString("username").toString().isNotEmpty()
            && userSession.getString("passowrd").toString().isNotEmpty()
            && userSession.getString("code").toString().isNotEmpty()
        ) {
            progressBar.openDialog()
            val goTo = Intent(this, MenuActivity::class.java)
            startActivity(goTo)
            finish()
            progressBar.closeDialog()
        }

        this.LoginButton.setOnClickListener {
            progressBar.openDialog()
            try {
                val username = Username.text.toString()
                val password = Password.text.toString()
                if (username.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        userSession.saveString("username", Username.text.toString())
                        userSession.saveString("password", Password.text.toString())
                        val loginController = LoginController(this).execute()
                        val response = loginController.get()
                        if (response["Status"].toString() == "0") {
                            progressBar.closeDialog()
                            val goTo = Intent(this, Validation::class.java)
                            startActivity(goTo)
                        } else {
                            Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG)
                                .show()
                            progressBar.closeDialog()
                        }
                    } else {
                        Toast.makeText(this, "password tidak boleh kosong", Toast.LENGTH_LONG)
                            .show()
                        progressBar.closeDialog()
                    }
                } else {
                    Toast.makeText(this, "username tidak boleh kosong", Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "data yang terkirim tidak valid / internet tidak setabil",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.closeDialog()
            }
        }

        ForgotPasswordButton.setOnClickListener {
            val goTo = Intent(this, SendPassword::class.java)
            startActivity(goTo)
            //finish()
        }

        RegisterButton.setOnClickListener {
            progressBar.openDialog()
            try {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.padippob.com/index.php/register")
                )
                startActivity(browserIntent)
                progressBar.closeDialog()
            } catch (e: Exception) {
                progressBar.closeDialog()
            }
        }

        Password.setOnClickListener {
            progressBar.openDialog()
            try {
                val username = Username.text.toString()
                val password = Password.text.toString()
                if (username.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        userSession.saveString("username", Username.text.toString())
                        userSession.saveString("password", Password.text.toString())
                        val loginController = LoginController(this).execute()
                        val response = loginController.get()
                        println("username : $username | password : $password")
                        println(response)
                        if (response["Status"].toString() == "0") {
                            progressBar.closeDialog()
                            val goTo = Intent(this, Validation::class.java)
                            startActivity(goTo)
                        } else {
                            Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG)
                                .show()
                            progressBar.closeDialog()
                        }
                    } else {
                        Toast.makeText(this, "password tidak boleh kosong", Toast.LENGTH_LONG)
                            .show()
                        progressBar.closeDialog()
                    }
                } else {
                    Toast.makeText(this, "username tidak boleh kosong", Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "data yang terkirim tidak valid / internet tidak setabil",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.closeDialog()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun doRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 100
            )
            val session = UserSession(this)
            session.saveString("username", "")
            session.saveString("password", "")
            session.saveString("code", "")
            session.saveInteger("balance", 0)
            session.saveString("ktp", "")
            session.saveString("selfAndKTP", "")
        }
    }
}
