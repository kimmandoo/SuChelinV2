# iOS Bottom Bar White Box Issue - Full Resolution Log

## 1) Issue Summary

- **Reported symptom**: On iOS, a small white box appeared around/near the custom floating bottom bar.
- **Target area**: `shared/src/commonMain/kotlin/com/suchelin/shared/App.kt` bottom bar block (`if (route.showInBottomBar) { ... }`).

---

## 2) Initial Investigation (Context Gathering)

### Repository inspection

- Located bottom bar implementation in:
  - `shared/src/commonMain/kotlin/com/suchelin/shared/App.kt`
- Confirmed route visibility logic in:
  - `shared/src/commonMain/kotlin/com/suchelin/shared/ui/navigation/NavRoutes.kt`
- Confirmed iOS entry point in:
  - `shared/src/iosMain/kotlin/com/suchelin/shared/AppViewController.kt`

### Relevant code characteristics (original)

- Bottom bar used:
  - `WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)`
  - `shadow(...)`
  - two layered `background(...)` with alpha/gradient
  - rounded shape
- This combination is known to be more fragile on iOS rendering pipeline versus Android.

### External/agent reasoning used

- Investigation included parallel exploration and oracle-style diagnosis.
- Main hypotheses identified:
  1. iOS safe-area vs Android navigation-bar inset mismatch.
  2. iOS compositing artifact from `shadow + multiple translucent backgrounds` without strict clipping discipline.

---

## 3) First Fix Attempt

### Changes applied

File: `shared/src/commonMain/kotlin/com/suchelin/shared/App.kt`

1. Changed bottom inset handling:
   - From: `WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)`
   - To: `WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)`
2. Added explicit clipping for bottom bar container:
   - Added `clip(bottomBarShape)`
3. Unified shape usage across shadow/background:
   - Introduced `bottomBarShape = RoundedCornerShape(22.dp)` and reused it.

### Result

- User feedback: **Not fixed yet** (`안고쳐졌는데`).
- Conclusion: Need a more conservative iOS rendering strategy.

---

## 4) Final (Stronger) Fix Strategy

After first attempt failed, the strategy was upgraded to reduce iOS artifact risk further:

1. Eliminate fragile visual stack for bottom bar (`shadow + dual translucent background`).
2. Force predictable root background behavior in iOS host view.

### Final code changes

#### A) Bottom bar rendering hardening

File: `shared/src/commonMain/kotlin/com/suchelin/shared/App.kt`

- Wrapped root content in `Surface(color = GroupedBackground)` instead of relying only on `Box.background(...)`.
- Bottom bar style changed from:
  - `shadow + background(alpha) + gradient background`
- To:
  - `clip(shape) + single background(alpha) + 1dp border`
- Kept iOS-safe bottom insets:
  - `WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)`

#### B) iOS host view background hardening

File: `shared/src/iosMain/kotlin/com/suchelin/shared/AppViewController.kt`

- Updated `MainViewController()` to explicitly set:
  - `view.backgroundColor = UIColor.clearColor`

---

## 5) Verification Performed

### Diagnostics

- `lsp_diagnostics` for Kotlin could not run due to missing local Kotlin LSP installation (`kotlin-lsp` not installed).

### Build / compile / test commands executed

1. `./gradlew :shared:compileKotlinMetadata` -> **SUCCESS**
2. `./gradlew :shared:compileKotlinIosSimulatorArm64` -> **SUCCESS**
3. `./gradlew :shared:allTests` -> **SUCCESS** (`NO-SOURCE`)

### Additional note

- A full `:shared:build` previously failed at `:shared:linkReleaseFrameworkIosArm64` with Java heap OOM in environment.
- This was an environment/memory constraint during release framework link, not a syntax/type regression in modified files.

---

## 6) Files Modified During Resolution

- `shared/src/commonMain/kotlin/com/suchelin/shared/App.kt`
- `shared/src/iosMain/kotlin/com/suchelin/shared/AppViewController.kt`

---

## 7) Why This Final Fix Is Safer on iOS

- iOS tends to expose artifacts more often with layered translucent compositing + shadows near safe-area boundaries.
- The final approach intentionally reduces those triggers:
  - safer inset source (`safeDrawing`)
  - simpler bar compositing (single fill + border)
  - explicit host background behavior (`clearColor`)

---

## 8) Manual QA Checklist (Recommended)

1. Run iOS app on a device/simulator with home indicator.
2. Navigate screens where bottom bar is shown.
3. Check around bottom bar edges while switching tabs.
4. Confirm no small white rectangle appears at bottom/edge.

---

## 9) Final Status

- Process documented from initial report to final patch.
- Stronger iOS-safe rendering changes applied and compiled successfully for shared/common + iOS simulator target.
