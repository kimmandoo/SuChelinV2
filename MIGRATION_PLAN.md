# SuChelinV2 → Compose Multiplatform + Koin + iOS 마이그레이션 계획

## Context

현재 SuChelinV2는 Android 전용 앱으로, XML DataBinding + 부분 Compose 하이브리드 UI, Fragment 기반 Navigation, LiveData, 수동 싱글톤 패턴을 사용 중입니다. 이를 **Kotlin Multiplatform (KMP) + Compose Multiplatform (CMP)** 구조로 전환하여 iOS를 지원하고, **Koin** DI를 도입하며, 기능 추가가 용이한 구조로 개선합니다.

---

## 1. 목표 모듈 구조

```
SuChelinV2/
  shared/                              # KMP 공유 모듈
    src/
      commonMain/kotlin/com/suchelin/shared/
        model/                         # StoreData, PostData, StoreMenuData, SchoolMealData
        data/
          repository/                  # Repository 인터페이스 + 구현 (GitLive Firebase)
        di/                            # Koin commonMain 모듈
        util/                          # StoreFilter, DateUtil, Constants
        ui/
          theme/                       # Color, Font, Theme (CMP Material3)
          component/                   # FilterBar, TopBar, LoadingIndicator, AdBanner
          screen/
            list/                      # ListScreen + RandomDialog
            detail/                    # DetailScreen
            feed/                      # FeedScreen + PostConfirmDialog
            vote/                      # VoteScreen
            map/                       # MapScreen (expect/actual)
            school/                    # SchoolScreen (expect/actual)
            mail/                      # SendMailDialog
          navigation/                  # NavRoutes, AppNavHost (Compose Navigation)
          viewmodel/                   # MainViewModel, FeedViewModel, VoteViewModel
      androidMain/kotlin/com/suchelin/shared/
        di/                            # platformModule (Android Room builder, Context)
        ui/map/                        # Google Maps AndroidView
        ui/school/                     # Android WebView
        ui/ad/                         # AdMob AndroidView
        ui/component/                  # LottieLoading (Android)
      iosMain/kotlin/com/suchelin/shared/
        di/                            # platformModule (iOS Room builder)
        ui/map/                        # Apple MapKit UIKitView (또는 placeholder)
        ui/school/                     # WKWebView UIKitView
        ui/ad/                         # iOS AdMob UIKitView (또는 placeholder)
        ui/component/                  # CircularProgressIndicator fallback
    build.gradle.kts
  androidApp/                          # Android 진입점 (thin shell)
    src/main/
      java/com/suchelin/android/
        SuChelinApp.kt                 # Application: startKoin, Firebase init
        MainActivity.kt                # ComponentActivity: setContent { App() }
      res/                             # 아이콘, font, lottie raw 등 리소스만
    google-services.json
    build.gradle.kts
  iosApp/                              # Xcode 프로젝트
    iosApp/
      iOSApp.swift                     # Koin init
      ContentView.swift                # ComposeUIViewController 호스팅
    GoogleService-Info.plist
  gradle/libs.versions.toml            # 버전 카탈로그 (buildSrc 대체)
  build.gradle.kts
  settings.gradle.kts                  # includes :shared, :androidApp
```

**삭제 대상:**
- `domain/` 모듈 → `shared/commonMain/model/`로 흡수
- `buildSrc/` → `gradle/libs.versions.toml`로 대체
- `app/` → `androidApp/`로 이름 변경 및 thin shell화
- 모든 XML 레이아웃 (8개), `nav_graph.xml`, `bottom_navigation_bar.xml`
- `base/` 패키지 전체 (BaseActivity, BaseFragment, BaseViewModel)
- Fragment 7개, MapViewAdapter, Parcelable args 2개, Dialog XML 3개
- EventWrapperUtil.kt
- Glide 의존성 (Coil3로 통일)
- Naver Maps SDK 의존성 (Google Maps로 대체)

---

## 2. 핵심 기술 결정

