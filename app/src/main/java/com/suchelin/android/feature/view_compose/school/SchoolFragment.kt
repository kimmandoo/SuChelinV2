package com.suchelin.android.feature.view_compose.school

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.databinding.FragmentSchoolBinding

class SchoolFragment :
    BaseFragment<FragmentSchoolBinding, SchoolViewModel>(R.layout.fragment_school) {
    override val viewModel: SchoolViewModel by viewModels()
    override fun initView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
            setContent {

            }
        }
    }
}