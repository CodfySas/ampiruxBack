import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("jacoco")
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.sonarqube") version "3.0"
    id("org.jmailen.kotlinter") version "3.3.0"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("kapt") version "1.4.20"
}
group = "com.osia.logistic"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
repositories {
    mavenCentral()
    jcenter()
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("io.github.resilience4j:resilience4j-spring-boot2:1.6.1")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("com.sipios:spring-search:0.2.4")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.thymeleaf:thymeleaf-spring5:3.0.11.RELEASE")
    implementation("org.thymeleaf:thymeleaf:3.0.12.RELEASE")
    implementation("com.h2database:h2")
    implementation ("com.google.code.gson:gson:2.8.7")
    implementation("org.hibernate:hibernate-envers")
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("org.springframework.retry:spring-retry:1.3.1")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.teastman:spring-data-hibernate-event:1.0.1")
    runtimeOnly("io.github.stavshamir:springwolf-ui:0.0.2")
    developmentOnly("io.springfox:springfox-boot-starter:3.0.0")
    api("org.mapstruct:mapstruct:1.4.1.Final")
    kapt("org.mapstruct:mapstruct-processor:1.4.1.Final")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.25.1")
    testImplementation("com.github.javafaker:javafaker:0.14") {
        exclude(module = "org.yaml")
    }
    implementation("io.sentry:sentry-spring-boot-starter:4.3.0")
    implementation("io.sentry:sentry-logback:4.3.0")
    implementation("io.data2viz.geojson:core:0.6.2")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation("org.apache.commons:commons-pool2:2.11.1")
    implementation("org.xerial.snappy:snappy-java:1.1.8.4")
}
extra["springCloudVersion"] = "2020.0.1"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
    }
}
sonarqube {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
    }
}
