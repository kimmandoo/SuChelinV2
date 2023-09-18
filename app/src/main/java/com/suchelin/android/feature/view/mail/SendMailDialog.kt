package com.suchelin.android.feature.view.mail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.suchelin.android.R
import com.suchelin.android.databinding.DialogMailBinding

class SendMailDialog(private val context: Context) {
    private val binding = DialogMailBinding.inflate(LayoutInflater.from(context), null, false)
    lateinit var alertDialog: AlertDialog

    init {
        initDialog()
    }

    fun showDialog() {
        alertDialog.show()
        binding.apply {
            btnSend.setOnClickListener {
                editEmail.text?.let {
                    sendEmail(content = editEmail.text.toString())
                }

                alertDialog.dismiss()
            }
            btnClose.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun sendEmail(content: String) {
        val emailAddress = "mingyuk1999@gmail.com"

        context.startActivity(Intent(Intent.ACTION_SEND).apply {
            selector = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
            }
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mail_title))
            putExtra(Intent.EXTRA_TEXT, content)
        })
    }

    private fun initDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
        alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}