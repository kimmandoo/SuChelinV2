package com.suchelin.android.feature.view.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suchelin.android.databinding.ItemMapBinding
import com.suchelin.domain.model.StoreData

class MapViewAdapter(private val storeList: List<StoreData>,
                     private val onMapItemClick: (store: StoreData) -> Unit = {  }) :
    RecyclerView.Adapter<MapViewAdapter.ItemMapBindingViewHolder>() {
    inner class ItemMapBindingViewHolder(private val binding: ItemMapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoreData) {
            binding.apply {
                mapStoreTitleTextView.text = item.storeDetailData.name
                mapStoreDetailTextView.text = item.storeDetailData.detail
                Glide.with(binding.root.context).load(item.storeDetailData.imageUrl).centerCrop()
                    .into(mapStoreImageView)
                mapStoreParent.setOnClickListener { onMapItemClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMapBindingViewHolder {
        return ItemMapBindingViewHolder(
            ItemMapBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: ItemMapBindingViewHolder, position: Int) {
        holder.bind(storeList[position])
    }

    override fun getItemCount(): Int = storeList.size
}