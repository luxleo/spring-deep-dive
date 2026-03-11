description = "advanced"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
}
