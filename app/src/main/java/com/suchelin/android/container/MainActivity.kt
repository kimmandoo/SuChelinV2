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

//        setStoreData(
//            path= 5,
//            name="코히맨즈",
//            mainMenu = "수제 스콘, 커피",
//// image address 이미지 주소
//            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220213_4%2F1644723595924Xg3Cw_JPEG%2FScreenshot_20220205-152057_Instagram.jpg",
//            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220426_48%2F1650950839211AvuWK_JPEG%2F1650950819553.jpg",
//            latitude = 37.2112220, // 30~
//            longitude = 126.9807451, // 120~
//            type = "cafe" // restaurant or cafe
//        )

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
}