@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import earth.terrarium.cloche.api.attributes.TargetAttributes
import earth.terrarium.cloche.api.metadata.ModMetadata
import earth.terrarium.cloche.api.target.*
import earth.terrarium.cloche.tasks.GenerateFabricModJson
import groovy.lang.Closure
import net.msrandom.minecraftcodev.core.utils.lowerCamelCaseGradleName
import net.msrandom.minecraftcodev.fabric.MinecraftCodevFabricPlugin
import org.gradle.jvm.tasks.Jar


plugins {
    java

    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"

    id("com.palantir.git-version") version "3.1.0"

    id("com.gradleup.shadow") version "9.0.2"

    id("earth.terrarium.cloche") version "0.13.4"
}

val archive_name: String by rootProject.properties
val id: String by rootProject.properties
val source: String by rootProject.properties

group = "settingdust.calypsos_afflatus"

val gitVersion: Closure<String> by extra
version = gitVersion()

base { archivesName = archive_name }

repositories {
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        content {
            includeGroup("thedarkcolour")
        }
    }

    maven("https://maven.theillusivec4.top/") {
        content {
            includeGroup("top.theillusivec4.curios")
        }
    }

    maven("https://maven.terraformersmc.com/") {
        content {
            includeGroup("dev.emi")
        }
    }

    maven("https://maven.ladysnake.org/releases") {
        content {
            includeGroup("dev.onyxstudios.cardinal-components-api")
            includeGroup("org.ladysnake.cardinal-components-api")
        }
    }

    maven("https://maven.wispforest.io/releases") {
        content {
            includeGroupAndSubgroups("io.wispforest")
        }
    }

    maven("https://maven.shedaniel.me/") {
        content {
            includeGroup("me.shedaniel.cloth")
        }
    }

    maven("https://maven.su5ed.dev/releases") {
        content {
            includeGroup("dev.su5ed.sinytra.fabric-api")
            includeGroupAndSubgroups("org.sinytra")
        }
    }

    mavenCentral()

    cloche {
        librariesMinecraft()
        main()
        mavenFabric()
        mavenForge()
        mavenNeoforged()
        mavenNeoforgedMeta()
        mavenParchment()
    }

    mavenLocal()
}

