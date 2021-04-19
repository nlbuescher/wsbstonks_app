import org.gradle.internal.os.OperatingSystem

plugins {
	kotlin("multiplatform")
	kotlin("plugin.serialization") version "1.4.32"
	kotlin("native.cocoapods")
	id("com.android.library")
}

group = "net.ddns.wsbstonks"
version = "1.0-SNAPSHOT"

val host: OperatingSystem = OperatingSystem.current()

kotlin {
	android()

	if (host.isMacOsX) {
		ios()

		cocoapods {
			ios.deploymentTarget = "14.0"

			summary = "shared"
			homepage = "shared"

			podfile = project.file("../iosApp/Podfile")
		}
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-common"))
				implementation(kotlin("reflect"))
				implementation("io.ktor:ktor-client-serialization:1.5.3")
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		val androidMain by getting {
			dependencies {
				implementation("com.google.android.material:material:1.3.0")
				implementation("io.ktor:ktor-client-android:1.5.3")
			}
		}
		val androidTest by getting {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("junit:junit:4.13.2")
			}
		}

		if (host.isMacOsX) {
			val iosMain by getting {}
			val iosTest by getting {}
		}
	}
}

android {
	compileSdkVersion(30)
	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	defaultConfig {
		minSdkVersion(21)
		targetSdkVersion(30)
	}
}
