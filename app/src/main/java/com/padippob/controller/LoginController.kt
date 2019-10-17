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
import java.net.HttpURLConnection
import java.net.URL

class LoginController(context : Context) : AsyncTask<Void, Void, JSONObject>() {
    @SuppressLint("StaticFieldLeak")
    private val thisContext = context
    override fun doInBackground(vararg params: Void?): JSONObject {
        try {
            val userSession = UserSession(thisContext)
            val userAgent = "Mozilla/5.0"
            val url = URL("https://www.padippob.com/api/login.php")
            val httpURLConnection = url.openConnection() as HttpURLConnection

            //add request header
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("User-Agent", userAgent)
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
            httpURLConnection.setRequestProperty("Accept", "application/json")

            val urlParameters = "a=ReqLogin&username=${userSession.getString("username")}&password=${userSession.getString("password")}"

            // Send post request
            httpURLConnection.doOutput = true
            val write = DataOutputStream(httpURLConnection.outputStream)
            write.writeBytes(urlParameters)
            write.flush()
            write.close()

            val responseCode = httpURLConnection.responseCode
            if (responseCode == 200) {
                val input = BufferedReader(
                    InputStreamReader(httpURLConnection.inputStream)
                )

                val inputData: String = input.readLine()
                val response = JSONObject(inputData)
                input.close()
                return response
            } else {
                return JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
            }
        }catch (e : Exception) {
            e.printStackTrace()
            return JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
        }
    }
}