| 영역 | 결정 | 이유 |
|------|------|------|
| DI | **Koin 4.0** | KMP 네이티브 지원, 경량, 러닝커브 낮음 |
| Firebase | **GitLive firebase-kotlin-sdk** | commonMain에서 직접 사용 가능, 콜백→suspend 변환 |
| Local DB | **Room KMP 2.7+** | 기존 코드 재사용 극대화, Google 공식 지원 |
| Navigation | **Compose Navigation (Jetpack)** | Google 공식 지원, 익숙한 API |
| Image | **Coil 3** | CMP 지원, 기존 Coil2 사용 중이라 전환 용이 |
| Logging | **Kermit** | KMP 네이티브, Timber 대체 |
| 상태관리 | **StateFlow + collectAsStateWithLifecycle** | LiveData 대체, KMP 호환 |
| Android 지도 | **Google Maps SDK** | Naver Maps 제거, 크로스플랫폼 일관성 |
| iOS 지도 | **Apple MapKit (UIKitView)** | 네이티브 iOS 경험 |
| iOS 로딩 | **CircularProgressIndicator** | Lottie iOS 연동 복잡, 초기에는 단순하게 |

---

## 3. 버전 업그레이드 (필수)

| 현재 | 목표 | 비고 |
|------|------|------|
| Kotlin 1.8.0 | **2.1.0** | KMP + Compose compiler plugin 통합 |
| AGP 8.1.1 | **8.7.3** | KMP 호환 |
| Gradle 8.5 | **8.9+** | AGP 8.7 요구 |
| Compose UI 1.5.4 | **CMP 1.7.3** (JetBrains) | Compose Multiplatform |
| Room 2.5.2 | **2.7.0+** | Room KMP |
| KSP 1.8.0-1.0.9 | **2.1.0-1.0.29** | Kotlin 버전 매칭 |

---

## 4. 마이그레이션 단계

### Phase 0: 빌드 인프라 업그레이드
> 목표: KMP 도입 전 기존 Android 앱을 최신 빌드 환경으로 안정화

1. `buildSrc/` → `gradle/libs.versions.toml` 전환
2. Kotlin 2.1.0, AGP 8.7.3, Gradle 8.9, KSP 2.1.0-1.0.29 업그레이드
3. Compose compiler를 별도 의존성에서 Kotlin plugin으로 전환 (`composeOptions` 블록 제거)
4. `compileSdk` 35로 업그레이드
5. DataBinding 제거, Safe Args 플러그인 제거
6. LiveData → StateFlow 전환 (모든 ViewModel)
7. Fragment 내 기존 Compose 코드는 유지하되, 빌드 확인

**검증:** `./gradlew assembleDebug` 성공, 기존 기능 동작 확인

### Phase 1: KMP 스켈레톤 + 도메인 이동
> 목표: KMP 멀티플랫폼 프로젝트 구조 수립

1. `settings.gradle.kts` 수정: `:shared`, `:androidApp` include
2. `shared/build.gradle.kts` 생성 (KMP plugin, commonMain/androidMain/iosMain)
3. `domain/` 모델 4개 → `shared/commonMain/model/`로 이동
4. `domain/` 모듈 삭제
5. `app/` → `androidApp/`로 리네이밍
6. `androidApp`가 `:shared`에 의존하도록 설정
7. `StoreFilter` enum, 상수들 → `shared/commonMain/util/`로 이동

**검증:** Android 빌드 성공

### Phase 2: Koin 도입 + Repository 패턴
> 목표: Firebase/Room 직접 호출을 Repository 추상화로 분리

1. Koin 의존성 추가 (`koin-core`, `koin-android`, `koin-compose`, `koin-compose-viewmodel`)
2. Repository 인터페이스 정의 (commonMain):
   - `StoreRepository` — Firestore store 컬렉션 조회
   - `MenuRepository` — Firestore menu 컬렉션 조회
   - `PostRepository` — Firestore suggest 컬렉션 CRUD
   - `VoteRepository` — RTDB 투표 읽기/쓰기
   - `AuthRepository` — 익명 로그인
   - `DailyLimitRepository` — Room FeedDao + LikeDao 래핑
3. Android Firebase SDK로 Repository 구현 (androidMain, 임시)
4. ViewModel 리팩토링: 생성자 주입으로 Repository 수신
5. `startKoin` 설정 (`SuChelinApp.kt`)

