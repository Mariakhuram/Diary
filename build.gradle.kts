// Top-level build file where you can add configuration options common to all sub-projects/modules.
// for dagger classpath
buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:4.1.3") //4.1.3
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}