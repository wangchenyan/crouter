plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("crouter-plugin")
}

android {
    namespace = "me.wcy.router.sample"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "me.wcy.router.sample"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    signingConfigs {
        register("release") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

/**
 * CRouter required params
 */
ksp {
    // 使用默认
    arg("moduleName", project.name)
    // 默认 scheme
    arg("defaultScheme", "(http|https|native|host)")
    // 默认 host
    arg("defaultHost", "(\\w+\\.)*host\\.com")
}

dependencies {
    implementation(libs.appcompat)
    ksp(project(":crouter-processor"))
    implementation(project(":crouter-api"))
    implementation(project(":sample-lib"))
}
