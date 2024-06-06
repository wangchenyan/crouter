plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("crouter-plugin")
}

android {
    namespace = "me.wcy.router.sample"
    compileSdk = 33

    defaultConfig {
        applicationId = "me.wcy.router.sample"
        minSdk = 14
        targetSdk = 33
        versionCode = 1
        versionName = "2.2.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    ksp(project(":crouter-processor"))
    implementation(project(":crouter-api"))
    implementation(project(":sample-lib"))
}
