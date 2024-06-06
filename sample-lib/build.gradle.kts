plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "me.wcy.router.sample.lib"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf(libs.versions.java.get())
        targetCompatibility = JavaVersion.valueOf(libs.versions.java.get())
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
}