package com.suchelin.android.feature.view_compose.feed

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.suchelin.android.R
import com.suchelin.android.databinding.DialogPostBinding

class PostAlertDialog(private val context: Context){
    private val binding = DialogPostBinding.inflate(LayoutInflater.from(context), null, false)
    lateinit var alertDialog: AlertDialog

    init {
        initDialog()
    }

    fun showDialog() {
        alertDialog.show()
        binding.apply {
            btnCancel.setOnClickListener {
                Toast.makeText(context, context.getString(R.string.post_cancel) ,Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
            btnConfirm.setOnClickListener {
                alertDialog.cancel()
            }
        }
    }

    private fun initDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
        alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}