package com.suchelin.android.feature.view_compose.vote

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.suchelin.android.base.BaseViewModel
import com.suchelin.android.util.StoreFilter
import com.suchelin.android.util.room.LikeDbInstance
import com.suchelin.android.util.room.dao.LikeDao
import com.suchelin.android.util.room.dto.LikeData
import com.suchelin.android.util.todayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoteViewModel : BaseViewModel() {
    private val database = Firebase.database
    private val votedValue = hashMapOf<String, Long>()
    private val _rtData = MutableLiveData<HashMap<String, Long>>()
    val rtData: LiveData<HashMap<String, Long>> = _rtData
    var currentFilter : StoreFilter = StoreFilter.ALL
    private val _isLimited = MutableLiveData<Boolean>(false)
    val isLimited: LiveData<Boolean> = _isLimited
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

    fun addVote(key: String, likeDao: LikeDao) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val todayLike = likeDao.getAll()
                val today = todayDate()
                var likes: Int = 0
                todayLike.forEach { dates ->
                    if(dates.date == today){
                        likes = dates.like ?: 0
                    }
                }
                when(likes++){
                    0 -> {
                        likeDao.insert(LikeData(today,likes))
                    }
                    in 1..9 ->{
                        likeDao.update(LikeData(today,likes))
                    }
                    10 ->{
                        withContext(Dispatchers.Main){
                            _isLimited.value = true
                        }
                    }
                }
                Log.d("DB","${todayLike}")
            }
            if(!_isLimited.value!!){
                if(votedValue[key] == null){
                    database.getReference(key).setValue(1)
                }else{
                    database.getReference(key).setValue(votedValue[key] as Long + 1)
                }
            }
        }
    }
}