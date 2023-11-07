package com.suchelin.android.feature.view_compose.school

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.databinding.FragmentSchoolBinding
import com.suchelin.android.util.loadSchoolMealMenu
import com.suchelin.domain.model.SchoolMealData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SchoolFragment :
    BaseFragment<FragmentSchoolBinding, SchoolViewModel>(R.layout.fragment_school) {
    override val viewModel: SchoolViewModel by viewModels()
    private lateinit var schoolMealData: SchoolMealData
    override fun initView() {
        lifecycleScope.launch {
            val job = CoroutineScope(Dispatchers.IO).launch {
                schoolMealData = loadSchoolMealMenu()
            }
            job.join()
            viewModel.setSchoolMealData(schoolMealData)
        }

        viewModel.schoolMeal.observe(viewLifecycleOwner) { schoolMeal ->
            schoolMeal?.let {
                binding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
                    setContent {
                        SchoolView()
                    }
                }
            }
        }
    }

    @Composable
    fun SchoolView() {
        // 예시
        Surface {
            Column {
                SchoolMeal(schoolMealData.littleKitchen)
                SchoolMeal(schoolMealData.momsCook)
                SchoolMeal(schoolMealData.officer)
            }
        }
    }

    @Composable
    fun SchoolMeal(schoolMealData: List<String>) {
        val schoolMeal by remember { mutableStateOf(schoolMealData) }

        /*
        각 리스트 크기는 5씩
        schoolMealData.littleKitchen
        schoolMealData.momsCook
        schoolMealData.officer // 교직원 식당
        schoolMealData.weekDays // 월 화 수 목 금
        */
        // 미리보기 용도
        LazyColumn(
            contentPadding = PaddingValues(12.dp, 0.dp, 12.dp, 60.dp)
        ) {
            items(
                count = schoolMeal.size,
                itemContent = { MealMenu(schoolMeal[it]) }
            )
        }
    }


    @Composable
    fun MealMenu(menu: String) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.padding(8.dp, 0.dp)) {
                    Text(
                        text = menu,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }

}