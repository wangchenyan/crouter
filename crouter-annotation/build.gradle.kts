plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                groupId = "com.github.wangchenyan"
                artifactId = "crouter-annotation"
                version = "1.0"
            }
        }
    }
}
