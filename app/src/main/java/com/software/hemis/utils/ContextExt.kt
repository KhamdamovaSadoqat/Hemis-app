package com.software.hemis.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showGenericAlertDialog(message: String) {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}