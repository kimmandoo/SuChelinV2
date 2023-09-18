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

    fun setStoreData(name: String, mainMenu: String, imageUrl: String, menuImageUrl: String, latitude: Double, longitude: Double, type: String){
        val db = Firebase.firestore
        val docData = hashMapOf(
            "name" to "신동랩 수원대점",
            "detail" to "카츠, 마제소바",
            // imageUrl, menuImageUrl -> Copy Image Address
            "imageUrl" to "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220310_203%2F1646920687474i7iRv_PNG%2F%25BD%25C5%25B5%25BF%25B7%25A6bi_%25B0%25F8%25B9%25E9.png",
            "latitude" to 37.2137092, // 30~
            "longitude" to 126.9761740, // 120~
            "menuImageUrl" to "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230826_132%2F1692985593242mHjUp_JPEG%2F230824_%25BC%25F6%25BF%25F8%25B4%25EB%25B7%25A6_%25B8%25DE%25B4%25BA%25C6%25C7_A3%2528RGB%2529.jpg",
            "type" to "restaurant" // restaurant or cafe
        )

        db.collection("store").document("3")
            .set(docData)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
    }
}