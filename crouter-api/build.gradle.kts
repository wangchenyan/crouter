plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "me.wcy.router"
    compileSdk = 33

    defaultConfig {
        minSdk = 14
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["release"])
            }
        }
    }
}

dependencies {
    api(project(":crouter-annotation"))
    compileOnly("androidx.appcompat:appcompat:1.6.1")
}
