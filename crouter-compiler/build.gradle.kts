plugins {
    id("kotlin")
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
    implementation("com.google.auto.service:auto-service:1.0")
    implementation("com.squareup:javapoet:1.13.0")
    implementation(project(":crouter-annotation"))
}