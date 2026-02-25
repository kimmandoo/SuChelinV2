# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SuChelinV2 is an Android app that helps Suwon University students discover nearby restaurants, cafes, and pubs. It uses Firebase as its backend and Naver Maps for location features.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run a single test class
./gradlew :app:testDebugUnitTest --tests "com.suchelin.android.ExampleUnitTest"

# Clean build
./gradlew clean
```

## Required local.properties

The build reads API keys from `local.properties` (not committed). This file must exist with:
```
NAVER_CLIENT_ID=<your_naver_map_client_id>
ADMOB_ID=<your_admob_app_id>
```

## Module Structure

- **`app`** — the Android application (minSdk 28, targetSdk 34, Kotlin 1.8, Java 17)
- **`domain`** — pure Kotlin data models only (`StoreData`, `StoreMenuData`, `PostData`, `SchoolMealData`)
- **`buildSrc`** — version catalog via `Versions.kt` and `Dependencies.kt` (Kotlin DSL objects)

All dependency versions are centrally managed in `buildSrc/src/main/java/Versions.kt`.

## Architecture

**MVVM** with shared ViewModel pattern:

- `BaseActivity<B, VM>` and `BaseFragment<B, VM>` — generic base classes using DataBinding. Subclasses must implement `initView()` and declare `viewModel`.
- `MainViewModel` — single shared ViewModel (via `activityViewModels()`) that holds all Firebase data: `storeData`, `menuData`, `postData`. Loaded once on app start via `initData()` after anonymous Firebase Auth sign-in.
- Feature ViewModels (e.g., `FeedViewModel`, `ListViewModel`) handle local UI state only.

**Navigation**: Jetpack Navigation Component with Safe Args. Bottom nav has 3 tabs (List, Map, Feed). Detail, SingleMap, and School fragments hide the bottom nav bar.

**UI pattern**: Hybrid XML + Compose. Fragments use XML layouts with DataBinding for the outer shell (toolbar, ads, loading spinner), and embed `ComposeView` for list content (LazyColumn). Compose functions are defined directly inside Fragment classes.

## Key Features & Data Flow

| Feature | Fragment | Data Source |
|---|---|---|
| Store list + search/filter | `ListFragment` | Firebase Firestore (`store` collection) via `MainViewModel.storeData` |
| Map with markers | `MapViewFragment` | Same `storeData` LiveData + Naver Maps SDK |
| Community feed | `FeedFragment` | Firebase Firestore (`suggest` collection), Room DB for write throttling |
| School cafeteria menu | `SchoolFragment` | WebView → Suwon University website |
| Store detail | `DetailFragment` | `StoreDataArgs` parcelable via Safe Args + Firestore `menu` collection |
| Voting | `VoteFragment` | Firebase RTDB |

## Firebase Collections

- `store` — store entries ordered by `path` field (ascending)
- `menu` — menu data keyed by store ID; `image: Boolean` determines if menu is image URLs (`List<String>`) or text items (`List<{menuName, menuPrice}>`)
- `suggest` — daily community posts, document ID is `yyyy-MM-dd`

## Store Types / Filters

`StoreFilter` enum defines: `CAFE`, `RESTAURANT`, `PUB`, `ALL`, `RANK`, `SEARCH`. The `type` field in Firestore maps to `"cafe"`, `"restaurant"`, or `"pub"`. Map markers use different icons per type (tea/beer/rice drawables).

## Commit Message Convention

| Tag | Use |
|---|---|
| `feat:` | New feature |
| `fix:` | Bug fix |
| `design:` | UI/visual change only |
| `refactor:` | Code refactoring, no behavior change |
| `style:` | Formatting, no logic change |
| `chore:` | Build config, dependencies |
| `docs:` | Documentation only |
| `rename:` / `remove:` | File moves or deletions |
| `!BREAKING CHANGE:` | Major API change |
| `!HOTFIX:` | Critical production fix |
