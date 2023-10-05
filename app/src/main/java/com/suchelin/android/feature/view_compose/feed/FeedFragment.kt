package com.suchelin.android.feature.view_compose.feed

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentSuggestBinding
import com.suchelin.android.feature.compose.ui.AppTheme
import com.suchelin.android.feature.compose.ui.jamsil
import com.suchelin.android.util.adRequest
import com.suchelin.android.util.sendMail
import com.suchelin.android.util.toastMessageShort
import com.suchelin.domain.model.PostData


class FeedFragment :
    BaseFragment<FragmentSuggestBinding, FeedViewModel>(R.layout.fragment_suggest) {
    override val viewModel: FeedViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()
    private val TAG = "FEED"

    override fun initView() {
        binding.adView.loadAd(adRequest)
        sharedViewModel.postData.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                binding.loading.isVisible = false
                binding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        AppTheme {
                            PostRecyclerView(it)
                        }
                    }
                }
            }
        }
        val postAlert = PostAlertDialog(requireContext())
        binding.apply {
            binding.loading.isVisible = true
            contact.setOnClickListener {
                sendMail(TAG)
            }
            btnSuggest.setOnClickListener {
                etSuggestPost.text?.let { post ->
                    // post전에 욕설 필터링이 필요할 것 같음, 하루 최대 한번만 글 쓸 수 있게 글쓰면 광고 팝업 나오게
                    // 50자 제한, 하루 한번 글 쓰기
                    // 다이얼로그로 한번만 글 쓸 수 있고, 부적절한 내용시 삭제가능하다고 안내
                    // 잘못 올린 글은 본인이 삭제할 수 없음
                    if (post.length < 10) {
                        toastMessageShort(getString(R.string.post_min_len))
                    } else {
                        postAlert.showDialog()
                        postAlert.alertDialog.apply {
                            setOnCancelListener {
                                viewModel.postData(post.toString())
                                // 첫 글이면 갱신이 안되어보이는 버그가 있음
                                sharedViewModel.postRefresh(getString(R.string.empty_post))
                                etSuggestPost.text.clear()
                            }
                            setOnDismissListener {
                                toastMessageShort(getString(R.string.post_cancel))
                            }
                        }

                    }
                }
            }
        }
    }

    fun showDialog() {
        val postMessage = AlertDialog.Builder(requireContext())
            .setIcon(resources.getDrawable(R.drawable.ic_post_alert, null))
            .setMessage("하루 한 번만 게시 가능합니다.")
            .setPositiveButton("확인") { dialogInterface, i ->
                Toast.makeText(
                    requireContext(),
                    "네",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(
                "취소"
            ) { dialogInterface, i ->
                Toast.makeText(
                    requireContext(),
                    "안 끔",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .create()
            .show()
    }

    @Composable
    fun PostRecyclerView(postDataList: List<PostData>) {
        val posts by remember { mutableStateOf(postDataList) }
        val nestedScrollInterop = rememberNestedScrollInteropConnection()
        LazyColumn(
            modifier = Modifier.nestedScroll(nestedScrollInterop),
            contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 60.dp),
        ) {
            items(
                count = posts.size,
                itemContent = { PostListItem(posts[it]) }
            )
        }
    }

    @Composable
    fun PostListItem(post: PostData) {
        Card(
            shape = RoundedCornerShape(6.dp),
            modifier =
            Modifier
                .clickable {
                    Toast
                        .makeText(context, "${post.date}: ${post.post}", Toast.LENGTH_SHORT)
                        .show()
                }
                .fillMaxWidth()
                .padding(0.dp, 2.dp, 0.dp, 4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp)
            ) {
                Column(Modifier.padding(8.dp, 2.dp)) {
                    Text(
                        text = post.post,
                        fontFamily = jamsil,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 4.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            text = post.date,
                            fontFamily = jamsil,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }


}