package com.suchelin.android.feature.view.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suchelin.android.base.BaseViewModel
import com.suchelin.domain.model.SchoolMealData

class SchoolViewModel: BaseViewModel() {
    private val _schoolMeal = MutableLiveData<SchoolMealData>()
    val schoolMeal : LiveData<SchoolMealData> = _schoolMeal

    fun setSchoolMealData(data: SchoolMealData){
        _schoolMeal.value = data
    }
}