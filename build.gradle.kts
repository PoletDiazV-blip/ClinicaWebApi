plugins {
    id("java")
    kotlin("jvm")
}

group = "com.thriftad"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // 1. Javalin - El framework para tu API
    implementation("io.javalin:javalin:6.1.3")

    // 2. Jackson - Para que Java entienda y envíe JSON automáticamente
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    // 3. Registro de Logs (necesario para que Javalin no dé avisos en consola)
    implementation("org.slf4j:slf4j-simple:2.0.12")

    // 4. MySQL Connector - Para conectar con tu Workbench local
    implementation("com.mysql:mysql-connector-j:8.3.0")

    // 5. HikariCP - Para gestionar la conexión a la base de datos de forma eficiente
    implementation("com.zaxxer:HikariCP:5.1.0")

    // 6. jBCrypt - Para validar las contraseñas "Cone", "Arizbeth", etc.
    implementation("org.mindrot:jbcrypt:0.4")

    // 7. Soporte para fechas de Java 8 (LocalDate) en JSON
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")

    // Pruebas (ya venían en tu archivo)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

// Configuración para que Java use la versión 21 que tienes instalada
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}