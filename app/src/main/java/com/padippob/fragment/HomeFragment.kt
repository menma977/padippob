package com.padippob.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.padippob.FinancialActivity
import com.padippob.ImageAdapter
import com.padippob.R
import com.padippob.controller.BalanceController
import com.padippob.controller.BannerController
import com.padippob.controller.ProgressBarFragment
import com.padippob.menu.TopUpActivity
import com.padippob.menu.WebViewActivity
import com.padippob.model.UserSession
import com.padippob.ppob.bpjs.BPJSActivity
import com.padippob.ppob.dana.DanaActivity
import com.padippob.ppob.e_money_mandiri.EMoneyMandiriActivity
import com.padippob.ppob.go_pay.GoPayActivity
import com.padippob.ppob.grab.GrabActivity
import com.padippob.ppob.insurance.InsuranceActivity
import com.padippob.ppob.multyFinance.MultiFinanceActivity
import com.padippob.ppob.ovo.OvoActivity
import com.padippob.ppob.payment.PaymentActivity
import com.padippob.ppob.pdam.PDAMActivity
import com.padippob.ppob.pln.PLNActivity
import com.padippob.ppob.plnCredit.PLNCreditActivity
import com.padippob.ppob.postPaid.PostPaidActivity
import com.padippob.ppob.postPaidCredit.PostPaidCreditActivity
import com.padippob.ppob.tap_cash_bni.TapCashBNIActivity
import com.padippob.ppob.wifi.WifiActivity
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val progressBarFragment = ProgressBarFragment(activity!!)

        progressBarFragment.openDialog()

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        Handler().postDelayed({
            val bannerController = BannerController().execute()
            val response = bannerController.get()
            val arrayBitmap = ImageAdapter.SetBackgroundImage(response).execute().get()

            val sliderView = root.findViewById<SliderView>(R.id.SliderView)
            val imageAdapter = ImageAdapter(root.context, arrayBitmap)
            sliderView.sliderAdapter = imageAdapter
            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            sliderView.scrollTimeInSec = 5
            sliderView.startAutoCycle()
        }, 500)

        val userSession = UserSession(activity)

        Handler().postDelayed({
            val balanceController = BalanceController(
                userSession.getString("username").toString(),
                userSession.getString("code").toString()
            ).execute()
            val responseBalance = balanceController.get()
            val getBalance: String
            getBalance = if (responseBalance["Status"].toString() == "0") {
                responseBalance["Saldo"].toString().replace(".", "")
            } else {
                "0"
            }
            val idr = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(idr)
            val balanceTextView = root.findViewById<TextView>(R.id.BalanceTextView)
            balanceTextView.text = numberFormat.format(getBalance.toInt())
            userSession.saveInteger("balance", getBalance.toInt())
        }, 1000)

        Handler().postDelayed({
            progressBarFragment.closeDialog()
        }, 1500)

        val updateBalance = root.findViewById<Button>(R.id.UpdateBalance)
        updateBalance.setOnClickListener {
            progressBarFragment.openDialog()
            val balanceControllerUpdate = BalanceController(
                userSession.getString("username").toString(),
                userSession.getString("code").toString()
            ).execute()
            val responseBalanceUpdate = balanceControllerUpdate.get()
            val getBalanceUpdate: String
            getBalanceUpdate = if (responseBalanceUpdate["Status"].toString() == "0") {
                responseBalanceUpdate["Saldo"].toString().replace(".", "")
            } else {
                "0"
            }
            val localIDR = Locale("in", "ID")
            val numberFormatUpdate = NumberFormat.getCurrencyInstance(localIDR)
            val balanceTextViewUpdate = root.findViewById<TextView>(R.id.BalanceTextView)
            balanceTextViewUpdate.text = numberFormatUpdate.format(getBalanceUpdate.toBigInteger())
            userSession.saveInteger("balance", getBalanceUpdate.toInt())
            progressBarFragment.closeDialog()
        }

        val topUpPPOBButton = root.findViewById<Button>(R.id.TopUpPPOBButton)
        val historyButton = root.findViewById<Button>(R.id.History)
        val financialButton = root.findViewById<Button>(R.id.Financial)
        val emailButton = root.findViewById<Button>(R.id.Support)

        topUpPPOBButton.setOnClickListener {
            val goTo = Intent(activity, TopUpActivity::class.java)
            startActivity(goTo)
        }

        historyButton.setOnClickListener {
            val goTo = Intent(activity, WebViewActivity::class.java).putExtra("ulr", "https://www.padippob.com/api/logger.php?a=Logger&idlogin=${userSession.getString("code")}&username=${userSession.getString("username")}")
            startActivity(goTo)
        }

        financialButton.setOnClickListener {
            val goTo = Intent(activity, FinancialActivity::class.java)
            startActivity(goTo)
        }

        emailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("support@padippob.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        }

        val postPaidLinearLayout = root.findViewById<LinearLayout>(R.id.PostPaid)
        val postPaidCreditLinearLayout = root.findViewById<LinearLayout>(R.id.PostPaidCredit)
        val plnLinearLayout = root.findViewById<LinearLayout>(R.id.ElectricToken)
        val plnBillLinearLayout = root.findViewById<LinearLayout>(R.id.ElectricityBills)
        val ovoLinearLayout = root.findViewById<LinearLayout>(R.id.ovo)
        val grabLinearLayout = root.findViewById<LinearLayout>(R.id.Grab)
        val goPayLinearLayout = root.findViewById<LinearLayout>(R.id.GoPay)
        val danaLinearLayout = root.findViewById<LinearLayout>(R.id.Dana)
        val pdamLinearLayout = root.findViewById<LinearLayout>(R.id.PDAM)
        val multiFinanceLinearLayout = root.findViewById<LinearLayout>(R.id.MultiFinance)
        val topCashBNILinearLayout = root.findViewById<LinearLayout>(R.id.TapCashBNI)
        val eMoneyMandiriLinearLayout = root.findViewById<LinearLayout>(R.id.EMoneyMandiri)
        val wifiLinearLayout = root.findViewById<LinearLayout>(R.id.InternetAndCableTV)
        val bpjsLinearLayout = root.findViewById<LinearLayout>(R.id.BPJS)
        val insuranceLinearLayout = root.findViewById<LinearLayout>(R.id.Insurance)
        val paymentLinearLayout = root.findViewById<LinearLayout>(R.id.Payment)

        postPaidLinearLayout.setOnClickListener {
            val goTo = Intent(activity, PostPaidActivity::class.java)
            startActivity(goTo)
        }

        postPaidCreditLinearLayout.setOnClickListener {
            val goTo = Intent(activity, PostPaidCreditActivity::class.java)
            startActivity(goTo)
        }

        plnLinearLayout.setOnClickListener {
            val goTo = Intent(activity, PLNActivity::class.java)
            startActivity(goTo)
        }

        plnBillLinearLayout.setOnClickListener {
            val goTo = Intent(activity, PLNCreditActivity::class.java)
            startActivity(goTo)
        }

        ovoLinearLayout.setOnClickListener {
            val goTo = Intent(activity, OvoActivity::class.java)
            startActivity(goTo)
        }

        grabLinearLayout.setOnClickListener {
            val goTo = Intent(activity, GrabActivity::class.java)
            startActivity(goTo)
        }

        goPayLinearLayout.setOnClickListener {
            val goTo = Intent(activity, GoPayActivity::class.java)
            startActivity(goTo)
        }

        danaLinearLayout.setOnClickListener {
            val goTo = Intent(activity, DanaActivity::class.java)
            startActivity(goTo)
        }

        pdamLinearLayout.setOnClickListener {
            val goTo = Intent(activity, PDAMActivity::class.java)
            startActivity(goTo)
        }

        multiFinanceLinearLayout.setOnClickListener {
            val goTo = Intent(activity, MultiFinanceActivity::class.java)
            startActivity(goTo)
        }

        topCashBNILinearLayout.setOnClickListener {
            val goTo = Intent(activity, TapCashBNIActivity::class.java)
            startActivity(goTo)
        }

        eMoneyMandiriLinearLayout.setOnClickListener {
            val goTo = Intent(activity, EMoneyMandiriActivity::class.java)
            startActivity(goTo)
        }

        wifiLinearLayout.setOnClickListener {
            val goTo = Intent(activity, WifiActivity::class.java)
            startActivity(goTo)
        }

        bpjsLinearLayout.setOnClickListener {
            val goTo = Intent(activity, BPJSActivity::class.java)
            startActivity(goTo)
        }

        insuranceLinearLayout.setOnClickListener {
            val goTo = Intent(activity, InsuranceActivity::class.java)
            startActivity(goTo)
        }

        paymentLinearLayout.setOnClickListener {
            val goTo = Intent(activity, PaymentActivity::class.java)
            startActivity(goTo)
        }

        return root
    }
}