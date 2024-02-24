import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("jacoco")
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.sonarqube") version "3.3"
    id("org.jmailen.kotlinter") version "3.3.0"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}

group = "com.osia.nota_maestro"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-httpclient:11.2")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("com.sipios:spring-search:0.2.4")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.security:spring-security-config:5.7.5")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.h2database:h2")
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.teastman:spring-data-hibernate-event:1.0.1")
    implementation("io.sentry:sentry-spring-boot-starter:4.3.0")
    implementation("io.sentry:sentry-logback:4.3.0")
    implementation("org.apache.kafka:kafka-clients")
    implementation("io.lettuce:lettuce-core:6.2.3.RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    api("org.mapstruct:mapstruct:1.4.1.Final")
    kapt("org.mapstruct:mapstruct-processor:1.4.1.Final")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.25.1")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("com.github.javafaker:javafaker:1.0.2") {
        exclude(module = "snakeyaml")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testImplementation ("io.mockk:mockk:1.9.3")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:3.2.0")
    implementation("org.apache.commons:commons-pool2:2.11.1")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.xerial.snappy:snappy-java:1.1.8.4")
    implementation("com.uber:h3:3.7.1")
    implementation("org.flywaydb:flyway-core")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")
    implementation("org.hibernate:hibernate-envers")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

}
extra["springCloudVersion"] = "2021.0.5"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport{
    reports {
        xml.required.set(true)
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

sonarqube {
    properties {
        property("sonar.sources", "src/main/kotlin")
        property("sonar.tests", "src/test/kotlin")
        property("sonar.verbose", "true")
        property("sonar.qualitygate.wait", "true")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property(
            "sonar.coverage.exclusions",
            "**/dto/**/*, **/model/**/*, **/kafka/**/*, **/client/**/*, **/util/**/*"
        )
    }
}
