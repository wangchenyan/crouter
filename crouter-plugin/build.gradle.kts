plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("java-gradle-plugin")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(gradleApi())
    compileOnly("com.android.tools.build:gradle:7.4.2")
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
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