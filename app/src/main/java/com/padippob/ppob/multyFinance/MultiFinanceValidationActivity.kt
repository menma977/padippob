package com.padippob.ppob.multyFinance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.padippob.MenuActivity
import com.padippob.R
import com.padippob.controller.PayPayment
import com.padippob.controller.ProgressBar
import com.padippob.model.UserSession
import kotlinx.android.synthetic.main.activity_multy_finance_validation.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class MultiFinanceValidationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multy_finance_validation)

        val progressBar = ProgressBar(this)
        progressBar.openDialog()

        MenuActivity().validationSession(this)
        val userSession = UserSession(this)
        val dataResponse = intent.getSerializableExtra("response").toString()
        val compriseJson = JSONObject(dataResponse)
        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)
        val username = userSession.getString("username").toString()
        val code = userSession.getString("code").toString()
        val firstBalance = userSession.getInteger("balance").toString()
        val type = compriseJson["Type"].toString()
        val clientID = compriseJson["IdPel"].toString()
        val clientName = compriseJson["NamaPelanggan"].toString()
        val price = compriseJson["JmlTagih"].toString()
        val admin = compriseJson["AdminBank"].toString()
        val totalPrice = compriseJson["TotalTagihan"].toString()
        val phoneNumber = compriseJson["NoHP"].toString()
        val remainingBalance = compriseJson["SisaSaldo"].toString()
        val ref = compriseJson["Ref"].toString()
        val periodic = compriseJson["PeriodeTagihan"].toString()

        PhoneNumberTextView.text = phoneNumber
        CustomerIDTextView.text = clientID
        CustomerNameTextView.text = clientName
        TypeTextView.text = type
        PeriodicTextView.text = periodic
        PriceTextView.text = numberFormat.format(price.toInt())
        FirstBalanceTextView.text = numberFormat.format(firstBalance.toInt())
        AdminTextView.text = numberFormat.format(admin.toInt())
        RemainingBalanceTextView.text = numberFormat.format(remainingBalance.toInt())

        Handler().postDelayed({
            progressBar.closeDialog()
        }, 500)

        BuyButton.setOnClickListener {
            val markupAdmin = MarkupAdminEditText.text.toString()
            if (markupAdmin.isNotEmpty()) {
                val payPayment = PayPayment(username, code, type, clientID, clientName, price, admin, markupAdmin, totalPrice, phoneNumber, remainingBalance, ref, periodic).execute()
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

        BackButton.setOnClickListener {
            finish()
        }
    }
}
