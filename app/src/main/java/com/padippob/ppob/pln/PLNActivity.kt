package com.padippob.ppob.pln

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.padippob.R
import com.padippob.controller.ProductController
import com.padippob.controller.ProgressBar
import com.padippob.controller.RequestToken
import com.padippob.model.UserSession
import kotlinx.android.synthetic.main.activity_pln.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PLNActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pln)

        val progressBar = ProgressBar(this)
        progressBar.openDialog()

        val userSession = UserSession(this)
        var type = ""

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)
        BalanceTextView.text = "Saldo anda saat ini : ${numberFormat.format(userSession.getInteger("balance"))}"

        val arrayCodeProduct = ArrayList<String>()
        val arrayNameProduct = ArrayList<String>()

        val productController = ProductController(
            userSession.getString("username").toString(),
            userSession.getString("code").toString()
        ).execute()
        val responseProduct = productController.get()
        val arrayProduct = responseProduct.getJSONObject(0).getJSONArray("PLN")
        for (value in 0 until arrayProduct.length() - 1) {
            arrayNameProduct.add(numberFormat.format("${arrayProduct.getJSONObject(value)["code"].toString().replace("PLN", "")}000".toInt()))
            arrayCodeProduct.add(arrayProduct.getJSONObject(value)["code"].toString())
        }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayNameProduct)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        ProductSpinner.adapter = spinnerAdapter

        Handler().postDelayed({
            progressBar.closeDialog()
        }, 500)

        ProductSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (parent.count > 1) {
                    type = arrayCodeProduct[position]
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        ContinueButton.setOnClickListener {
            progressBar.openDialog()
            when {
                PhoneNumberEditText.text.toString().isEmpty() -> {
                    Toast.makeText(this, "No Telfon tidak boleh kosong.", Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        progressBar.closeDialog()
                    }, 500)
                }
                TokenNumberEditText.text.toString().isEmpty() -> {
                    Toast.makeText(this, "Token tidak boleh kosong.", Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        progressBar.closeDialog()
                    }, 500)
                }
                else -> {
                    val username = userSession.getString("username").toString()
                    val code = userSession.getString("code").toString()
                    val phone = PhoneNumberEditText.text.toString().replace("-", "").replace("+62", "0").replace(" ", "")
                    val token = TokenNumberEditText.text.toString()
                    println(type)
                    val postPaidController = RequestToken(username, code, phone, token, type).execute()
                    val response = postPaidController.get()
                    println(response)
                    if (response["Status"].toString() == "0") {
                        progressBar.closeDialog()
                        val goTo = Intent(this, PLNValidationActivity::class.java).putExtra("response", response.toString())
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
