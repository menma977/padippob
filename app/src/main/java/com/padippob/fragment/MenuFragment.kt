package com.padippob.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.padippob.R
import com.padippob.controller.ProgressBarFragment
import com.padippob.menu.WebViewActivity
import com.padippob.model.UserSession
import com.padippob.validation.SendImageActivity

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        val progressBarFragment = ProgressBarFragment(activity!!)

        progressBarFragment.openDialog()

        Handler().postDelayed({
            progressBarFragment.closeDialog()
        }, 1500)

        val userSession = UserSession(activity)
        val dashboard = root.findViewById<Button>(R.id.dashboard)
        val bonus = root.findViewById<Button>(R.id.bonus)
        val register = root.findViewById<Button>(R.id.register)
        val profile = root.findViewById<Button>(R.id.profile)
        val pin = root.findViewById<Button>(R.id.pin)
        val network = root.findViewById<Button>(R.id.network)
        val withdraw = root.findViewById<Button>(R.id.withdraw)

        println(
            "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                "apiKey"
            )}/home"
        )

        dashboard.setOnClickListener {
            val goTo = Intent(activity, WebViewActivity::class.java)
                .putExtra(
                    "ulr",
                    "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                        "apiKey"
                    )}/home"
                )
            startActivity(goTo)
        }

        bonus.setOnClickListener {
            val goTo = Intent(activity, WebViewActivity::class.java)
                .putExtra(
                    "ulr",
                    "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                        "apiKey"
                    )}/bonus"
                )
            startActivity(goTo)
        }

        register.setOnClickListener {
            val goTo = Intent(activity, WebViewActivity::class.java)
                .putExtra(
                    "ulr",
                    "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                        "apiKey"
                    )}/user.create"
                )
            startActivity(goTo)
        }

        profile.setOnClickListener {
            val goTo = Intent(activity, WebViewActivity::class.java)
                .putExtra(
                    "ulr",
                    "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                        "apiKey"
                    )}/user.index"
                )
            startActivity(goTo)
        }

        pin.setOnClickListener {
            val goTo = Intent(activity, WebViewActivity::class.java)
                .putExtra(
                    "ulr",
                    "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                        "apiKey"
                    )}/pin.index.user"
                )
            startActivity(goTo)
        }

        network.setOnClickListener {
            val goTo = Intent(activity, WebViewActivity::class.java)
                .putExtra(
                    "ulr",
                    "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                        "apiKey"
                    )}/relation.index"
                )
            startActivity(goTo)
        }

        withdraw.setOnClickListener {
            val session = UserSession(activity)
            println(session.getString("ktp").toString())
            println(session.getString("selfAndKTP").toString())
            if (session.getString("ktp") == null || session.getString("selfAndKTP") == null) {
                val goTo = Intent(activity, SendImageActivity::class.java)
                startActivity(goTo)
            } else {
                val goTo = Intent(activity, WebViewActivity::class.java)
                    .putExtra(
                        "ulr",
                        "https://www.padippob.com/auto/login/${userSession.getString("idUser")}/${userSession.getString(
                            "apiKey"
                        )}/withdrawal.create"
                    )
                startActivity(goTo)
            }
        }

        return root
    }
}