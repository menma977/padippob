package com.padippob.controller

import android.annotation.SuppressLint
import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class BannerController : AsyncTask<Void, Void, JSONArray>() {
    @SuppressLint("StaticFieldLeak")
    override fun doInBackground(vararg params: Void?): JSONArray {
        return try {
            val request = URL("https://www.padippob.com/index.php/api/list/banner").readText()
            if (request.isNotEmpty()) {
                JSONArray(request)
            } else {
                JSONArray("{Status: 1, data: 'internet tidak setabil'}")
            }
        }catch (e : Exception) {
            e.printStackTrace()
            JSONArray("{Status: 1, data: 'internet tidak setabil'}")
        }
    }
}