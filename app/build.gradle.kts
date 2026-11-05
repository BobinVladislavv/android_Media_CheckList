plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.VladBobin.mediachecklist"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.VladBobin.moviechecklist"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Unit-тесты (JVM)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    testImplementation(libs.core.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.lifecycle.runtime.testing)

    // Инструментальные тесты (эмулятор)
    androidTestImplementation(libs.junit.v121)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.core)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.core.testing)
}