plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("auto-register")
}

android {
    namespace = "me.wcy.crouter.example"
    compileSdk = 33

    defaultConfig {
        applicationId = "me.wcy.crouter.example"
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
 * crouter 参数，必须！！！
 */
kapt {
    arguments {
        // 使用默认
        arg("moduleName", project.name)
        // 默认 scheme
        arg("defaultScheme", "(http|https|native|host)")
        // 默认 host
        arg("defaultHost", "(\\w+\\.)*host\\.com")
    }
}

/**
 * 路由收集，必须！！！
 */
autoregister {
    registerInfo = listOf(
        mapOf(
            "scanInterface" to "me.wcy.router.annotation.RouteLoader",
            "codeInsertToClassName" to "me.wcy.router.RouteSet",
            "registerMethodName" to "register",
            "include" to listOf("me/wcy/router/annotation/loader/.*")
        )
    )
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    kapt(project(":crouter-compiler"))
    implementation(project(":crouter-api"))
}
