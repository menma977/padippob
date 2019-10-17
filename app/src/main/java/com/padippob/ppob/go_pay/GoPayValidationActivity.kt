package com.padippob.ppob.go_pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.padippob.MenuActivity
import com.padippob.R
import com.padippob.controller.PayPostPaid
import kotlinx.android.synthetic.main.activity_go_pay_validation.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class GoPayValidationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_pay_validation)

        MenuActivity().validationSession(this)
        val dataResponse = intent.getSerializableExtra("response").toString()
        val compriseJson = JSONObject(dataResponse)
        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)
        PhoneNumberTextView.text = compriseJson["NoHP"].toString()
        PriceTextView.text = numberFormat.format(compriseJson["Harga"].toString().toInt())
        FirstBalanceTextView.text = numberFormat.format(compriseJson["SaldoAwal"].toString().toInt())
        RemainingBalanceTextView.text = numberFormat.format(compriseJson["SisaSaldo"].toString().toInt())

        BuyButton.setOnClickListener {
            if (MarkupAdminEditText.text.toString().isNotEmpty()) {
                val username = compriseJson["Username"].toString()
                val code = compriseJson["IdLogin"].toString()
                val phone = compriseJson["NoHP"].toString()
                val payCode = compriseJson["Kode"].toString()
                val nominal = compriseJson["Nominal"].toString()
                val firstBalance = compriseJson["SaldoAwal"].toString()
                val markupAdmin = MarkupAdminEditText.text.toString()
                val price = compriseJson["Harga"].toString()
                val remainingBalance = compriseJson["SisaSaldo"].toString()
                val payPostPaid = PayPostPaid(username, code, phone, payCode, nominal, firstBalance, markupAdmin, price, remainingBalance).execute()
                val responsePayPostPaid = payPostPaid.get()
                if (responsePayPostPaid["Status"].toString() == "0") {
                    Toast.makeText(this, responsePayPostPaid["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, responsePayPostPaid["Pesan"].toString(), Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Markup Admin tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
        }

        BackButton.setOnClickListener {
            finish()
        }
    }
}
