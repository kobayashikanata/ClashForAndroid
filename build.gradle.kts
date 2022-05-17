@file:Suppress("UNUSED_VARIABLE")

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import java.net.URL
import java.util.*

buildscript {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.kr328.app/releases")
    }
    dependencies {
        classpath(libs.build.android)
        classpath(libs.build.kotlin.common)
        classpath(libs.build.kotlin.serialization)
        classpath(libs.build.ksp)
        classpath(libs.build.golang)
        classpath("com.github.kezong:fat-aar:1.3.8")
    }
}

subprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.kr328.app/releases")
    }

    val isApp = false

    apply(plugin = if (isApp) "com.android.application" else "com.android.library")

    extensions.configure<BaseExtension> {
        defaultConfig {

            minSdk = 21
            targetSdk = 31

            versionName = "2.5.9"
            versionCode = 205009

            resValue("string", "release_name", "v$versionName")
            resValue("integer", "release_code", "$versionCode")

            externalNativeBuild {
                cmake {
                    abiFilters("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                }
            }

            if (!isApp) {
                consumerProguardFiles("consumer-rules.pro")
            } else {
                setProperty("archivesBaseName", "cfa-$versionName")
            }
        }

        ndkVersion = "23.0.7599858"

        compileSdkVersion(defaultConfig.targetSdk!!)

        sourceSets {
            maybeCreate("foss")
            maybeCreate("premium")
        }
        productFlavors {
            flavorDimensions("feature")

            create("premium") {
                isDefault = true
                dimension = flavorDimensionList[0]

                buildConfigField("boolean", "PREMIUM", "Boolean.parseBoolean(\"true\")")

                val tracker = rootProject.file("tracker.properties")
                if (tracker.exists()) {
                    val prop = Properties().apply {
                        tracker.inputStream().use(this::load)
                    }

                    buildConfigField(
                        "String",
                        "APP_CENTER_KEY",
                        "\"${prop.getProperty("appcenter.key")!!}\""
                    )
                }
            }

            create("foss") {
                dimension = flavorDimensionList[0]

                buildConfigField("boolean", "PREMIUM", "Boolean.parseBoolean(\"false\")")
            }
        }

        buildTypes {
            named("debug") {

            }
            named("release") {
                isMinifyEnabled = false
                isShrinkResources = false
            }
        }

        buildFeatures.apply {
            dataBinding {
                isEnabled = false
            }
        }

        variantFilter {
//            ignore = name.startsWith("premium") && !project(":core")
//                .file("src/premium/golang/clash/go.mod").exists()
            ignore = false
        }

        if (isApp) {
            this as AppExtension

            splits {
                abi {
                    isEnable = true
                    isUniversalApk = true
                }
            }
        }
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL

    doLast {
        val sha256 = URL("$distributionUrl.sha256").openStream()
            .use { it.reader().readText().trim() }

        file("gradle/wrapper/gradle-wrapper.properties")
            .appendText("distributionSha256Sum=$sha256")
    }
}