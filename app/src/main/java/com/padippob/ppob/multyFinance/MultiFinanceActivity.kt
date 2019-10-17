package com.padippob.ppob.multyFinance

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
import kotlinx.android.synthetic.main.activity_multy_finance.*
import org.json.JSONArray
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MultiFinanceActivity : AppCompatActivity() {

    private var type = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multy_finance)

        MenuActivity().validationSession(this)

        val jsonArrayConverter = JSONArray("[" +
                "{ code: 'LSADIRAL;1', name: 'ADIRA ELEKTRONIK' },\n" +
                "{ code: 'LSADIRA;1', name: 'ADIRA MOTOR' },\n" +
                "{ code: 'LSBIMA;1', name: 'Bima Finance' },\n" +
                "{ code: 'LSBAF;1', name: 'Bussan Auto Finance (BAF)' },\n" +
                "{ code: 'LSCLMB;1', name: 'COLUMBIA' },\n" +
                "{ code: 'LSFIF;1', name: 'FIF' },\n" +
                "{ code: 'LSMAF;1', name: 'MEGA AUTO FINANCE' },\n" +
                "{ code: 'LSMEGA;1', name: 'MEGA CENTRAL FINANCE' },\n" +
                "{ code: 'LSMPM;1', name: 'MPM Finance' },\n" +
                "{ code: 'LSOTO;1', name: 'Oto Finance' },\n" +
                "{ code: 'LSRADA;1', name: 'Radana Finance' },\n" +
                "{ code: 'LSWKF;1', name: 'WOKA FINANCE' },\n" +
                "{ code: 'LSWOM;1', name: 'WOM FINANCE' },"+
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
