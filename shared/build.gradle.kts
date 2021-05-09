import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	kotlin("plugin.serialization") version "1.4.32"
	id("com.android.library")
}

group = "net.ddns.wsbstonks"
version = "1.0-SNAPSHOT"

val host = OperatingSystem.current()!!

val ktorVersion = "1.5.4"

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
		all {
			languageSettings.run {
				useExperimentalAnnotation("kotlin.RequiresOptIn")
				useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
			}
		}

		named("commonMain") {
			dependencies {
				implementation(kotlin("stdlib-common"))
				implementation(kotlin("reflect"))
				implementation("io.ktor:ktor-client-serialization:$ktorVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
			}
		}

		named("androidMain") {
			dependencies {
				implementation("com.google.android.material:material:1.3.0")
				implementation("io.ktor:ktor-client-android:$ktorVersion")
			}
		}

		if (host.isMacOsX) {
			named("iosMain") {
				dependencies {
					implementation("io.ktor:ktor-client-ios:$ktorVersion")
				}
			}
		}
	}
}

android {
	compileSdkVersion(30)
	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	defaultConfig {
		minSdkVersion(24)
		targetSdkVersion(30)
	}
}

if (host.isMacOsX) {
	val packForXCode by tasks.creating(Sync::class) {
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

	tasks.getByName("build").dependsOn(packForXCode)
}
