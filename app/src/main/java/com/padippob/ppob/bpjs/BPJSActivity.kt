package com.padippob.ppob.bpjs

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.padippob.MenuActivity
import com.padippob.R
import com.padippob.controller.ProgressBar
import com.padippob.controller.RequestPayment
import com.padippob.model.UserSession
import kotlinx.android.synthetic.main.activity_bpjs.*
import org.json.JSONArray
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BPJSActivity : AppCompatActivity() {

    private var type = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bpjs)

        MenuActivity().validationSession(this)

        val jsonArrayConverter = JSONArray("[" +
                "{ code: 'ASBPJSKS;3;1', name: 'BPJS KESEHATAN 1 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;2', name: 'BPJS KESEHATAN 2 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;3', name: 'BPJS KESEHATAN 3 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;4', name: 'BPJS KESEHATAN 4 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;5', name: 'BPJS KESEHATAN 5 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;6', name: 'BPJS KESEHATAN 6 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;7', name: 'BPJS KESEHATAN 7 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;8', name: 'BPJS KESEHATAN 8 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;9', name: 'BPJS KESEHATAN 9 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;10', name: 'BPJS KESEHATAN 10 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;11', name: 'BPJS KESEHATAN 11 BULAN' },\n" +
                "{ code: 'ASBPJSKS;3;12', name: 'BPJS KESEHATAN 12 BULAN' },"+
                "]")

        val progressBar = ProgressBar(this)
        progressBar.openDialog()

        val arrayListName = ArrayList<String>()
        val arrayCodeName = ArrayList<String>()

        for (value in 0 until jsonArrayConverter.length() - 1) {
            arrayCodeName.add(jsonArrayConverter.getJSONObject(value)["code"].toString())
            arrayListName.add(jsonArrayConverter.getJSONObject(value)["name"].toString())
        }

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayListName)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        ProductSpinner.adapter = spinnerAdapter

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)
        val userSession = UserSession(this)
        BalanceTextView.text = "Saldo anda saat ini : ${numberFormat.format(userSession.getInteger("balance"))}"

        Handler().postDelayed({
            progressBar.closeDialog()
        }, 500)

        ProductSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                type = arrayCodeName[position]
            }
        }

        ContinueButton.setOnClickListener {
            progressBar.openDialog()
            val username = userSession.getString("username").toString()
            val code = userSession.getString("code").toString()
            val costumerID = CustomerID.text.toString()
            val phoneNumber = PhoneNumberEditText.text.toString().replace("-", "").replace("+62", "0").replace(" ", "")
            val balance = userSession.getInteger("balance").toString()
            when {
                phoneNumber.isEmpty() -> {
                    Toast.makeText(this, "Nomor telfon tidak boleh kosong", Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        progressBar.closeDialog()
                    }, 500)
                }
                costumerID.isEmpty() -> {
                    Toast.makeText(this, "Id pelanggan tidak boleh kosong", Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        progressBar.closeDialog()
                    }, 500)
                }
                type.isEmpty() -> {
                    Toast.makeText(this, "Product Tidak Boleh Kosong", Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        progressBar.closeDialog()
                    }, 500)
                }
                else -> {
                    val requestPayment = RequestPayment(username, code, costumerID, phoneNumber, balance, type).execute()
                    val response = requestPayment.get()
                    if (response["Status"].toString() == "0") {
                        Handler().postDelayed({
                            progressBar.closeDialog()
                        }, 500)
                        val goTo = Intent(this, BPJSValidationActivity::class.java).putExtra("response", response.toString())
                        startActivity(goTo)
                    } else {
                        Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                        Handler().postDelayed({
                            progressBar.closeDialog()
                        }, 500)
                    }
                }
            }
        }
    }
}
