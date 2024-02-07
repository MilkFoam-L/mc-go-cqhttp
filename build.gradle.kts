import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.2"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

taboolib {
    description {
        name("mc-go-cqhttp")
        desc("mc-go-cqhttp 是一个高效的 Minecraft 机器人插件")
        contributors {
            name("Milk_Foam")
        }
        dependencies {
            name("PlaceholderAPI").optional(true)
        }
    }
    env {
        // 中央仓库地址
        repoCentral = "https://maven.aliyun.com/repository/central"
        // TabooLib 仓库地址
        repoTabooLib = "http://ptms.ink:8081/repository/releases"
        // 安装模块
        install(
            UNIVERSAL,
            BUKKIT_ALL,

            UI,
            NMS,
            NMS_UTIL,
            KETHER,
            DATABASE,
            EXPANSION_SUBMIT_CHAIN,
        )
    }
    version { taboolib = "6.1.0" }

    // 重定向包名
    // okhttp3
    relocate("okhttp3", "com.milkfoam.mcgocqhttp.libs.okhttp3")
    relocate("okio", "com.milkfoam.mcgocqhttp.libs.okio")
    relocate("okhttp3.logging", "com.milkfoam.mcgocqhttp.libs.okhttp3.logging")
    // fastjson2
    relocate("com.alibaba.fastjson2", "com.milkfoam.mcgocqhttp.libs.fastjson2")
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven("https://repo.spongepowered.org/maven")
    maven("https://maven.aliyun.com/nexus/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))

    taboo("com.squareup.okio:okio-jvm:3.4.0")
    taboo("com.squareup.okhttp3:okhttp:4.12.0")
    taboo("com.squareup.okhttp3:logging-interceptor:4.12.0")
    taboo("com.alibaba.fastjson2:fastjson2-kotlin:2.0.46")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}