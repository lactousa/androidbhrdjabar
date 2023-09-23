plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.bhrdjawabarat"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.bhrdjawabarat"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.16")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("me.grantland:autofittextview:0.2.1")
    testImplementation("junit:junit:4.13.2")
    implementation ("org.osgeo:proj4j:0.1.0")
    implementation ("com.ibm.icu:icu4j:69.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}