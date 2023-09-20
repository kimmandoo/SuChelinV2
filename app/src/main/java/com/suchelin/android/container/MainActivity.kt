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
        initNavBar()
        viewModel.getMenuData()
        viewModel.getStoreData()
        binding.apply {
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    1 -> bottomNavigationBar.visibility =
                        View.INVISIBLE // bottomNavBar를 보여주지 않을 곳 지정
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

    private fun apply() {
        setStoreData(
            path = 6,
            name = "이삭토스트",
            mainMenu = "햄치즈토스트",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDAyMjNfMTkw%2FMDAxNTgyNDY0NjY5MzEw.mj309qImHSTLjndXY4DsWWISdxSOsdoRuG3bRuMsHEEg.vy7HfJu3gPFtiGYWuPD8YQ7wDUnJ8RuvjfAVrAK-Vl8g.JPEG.koreasc8101%2F20200223_223022.jpg&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAxOTExMDVfMTQ5%2FMDAxNTcyOTQ3OTczNDA1.qW4SlZ0Bg4qrpknvu-1oE81KV6sWd_LX9O25aKGJ4wMg.1r-9o_WKKVUlGCFDfzSZFY6uOLrZtnu4lgUalwyZoVEg.JPEG.qlrk10%2FIMG_0864.JPG",
            latitude = 37.2142275, // 30~
            longitude = 126.9785260, // 120~
            type = "restaurant" // restaurant or cafe

        )
    }

}