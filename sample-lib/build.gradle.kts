plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "me.wcy.router.sample.lib"
    compileSdk = 33

    defaultConfig {
        minSdk = 14

        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
}