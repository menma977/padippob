package com.padippob.controller

import android.os.AsyncTask
import org.json.JSONArray
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class HlrController(private val username : String, private val code : String) : AsyncTask<Void, Void, JSONArray>() {
    override fun doInBackground(vararg params: Void?): JSONArray {
        try {
            val userAgent = "Mozilla/5.0"
            val url = URL("https://padippob.com/api/hlr.php")
            val httpURLConnection = url.openConnection() as HttpURLConnection

            //add request header
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("User-Agent", userAgent)
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
            httpURLConnection.setRequestProperty("Accept", "application/json")

            val urlParameters = "a=Hlr&username=${username}&idlogin=${code}"

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
                val response = JSONArray(inputData)
                input.close()
                return response
            } else {
                return JSONArray("[{Status: 1, Pesan: 'internet tidak setabil'}, {Status: 1, Pesan: 'internet tidak setabil'}]")
            }
        }catch (e : Exception) {
            e.printStackTrace()
            return JSONArray("[{Status: 1, Pesan: 'internet tidak setabil'}, {Status: 1, Pesan: 'internet tidak setabil'}]")
        }
    }

}