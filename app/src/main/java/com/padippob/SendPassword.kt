package com.padippob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.padippob.controller.ProgressBar
import com.padippob.controller.SendPasswordController
import com.padippob.model.UserSession
import kotlinx.android.synthetic.main.activity_send_password.*
import java.lang.Exception

class SendPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_password)

        val userSession = UserSession(this)
        val progressBar = ProgressBar(this)

        ForgotPasswordButton.setOnClickListener {
            progressBar.openDialog()
            try {
                val email = Email.text.toString()
                userSession.saveString("email", email)
                val sendPasswordController = SendPasswordController(this).execute()
                val response = sendPasswordController.get()
                if (response["Status"].toString() == "0") {
                    Toast.makeText(this, "password anda telah di kirim via email", Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                } else {
                    Toast.makeText(this, "password anda gagal di kirim mungkin email anda salah", Toast.LENGTH_LONG).show()
                    progressBar.closeDialog()
                }
            }catch (e : Exception) {
                Toast.makeText(this, "data yang terkirim tidak valid / internet tidak setabil", Toast.LENGTH_LONG).show()
                progressBar.closeDialog()
            }
        }
    }
}
