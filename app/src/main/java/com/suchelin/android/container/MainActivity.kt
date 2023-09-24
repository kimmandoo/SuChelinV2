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
//        apply()
        if(!viewModel.isInit.value!!){
            viewModel.initData(getString(R.string.empty_post))
        }

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
            path=57,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190118_178%2F1547771896444kqoT0_JPEG%2FKHNiPJwdhwJPxbvEaVsIpNgP.jpg",
        menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220905_42%2F1662369079692pTVXX_JPEG%2F1662332952991.jpg",
        name= "1인자24시감자탕뼈해장국 수원대점",
        mainMenu= "뼈해장국",
        latitude= 37.2148657,
        longitude= 126.9779665,
        type= "restaurant"// cafe, restaurant, pub

        )
        setStoreData(
            path=58,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220925_244%2F166410686991997L0e_GIF%2F%25BF%25DC%25B0%25FC1.gif",
        menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220925_64%2F16641070810924pNdk_JPEG%2F%25C8%25B2%25C1%25A6%25B0%25A5%25BA%25F1_%25283%2529.jpg",
        name= "황제갈비",
        mainMenu= "황제갈비, 냉면",
        latitude= 37.2149810,
        longitude= 126.9778144,
        type= "restaurant" // cafe, restaurant, pub

        )
        setStoreData(
            path=59,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200813_224%2F1597319917983khMmV_JPEG%2Fro0LMdMaNUhY5iUdpediB858.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220809_257%2F1660021277668zAhrG_JPEG%2FF67B635F-5619-439A-9731-38EA4D31C4F9.jpeg" ,
            name= "정가네곱창전골",
            mainMenu= "곱창전골",
            latitude= 37.2147182,
            longitude= 126.9774907,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(
            path=60,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200813_242%2F1597319809832AEq5G_JPEG%2F1yzXovwrqqnDzidig5_QO9ik.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201122_289%2F16060264340995wG77_JPEG%2F5ko0g5wOyD1VDoPZrzcIqe4O.jpg" ,
            name= "옥류관",
            mainMenu= "짜장, 등심탕수육",
            latitude= 37.2147182,
            longitude= 126.9774907,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(
            path=61,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200813_280%2F1597330473204ieNzs_JPEG%2FpF1DpuI8AWWOTLjBmISRJkIg.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAxOTA3MDlfMjYg%2FMDAxNTYyNjUyMDQ2NTEx.htk8-43dHZXg9lldGe-TJaRgzXRdbMxfzvevM0wrvIYg.IfcMZMFtm7eJRRnu5nJ5zPzjgbW1_JcM35XrsBqdQB0g.JPEG.leeeyeji%2Foutput_280854227.jpg" ,
            name= "하오츠양꼬치",
            mainMenu= "꼬치무한리필",
            latitude= 37.2152151,
            longitude= 126.9776157,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(
            path=62,
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjExMTFfMTgw%2FMDAxNjY4MTM4NzM3NjQ0.Lqg7YhyXI9yVK0qnZwXw4klX0Fzeja3IWDTLYJjdyZsg.X25mk-Z7L2HFAvhRam5AJUN99G4gki4rWtIrclr2d5Ug.JPEG.junhyi720%2FDSC03921.JPG",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA2MDFfMjAw%2FMDAxNjIyNTM0OTI2NjQy.Q8UQdDZdB5d_lFPH650kZ-TeA_vUMeZ43Qwf8ChKSdIg.HcyuqS3zNT9ZOR6eTJsAMwHyYrEgTXsaqRv4-YOrqZIg.PNG.racine91%2Fimage.png" ,
            name= "카페오늘",
            mainMenu= "커피,디저트",
            latitude= 37.2148100,
            longitude= 126.9773085,
            type= "cafe" // cafe, restaurant, pub
        )
        setStoreData(
            path=63,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190308_180%2F1552028324577NpHpf_JPEG%2FMwKaxUlWoJLIju2hRM7cfW8j.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220625_6%2F1656134912707PhjjC_JPEG%2F%25C1%25A6%25C1%25D6%25B8%25DE1.jpg" ,
            name= "제주도그릴",
            mainMenu= "흑돼지모듬",
            latitude= 37.2147182,
            longitude= 126.9774907,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(
            path=64,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210917_140%2F1631857921942dW7BB_PNG%2FaP3HzS4NLOgZelDz0BcwFbQp.png",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTAxMTlfMTg0%2FMDAxNjExMDQwMDE2MzQ0.d4Mwx-TJxsCS6D-GysxE8RN4Bqyp4jCKuZxMlPsr0NMg.KPC5pfzf0lONdJXhrde9lA1MeXB8dc_-WbeWZgRPC-sg.JPEG.hyemi0202%2F1611040015950.jpg" ,
            name= "한신포차",
            mainMenu= "한신닭발",
            latitude= 37.2150422,
            longitude= 126.9769767,
            type= "pub" // cafe, restaurant, pub

        )
        setStoreData(
            path=65,
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjAzMDlfMjcx%2FMDAxNjQ2Nzk2NDU3NTIz.1igTvJVvZYOWnFuwgK06ZKHWmLziNvCG7I6TewMK7ZAg.KPuudEVHdZA6NOeMCuFGQRpapwGV8VwaLyxxlqN4RDQg.JPEG.rkdls413%2F20220309%25A3%25DF112903.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221130_156%2F1669773187165qNyOK_JPEG%2F20220811%25A3%25DF131031.jpg" ,
            name= "와우순대국",
            mainMenu= "순대국",
            latitude= 37.2152651,
            longitude= 126.9773398,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(
            path=66,
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2F20150519_217%2Fmadehjw_1431999237561OfE0a_JPEG%2F1.jpg&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190515_157%2F1557884223454Tk6h3_JPEG%2FZtm6HjGnMLTJ9KUPx4pHVXCz.jpeg.jpg" ,
            name= "설빙 경기화성수원대점",
            mainMenu= "인절미설빙",
            latitude= 37.2151685,
            longitude= 126.9770195,
            type= "cafe" // cafe, restaurant, pub
        )
        setStoreData(
            path=67,
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA0MTFfMTMz%2FMDAxNjE4MTAxNDkzMDAx.U8BpVsmqsdVZZqb33anLRPHoeDdiD2XMJsITVUl6Y2Qg.eTN-WndWj-uPxlCW1m3pCM-fJsJUW8Mgd4aE5rM-pTUg.JPEG.oh-desk%2F%25C6%25F7%25B8%25CB%25BA%25AF%25C8%25AFIM20170928035058296573.jpg&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220423_81%2F1650640399151pJcx9_JPEG%2F57C6EA0C-3593-4235-B50C-2B462291BB58.jpeg" ,
            name= "공차 수원대점",
            mainMenu= "밀크티+펄",
            latitude= 37.2151685,
            longitude= 126.9770195,
            type= "cafe" // cafe, restaurant, pub

        )
        setStoreData(
            path=68,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200601_104%2F15909877491223w8XK_JPEG%2Fv2468i8Wf8MxqGjY69mjwLv0.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230103_250%2F1672716318713bclOv_JPEG%2F2ADFDB75-76E4-42C5-93B3-9B8CFE511F23.jpeg" ,
            name= "여너여너",
            mainMenu= "연어초밥",
            latitude= 37.2153111,
            longitude= 126.9768259,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(

            path=69,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220629_100%2F1656482712129ItU6B_PNG%2F0._%25B3%25D7%25C0%25CC%25B9%25F6%25C7%25C3%25B7%25B9%25C0%25CC%25BD%25BA_%25B4%25EB%25C7%25A5img.png",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyMjEwMTRfMjEg%2FMDAxNjY1NzM3MjMzMjM1.yHskukJx3NmQ5_Jp4pTOIlj2BMJLJlZywdNpjbixoEwg.SMUA1Li8axDZz-noERIFVY0QYxq6kTxH7JNtNQHBrH4g.JPEG%2F20221014_174421.jpg" ,
            name= "돈까스회관 화성봉담점",
            mainMenu= "등심돈까스",
            latitude= 37.2153111,
            longitude= 126.9768259,
            type= "restaurant" // cafe, restaurant, pub
//홀식사 가능해짐

        )
        setStoreData(
            path=70,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190321_253%2F1553167479956XPc2P_JPEG%2FbuHnUwLiaFr1Ns2-uQMenuHl.jpeg.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20191130_71%2F1575046066407EOjQm_JPEG%2FgrjTmTqwp0zcdyKKemGLJmxo.jpeg.jpg" ,
            name= "술빛상회",
            mainMenu= "술상타코",
            latitude= 37.2149673,
            longitude= 126.9789394,
            type= "pub" // cafe, restaurant, pub
        )
        setStoreData(

            path=71,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190416_83%2F1555404006975txd5q_PNG%2FSJn3reseWXfvh5OMYx7YESGl.png",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190416_71%2F1555404001881AoTHc_PNG%2F05gZtOHBsJlvJC8jZ3wZOvF6.png" ,
            name= "이찌돈 와우점",
            mainMenu= "이찌돈정식",
            latitude= 37.2152158,
            longitude= 126.9785314,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(
            path=72,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201202_217%2F1606885719877g7EB2_JPEG%2F0l81FLeut_iafpTZmZFro2wq.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230912_214%2F1694478750508Yi9J2_JPEG%2F2308_%25B0%25AD%25B4%25D9%25C1%25FC_%25B8%25DE%25B4%25BA%25C6%25C7%2528%25B8%25E9%25B7%25F9%2529.jpg" ,
            name= "강다짐 삼각김밥 화성와우점",
            mainMenu= "참치마요",
            latitude= 37.2139390,
            longitude= 126.9733429,
            type= "restaurant" // cafe, restaurant, pub

        )
        setStoreData(
            path=73,
            imageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA2MDVfMTA4%2FMDAxNjIyODkwNjc1NTE0.HJWPLghZfVjP3Vqn_sS2IqD4Ki7b7S_MV51Iz2NZ0NQg.CqwwgvTWNTcEeQrhei13SSTcMAis9ziHp0fDlUec1DIg.JPEG.newnewry2%2F1622890050556%25A3%25AD9.jpg&type=sc960_832",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221223_79%2F1671756583265aY4R5_JPEG%2F%25C7%25C7%25C0%25DA%25BD%25BA%25C4%25F0_%25C0%25FC%25C3%25BC%25B8%25DE%25B4%25BA_%25C0%25CC%25B9%25CC%25C1%25F6.jpg" ,
            name= "피자스쿨 수원대점",
            mainMenu= "고구마피자",
            latitude= 37.2156298,
            longitude= 126.9769230,
            type= "restaurant" // cafe, restaurant, pub
        )

        setStoreData(
            path=74,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190829_34%2F1567040580460Bheog_JPEG%2FdyLCsYq4159tbRpFxiqn9JDa.JPG.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjA3MTNfMTUw%2FMDAxNjU3NjY5MTYzNzg0.QGVbULVXx_bBmRl-cMCYEcFRuBLuKvXaxxK-YETMJ-Ig._PndKuS_LHGXvHFOwq2nsmT8MTycEYjfMyKjWf251LYg.JPEG.sunlight23%2FP20220712_183653152_C5C575FB-3C2A-4F67-8BFD-1039D4C66F65.jpg",
            name= "꼴두바우",
            mainMenu= "막창",
            latitude= 37.2156298,
            longitude= 126.9769230,
            type= "restaurant" // cafe, restaurant, pub
        )

        setStoreData(
            path=75,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20181127_220%2F1543283117250BBoFD_JPEG%2Fmj9YOxCPvo6HFMr8MH3XZCFE.JPG.jpg",
            menuImageUrl = null,
            name= "도담치킨 수원대점",
            mainMenu= "도담윙봉콤보",
            latitude= 37.2156298,
            longitude= 126.9769230,
            type= "pub" // cafe, restaurant, pub

        )
        setStoreData(

            path=76,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fmyplace-phinf.pstatic.net%2F20200627_33%2F1593235191662lrqIm_JPEG%2Fupload_f3cceabd5434e0c8af80b7c15354868a.jpg",
            menuImageUrl = null,
            name= "마카롱타임",
            mainMenu= "마카롱, 수제 디저트",
            latitude= 37.2142075,
            longitude= 126.9747362,
            type= "cafe" // cafe, restaurant, pub
//메뉴가 달마다바뀜 인스타게시

        )
        setStoreData(
            path=78,
            imageUrl = "https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=https%3A%2F%2Fpostfiles.pstatic.net%2FMjAyMzA5MTJfMjIg%2FMDAxNjk0NDQ4NTc3MTk4.rw1UVRlNYaL7gygdJsQjevVM0uvTA--bFxcTXCxziS8g.N8ojbwNDOHdBARHx9EMlbK7fQwuhurO83O31rtZCQ8sg.JPEG.cheerup121%2FIMG_6784.JPG%3Ftype%3Dw773",
            menuImageUrl = "https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=https%3A%2F%2Fpostfiles.pstatic.net%2FMjAyMzA3MDJfMjIw%2FMDAxNjg4MzA4MTU0ODMw.TllgN2eLLDzo-Q5vff-NjZHC8cP_UUVHsKRep2A9Vkkg.D7gRJ0u6JcyeMRNIVn7YF89vzxuhIWi_4944JKpM7cYg.JPEG.foodhyeon3%2FSE-994837d8-9af7-4cdc-82ec-c7f8a83ec4ec.jpg%3Ftype%3Dw966",
            name= "나의유부",
            mainMenu= "한우육회유부",
            latitude= 37.2137969,
            longitude= 126.9764690,
            type= "restaurant" // cafe, restaurant, pub
        )
        setStoreData(
            path=77,
            imageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190118_201%2F1547815122640LonFR_JPEG%2FQeurfayFr57N6Gn2zNgckWOO.jpg",
            menuImageUrl = "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221130_117%2F1669773322905lq6vo_JPEG%2F20220502%25A3%25DF115504.jpg" ,
            name= "전주집",
            mainMenu= "돼지고기두루치기",
            latitude= 37.2147002,
            longitude= 126.9770651,
            type= "restaurant" // cafe, restaurant, pub
        )
    }

}