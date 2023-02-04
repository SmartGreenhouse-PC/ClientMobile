import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    jacoco
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.jacocoCoverage)
}

jacoco {
    toolVersion = "0.8.8"
}

android {
    compileSdk =  33

    defaultConfig {
        applicationId = "it.unibo.smartgh"
        minSdk = 24
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isTestCoverageEnabled = true
        }
    }

    compileOptions {
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {

    implementation(libs.bundles.android.dependencies)
    implementation(libs.bundles.vertx.dependencies)
    implementation(libs.picasso)
    implementation(libs.gson)
    implementation(libs.jacoco)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    androidTestImplementation(libs.awaitility)
    androidTestImplementation(libs.bundles.androidx.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}