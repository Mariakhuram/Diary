plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //kapt
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "my.dialy.dairy.journal.dairywithlock"
    compileSdk = 34

    defaultConfig {
        applicationId = "my.dialy.dairy.journal.dairywithlock"
        minSdk = 23
        targetSdk = 34
        versionCode = 5
        versionName = "1.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "admobSDKKey", "ca-app-pub-8731080558489121~7896130253")
            resValue("string", "admobBannerAd", "ca-app-pub-3940256099942544/6300978111")
            resValue("string", "admobNativeAd", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "admobInterstitialAd", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "admobRewardAd", "ca-app-pub-3940256099942544/5224354917")
            resValue("string", "admobRewardAdsInitialID", "ca-app-pub-3940256099942544/5354046379")
            resValue("string", "admobAppOpenAd", "ca-app-pub-8731080558489121/4481028228")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "admobSDKKey", "ca-app-pub-3940256099942544~3347511713")
            resValue("string", "admobBannerAd", "ca-app-pub-3940256099942544/6300978111")
            resValue("string", "admobNativeAd", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "admobInterstitialAd", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "admobRewardAd", "ca-app-pub-3940256099942544/5224354917")
            resValue("string", "admobRewardAdsInitialID", "ca-app-pub-3940256099942544/5354046379")
            resValue("string", "admobAppOpenAd", "ca-app-pub-3940256099942544/9257395921")
        }
    }
    // Enable hilt
    hilt {
        enableAggregatingTask = true;
    }
    //viewBinding
    buildFeatures{
        viewBinding=true;
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation ("com.airbnb.android:lottie:4.1.0")
    //emoji
    implementation("androidx.emoji2:emoji2-emojipicker:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-crashlytics:18.6.0")
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    implementation("androidx.lifecycle:lifecycle-process:2.7.0")
    implementation("com.android.volley:volley:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //ssp&ssp
    implementation ("com.intuit.ssp:ssp-android:1.1.0")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //room
    // Room components
    val room_version   = ("2.6.0")
    implementation("androidx.room:room-runtime:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    implementation( "androidx.room:room-ktx:$room_version")
    //life
    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    //hilt
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-android-compiler:2.48")
    kapt ("androidx.hilt:hilt-compiler:1.1.0")
    implementation ("androidx.activity:activity-ktx:1.8.1")
    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")
    //swipRefreshlayout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //gson
    implementation ("com.google.code.gson:gson:2.9.0")
    //zoomage
    implementation ("com.jsibbold:zoomage:1.3.1")
    //figner
    implementation ("androidx.biometric:biometric:1.2.0-alpha03")
    //one sing
    implementation("com.onesignal:OneSignal:[4.0.0, 4.99.99]")
    //
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.google.android.gms:play-services-ads:22.6.0")
    //
    implementation ("com.google.android.gms:play-services-auth:16.0.1")
   /* implementation ("com.google.http-client:google-http-client-gson:1.26.0")
    implementation("com.google.api-client:google-api-client-android:1.26.0") {
    }
    implementation("com.google.apis:google-api-services-drive:v3-rev136-1.25.0") {
    }*/

}