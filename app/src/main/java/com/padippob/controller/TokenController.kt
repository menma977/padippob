package com.padippob.controller

import android.os.AsyncTask
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RequestToken(
    private val username : String,
    private val code : String,
    private val phone : String,
    private val token : String,
    private val type : String
) : AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
        try {
            val userAgent = "Mozilla/5.0"
            val url = URL("https://padippob.com/api/isitoken.php")
            val httpURLConnection = url.openConnection() as HttpsURLConnection

            //add request header
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("User-Agent", userAgent)
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
            httpURLConnection.setRequestProperty("Accept", "application/json")

            val urlParameters = "a=ReqToken&username=${username}&idlogin=${code}&nohp=$phone&idpel=$token&type=$type"

            // Send post request
            httpURLConnection.doOutput = true
            val write = DataOutputStream(httpURLConnection.outputStream)
            write.writeBytes(urlParameters)
            write.flush()
            write.close()

            val responseCode = httpURLConnection.responseCode
            return if (responseCode == 200) {
                val input = BufferedReader(
                    InputStreamReader(httpURLConnection.inputStream)
                )

                val inputData: String = input.readLine()
                val response = JSONObject(inputData)
                input.close()
                response
            } else {
                JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
            }
        }catch (e : Exception) {
            e.printStackTrace()
            return JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
        }
    }
}

class PayToken(
    private val username : String,
    private val code : String,
    private val phone : String,
    private val payCode : String,
    private val token : String,
    private val firstBalance : String,
    private val markupAdmin : String,
    private val price : String,
    private val reamingBalance : String
) : AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
        try {
            val userAgent = "Mozilla/5.0"
            val url = URL("https://padippob.com/api/isitoken.php")
            val httpURLConnection = url.openConnection() as HttpsURLConnection

            //add request header
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("User-Agent", userAgent)
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
            httpURLConnection.setRequestProperty("Accept", "application/json")

            val urlParameters = "a=PayToken&username=$username&idlogin=$code&nohp=$phone&kode=$payCode" +
                    "&idpel=$token&saldoawal=$firstBalance&markup=$markupAdmin&harga=$price&sisasaldo=$reamingBalance"

            // Send post request
            httpURLConnection.doOutput = true
            val write = DataOutputStream(httpURLConnection.outputStream)
            write.writeBytes(urlParameters)
            write.flush()
            write.close()

            val responseCode = httpURLConnection.responseCode
            return if (responseCode == 200) {
                val input = BufferedReader(
                    InputStreamReader(httpURLConnection.inputStream)
                )

                val inputData: String = input.readLine()
                val response = JSONObject(inputData)
                input.close()
                response
            } else {
                JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
            }
        }catch (e : Exception) {
            e.printStackTrace()
            return JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
        }
    }
}