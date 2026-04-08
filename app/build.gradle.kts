plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

//    id("io.realm.kotlin")
}

android {
    namespace = "com.newBie.new_bie"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.newBie.new_bie"
        minSdk = 24
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.googleid)
    implementation(libs.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.github.bumptech.glide:glide:5.0.5")

    //레트로핏
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // 컨버터 Json을 Kotlin으로 바꿔주는 컨버터는 모두 바꾸는 것
    //서버에서 들어온 Json 데이터를 안드에서 사용하는 dataClass로 바꿔주는 것
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    // 통신 라이브러리
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    // inter = between
    // cept - catch
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation("com.github.bumptech.glide:compose:1.0.0-beta07")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //뷰모델
    // view model
    val lifecycle_version = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

//    implementation("io.realm.kotlin:library-base:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")


    //dataStore
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    //supaBase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.2.2"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:supabase-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.ktor:ktor-client-android:3.2.3")
    implementation("io.github.jan-tennert.supabase:storage-kt:VERSION")

    implementation("io.github.jan-tennert.supabase:auth-kt:3.2.2")

    implementation ("com.github.bumptech.glide:glide:5.0.4")

    //캘린더 오픈소스
    implementation("com.kizitonwose.calendar:compose:2.7.0")

    //nav
    val nav_version = "2.9.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //구글 로그인
    implementation("androidx.credentials:credentials:1.6.0-rc01")
    implementation("androidx.credentials:credentials-play-services-auth:1.6.0-rc01")
    implementation("com.google.android.libraries.identity.googleid:googleid:<latest version>")

    implementation("androidx.compose.material:material-icons-extended")

    // 인터넷 이미지 띄우기
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation("com.cloudinary:kotlin-url-gen:1.7.0")

    // Zoomable
    val ZoomableVersion = "2.11.1"
    implementation("net.engawapg.lib:zoomable:${ZoomableVersion}")

}