
import Versions.androidConstraintLayoutVersion
import Versions.androidMaterialVersion
import Versions.androidTestRunnerVersion
import Versions.androidxCoreKtxVersion
import Versions.appCompatVersion
import Versions.daggerVersion
import Versions.epoxyVersion
import Versions.espressoVersion
import Versions.glideVersion
import Versions.jUnitVersion
import Versions.kotlinVersion
import Versions.lifecycleExtensionsVersion
import Versions.lottieVersion
import Versions.markwonVersion
import Versions.mockitoCoreVersion
import Versions.mockitoKotlinVersion
import Versions.moshiVersion
import Versions.navVersion
import Versions.retrofitVersion
import Versions.roomRxJavaVersion
import Versions.roomVersion
import Versions.rxJava2Version
import Versions.rxJavaAndroidVersion
import Versions.swipeToRevealVersion
import Versions.timberVersion

object Versions {
    const val kotlinVersion = "1.3.61"
    const val minSdk = 21
    const val compileAndTargetSdk = 29
    const val buildToolsVersion = "29.0.2"

    const val lottieVersion = "3.3.1"
    const val swipeToRevealVersion = "1.0"
    const val epoxyVersion = "3.9.0"
    const val markwonVersion = "4.2.0"
    const val navVersion = "2.1.0"
    const val retrofitVersion = "2.4.0"
    const val lifecycleExtensionsVersion = "2.1.0"
    const val moshiVersion = "1.8.0"
    const val daggerVersion = "2.23.2"
    const val timberVersion = "4.7.1"
    const val rxJava2Version = "2.3.0"
    const val rxJavaAndroidVersion = "2.1.0"
    const val appCompatVersion = "1.1.0"
    const val androidxCoreKtxVersion = "1.1.0"
    const val androidMaterialVersion = "1.0.0"
    const val androidConstraintLayoutVersion = "1.1.3"
    const val roomVersion ="2.2.3"
    const val roomRxJavaVersion = "1.1.1"
    const val glideVersion = "4.8.0"

    // testing
    const val jUnitVersion = "4.12"
    const val mockitoCoreVersion = "2.11.0"
    const val mockitoKotlinVersion = "1.5.0"
    const val androidTestRunnerVersion = "1.2.0"
    const val espressoVersion = "3.2.0"
}

object Dependencies {
    // https://github.com/jmfayard/refreshVersions

    // dependencies
    const val lottie = "com.airbnb.android:lottie:${lottieVersion}"
    const val jRoskopfSwipeReveal = "com.github.j-roskopf:SwipeToReveal:${swipeToRevealVersion}"
    const val epoxy = "com.airbnb.android:epoxy:${epoxyVersion}"
    const val epoxyProcessor = "com.airbnb.android:epoxy-processor:${epoxyVersion}"
    const val markwonImage = "io.noties.markwon:image:${markwonVersion}"
    const val markwonCore = "io.noties.markwon:core:${markwonVersion}"
    const val androidNavigation = "androidx.navigation:navigation-ui-ktx:${navVersion}"
    const val androidNavigationFragment = "androidx.navigation:navigation-fragment-ktx:${navVersion}"
    const val retrofitBase = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val retrofitRx2Adapter = "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${lifecycleExtensionsVersion}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${moshiVersion}"
    const val moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:${moshiVersion}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${daggerVersion}"
    const val dagger = "com.google.dagger:dagger:${daggerVersion}"
    const val timber = "com.jakewharton.timber:timber:${timberVersion}"
    const val rxJava = "io.reactivex.rxjava2:rxkotlin:${rxJava2Version}"
    const val rxJavaAndroid = "io.reactivex.rxjava2:rxandroid:${rxJavaAndroidVersion}"
    const val appCompat = "androidx.appcompat:appcompat:${appCompatVersion}"
    const val androidCoreKtx = "androidx.core:core-ktx:${androidxCoreKtxVersion}"
    const val androidMaterial = "com.google.android.material:material:${androidMaterialVersion}"
    const val androidConstraintLayout = "androidx.constraintlayout:constraintlayout:${androidConstraintLayoutVersion}"
    const val roomRuntime = "androidx.room:room-runtime:${roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    const val roomKtx = "androidx.room:room-ktx:$roomVersion"
    const val roomRxJava =  "android.arch.persistence.room:rxjava2:${roomRxJavaVersion}"
    const val glide = "com.github.bumptech.glide:glide:$glideVersion"

    // testing
    const val jUnit = "junit:junit:${jUnitVersion}"
    const val mockitoCore = "org.mockito:mockito-core:$mockitoCoreVersion"
    const val mockitoKotlin = "com.nhaarman:mockito-kotlin:$mockitoKotlinVersion"
    const val androidTestRunner = "androidx.test:runner:${androidTestRunnerVersion}"
    const val espresso = "androidx.test.espresso:espresso-core:${espressoVersion}"

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${kotlinVersion}"
}