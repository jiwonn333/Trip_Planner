object Dependencies {
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
}

object Versions {
    // App Level
    const val GRADLE = "7.4.2"
    const val KOTLIN = "1.8.0"

    // AndroidX
    const val APP_COMPAT = "1.6.1"
    const val MATERIAL = "1.9.0"
    const val CONSTRAINT_LAYOUT = "2.1.4"

    // KTX
    const val CORE = "1.7.0"

    // Legacy
    const val LEGACY = "1.0.0"

    // swiperefreshlayout
    const val SWIPE_REFRESH_LAYOUT = "1.0.0"

    // TEST
    const val JUNIT = "4.13.2"

    // Android Test
    const val ESPRESSO_CORE = "3.5.1"
    const val EXT_JUNIT = "1.1.5"

    // RecyclerView
    const val RECYCLERVIEW = "1.3.0"
    const val RECYCLERVIEW_SELECTION = "1.1.0"

    // Google
    const val GOOGLE_MAP = "18.1.0"
    const val GOOGLE_LIB_PLACES = "3.1.0"
    const val GOOGLE_LOCATION = "21.0.1"

    // Navigation
    const val NAVIGATION = "2.5.3"

    // SNS
    const val KAKAO = "2.12.0"
    const val NAVER = "5.3.0"

    // LifeCycle
    const val LIFECYCLE = "2.5.1"

    // Coordinatorlayout
    const val COORDINATOR = "1.2.0"

}

object Libraries {
    object CoordinatorLayout {
        const val COORDINATOR_LAYOUT =
            "androidx.coordinatorlayout:coordinatorlayout:${Versions.COORDINATOR}"
    }

    object Lifecycle {
        const val VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
        const val LIVE_DATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    }

    object SNS {
        const val KAKAO = "com.kakao.sdk:v2-user:${Versions.KAKAO}"
        const val NAVER = "com.navercorp.nid:oauth-jdk8:${Versions.NAVER}"
    }

    object Navigation {
        const val COMPOSE = "androidx.navigation:navigation-compose:${Versions.NAVIGATION}"
        const val FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
        const val UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    }

    object Google {
        const val GOOGLE_MAP = "com.google.android.gms:play-services-maps:${Versions.GOOGLE_MAP}"
        const val GOOGLE_LIB_PLACES = "com.google.android.libraries.places:places:${Versions.GOOGLE_LIB_PLACES}"
        const val GOOGLE_LOCATION =
            "com.google.android.gms:play-services-location:${Versions.GOOGLE_LOCATION}"
    }

    object RecyclerView {
        const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"
        const val RECYCLERVIEW_SELECTION =
            "androidx.recyclerview:recyclerview-selection:${Versions.RECYCLERVIEW_SELECTION}"
    }

    object AndroidX {
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
        const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    }

    object Legacy {
        const val LEGACY = "androidx.legacy:legacy-support-v4:${Versions.LEGACY}"
    }

    object Swipe {
        const val REFRESH_LAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPE_REFRESH_LAYOUT}"
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
    }

    object AndroidTest {
        const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
        const val EXT_JUNIT = "androidx.test.ext:junit:${Versions.EXT_JUNIT}"
    }
}