package com.padippob.menu

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.padippob.MenuActivity
import com.padippob.R
import com.padippob.controller.ProgressBar
import com.padippob.controller.TopUpController
import com.padippob.model.UserSession
import kotlinx.android.synthetic.main.activity_top_up.*

class TopUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        MenuActivity().validationSession(this)

        val progressBar = ProgressBar(this)
        val userSession = UserSession(this)

        TopUpButton.setOnClickListener {
            progressBar.openDialog()
            if (NominalEditText.text.toString().isNotEmpty()) {
                val topUpController = TopUpController(
                    userSession.getString("username").toString(),
                    userSession.getString("code").toString(),
                    NominalEditText.text.toString().replace(".", "").replace(",", "")
                ).execute()
                val response = topUpController.get()
                println(response)
                if (response["Status"].toString() == "0") {
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                } else {
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                }
            } else {
                Toast.makeText(this, "Nominal tidak boleh kosong", Toast.LENGTH_LONG).show()
                progressBar.closeDialog()
            }
        }
    }
}
