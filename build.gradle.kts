plugins {
    id("java-library")
}

group = "fr.swordtales"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
    compileOnly("org.jetbrains:annotations:24.1.0")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
    }

    jar {
        archiveBaseName.set("ClickEventLib")
        archiveClassifier.set("")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
