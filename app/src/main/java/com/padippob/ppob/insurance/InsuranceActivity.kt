package com.padippob.ppob.insurance

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
import com.padippob.ppob.bpjs.BPJSValidationActivity
import kotlinx.android.synthetic.main.activity_insurance.*
import org.json.JSONArray
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class InsuranceActivity : AppCompatActivity() {

    private var type = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance)

        MenuActivity().validationSession(this)

        MenuActivity().validationSession(this)

        val jsonArrayConverter = JSONArray("[" +
                "{ code: 'ASCAR;1', name: 'CAR' },\n" +
                "{ code: 'ASPRU;2', name: 'PRUDENTIAL PREMI LANJUTAN' },\n" +
                "{ code: 'ASBINT1;2', name: 'ASURANSI BINTANG PAKET' },\n" +
                "{ code: 'ASBINT2;2', name: 'ASURANSI BINTANG PAKET 2' },\n" +
                "{ code: 'ASJWS;2', name: 'ASURANSI JIWASRAYA -' },\n" +
                "{ code: 'ASTOKIOS;2', name: 'TOKIO MARINE (SatuTagihan)' },\n" +
                "{ code: 'ASTOKIO;2', name: 'TOKIO MARINE (SemuaTagihan)' },"+
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
