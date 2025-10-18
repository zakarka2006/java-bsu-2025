plugins {
    id("java")
    application
}

group = ""
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("dzenishchych.Main")
}

tasks.test {
    useJUnitPlatform()
}