cloche {
    metadata {
        modId = id
        name = rootProject.property("name").toString()
        description = rootProject.property("description").toString()
        license = "ARR"
        icon = "assets/$id/icon.png"
        sources = source
        issues = "$source/issues"
        author("SettingDust")

        dependency {
            modId = "minecraft"
            required = true
            version {
                start = "1.20.1"
            }
        }

        dependency {
            modId = "geckolib"
            required = true
        }

        dependency {
            modId = "smartbrainlib"
            required = true
        }
    }

    mappings {
        official()
    }

    val mainCommon = common("common:common") {
        mixins.from(file("src/common/common/main/resources/$id.mixins.json"))
        accessWideners.from(file("src/common/common/main/resources/$id.accessWidener"))

        dependencies {
            compileOnly("org.spongepowered:mixin:0.8.7")
        }
    }

    val commons = mapOf(
        "1.20.1" to common("common:1.20.1"),
        "1.21.1" to common("common:1.21.1"),
    )

    run fabric@{
        val fabricCommon = common("fabric:common") {
            mixins.from(file("src/fabric/common/main/resources/$id.fabric.mixins.json"))
        }

        val fabric1201 = fabric("fabric:1.20.1") {
            minecraftVersion = "1.20.1"

            metadata {
                dependency {
                    modId = "minecraft"
                    required = true
                    version {
                        start = "1.20.1"
                        end = "1.21"
                    }
                }
            }

            dependencies {
                fabricApi("0.92.6")

                modImplementation(catalog.accessories.get1().get20().get1().fabric)
                modImplementation(catalog.trinkets.get1().get20().get1())
            }

            tasks.named<GenerateFabricModJson>(generateModsManifestTaskName) {
                commonMetadata = objects.newInstance<ModMetadata>().apply {
                    modId.value("${id}_1_20")
                    name.value(cloche.metadata.name)
                    description.value(cloche.metadata.description)
                    license.value(cloche.metadata.license)
                    icon.value(cloche.metadata.icon)
                    sources.value(cloche.metadata.sources)
                    issues.value(cloche.metadata.issues)
                    authors.value(cloche.metadata.authors)
                    contributors.value(cloche.metadata.contributors)
                    dependencies.value(cloche.metadata.dependencies)
                    custom.value(cloche.metadata.custom)
                }
            }
        }

        val fabric121 = fabric("fabric:1.21") {
            minecraftVersion = "1.21.1"

            metadata {
                dependency {
                    modId = "minecraft"
                    required = true
                    version {
                        start = "1.21"
                    }
                }
            }

            dependencies {
                fabricApi("0.116.5")

                modImplementation(catalog.accessories.get1().get21().get1().fabric)
                modImplementation(catalog.trinkets.get1().get21().get1())
            }

            tasks.named<GenerateFabricModJson>(generateModsManifestTaskName) {
                commonMetadata = objects.newInstance<ModMetadata>().apply {
                    modId.value("${id}_1_21")
                    name.value(cloche.metadata.name)
                    description.value(cloche.metadata.description)
                    license.value(cloche.metadata.license)
                    icon.value(cloche.metadata.icon)
                    sources.value(cloche.metadata.sources)
                    issues.value(cloche.metadata.issues)
                    authors.value(cloche.metadata.authors)
                    contributors.value(cloche.metadata.contributors)
                    dependencies.value(cloche.metadata.dependencies)
                    custom.value(cloche.metadata.custom)
                }
            }
        }

        fabric("container:fabric") {
            minecraftVersion = "1.20.1"

            val targets = setOf(fabric1201, fabric121)

            dependencies {
                for (target in targets) {
                    include(project()) {
                        capabilities {
                            requireFeature(target.capabilitySuffix)
                        }

                        attributes {
                            attributeProvider(TargetAttributes.MINECRAFT_VERSION, target.minecraftVersion)
                        }
                    }
                }
            }

            tasks.named<Jar>(includeJarTaskName) {
                archiveClassifier = target.loaderName

                dependsOn(targets.map { it.includeJarTaskName })
            }

            tasks.named<GenerateFabricModJson>(generateModsManifestTaskName) {
                commonMetadata = objects.newInstance<ModMetadata>().apply {
                    modId.value(cloche.metadata.modId)
                    license.value(cloche.metadata.license)
                }
            }
        }

        targets.withType<FabricTarget> {
            loaderVersion = "0.16.14"

            includedClient()

            if (isContainer()) return@withType

            dependsOn(fabricCommon)

            metadata {
                entrypoint("main") {
                    adapter = "kotlin"
                    value = "$group.fabric.Entrypoint::init"
                }

                entrypoint("client") {
                    adapter = "kotlin"
                    value = "$group.fabric.Entrypoint::clientInit"
                }

                dependency {
                    modId = "fabric-api"
                    required = true
                }

                dependency {
                    modId = "fabric-language-kotlin"
                    required = true
                }

                dependency {
                    modId = "trinkets"
                }
            }

            dependencies {
                modImplementation("net.fabricmc:fabric-language-kotlin:1.13.1+kotlin.2.1.10")
            }
        }
    }

    run forge@{
        val forge1201 = forge("forge:1.20.1") {
            minecraftVersion = "1.20.1"
            loaderVersion = "47.4.4"

            metadata {
                modLoader = "kotlinforforge"
                loaderVersion {
                    start = "4"
                }

                dependency {
                    modId = "minecraft"
                    required = true
                    version {
                        start = "1.20.1"
                        end = "1.21"
                    }
                }
            }

            repositories {
                maven("https://repo.spongepowered.org/maven") {
                    content {
                        includeGroup("org.spongepowered")
                    }
                }
            }

            dependencies {
                implementation("org.spongepowered:mixin:0.8.7")
                implementation(catalog.mixinextras.common)
                implementation(catalog.mixinextras.forge)

                modImplementation("thedarkcolour:kotlinforforge:4.11.0")

                modImplementation(catalog.accessories.get1().get20().get1().neoforge)
                modImplementation(catalog.curios.get1().get20().get1().forge)
            }
        }
    }

    run neoforge@{
        val neoforge121 = neoforge("neoforge:1.21") {
            minecraftVersion = "1.21.1"

            metadata {
                modLoader = "kotlinforforge"
                loaderVersion {
                    start = "5"
                }

                dependency {
                    modId = "minecraft"
                    required = true
                    version {
                        start = "1.21"
                    }
                }
            }

            dependencies {
                modImplementation("thedarkcolour:kotlinforforge-neoforge:5.9.0")

                modImplementation(catalog.accessories.get1().get21().get1().neoforge)
                modImplementation(catalog.curios.get1().get21().get1().neoforge)
            }
        }

        neoforge("container:neoforge") {
            minecraftVersion = "1.21.1"

            val targets = setOf(neoforge121)

            dependencies {
                for (target in targets) {
                    include(project()) {
                        capabilities {
                            requireFeature(target.capabilitySuffix)
                        }
                    }
                }
            }

            tasks.named<Jar>(includeJarTaskName) {
                archiveClassifier = target.loaderName

                dependsOn(targets.map { it.includeJarTaskName })
            }
        }

        targets.withType<NeoforgeTarget> {
            loaderVersion = "21.1.192"

            if (isContainer()) return@withType

            metadata {
                modLoader = "kotlinforforge"
                loaderVersion {
                    start = "5"
                }
            }
        }
    }

    commonTargets.all {
        if (this != mainCommon && this != common()) {
            dependsOn(mainCommon)
        }
    }

    targets.withType<ForgeLikeTarget> {
        metadata {
            dependency {
                modId = "curios"
            }

            dependency {
                modId = "accessories"
            }
        }
    }

    targets.all {
        if (isContainer()) {
            if (this is ForgeLikeTarget) {
                tasks.named<Jar>(includeJarTaskName) {
                    exclude(modsManifestPath)
                }
            }

            return@all
        }

        dependsOn(mainCommon, commons.getValue(minecraftVersion.get()))

        runs {
            client()
        }

        mappings {
            parchment(minecraftVersion.map {
                when (it) {
                    "1.20.1" -> "2023.09.03"
                    "1.21.1" -> "2024.11.17"
                    else -> throw IllegalArgumentException("Unsupported minecraft version $it")
                }
            })
        }
    }
}

