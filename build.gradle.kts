plugins {
    val kotlinVersion = "1.9.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "com.honoka"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
    maven {
        url = uri("https://maven.pkg.github.com/mzdluo123/silk4j")
        credentials {
            username = "username"
            password = "password"
        }
    }
    mavenCentral()
}

dependencies {
    implementation("com.alibaba:fastjson:1.2.76")
    implementation("org.reflections:reflections:0.10.2")
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
    implementation("cn.hutool:hutool-all:5.8.8")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    implementation("com.unfbx:chatgpt-java:1.1.5")
    implementation("io.github.mzdluo123:silk4j:1.2-dev")
    // 引入mysql、mybatis、druid
    implementation("mysql:mysql-connector-java:8.0.26")
    implementation("org.mybatis:mybatis:3.5.7")
    implementation("com.alibaba:druid:1.2.8")
    implementation("org.junit.jupiter:junit-jupiter:5.7.1")
    // 引入mybatis-plus
    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.3.1")
    // 引入swagger
    implementation("io.swagger.core.v3:swagger-annotations:2.2.8")
    implementation("com.google.guava:guava:31.1-jre")
    // 引入yaml解析
    implementation("org.yaml:snakeyaml:2.0")
    // 引入disruptor
    implementation("com.lmax:disruptor:3.4.4")
}