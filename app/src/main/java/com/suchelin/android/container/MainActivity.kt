package com.suchelin.android.container

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.suchelin.android.R
import com.suchelin.android.base.BaseActivity
import com.suchelin.android.databinding.ActivityMainBinding
import com.suchelin.android.util.setStoreData

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

    }
    override fun initView() {
        apply()

        initNavBar()
        viewModel.getMenuData()
        viewModel.getStoreData()
        binding.apply {
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when(destination.id){
                    1 -> bottomNavigationBar.visibility = View.INVISIBLE // bottomNavBar를 보여주지 않을 곳 지정
                    else -> bottomNavigationBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationBar.setupWithNavController(navController)
        binding.bottomNavigationBar.setOnItemReselectedListener { }
    }

    private fun apply(){
                setStoreData(
                    path= 6,
                    name="이삭토스트",
                    mainMenu = "햄치즈토스트",
// image address 이미지 주소
                    imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDAyMjNfMTkw%2FMDAxNTgyNDY0NjY5MzEw.mj309qImHSTLjndXY4DsWWISdxSOsdoRuG3bRuMsHEEg.vy7HfJu3gPFtiGYWuPD8YQ7wDUnJ8RuvjfAVrAK-Vl8g.JPEG.koreasc8101%2F20200223_223022.jpg&type=sc960_832",
                    menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAxOTExMDVfMTQ5%2FMDAxNTcyOTQ3OTczNDA1.qW4SlZ0Bg4qrpknvu-1oE81KV6sWd_LX9O25aKGJ4wMg.1r-9o_WKKVUlGCFDfzSZFY6uOLrZtnu4lgUalwyZoVEg.JPEG.qlrk10%2FIMG_0864.JPG",
                    latitude = 37.2142275, // 30~
                    longitude = 126.9785260, // 120~
                    type = "restaurant" // restaurant or cafe

                )
        setStoreData(
            path= 7,
            name="밥집",
            mainMenu = "한식, 분식, 돈까스",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220303_159%2F1646269633382DDfb5_JPEG%2FDD576956-73B9-4D0F-8E7D-7033C897AA1A.jpeg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fmyplace-phinf.pstatic.net%2F20210411_137%2F1618108310358ihCPW_JPEG%2Fupload_33d5976138eceba66fede22b302dd908.jpg",
            latitude = 37.2144075, // 30~
            longitude = 126.9777068, // 120~
            type = "restaurant" // restaurant or cafe
        )
        setStoreData(
            path= 8,
            name="화이트스노우",
            mainMenu = "빙수",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220223_2%2F1645602511100qzFVd_JPEG%2FDDA14E25-84B8-4F9A-A238-08283D9072A4.jpeg",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjEwMThfMjMw%2FMDAxNjY2MDk1OTY4NDQ1.ByArVJzLfqFUxeCyR101hvhI6chyiJkpe3TVigEX8pMg.ztS5L5us2Rl37fbotDzeUwEhVzOXPKdHsFjKtM9rO3Ig.JPEG.rnapish%2FIMG_5742.JPEG",
            latitude = 37.2141946, // 30~
            longitude = 126.9770596, // 120~
            type = "cafe" // restaurant or cafe
        )
        setStoreData(
            path= 9,
            name="빽다방",
            mainMenu = " 커피",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjAyMDhfMjAy%2FMDAxNjQ0Mjk1ODIyMDc1.0Gzm82rsFPSQpGiYziefEuXV8i0Ss69Oe7l2slg4SmAg.sh3zUFSHzu0jMfJ50i6Z-Xo-mTXsydhPeadkssL2F20g.JPEG.sesang0314%2FIMG_7420.jpg&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyMzA4MDhfMjA1%2FMDAxNjkxNDY3OTM4OTMw.84eox1imTyxH_fc-bCLzMQfQfp0j9YAwQb1RpTtL_Uog.tc0xU7T8qvCJuPlCtU0QyTHNz2XpQd6_rtrimMuSofEg.JPEG%2F20230718_124813.jpg",
            latitude = 37.2139851, // 30~
            longitude = 126.9776783, // 120~
            type = "cafe" // restaurant or cafe
        )

    }

}