**검증:** Android 빌드 및 기능 동작

### Phase 3: Firebase → GitLive KMP SDK
> 목표: Firebase 호출을 commonMain으로 이동

1. `com.google.firebase:firebase-*-ktx` → `dev.gitlive:firebase-*` 교체
2. Repository 구현을 `androidMain` → `commonMain`으로 이동
3. 콜백 패턴 → suspend 함수 전환:
   - `addOnSuccessListener` → `await()` (GitLive 제공)
   - `addValueEventListener` → `valueEvents` Flow 수집
4. `google-services.json`은 `androidApp/`에 유지

**검증:** Android 빌드 및 Firebase 데이터 정상 로딩

### Phase 4: Room KMP 마이그레이션
> 목표: Room 코드를 commonMain으로 이동

1. Room 2.7.0+ 업그레이드
2. `FeedDB` + `LikeDB` → 단일 `AppDatabase`로 통합
3. Entity, DAO, Database 클래스 → `commonMain`으로 이동
4. `expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>` 정의
5. `androidMain` actual: `Room.databaseBuilder(context, ...)`
6. `iosMain` actual: `Room.databaseBuilder(NSHomeDirectory() + "/appDB")`
7. Koin으로 DAO 제공

**검증:** Android 빌드 및 일일 제한 기능 동작

### Phase 5: Compose UI를 shared로 이동
> 목표: 모든 화면을 commonMain Compose로 전환

1. CMP plugin 설정, Compose Navigation 추가
2. Theme (Color, Font, Theme) → `commonMain/ui/theme/`
3. Font: `expect val suChelinFont: FontFamily` + platform actual
4. Coil2 → Coil3 교체
5. 공유 컴포넌트 생성: `FilterBar`, `TopBar`, `AdBanner` (expect/actual), `LoadingIndicator` (expect/actual)
6. 화면별 마이그레이션 (Fragment → Screen composable):

| 순서 | 화면 | 난이도 | 비고 |
|------|------|--------|------|
| 1 | ListScreen | 낮음 | 이미 대부분 Compose, Fragment 쉘만 제거 |
| 2 | DetailScreen | 낮음 | 순수 Compose, 전화 걸기만 expect/actual |
| 3 | VoteScreen | 낮음 | 이미 대부분 Compose |
| 4 | FeedScreen | 낮음 | TextField로 EditText 대체 |
| 5 | MapScreen | **높음** | Google Maps AndroidView / Apple MapKit + HorizontalPager, expect/actual |
| 6 | SingleMapScreen | 중간 | Google Maps / Apple MapKit expect/actual |
| 7 | SchoolScreen | 중간 | WebView expect/actual |

7. Dialog 전환: RandomDialog, PostConfirmDialog, SendMailDialog → Compose AlertDialog
8. `androidApp/MainActivity.kt`를 thin shell로 축소: `setContent { App() }`
9. 모든 Fragment, XML 레이아웃, base 클래스 삭제

**검증:** Android 전체 기능 동작

### Phase 6: iOS 타겟 추가
> 목표: iOS 앱 빌드 및 실행

1. `iosApp/` Xcode 프로젝트 생성
2. `GoogleService-Info.plist` 추가
3. iOS `actual` 구현:
   - `getDatabaseBuilder()` — Room iOS
   - `NativeMapView()` — Apple MapKit UIKitView (같은 위경도 좌표로 마커 표시)
   - `WebViewScreen()` — WKWebView UIKitView
   - `BannerAd()` — 초기에는 빈 placeholder
   - `LoadingIndicator()` — CircularProgressIndicator
   - `makePhoneCall()` — `UIApplication.shared.open(URL("tel:..."))`
   - `suChelinFont` — iOS 번들에서 폰트 로딩
4. Koin iOS 초기화 (`doInitKoin()` from Swift)
5. `ComposeUIViewController` → SwiftUI `ContentView` 연결

**검증:** iOS 시뮬레이터에서 전 화면 동작

### Phase 7: iOS 폴리싱
> 목표: iOS 기능 완성도 향상

