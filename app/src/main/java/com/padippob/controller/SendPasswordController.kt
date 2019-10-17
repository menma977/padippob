package com.padippob.controller

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.padippob.model.UserSession
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL

class SendPasswordController(context : Context) : AsyncTask<Void, Void, JSONObject>() {
    @SuppressLint("StaticFieldLeak")
    private val thisContext = context
    override fun doInBackground(vararg params: Void?): JSONObject {
        return try {
            val userSession = UserSession(thisContext)
            val request = URL("https://www.padippob.com/api/send/data/${userSession.getString("email")}").readText()
            if (request == "true") {
                JSONObject("{Status: 0, Pesan: $request}")
            } else {
                JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
            }
        }catch (e : Exception) {
            e.printStackTrace()
            JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
        }
    }
}