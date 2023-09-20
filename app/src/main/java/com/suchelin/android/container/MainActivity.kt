package com.suchelin.android.container

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

        viewModel.loadMenuData()
        viewModel.loadStoreData()
        viewModel.loadPostData()
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
            path= 10,
            name="한솥",
            mainMenu = " 치킨 마요",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAxNzA5MDhfMjAg%2FMDAxNTA0ODY0NDAxMDM2.VlRUyXJIGCn_E2RUqIhrIXfi7O7YgggZ-V8ng2H-8WAg.QkONDw4qqIkaO5F8p2A1eq7xhgLMOPhdFO_XLXfAEL4g.PNG.wothd5307%2F%25BA%25ED%25C7%25D1%25BC%25DC1.png&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA3MDhfMTE1%2FMDAxNjg4Nzk1Mjg3ODc2.aQj5zWIQLTvePB-1EBkyAlyuaDML9DUm6ByoQSlOxfUg.d_belVFX4Ky1gEDY2vwYMLb92HPfmnN7rpGJoUGm2F4g.JPEG.mamayoon72%2F1688795247982.jpg&type=sc960_832",
                    latitude = 37.2147291, // 30~
            longitude = 126.9782402, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 11,
            name="맘스터치",
            mainMenu = " 싸이버거",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230829_137%2F1693282401170BAq7o_PNG%2F%25B7%25CE%25B0%25ED_%25C1%25A4%25B9%25E6%25C7%25FC.png",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA0MjdfMTEz%2FMDAxNjgyNTg4MDk0Njcz.pGc_R9V7ntbNkKMW2Bz_vxflmpmySl_rc-fVudCU05Qg._BqAWhKJ7wwCuiISOU5hRsoM58KNBL4SGA0gfFWHTZUg.JPEG.loveek777%2F20230427%25A3%25DF181234.jpg&type=sc960_832",
                    latitude = 37.2144688, // 30~
            longitude = 126.9788923, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 12,
            name="와우당",
            mainMenu = " 바게트버거",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200729_90%2F1595990793466J5N1p_JPEG%2F9yJSvHUr4skU3Mzli9UFQJxn.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA2MTRfMjE4%2FMDAxNjIzNjQ5Njc1OTgy.5gHnRJcZjq3watk_t-4ou1EPshJC8fPXkjm6Woi-kL8g.l_b3CqsZDDtm8N5p0o_frMNNu218eKQ9xjhtp_V08H8g.JPEG.asdf8538%2FIMG_0052.jpg"
                    ,latitude = 37.2140311, // 30~
            longitude = 126.9797804, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 13,
            name="이디야",
            mainMenu = " 커피,음료",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA1MTBfMjU3%2FMDAxNjgzNjk3MzIzNzA5.ZS_xXgeHGtavVLzkh-fqyfw9qwuuvOUy5mm8ZDM6bAIg._hbJEEiwMD9rN6ImwPGCWu3v6XR9MyVMF7jIPabAftgg.PNG.tpnews%2Fimage.png&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA0MjRfOCAg%2FMDAxNjgyMjY1NzE1Mzky.6GFQ7L0u-vPx2GeG-DPirdsFqUWx3Q8Pdg9l32iloqgg.LMk6gWY4qoYJ-SzGinyKeS8iLl3ARgdv05O7k5Gd-Bgg.JPEG.k1aldhr%2F1682265307083.jpg&type=sc960_832"
                   , latitude = 37.2141612, // 30~
            longitude = 126.9794391, // 120~
            type = "cafe" // restaurant or cafe


        )
        setStoreData(
            path= 14,
            name="행복한 찹쌀꽈배기",
            mainMenu = " 꽈배기",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA5MTdfMTc5%2FMDAxNjk0OTMzOTg4OTE1.nQBvFpYVW7gq7AJQ19kgw2kqpJjKX1U_XdypIUG-pxwg.WqalTUgJHMSSBoyuyjIlBZdFJ6L28w5dtTCDMsPFpjUg.JPEG.dudonehi%2F20230917_143352.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA4MjlfMTAz%2FMDAxNjkzMjcwNzI4MTUy.La5Rfqh4s6yuTjZ4b3hN3_jiKKQ4A8HPHm00sdCH8lkg.jenEp_NAgPcsQW5wiykns7lZYeTxotCsbg7pDQO9s8Yg.JPEG.zhiyi19%2FIMG_4575.JPG"
                    ,latitude = 37.2141901, // 30~
            longitude = 126.9799705, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 15,
            name="무공돈까스",
            mainMenu = "무공돈까스",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimage-logo.alba.kr%2Fdata_image2%2Fcomlogo%2F202308%252F18%252F2023081814031963623_0_C1.png&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA2MDZfMzAg%2FMDAxNjg2MDQ4MTY4NTU3.Kiqwn7EgUqvT_BmrrxIEsQZ8PxcAfobh4ifE1xxJb68g.kZJ_xDspjFtC_P4t_zZM2q3sNxxonUkvVE8h4hdS-Fgg.JPEG.clearly1627%2Foutput_2383196176.jpg&type=sc960_832",
                    latitude = 37.2144688, // 30~
            longitude = 126.9788923, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 16,
            name="유쓰동",
            mainMenu = "가츠동",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210419_269%2F161880545229211wQW_JPEG%2FAGwzcnc9k_yHFShzQw7cIgRo.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDA4MTNfMTYw%2FMDAxNTk3MjkwMzQyMDU4.cZsinkP36mCt_Xt0mXuHKxj3KXROWe9mU5nOk6ka-Gkg.-IdJPb9j6J5twmMEwilr6OENNJ2uS6qiaC-6-401vHQg.PNG.danbi9123%2Fimage.png"
                    ,latitude = 37.2141612, // 30~
            longitude = 126.9794391, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 17,
            name="명랑핫도그",
            mainMenu = "명랑핫도그",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230912_177%2F1694483641035g26zG_JPEG%2F%25B8%25ED%25B6%25FB%25C7%25D6%25B5%25B5%25B1%25D7_%25B7%25CE%25B0%25ED.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDA2MzBfNSAg%2FMDAxNTkzNDc4NjQwMDE5.Sddfc2hZyfbG4hfjDTqHQ2nRinEFUg7aNmQ85mJYNpcg._656T5ugv6bKiZtpE7hcavJvtRyuL4WdAAPp2ZsZ494g.JPEG.2020mmdd%2F1593432926798.jpg&type=sc960_832"
                    ,latitude = 37.2142275, // 30~
            longitude = 126.9785260, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 18,
            name="탄탄석쇠",
            mainMenu = " 연탄초벌 파불고기",
// image address 이미지 주소
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA5MTNfODYg%2FMDAxNjk0NTc0NTk2MjQy.eQxD51vaGvlAXOpJ_n_pcK34xICHv0ah5Z2w5A6p_IAg.pbuvV9OoqGNDEjrKLFWe9pzdz3n9choIfMRSTwL5Lcwg.JPEG.jmi85%2FIMG_2871.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20211108_183%2F1636330269271N87UF_PNG%2FJjMR6NQlU4UfWmuQfveeR8Yn.png"
                    ,latitude = 37.2144858, // 30~
            longitude = 126.9779086, // 120~
            type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 19,
            name="럼블피쉬",
            mainMenu = "회",
// image address 이미지 주소
        imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221104_291%2F1667548006200TYzSp_JPEG%2Fimage-7753112672415056490.jpg",
        menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230812_244%2F1691800795081FjVyj_JPEG%2FScreenshot_20230812_093700_KakaoTalk.jpg"
       , latitude = 37.2140990, // 30~
        longitude = 126.9778695, // 120~
        type = "restaurant" // restaurant or cafe

        )
        setStoreData(
            path= 20,
            name="더벤티",
            mainMenu = "카페",
// image address 이미지 주소
        imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200617_94%2F1592362308610bnDTF_JPEG%2FguxhSS81pQo8vNapEfQwT65_.jpg",
        menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA3MDRfMjkz%2FMDAxNjg4NDc4OTcwNjA3.NgduREWTpbOW78T_vK_ZmxfrZUyK4V8-J0SZQ53Md_Eg.HCSnGKUJv-gnh86FgbHI1FgEGpvP1umHgPAxIXZ7rBcg.PNG.roniiiiiii_s2%2Fimage.png&type=sc960_832"
        ,latitude = 37.2139411, // 30~
        longitude = 126.9769280, // 120~
        type = "cafe" // restaurant or cafe

        )
    }

}