plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.valueOf(libs.versions.java.get())
    targetCompatibility = JavaVersion.valueOf(libs.versions.java.get())
}

dependencies {
    implementation(gradleApi())
    compileOnly(libs.gradle)
    implementation(libs.asm)
    implementation(libs.asm.commons)
    implementation(project(":crouter-annotation"))
}

gradlePlugin {
    plugins {
        create("crouter-plugin") {
            id = "crouter-plugin"
            implementationClass = "me.wcy.router.plugin.RouterPlugin"
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                groupId = "com.github.wangchenyan"
                artifactId = "crouter-plugin"
                version = "1.0"
            }
        }
    }
}