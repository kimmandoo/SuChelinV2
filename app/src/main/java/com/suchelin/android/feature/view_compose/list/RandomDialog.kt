package com.suchelin.android.feature.view_compose.list

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.suchelin.android.R
import com.suchelin.android.databinding.DialogPostBinding
import com.suchelin.android.databinding.DialogRandomBinding
import com.suchelin.domain.model.StoreData
import com.suchelin.domain.model.StoreDetail

class RandomDialog(private val context: Context,private val goToDetailPage: (StoreData) -> Unit = {  }){
    private val binding = DialogRandomBinding.inflate(LayoutInflater.from(context), null, false)
    lateinit var alertDialog: AlertDialog

    init {
        initDialog()
    }

    fun showDialog(storeInfo:StoreData) {
        alertDialog.show()
        binding.apply {
            Glide.with(context).load(storeInfo.storeDetailData.imageUrl).centerCrop().into(ivRandom)
            ivRandom.setOnClickListener {
                goToDetailPage(storeInfo)
                alertDialog.cancel()
            }
            tvRandom.text = storeInfo.storeDetailData.name
            tvRandomDetail.text = storeInfo.storeDetailData.detail
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