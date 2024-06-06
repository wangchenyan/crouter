plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}

dependencies {
    implementation(libs.ksp)
    implementation(libs.kotlinpoet)
    implementation(project(":crouter-annotation"))
}