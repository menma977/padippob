package com.padippob.controller

import android.os.AsyncTask
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class ImageController {
    class ReUploadImage(
        private val idUser: String,
        private val ktp: String,
        private val ktpAndSelf: String
    ) : AsyncTask<Void, Void, JSONObject>() {
        override fun doInBackground(vararg params: Void?): JSONObject {
            return try {
                val request = URL("https://www.padippob.com/api/upload/image/$idUser/$ktp/$ktpAndSelf").readText()
                println(request)
                JSONObject(request)
            } catch (e: Exception) {
                e.printStackTrace()
                JSONObject("{Status: 1, Message: 'internet tidak setabil'}")
            }
        }

    }
}