1. Apple MapKit에 커스텀 마커/핀 표시
2. iOS AdMob 연동 (UIKitView)
3. iOS Splash 화면 (LaunchScreen.storyboard)
4. 양 플랫폼 E2E 테스트

---

## 5. expect/actual 인터페이스 목록

```kotlin
// commonMain
expect val suChelinFont: FontFamily

@Composable
expect fun NativeMapView(
    stores: List<StoreData>,
    onStoreClick: (StoreData) -> Unit,
    modifier: Modifier
)

@Composable
expect fun WebViewScreen(url: String, modifier: Modifier)

@Composable
expect fun BannerAd(modifier: Modifier)

@Composable
expect fun LoadingIndicator(modifier: Modifier)

expect fun makePhoneCall(context: Any, tel: String)

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
```

---

## 6. Koin 모듈 구조

```kotlin
// commonMain - sharedModule
val sharedModule = module {
    single<StoreRepository> { StoreRepositoryImpl() }
    single<MenuRepository> { MenuRepositoryImpl() }
    single<PostRepository> { PostRepositoryImpl() }
    single<VoteRepository> { VoteRepositoryImpl() }
    single<AuthRepository> { AuthRepositoryImpl() }
    single<DailyLimitRepository> { DailyLimitRepositoryImpl(get(), get()) }
    viewModelOf(::MainViewModel)
    viewModelOf(::FeedViewModel)
    viewModelOf(::VoteViewModel)
}

// expect/actual - platformModule
expect val platformModule: Module
// androidMain: Room builder with Context, AdManager init
// iosMain: Room builder with file path
```

---

## 7. 주요 리스크

| 리스크 | 심각도 | 대응 |
|--------|--------|------|
| Kotlin 1.8→2.1 대규모 업그레이드 | **높음** | Phase 0에서 격리 처리, 빌드 안정화 후 진행 |
| GitLive Firebase SDK 안정성 | 중간 | 사용 패턴이 단순(read/write)하여 문제 가능성 낮음. 문제 시 해당 메서드만 expect/actual |
| Room KMP iOS 안정성 | 중간 | 테이블 2개로 매우 단순. 문제 시 iOS에서 NSUserDefaults로 대체 가능 |
| Naver Maps → Google Maps 전환 | 중간 | API가 유사하나 마커/카메라 코드 재작성 필요. 위경도 좌표는 동일하게 사용 |

---

## 8. 주요 파일 경로

**수정 대상 (핵심):**
- `app/build.gradle.kts` → 완전 재작성 (`androidApp/build.gradle.kts`)
- `app/src/main/java/com/suchelin/android/container/MainViewModel.kt` → Repository 패턴 + StateFlow
- `app/src/main/java/com/suchelin/android/container/MainActivity.kt` → thin shell ComponentActivity
- `app/src/main/java/com/suchelin/android/feature/view_compose/vote/VoteViewModel.kt` → StateFlow + Koin
- `app/src/main/java/com/suchelin/android/feature/view_compose/feed/FeedViewModel.kt` → StateFlow + Koin
- `app/src/main/java/com/suchelin/android/util/FunctionUtil.kt` → 분리 (순수 유틸 → commonMain, Naver Map 코드 삭제)

**삭제 대상 (Naver Maps → Google Maps 전환):**
- Naver Maps SDK 의존성 (`com.naver.maps:map-sdk`)
- `AndroidManifest.xml`의 Naver CLIENT_ID meta-data
- `local.properties`의 `NAVER_CLIENT_ID`
- `FunctionUtil.kt`의 NaverMap 확장 함수들 (initMap, initMarker, singleMarker, moveMarker)

**새로 추가 (Google Maps):**
- `com.google.maps.android:maps-compose` 의존성 (androidMain)
- Google Maps API 키 설정

**새로 생성:**
- `shared/build.gradle.kts`
- `gradle/libs.versions.toml`
- `shared/src/commonMain/` 전체 패키지 구조
- `shared/src/androidMain/` expect actual 구현 (Google Maps)
- `shared/src/iosMain/` expect actual 구현 (Apple MapKit)
- `iosApp/` Xcode 프로젝트
