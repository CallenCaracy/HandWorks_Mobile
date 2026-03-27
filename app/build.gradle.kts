import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "handworks_cleaning_service.handworks_mobile"
    compileSdk = 36

    defaultConfig {
        applicationId = "handworks_cleaning_service.handworks_mobile"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.2.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
//                value = "\"http://10.14.35.240:8080/api/\"",
                value = "\"https://qa-handworks-api.onrender.com/api/\"",
            )
            buildConfigField(
                type = "String",
                name = "WS_BASE_URL",
//                value = "\"ws://10.14.35.240:8080/api/\""
                value = "\"wss://qa-handworks-api.onrender.com/api/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://qa-handworks-api.onrender.com/api/\"",
            )
            buildConfigField(
                type = "String",
                name ="WS_BASE_URL",
                value = "\"wss://qa-handworks-api.onrender.com/api/\"",
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.hilt.android)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.clerk.android)
    implementation(libs.lifecycle.viewmodel.compose.v292)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.google.flexbox)
    implementation(libs.glide.v4151)
    implementation(libs.swiperefreshlayout)
    implementation(libs.okhttp)
    ksp(libs.hilt.android.compiler)
}