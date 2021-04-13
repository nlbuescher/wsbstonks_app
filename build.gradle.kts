buildscript {
	repositories {
		gradlePluginPortal()
		google()
		mavenCentral()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
		classpath("com.android.tools.build:gradle:4.1.1")
	}
}

group = "net.ddns.wsbstonks"
version = "1.0-SNAPSHOT"

allprojects {
	repositories {
		google()
		mavenCentral()
	}
}
