import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.akansha.catapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.akansha.catapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localPropertiesFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(localPropertiesFile.inputStream())

        buildConfigField(
            type = "String",
            name = "CAT_API_KEY",
            value = "\"${properties.getProperty("CAT_API_KEY")}\""
        )
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
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            pickFirsts.add("**/LICENSE**.md")
        }
    }
}

ksp { arg("ksp.incremental", "false") }

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.timber)
    implementation(libs.bundles.network)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.coil.kt.compose)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    testImplementation(libs.junit)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}