// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "M430CapacitorAppUpdater",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "M430CapacitorAppUpdater",
            targets: ["AppUpdaterPluginPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "AppUpdaterPluginPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/AppUpdaterPluginPlugin"),
        .testTarget(
            name: "AppUpdaterPluginPluginTests",
            dependencies: ["AppUpdaterPluginPlugin"],
            path: "ios/Tests/AppUpdaterPluginPluginTests")
    ]
)