val SourceSet.mergedIncludeJarTaskName: String
    get() = lowerCamelCaseGradleName(takeUnless(SourceSet::isMain)?.name, "mergeIncludeJar")

val SourceSet.includeJarTaskName: String
    get() = lowerCamelCaseGradleName(takeUnless(SourceSet::isMain)?.name, "includeJar")

val MinecraftTarget.includeJarTaskName: String
    get() = when (this) {
        is FabricTarget -> sourceSet.includeJarTaskName
        is ForgeLikeTarget -> sourceSet.includeJarTaskName
        else -> throw IllegalArgumentException("Unsupported target $this")
    }

val FabricTarget.modsJsonPath: String
    get() = "fabric.mod.json"

val ForgeTarget.modsTomlPath: String
    get() = "META-INF/mods.toml"

val NeoforgeTarget.modsTomlPath: String
    get() = "META-INF/neoforge.mods.toml"

val MinecraftTarget.modsManifestPath: String
    get() = when (this) {
        is FabricTarget -> modsJsonPath
        is ForgeTarget -> modsTomlPath
        is NeoforgeTarget -> modsTomlPath
        else -> throw IllegalArgumentException("Unsupported target $this")
    }

val FabricTarget.generateModsJsonTaskName: String
    get() = lowerCamelCaseGradleName("generate", featureName, "ModJson")

val ForgeLikeTarget.generateModsTomlTaskName: String
    get() = lowerCamelCaseGradleName("generate", featureName, "modsToml")

val MinecraftTarget.generateModsManifestTaskName: String
    get() = when (this) {
        is FabricTarget -> generateModsJsonTaskName
        is ForgeLikeTarget -> generateModsTomlTaskName
        else -> throw IllegalArgumentException("Unsupported target $this")
    }

fun String.camelToKebabCase(): String {
    val pattern = "(?<=.)[A-Z]".toRegex()
    return this.replace(pattern, "-$0").lowercase()
}

fun MinecraftTarget.isContainer() = name.startsWith("container")

tasks {
    withType<ProcessResources> {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }

    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }

    shadowJar {
        archiveClassifier = ""

        configurations.empty()

        for (target in cloche.targets.filter { it.isContainer() }) {
            from(target.finalJar.map { zipTree(it.archiveFile) })
            manifest.inheritFrom(getByName<Jar>(target.includeJarTaskName).manifest)
        }

        manifest {
            attributes(
                "FMLModType" to "GAMELIBRARY"
            )
        }

        append("META-INF/accesstransformer.cfg")
    }

    val shadowSourcesJar by registering(ShadowJar::class) {
        dependsOn(cloche.targets.map { it.generateModsManifestTaskName })

        mergeServiceFiles()
        archiveClassifier.set("sources")
        from(sourceSets.map { it.allSource })

        doFirst {
            manifest {
                from(source.filter { it.name.equals("MANIFEST.MF") }.toList())
            }
        }
    }

    build {
        dependsOn(shadowSourcesJar)
    }

    val remapFabricMinecraftIntermediary by registering {
        dependsOn(cloche.targets.filterIsInstance<FabricTarget>().flatMap {
            listOf(
                lowerCamelCaseGradleName(
                    "remap",
                    it.name,
                    "commonMinecraft",
                    MinecraftCodevFabricPlugin.INTERMEDIARY_MAPPINGS_NAMESPACE,
                ), lowerCamelCaseGradleName(
                    "remap",
                    it.name,
                    "clientMinecraft",
                    MinecraftCodevFabricPlugin.INTERMEDIARY_MAPPINGS_NAMESPACE,
                )
            )
        })
    }
}