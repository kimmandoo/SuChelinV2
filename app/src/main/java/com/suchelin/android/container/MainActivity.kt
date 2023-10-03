package com.suchelin.android.container

import android.R.attr.path
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suchelin.android.R
import com.suchelin.android.base.BaseActivity
import com.suchelin.android.databinding.ActivityMainBinding
import com.suchelin.android.util.setStoreData
import com.suchelin.android.util.setStoreMenu
import com.suchelin.domain.model.StoreMenuDetail


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()

        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (!viewModel.isInit.value!!) {
                        viewModel.initData(getString(R.string.empty_post))
                    }
                    Log.d("TAG", "auth success: ${userId}")
                } else {

                }
            }
    }

    override fun initView() {
        initNavBar()
//        apply()

        binding.apply {
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.navigation_detail -> bottomNavigationBar.visibility =
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
//        setStoreData(
//            path=90,
//            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA1MDJfMzcg%2FMDAxNjgyOTk1NjY2NTY5.IzJ8T4q6Mx53tQyPe9YhJErOSQ5JwpS1KdvODpeUqlIg.FT9k0Lf2VBnxy9JOtVJE1P8o84ae20uomxYa_NB27AQg.JPEG.asdzxc1412%2FIMG_6195.JPG",
//            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190515_139%2F1557917202917I0lXX_JPEG%2Fx-0f2C3z9rBeHEv4TSznE65M.jpeg.jpg",
//            name= "여장군 수원대점",
//            mainMenu= "소막창구이",
//            latitude= 37.2141219,
//            longitude= 126.9758427,
//            type= "restaurant" // cafe, restaurant, pub
//        )
//        setStoreMenu()


    }

}