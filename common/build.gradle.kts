import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("com.squareup.sqldelight")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

// CocoaPods requires the podspec to have a version.
version = "1.0"

android {
    compileSdkVersion(AndroidSdk.compile)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}


// workaround for https://youtrack.jetbrains.com/issue/KT-43944
android {
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}


kotlin {
    val sdkName: String? = System.getenv("SDK_NAME")

    val isiOSDevice = sdkName.orEmpty().startsWith("iphoneos")
    if (isiOSDevice) {
        iosArm64("iOS")
    } else {
        iosX64("iOS")
    }

    android()
    jvm()

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Tv Search Application"
        homepage = "github.com/MateuszKrawczuk/tvprogramsearcher"
    }

    sourceSets {

        sourceSets["commonMain"].dependencies {
            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}") {
                isForce = true
            }

            // Ktor
            implementation(Ktor.clientCore)
            implementation(Ktor.clientJson)
            implementation(Ktor.clientLogging)
            implementation(Ktor.clientSerialization)

            // Kotlinx Serialization
            implementation(Serialization.core)

            // SQL Delight
            implementation(SqlDelight.runtime)
            implementation(SqlDelight.coroutineExtensions)

            // koin
            api(Koin.core)
            api(Koin.test)

            // kermit
            api(Deps.kermit)
        }
        sourceSets["commonTest"].dependencies {
        }

        sourceSets["androidMain"].dependencies {
            implementation(Ktor.clientAndroid)
            implementation(SqlDelight.androidDriver)
        }
        sourceSets["androidTest"].dependencies {
            implementation(kotlin("test-junit"))
            implementation(Test.junit)
        }

        sourceSets["jvmMain"].dependencies {
            implementation(Ktor.clientApache)
            implementation(Ktor.slf4j)
            implementation(SqlDelight.jdbcDriver)
            implementation(SqlDelight.sqlliteDriver)
        }

        sourceSets["iOSMain"].dependencies {
            implementation(Ktor.clientIos)
            implementation(SqlDelight.nativeDriver)
        }
        sourceSets["iOSTest"].dependencies {
        }
    }
}

sqldelight {
    database("TvProgramSearcherDatabase") {
        packageName = "com.mateuszkrawczuk.tvprogramsearcher.db"
        sourceFolders = listOf("sqldelight")
    }
}

multiplatformSwiftPackage {
    packageName("tvprogramsearcher")
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("13") }
    }
}

