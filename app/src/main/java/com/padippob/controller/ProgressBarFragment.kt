package com.padippob.controller

import android.R as RS
import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import com.padippob.R

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProgressBarFragment(fragmentActivity: FragmentActivity) {
    private val dialog = Dialog(fragmentActivity, RS.style.Theme_Translucent_NoTitleBar)
    init {
        val view = fragmentActivity.layoutInflater.inflate(R.layout.activity_progress_bar, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
    }

    fun openDialog() {
        dialog.show()
    }

    fun closeDialog() {
        dialog.dismiss()
    }
}