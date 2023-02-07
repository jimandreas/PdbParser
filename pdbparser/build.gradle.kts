/*
 *  Copyright 2021 James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

//import org.gradle.api.tasks.testing.logging.TestExceptionFormat
//import org.gradle.api.tasks.testing.logging.TestLogEvent

// val currentVersion = "1.0.4-SNAPSHOT"
val currentVersion = "1.0.5"
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
    id("java-library")
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.4.32"
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}


val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}


signing {
    sign( publishing.publications)
}

subprojects {
    group = "com.kotmol"
    version = "1.0.5-SNAPSHOT"
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // implementation("com.google.guava:guava:30.0-jre")

    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

//    testImplementation("org.jetbrains.kotlin:kotlin-test")
//
//     testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

// https://github.com/Kotlin/kotlin-examples/blob/master/gradle/dokka/dokka-library-publishing-example/build.gradle.kts
// https://github.com/sirnuke/godot-kotlin-jvm-slf4j/blob/cbdcbaffd003734be874c168bfdcd2bfec75eb41/lib/build.gradle.kts

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val dokkaHtmlJar by tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-doc")
}

if (project.properties.containsKey("sonatypeNexusUsername")) {
    val sonatypeNexusUsername: String by project
    val sonatypeNexusPassword: String by project
    println("user $sonatypeNexusUsername pwd $sonatypeNexusPassword")

// https://docs.gradle.org/current/userguide/publishing_maven.html

    publishing {

        repositories {
            maven {
                name = "kotmolpdbparser"
                credentials {
                    username = sonatypeNexusUsername
                    password = sonatypeNexusPassword
                }
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                //name = "ossrh"
            }
        }

        publications {
            create<MavenPublication>("kotmolparser") {

                groupId = "com.kotmol.kotmolpdbparser"
                artifactId = "kotmolpdbparser"
                version = currentVersion
                from (components["java"])
                artifact(javadocJar)
                pom {
                    name.set("Kotmol PDB Parser")
                    description.set("Parser for RCSB PDB files")

                    url.set("https://github.com/kotmol/KotmolPdbParser")
                    inceptionYear.set("2019")
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("kotmol")
                            name.set("Molecular Kotlin")
                            email.set("info@kotmol.com")
                        }
                    }

                    scm {
                        connection.set( "scm:git:git://github.com/kotmol/KotmolPdbParser.git")
                        developerConnection.set("scm:git:ssh://git@github.com/kotmol/KotmolPdbParser.git")
                        url.set("https://github.com/kotmol/KotmolPdbParser")
                    }

                }

            }

        }

    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// https://github.com/sauravjha/kotlin-application-multiple-test-config/blob/master/build.gradle.kts
tasks {
    test {
        useJUnitPlatform()

        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {}
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}

            override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                val suiteName = suite.name
                if (suiteName.contains("Gradle Test")) { // root suite
                    println(
                        "Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped"
                    )

                }
            }
        })
    }
}

