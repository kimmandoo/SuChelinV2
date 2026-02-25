import SwiftUI
import FirebaseCore
import shared

@main
struct iOSApp: App {
    init() {
        if FirebaseApp.app() == nil {
            FirebaseApp.configure()
        }
        KoinModulesKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
