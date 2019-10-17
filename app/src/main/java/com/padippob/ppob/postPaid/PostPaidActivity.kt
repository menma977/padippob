package com.padippob.ppob.postPaid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.setMargins
import androidx.core.widget.doOnTextChanged
import com.padippob.R
import com.padippob.controller.HlrController
import com.padippob.controller.ProductController
import com.padippob.controller.RequestPostPaid
import com.padippob.model.UserSession
import kotlinx.android.synthetic.main.activity_post_paid.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView
import android.view.View
import com.padippob.MenuActivity
import com.padippob.controller.ProgressBar
import java.lang.Exception

class PostPaidActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_paid)

        val progressBar = ProgressBar(this)
        progressBar.openDialog()

        MenuActivity().validationSession(this)

        val optionImage = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            398
        )
        optionImage.setMargins(20)
        LogoImageView.layoutParams = optionImage
        LogoImageView.scaleType = ImageView.ScaleType.FIT_CENTER

        LogoImageView.setImageResource(R.mipmap.ic_launcher_foreground)

        PostPaidButton.isChecked = true

        val userSession = UserSession(this)

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)
        BalanceTextView.text = "Saldo anda saat ini : ${numberFormat.format(userSession.getInteger("balance"))}"

        val hlrController = HlrController(
            userSession.getString("username").toString(),
            userSession.getString("code").toString()
        ).execute()
        val responseHLR = hlrController.get()

        val arrayList = ArrayList<JSONObject>()

        for (value in 0 until responseHLR.length()) {
            arrayList.add(JSONObject(responseHLR[value].toString()))
        }

        val productController = ProductController(
            userSession.getString("username").toString(),
            userSession.getString("code").toString()
        ).execute()
        val responseProduct = productController.get()

        var productNameArrayList =ArrayList<String>()
        var productCodeArrayList =ArrayList<String>()
        productNameArrayList.add("Mohon isikan nomor terlebih dahulu")
        var spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNameArrayList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        ProductSpinner.adapter = spinnerAdapter

        Handler().postDelayed({
            progressBar.closeDialog()
        }, 500)

        var operator = ""
        var nominal = ""
        var type = ""

        val jsonConverterProductName = responseProduct.getJSONObject(0)

        PhoneNumberEditText.doOnTextChanged { text, _, _, _ ->
            val arrayResponse = arrayList.find { it["Hlr"].toString() == text.toString()}
            if (text.toString().length <= 4) {
                operator = arrayResponse?.get("Operator").toString()
                try {
                    when (operator) {
                        "TELKOMSEL" -> LogoImageView.setImageResource(R.drawable.telkomsel)
                        "INDOSAT" -> LogoImageView.setImageResource(R.drawable.indosat)
                        "XL" -> LogoImageView.setImageResource(R.drawable.xl)
                        "AXIS" -> LogoImageView.setImageResource(R.drawable.axis)
                        "SMART" -> LogoImageView.setImageResource(R.drawable.smartfren)
                        "THREE" -> LogoImageView.setImageResource(R.drawable.three)
                        else -> LogoImageView.setImageResource(R.mipmap.ic_launcher_foreground)
                    }

                    if (!arrayResponse?.get("Operator")?.toString().isNullOrEmpty() && arrayResponse?.get("Operator")?.toString() != "Default") {
                        if (operator != "Default" && operator.isNotEmpty()) {
                            val arrayProduct = jsonConverterProductName.getJSONArray(operator)
                            productNameArrayList = ArrayList()
                            for (value in 0 until arrayProduct.length() - 1) {
                                if (PostPaidButton.isChecked) {
                                    if (arrayProduct.getJSONObject(value).get("typeProduct").toString() == "REGULER") {
                                        productNameArrayList.add("Rp${arrayProduct.getJSONObject(value).get("code").toString()
                                            .replace("TELKOMSEL", "")
                                            .replace("INDOSAT", "")
                                            .replace("XL", "")
                                            .replace("AXIS", "")
                                            .replace("SMART", "")
                                            .replace("THREE", "")
                                            .replace("CERIA", "")
                                        }.000")
                                        //productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString())
                                        productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString()
                                        )
                                    }
                                } else {
                                    if (arrayProduct.getJSONObject(value).get("typeProduct").toString() == "DATA") {
                                        productNameArrayList.add(arrayProduct.getJSONObject(value).get("name").toString())
                                        //productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString())
                                        productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString()
                                        )
                                    }
                                }
                            }
                            spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNameArrayList)
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                            ProductSpinner.adapter = spinnerAdapter
                        }
                    } else {
                        productNameArrayList = ArrayList()
                        productCodeArrayList = ArrayList()
                        productNameArrayList.add("Nomor yang anda inputkan tidak valid")
                        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNameArrayList)
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                        ProductSpinner.adapter = spinnerAdapter
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }

        PostPaidButton.setOnClickListener {
            try {
                if (operator != "Default" && operator.isNotEmpty()) {
                    val arrayProduct = jsonConverterProductName.getJSONArray(operator)
                    productNameArrayList = ArrayList()
                    if (PostPaidButton.isChecked) {
                        for (value in 0 until arrayProduct.length() - 1) {
                            if (arrayProduct.getJSONObject(value).get("typeProduct").toString() == "REGULER") {
                                productNameArrayList.add("Rp${arrayProduct.getJSONObject(value).get("code").toString()
                                    .replace("TELKOMSEL", "")
                                    .replace("INDOSAT", "")
                                    .replace("XL", "")
                                    .replace("AXIS", "")
                                    .replace("SMART", "")
                                    .replace("THREE", "")
                                    .replace("CERIA", "")
                                }.000")
                                //productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString())
                                productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString()
                                )
                            }
                        }
                    }
                    type = ""
                    spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNameArrayList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    ProductSpinner.adapter = spinnerAdapter
                } else {
                    productNameArrayList = ArrayList()
                    productCodeArrayList = ArrayList()
                    productNameArrayList.add("Nomor yang anda inputkan tidak valid")
                    spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNameArrayList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    ProductSpinner.adapter = spinnerAdapter
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        DataButton.setOnClickListener {
            try {
                if (operator != "Default" && operator.isNotEmpty()) {
                    val arrayProduct = jsonConverterProductName.getJSONArray(operator)
                    productNameArrayList = ArrayList()
                    if (DataButton.isChecked) {
                        for (value in 0 until arrayProduct.length() - 1) {
                            if (arrayProduct.getJSONObject(value).get("typeProduct").toString() == "DATA") {
                                productNameArrayList.add(arrayProduct.getJSONObject(value).get("name").toString())
                                //productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString())
                                productCodeArrayList.add(arrayProduct.getJSONObject(value).get("code").toString()
                                )
                            }
                        }
                    }
                    type = "DATA"
                    spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNameArrayList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    ProductSpinner.adapter = spinnerAdapter
                } else {
                    productNameArrayList = ArrayList()
                    productCodeArrayList = ArrayList()
                    productNameArrayList.add("Nomor yang anda inputkan tidak valid")
                    spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNameArrayList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    ProductSpinner.adapter = spinnerAdapter
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        ProductSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (parent.count > 1) {
                    nominal = productCodeArrayList[position]
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                nominal = ""
            }
        }

        ContinueButton.setOnClickListener {
            progressBar.openDialog()
            if (PhoneNumberEditText.text.length >= 10 && nominal.isNotEmpty()) {
                onRequestPayment(
                    userSession.getString("username").toString(),
                    userSession.getString("code").toString(),
                    PhoneNumberEditText.text.toString().replace("-", "").replace("+62", "0").replace(" ", ""),
                    nominal,
                    type
                )
                Handler().postDelayed({
                    progressBar.closeDialog()
                }, 500)
            } else if(PhoneNumberEditText.text.length < 10) {
                Toast.makeText(this, "Nomoar Telfon yang anda inputkan kurang dari 10 digit.", Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    progressBar.closeDialog()
                }, 500)
            } else {
                Toast.makeText(this, "Provider tidak di temukan.", Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    progressBar.closeDialog()
                }, 500)
            }
        }
    }

    private fun onRequestPayment(username : String, code : String, phone : String, nominal : String, type : String) {
        try {
            val postPaidController = RequestPostPaid(username, code, phone, nominal, type).execute()
            val response = postPaidController.get()
            println(response)
            if (response["Status"].toString() == "1") {
                Toast.makeText(this, "Transaksi dengan nominal no hp yang sama hanya bisa di lakukan 1x12jam. Gunakan nominal lain.", Toast.LENGTH_LONG).show()
            } else {
                val goTo = Intent(this, PostPaidValidationActivity::class.java).putExtra("response", response.toString())
                startActivity(goTo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Produk atau Nomor Telfon tidak valid", Toast.LENGTH_LONG).show()
        }
    }
}
