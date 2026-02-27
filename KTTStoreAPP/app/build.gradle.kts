plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.nhom1.kttstoreapp"
    compileSdk = 35

    defaultConfig {
        // GIỮ applicationId riêng để không conflict với app gốc
        applicationId = "com.nhom1.kttstoreapp"
        minSdk = 24
        targetSdk = 34
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
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.glide)
    implementation(libs.swiperefreshlayout)
    implementation(libs.volley)
    implementation(libs.picasso)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Thêm các dependency dạng chuỗi giống kttstore-android (nếu cần)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("io.socket:socket.io-client:2.0.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
}