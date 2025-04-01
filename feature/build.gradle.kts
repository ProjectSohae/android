import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
    kotlin("plugin.serialization")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

val properties = Properties()
properties.load(FileInputStream("local.properties"))

android {
    namespace = "com.sohae.feature"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "NAVER_MAP_CLIENT", properties.getProperty("naver.map.client.id"))

        buildConfigField("String", "KAKAO_NATIVE_KEY", properties.getProperty("kakao.native.app.key"))
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", properties.getProperty("google.web.client.id"))
        buildConfigField("String", "GOOGLE_ANDROID_CLIENT_ID", properties.getProperty("google.android.client.id"))
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(project(":domain"))
    implementation(project(":common:remote"))
    implementation(libs.play.services.auth)
    implementation(project(":controller"))
    implementation(libs.firebase.messaging.ktx)
    kapt(libs.hilt.android.compiler)

    implementation(project(":common:resource"))
    implementation(project(":common:ui:custom"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.coil3.coil.compose)

    implementation(libs.haze)
    implementation(libs.haze.materials)

    implementation(libs.kakao.v2.user)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    implementation(libs.volley)
    implementation(libs.map.sdk)
    implementation(libs.naver.map.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.jetbrains.kotlinx.datetime)
}