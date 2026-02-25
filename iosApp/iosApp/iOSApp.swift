import SwiftUI
import FirebaseCore

@main
struct iOSApp: App {
    init() {
        if FirebaseApp.app() == nil {
            FirebaseApp.configure()
        }
        KoinHelperKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
