import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.2.70"
    val gradleDockerPluginVersion = "3.6.1"
    val springBootVersion = "2.0.5.RELEASE"
    val dependencyManagementVersion = "1.0.6.RELEASE"
    val uptodatePluginVersion = "1.6.3"

    java
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version dependencyManagementVersion
    id("com.ofg.uptodate") version uptodatePluginVersion
    id("com.bmuschko.docker-spring-boot-application") version gradleDockerPluginVersion
}

springBoot {
    buildInfo()
}

repositories {
    mavenCentral()
    jcenter()
}

configurations.compile.exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    compile("org.springframework.boot", "spring-boot-starter-web")
    compile("org.springframework.boot", "spring-boot-starter-jetty")
    compile("org.springframework.boot", "spring-boot-starter-actuator")
    compile("io.springfox", "springfox-swagger2", "2.9.2")
    compile("io.springfox", "springfox-swagger-ui", "2.9.2")
    compile("com.fasterxml.jackson.module", "jackson-module-kotlin")

    testCompile("junit", "junit", "4.12")
    testCompile("org.springframework.boot", "spring-boot-starter-test")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

docker {
//    registry configurations
//    url = "unix:///var/run/docker.sock"
//    url = "${private-registry-url}:${private-registry-port}"
//
//    registryCredentials {
//        url = 'https://index.docker.io/v1/'
//        username = 'user'
//        password = 'pass'
//        email = 'anuarneto@adaptti.com'
//    }
    springBootApplication {
        baseImage = "openjdk:8-alpine"
        ports.plus(arrayOf(8080))
        tag = "$group/${project.name}:$version"
    }
}
