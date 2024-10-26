plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.googlecode.windowlicker:windowlicker-swing:r268")
    testImplementation("org.hamcrest:hamcrest:3.0")
    implementation("org.igniterealtime.smack:smack:3.2.1")
    testImplementation("org.jmock:jmock-junit5:2.13.1")

}

tasks.test {
    useJUnitPlatform()
}