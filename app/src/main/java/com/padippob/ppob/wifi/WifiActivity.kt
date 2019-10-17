package com.padippob.ppob.wifi

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
import kotlinx.android.synthetic.main.activity_wifi.*
import org.json.JSONArray
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class WifiActivity : AppCompatActivity() {

    private var type = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)

        MenuActivity().validationSession(this)

        val jsonArrayConverter = JSONArray("[" +
                "{ name: 'SPEEDY', code: 'SPEEDY;2' },\n" +
                "{ code: 'TKBIG;2', name: 'BIG TV' },\n" +
                "{ code: 'TKGEN100;2', name: 'GENFLIX (100.000)' },\n" +
                "{ code: 'TKGEN25;2', name: 'GENFLIX (25.000)' },\n" +
                "{ code: 'TKGEN50;2', name: 'GENFLIX (50.000)' },\n" +
                "{ code: 'TKINDVS;2', name: 'INDOVISION, TOPTV, OKEVISION' },\n" +
                "{ code: 'TKINNOV;2', name: 'INNOVATE TV' },\n" +
                "{ code: 'TKKV1000;2', name: 'K VISION (1.000.000)' },\n" +
                "{ code: 'TKKV100;2', name: 'K VISION (100.000)' },\n" +
                "{ code: 'TKKV125;2', name: 'K VISION (125.000)' },\n" +
                "{ code: 'TKKV150;2', name: 'K VISION (150.000)' },\n" +
                "{ code: 'TKKV175;2', name: 'K VISION (175.000)' },\n" +
                "{ code: 'TKKV200;2', name: 'K VISION (200.000)' },\n" +
                "{ code: 'TKKV250;2', name: 'K VISION (250.000)' },\n" +
                "{ code: 'TKKV300;2', name: 'K VISION (300.000)' },\n" +
                "{ code: 'TKKV50;2', name: 'K VISION (50.000)' },\n" +
                "{ code: 'TKKV500;2', name: 'K VISION (500.000)' },\n" +
                "{ code: 'TKKV75;2', name: 'K VISION (75.000)' },\n" +
                "{ code: 'TKKV750;2', name: 'K VISION (750.000)' },\n" +
                "{ code: 'TKNEX;2', name: 'NEX MEDIA' },\n" +
                "{ code: 'TKORG100;2', name: 'ORANGE TV (100.000)' },\n" +
                "{ code: 'TKORG300;2', name: 'ORANGE TV (300.000)' },\n" +
                "{ code: 'TKORG50;2', name: 'ORANGE TV (50.000)' },\n" +
                "{ code: 'TKORG80;2', name: 'ORANGE TV (80.000)' },\n" +
                "{ code: 'TKORANGE;2', name: 'ORANGE TV POSTPAID' },\n" +
                "{ code: 'TKSKYDEL1;2', name: 'SKYNINDO TV DELUXE 1 BLN (80.000)' },\n" +
                "{ code: 'TKSKYDEL12;2', name: 'SKYNINDO TV DELUXE 12 BLN (960.000)' },\n" +
                "{ code: 'TKSKYDEL3;2', name: 'SKYNINDO TV DELUXE 3 BLN (240.000)' },\n" +
                "{ code: 'TKSKYDEL6;2', name: 'SKYNINDO TV DELUXE 6 BLN (480.000)' },\n" +
                "{ code: 'TKSKYFAM1;2', name: 'SKYNINDO TV FAMILY 1 BLN (40.000)' },\n" +
                "{ code: 'TKSKYFAM12;2', name: 'SKYNINDO TV FAMILY 12 BLN (480.000)' },\n" +
                "{ code: 'TKSKYFAM3;2', name: 'SKYNINDO TV FAMILY 3 BLN (120.000)' },\n" +
                "{ code: 'TKSKYFAM6;2', name: 'SKYNINDO TV FAMILY 6 BLN (240.000)' },\n" +
                "{ code: 'TKSKYMAN1;2', name: 'SKYNINDO TV MANDARIN 1 BLN (140.000)' },\n" +
                "{ code: 'TKSKYMAN12;2', name: 'SKYNINDO TV MANDARIN 12 BLN (1.680.000)' },\n" +
                "{ code: 'TKSKYMAN3;2', name: 'SKYNINDO TV MANDARIN 3 BLN (420.000)' },\n" +
                "{ code: 'TKSKYMAN6;2', name: 'SKYNINDO TV MANDARIN 6 BLN (840.000)' },\n" +
                "{ code: 'TKTOPAS;2', name: 'TOPAS TV' },\n" +
                "{ code: 'TKTLKMV;2', name: 'TRANSVISION, TELKOMVISION, YESTV' },"+
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
                    println("$username, $code, $costumerID, $phoneNumber, $balance, $type")
                    val requestPayment = RequestPayment(username, code, costumerID, phoneNumber, balance, type).execute()
                    val response = requestPayment.get()
                    println(response)
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
