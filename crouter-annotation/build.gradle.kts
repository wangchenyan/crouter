plugins {
    id("kotlin")
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
