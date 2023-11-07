package com.suchelin.android.feature.view_compose.school

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suchelin.android.base.BaseViewModel
import com.suchelin.android.util.SCHOOL_MEAL
import com.suchelin.domain.model.SchoolMealData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import timber.log.Timber

class SchoolViewModel: BaseViewModel() {
    private val _schoolMeal = MutableLiveData<SchoolMealData>()
    val schoolMeal : LiveData<SchoolMealData> = _schoolMeal

    fun setSchoolMealData(data: SchoolMealData){
        _schoolMeal.value = data
    }
}