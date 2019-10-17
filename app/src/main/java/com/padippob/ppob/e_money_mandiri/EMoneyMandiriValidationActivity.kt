package com.padippob.ppob.e_money_mandiri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.padippob.R
import com.padippob.controller.PayToken
import com.padippob.controller.ProgressBar
import kotlinx.android.synthetic.main.activity_emonay_mandiri_validation.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class EMoneyMandiriValidationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emonay_mandiri_validation)

        val progressBar = ProgressBar(this)
        progressBar.openDialog()

        val dataResponse = intent.getSerializableExtra("response").toString()
        val compriseJson = JSONObject(dataResponse)
        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)
        val username = compriseJson["Username"].toString()
        val code = compriseJson["IdLogin"].toString()
        val costumerID = compriseJson["IdPel"].toString()
        val type = compriseJson["Kode"].toString()
        val firstBalance = compriseJson["SaldoAwal"].toString()
        val price = compriseJson["Harga"].toString()
        val remainingBalance = compriseJson["SisaSaldo"].toString()
        val phone = compriseJson["NoHP"].toString()

        PhoneNumberTextView.text = phone
        TokenNumberEditTextTextView.text = costumerID
        PriceTextView.text = numberFormat.format(price.toInt())
        FirstBalanceTextView.text = numberFormat.format(firstBalance.toInt())
        RemainingBalanceTextView.text = numberFormat.format(remainingBalance.toInt())

        Handler().postDelayed({
            progressBar.closeDialog()
        }, 500)

        BuyButton.setOnClickListener {
            val markupAdmin = MarkupAdminEditText.text.toString()
            if (markupAdmin.isNotEmpty()) {
                val payPayment = PayToken(username, code, phone, type, costumerID, firstBalance, markupAdmin, price, remainingBalance).execute()
                val response = payPayment.get()
                if (response["Status"].toString() == "0") {
                    Handler().postDelayed({
                        progressBar.closeDialog()
                    }, 500)
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        progressBar.closeDialog()
                    }, 500)
                }
            } else {
                Toast.makeText(this, "Markup Admin tidak boleh kosong", Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    progressBar.closeDialog()
                }, 500)
            }
        }
    }
}
