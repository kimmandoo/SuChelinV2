package com.suchelin.android.feature.view_compose.vote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.suchelin.android.base.BaseViewModel
import com.suchelin.android.feature.view_compose.list.StoreFilter
import com.suchelin.domain.model.StoreMenuData

class VoteViewModel : BaseViewModel() {
    private val database = Firebase.database
    private val votedValue = hashMapOf<String, Long>()
    private val _rtData = MutableLiveData<HashMap<String, Long>>()
    val rtData: LiveData<HashMap<String, Long>> = _rtData
    var currentFilter : StoreFilter = StoreFilter.ALL

    fun readRTDB() {
        database.reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (id in dataSnapshot.children) {
                    votedValue[id.key!!] = id.value!! as Long
                }
                _rtData.value = votedValue
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    fun addVote(key: String) {
        if(votedValue[key] == null){
            database.getReference(key).setValue(1)
        }else{
            database.getReference(key).setValue(votedValue[key] as Long + 1)
        }
    }
}