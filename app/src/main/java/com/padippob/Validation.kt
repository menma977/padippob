package com.padippob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.padippob.controller.ProgressBar
import com.padippob.controller.ValidationController
import com.padippob.model.UserSession
import kotlinx.android.synthetic.main.activity_validation.*

class Validation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validation)

        val progressBar = ProgressBar(this)
        val userSession = UserSession(this)

        LoginButton.setOnClickListener {
            progressBar.openDialog()
            if (PasswordTextView.text.toString().isNotEmpty()) {
                val validationController = ValidationController(
                    userSession.getString("username").toString(),
                    PasswordTextView.text.toString()
                ).execute()
                val response = validationController.get()
                println(response)
                if (response["Status"].toString() == "0") {
                    userSession.saveString("code", response["IdLogin"].toString())
                    userSession.saveString("apiKey", response["IdLogin"].toString())
                    userSession.saveString("idUser", response["IdUser"].toString())
                    userSession.saveString("ktp", response["ktp"].toString())
                    userSession.saveString("selfAndKTP", response["selfKTP"].toString())
                    progressBar.closeDialog()
                    val goTo = Intent(this, MenuActivity::class.java)
                    startActivity(goTo)
                    finish()
                } else {
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                }
            } else {
                Toast.makeText(this, "password tidak boleh kosong", Toast.LENGTH_LONG).show()
                progressBar.closeDialog()
            }
        }

        PasswordTextView.setOnClickListener {
            progressBar.openDialog()
            if (PasswordTextView.text.toString().isNotEmpty()) {
                val validationController = ValidationController(
                    userSession.getString("username").toString(),
                    PasswordTextView.text.toString()
                ).execute()
                val response = validationController.get()
                println(response)
                if (response["Status"].toString() == "0") {
                    userSession.saveString("code", response["IdLogin"].toString())
                    userSession.saveString("apiKey", response["IdLogin"].toString())
                    userSession.saveString("idUser", response["IdUser"].toString())
                    progressBar.closeDialog()
                    val goTo = Intent(this, MenuActivity::class.java)
                    startActivity(goTo)
                    finish()
                } else {
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                }
            } else {
                Toast.makeText(this, "password tidak boleh kosong", Toast.LENGTH_LONG).show()
                progressBar.closeDialog()
            }
        }
    }
}
