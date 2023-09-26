package com.suchelin.android.feature.view.vote

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentVoteBinding

class VoteFragment : BaseFragment<FragmentVoteBinding, VoteViewModel>(R.layout.fragment_vote) {
    override val viewModel: VoteViewModel by viewModels()
    val sharedViewModel: MainViewModel by activityViewModels()
    override fun initView() {

    }
}