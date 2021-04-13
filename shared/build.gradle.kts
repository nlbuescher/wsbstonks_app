import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	id("com.android.library")
}

group = "net.ddns.wsbstonks"
version = "1.0-SNAPSHOT"

val host: OperatingSystem = OperatingSystem.current()

kotlin {
	android()

	if (host.isMacOsX) {
		ios {
			binaries.framework {
				baseName = "shared"
			}
		}
	}

	sourceSets {
		val commonMain by getting {}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		val androidMain by getting {
			dependencies {
				implementation("com.google.android.material:material:1.3.0")
			}
		}
		val androidTest by getting {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("junit:junit:4.13")
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

if (host.isMacOsX) {
	val packForXcode by tasks.creating(Sync::class) {
		group = "build"
		val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
		val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
		val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
		val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
		inputs.property("mode", mode)
		dependsOn(framework.linkTask)
		val targetDir = File(buildDir, "xcode-frameworks")
		from({ framework.outputDirectory })
		into(targetDir)
	}

	tasks.getByName("build").dependsOn(packForXcode)
}
