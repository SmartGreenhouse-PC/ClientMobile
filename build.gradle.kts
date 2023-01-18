// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply{
        set("kotlin_version", "1.5.10")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android)
        classpath(libs.kotlin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}