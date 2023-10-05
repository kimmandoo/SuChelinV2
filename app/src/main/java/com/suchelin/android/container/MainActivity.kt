package com.suchelin.android.container

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suchelin.android.R
import com.suchelin.android.base.BaseActivity
import com.suchelin.android.databinding.ActivityMainBinding
import com.suchelin.android.util.setStoreMenu
import com.suchelin.domain.model.StoreMenuDetail


const val testAdId = "ca-app-pub-3940256099942544/6300978111"
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
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
        apply()

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
//
//        setStoreMenu(// 화이트스노우
//            path = 8,
//            image = false, // 일일이 넣기 힘들 정도로 메뉴가 많으면
//            menu = listOf( // 메뉴판 여러 장도 가능
//                StoreMenuDetail("[초코시리얼]BEST MENU 바삭함 살아있네(small(소))","8,500원"),
//                StoreMenuDetail("[초코시리얼]BEST MENU 바삭함 살아있네(Medium(중))","16,000원"),
//                StoreMenuDetail("[초코시리얼]BEST MENU 바삭함 살아있네(Large(대))","21,000원"),
//                StoreMenuDetail("[오레오빙수] 단맛의 끝판왕(small(소))","8,500원"),
//                StoreMenuDetail("[오레오빙수] 단맛의 끝판왕(Medium(중))","16,000원"),
//                StoreMenuDetail("[오레오빙수] 단맛의 끝판왕(Large(대))","21,000원"),
//                StoreMenuDetail("[팥생과일빙수] BEST MENU(small(소))","8,500원"),
//                StoreMenuDetail("[팥생과일빙수] BEST MENU(Medium(중))","16,000원"),
//                StoreMenuDetail("[팥생과일빙수] BEST MENU(Large(대))","21,000원"),
//                StoreMenuDetail("[망고빙수]BEST MENU(small(소))","9,500원"),
//                StoreMenuDetail("[망고빙수]BEST MENU(Medium(중))","18,000원"),
//                StoreMenuDetail("[망고빙수]BEST MENU(Large(대))","23,000원"),
//                StoreMenuDetail("[수박빙수] 깔끔 션한맛(small(소))","8,000원"),
//                StoreMenuDetail("[수박빙수] 깔끔 션한맛(Medium(중))","15,000원"),
//                StoreMenuDetail("[수박빙수] 깔끔 션한맛(Large(대))","20,000원"),
//                StoreMenuDetail("[팥콩가루빙수] 고소한맛(small(소))","7,500원"),
//                StoreMenuDetail("[팥콩가루빙수] 고소한맛(Medium(중))","14,000원"),
//                StoreMenuDetail("[팥콩가루빙수] 고소한맛(Large(대))","19,000원"),
//                StoreMenuDetail("[팥빙수]추억의 빙수(small(소))","8,000원"),
//                StoreMenuDetail("[팥빙수]추억의 빙수(Medium(중))","15,000원"),
//                StoreMenuDetail("[팥빙수]추억의 빙수(Large(대))","20,000원"),
//                StoreMenuDetail("[팥씨리얼빙수] 팥과 시리얼의 환상조합(small(소))","8,500원"),
//                StoreMenuDetail("[팥씨리얼빙수] 팥과 시리얼의 환상조합(Medium(중))","16,000원"),
//                StoreMenuDetail("[팥씨리얼빙수] 팥과 시리얼의 환상조합(Large(대))","21,000원"),
//                StoreMenuDetail("[팥 없는 콩가루 빙수](Small(소))","7,000원"),
//                StoreMenuDetail("[팥 없는 콩가루 빙수](Medium(중))","13,000원"),
//                StoreMenuDetail("[팥 없는 콩가루 빙수](Large(대))","18,000원"),
//                StoreMenuDetail("[죠리퐁빙수]단맛의 고소함(small(소))","7,000원"),
//                StoreMenuDetail("[죠리퐁빙수]단맛의 고소함(Medium(중))","13,000원"),
//                StoreMenuDetail("[죠리퐁빙수]단맛의 고소함(Large(대))","18,000원"),
//                StoreMenuDetail("[초코빙수]과일과 초콜릿의만남(small(소))","8,000원"),
//                StoreMenuDetail("[초코빙수]과일과 초콜릿의만남(Medium(중))","15,000원"),
//                StoreMenuDetail("[초코빙수]과일과 초콜릿의만남(Large(대))","20,000원"),
//                StoreMenuDetail("[녹차빙수] 녹차의 향과 깔끔한맛(small(소))","8,000원"),
//                StoreMenuDetail("[녹차빙수] 녹차의 향과 깔끔한맛(Medium(중))","15,000원"),
//                StoreMenuDetail("[녹차빙수] 녹차의 향과 깔끔한맛(Large(대))","20,000원"),
//                StoreMenuDetail("[커피빙수] 믹스커피의 양촌리스퇄(small(소))","8,000원"),
//                StoreMenuDetail("[커피빙수] 믹스커피의 양촌리스퇄(Medium(중))","15,000원"),
//                StoreMenuDetail("[커피빙수] 믹스커피의 양촌리스퇄(Large(대))","20,000원"),
//                StoreMenuDetail("[돼지꾸꾸 빙수] 시즌 신메뉴/2가지맛의 아이스크림(small(소))","9,000원"),
//                StoreMenuDetail("[돼지꾸꾸 빙수] 시즌 신메뉴/2가지맛의 아이스크림(Medium(중))","17,000원"),
//                StoreMenuDetail("[돼지꾸꾸 빙수] 시즌 신메뉴/2가지맛의 아이스크림(Large(대))","22,000원"),
//                StoreMenuDetail("[블루베리빙수]새콤달콤(small(소))","9,000원"),
//                StoreMenuDetail("[블루베리빙수]새콤달콤(Medium(중))","17,000원"),
//                StoreMenuDetail("[블루베리빙수]새콤달콤(Large(대))","22,000원"),
//            ),
//            tel = "0507-1419-8934"
//        )
    }

}