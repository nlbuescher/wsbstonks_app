import org.jetbrains.kotlin.gradle.tasks.*

plugins {
	id("com.android.application")
	kotlin("android")
}

group = "net.ddns.wsbstonks"
version = "1.0-SNAPSHOT"

dependencies {
	implementation(project(":shared"))
	implementation("com.google.android.material:material:1.3.0")
	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("androidx.constraintlayout:constraintlayout:2.0.4")
	implementation("androidx.activity:activity-ktx:1.2.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
}

android {
	compileSdkVersion(30)
	defaultConfig {
		applicationId = "net.ddns.wsbstonks.androidApp"
		minSdkVersion(24)
		targetSdkVersion(30)
		versionCode = 0x00_01_00
		versionName = "0.1.0"
	}
	buildFeatures {
		viewBinding = true
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}
