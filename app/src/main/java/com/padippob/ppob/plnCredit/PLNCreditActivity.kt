package com.padippob.ppob.plnCredit

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
import kotlinx.android.synthetic.main.activity_pln_credit.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PLNCreditActivity : AppCompatActivity() {

    private var type = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pln_credit)

        MenuActivity().validationSession(this)

        val progressBar = ProgressBar(this)
        progressBar.openDialog()

        val arrayListName = ArrayList<String>()
        arrayListName.add("BPJS KESEHATAN 1 BULAN")
        arrayListName.add("BPJS KESEHATAN 2 BULAN")
        arrayListName.add("BPJS KESEHATAN 3 BULAN")
        arrayListName.add("BPJS KESEHATAN 4 BULAN")
        arrayListName.add("BPJS KESEHATAN 5 BULAN")
        arrayListName.add("BPJS KESEHATAN 6 BULAN")
        arrayListName.add("BPJS KESEHATAN 7 BULAN")
        arrayListName.add("BPJS KESEHATAN 8 BULAN")
        arrayListName.add("BPJS KESEHATAN 9 BULAN")
        arrayListName.add("BPJS KESEHATAN 10 BULAN")
        arrayListName.add("BPJS KESEHATAN 11 BULAN")
        arrayListName.add("BPJS KESEHATAN 12 BULAN")

        val arrayCodeName = ArrayList<String>()
        arrayCodeName.add("ASBPJSKS;3;1")
        arrayCodeName.add("ASBPJSKS;3;2")
        arrayCodeName.add("ASBPJSKS;3;3")
        arrayCodeName.add("ASBPJSKS;3;4")
        arrayCodeName.add("ASBPJSKS;3;5")
        arrayCodeName.add("ASBPJSKS;3;6")
        arrayCodeName.add("ASBPJSKS;3;7")
        arrayCodeName.add("ASBPJSKS;3;8")
        arrayCodeName.add("ASBPJSKS;3;9")
        arrayCodeName.add("ASBPJSKS;3;10")
        arrayCodeName.add("ASBPJSKS;3;11")
        arrayCodeName.add("ASBPJSKS;3;12")

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
                        val goTo = Intent(this, PLNCreditValidationActivity::class.java).putExtra("response", response.toString())
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
