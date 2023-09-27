import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    kotlin("android")
}

android {
    namespace = "com.example.letstravel"
    compileSdk = AppConfig.COMPILE_SDK

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.letstravel"
        minSdk = AppConfig.MIN_SDK
        targetSdk = AppConfig.TARGET_SDK
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME

        buildConfigField("String", "KAKAO_APP_KEY", getApiKey("KAKAO_APP_KEY"))
        buildConfigField("String", "MAPS_API_KEY", getApiKey("MAPS_API_KEY"))
        manifestPlaceholders["MAPS_API_KEY"] = properties["MAPS_API_KEY"].toString()
        manifestPlaceholders["KAKAO_SCHEME_APP_KEY"] = properties["KAKAO_SCHEME_APP_KEY"].toString()
        manifestPlaceholders["NAVER_CLIENT_ID"] = properties["NAVER_CLIENT_ID"].toString()
        manifestPlaceholders["GOOGLE_CLIENT_ID"] = properties["GOOGLE_CLIENT_ID"].toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        jvmTarget = AppConfig.JVM_TARGET
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_STDLIB)

    // AndroidX
    implementation(Libraries.AndroidX.APP_COMPAT)
    implementation(Libraries.AndroidX.MATERIAL)
    implementation(Libraries.AndroidX.CONSTRAINT_LAYOUT)

    // Legacy
    implementation(Libraries.Legacy.LEGACY)
    implementation(Libraries.Swipe.REFRESH_LAYOUT)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // TEST
    testImplementation(Libraries.Test.JUNIT)

    // AndroidTest
    androidTestImplementation(Libraries.AndroidTest.ESPRESSO_CORE)
    androidTestImplementation(Libraries.AndroidTest.EXT_JUNIT)

    // Recyclerview
    implementation(Libraries.RecyclerView.RECYCLERVIEW)
    implementation(Libraries.RecyclerView.RECYCLERVIEW_SELECTION)

    // Google
    implementation(Libraries.Google.GOOGLE_MAP)
    implementation(Libraries.Google.GOOGLE_LOCATION)
    implementation(Libraries.Google.GOOGLE_LIB_PLACES)

    // Navigation
    implementation(Libraries.Navigation.UI)
    implementation(Libraries.Navigation.FRAGMENT)
    implementation("androidx.navigation:navigation-fragment:${Versions.NAVIGATION}")
    implementation("androidx.navigation:navigation-ui:${Versions.NAVIGATION}")
    implementation(Libraries.Navigation.COMPOSE)

    // SNS
    implementation(Libraries.SNS.KAKAO)
    implementation(Libraries.SNS.NAVER)

    // Lifecycle
    implementation(Libraries.Lifecycle.VIEW_MODEL)
    implementation(Libraries.Lifecycle.LIVE_DATA)

    // Coordinator
    implementation(Libraries.CoordinatorLayout.COORDINATOR_LAYOUT)

}
fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}


