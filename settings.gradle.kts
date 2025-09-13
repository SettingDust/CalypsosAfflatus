dependencyResolutionManagement {
    pluginManagement {
        repositories {
            mavenCentral()
            gradlePluginPortal()
            maven("https://maven.msrandom.net/repository/cloche")
            maven("https://raw.githubusercontent.com/SettingDust/cloche/refs/heads/maven-repo/")
            maven("https://raw.githubusercontent.com/SettingDust/minecraft-codev/refs/heads/maven-repo/")
            mavenLocal()
        }
    }
}

object VersionFormats {
    val versionPlusMc = { mcVer: String, ver: String -> "$ver+$mcVer" }
    val mcDashVersion = { mcVer: String, ver: String -> "$mcVer-$ver" }
}

object VersionTransformers {
    val versionDashLoader = { ver: String, variant: String -> "$ver-$variant" }
    val loaderUnderlineVersion = { ver: String, variant: String -> "${variant}_$ver" }
}

object ArtifactTransformers {
    val artifactDashLoaderDashMcVersion =
        { artifact: String, variant: String, mcVersion: String -> "$artifact-$variant-$mcVersion" }
    val artifactDashLoader = { artifact: String, variant: String, _: String -> "$artifact-$variant" }
}

open class VariantConfig(
    val artifactTransformer: (artifact: String, variant: String, mcVersion: String) -> String = { artifact, _, _ -> artifact },
    val versionTransformer: (version: String, variant: String) -> String = { ver, _ -> ver }
) {
    companion object : VariantConfig()
}

data class VariantMapping(
    val mcVersion: String,
    val loaders: Map<String, VariantConfig>
)

fun VersionCatalogBuilder.modrinth(
    id: String,
    artifact: String = id,
    mcVersionToVersion: Map<String, String>,
    versionFormat: (String, String) -> String = { _, v -> v },
    mapping: List<VariantMapping> = emptyList()
) {
    val allLoaders = mapping.flatMap { it.loaders.keys }.toSet()
    val isSingleLoader = allLoaders.size == 1
    val isSingleMcVersion = mcVersionToVersion.size == 1

    if (isSingleMcVersion) {
        val (mcVersion, modVersion) = mcVersionToVersion.entries.single()
        val config = mapping.find { it.mcVersion == mcVersion }
            ?: error("No loader config found for MC $mcVersion")

        val version = versionFormat(mcVersion, modVersion)

        config.loaders.forEach { (loaderName, loader) ->
            library(
                if (isSingleLoader) "$id"
                else "${id}_$loaderName",
                "maven.modrinth",
                loader.artifactTransformer(artifact, loaderName, mcVersion)
            ).version(loader.versionTransformer(version, loaderName))
        }
        return
    }

    mcVersionToVersion.forEach { (mcVersion, modVersion) ->
        val config = mapping.find { it.mcVersion == mcVersion }
            ?: error("No loader config found for MC $mcVersion")

        val version = versionFormat(mcVersion, modVersion)

        config.loaders.forEach { (loaderName, loader) ->
            library(
                if (isSingleLoader) "${id}_${mcVersion}"
                else "${id}_${mcVersion}_$loaderName",
                "maven.modrinth",
                loader.artifactTransformer(artifact, loaderName, mcVersion)
            ).version(loader.versionTransformer(version, loaderName))
        }
    }
}

fun VersionCatalogBuilder.maven(
    id: String,
    group: String,
    artifact: String = id,
    mcVersionToVersion: Map<String, String>,
    versionFormat: (String, String) -> String = { _, v -> v },
    mapping: List<VariantMapping> = emptyList()
) {
    val allLoaders = mapping.flatMap { it.loaders.keys }.toSet()
    val isSingleLoader = allLoaders.size == 1
    val isSingleMcVersion = mcVersionToVersion.size == 1

    if (isSingleMcVersion) {
        val (mcVersion, modVersion) = mcVersionToVersion.entries.single()
        val config = mapping.find { it.mcVersion == mcVersion }
            ?: error("No loader config found for MC $mcVersion")

        val version = versionFormat(mcVersion, modVersion)

        config.loaders.forEach { (loaderName, loader) ->
            library(
                if (isSingleLoader) id
                else "${id}_$loaderName",
                group,
                loader.artifactTransformer(artifact, loaderName, mcVersion)
            ).version(loader.versionTransformer(version, loaderName))
        }
        return
    }

    mcVersionToVersion.forEach { (mcVersion, baseVersion) ->
        val config = mapping.find { it.mcVersion == mcVersion }
            ?: error("No loader config found for MC $mcVersion")

        val version = versionFormat(mcVersion, baseVersion)

        config.loaders.forEach { (loaderName, loader) ->
            library(
                if (mcVersion == "*") {
                    if (isSingleLoader) id
                    else "${id}_$loaderName"
                } else {
                    if (isSingleLoader) "${id}_${mcVersion}"
                    else "${id}_${mcVersion}_$loaderName"
                },
                group,
                loader.artifactTransformer(artifact, loaderName, mcVersion)
            ).version(loader.versionTransformer(version, loaderName))
        }
    }
}

dependencyResolutionManagement.versionCatalogs.create("catalog") {
    maven(
        id = "mixinextras",
        group = "io.github.llamalad7",
        artifact = "mixinextras",
        mcVersionToVersion = mapOf("*" to "0.5.0"),
        versionFormat = { _, v -> v },
        mapping = listOf(
            VariantMapping(
                "*", mapOf(
                    "forge" to VariantConfig(ArtifactTransformers.artifactDashLoader),
                    "fabric" to VariantConfig(ArtifactTransformers.artifactDashLoader),
                    "common" to VariantConfig(ArtifactTransformers.artifactDashLoader)
                )
            )
        )
    )

    maven(
        id = "accessories",
        group = "io.wispforest",
        artifact = "accessories",
        mcVersionToVersion = mapOf(
            "1.20.1" to "1.0.0-beta.47",
            "1.21.1" to "1.1.0-beta.48"
        ),
        versionFormat = VersionFormats.versionPlusMc,
        mapping = listOf(
            VariantMapping(
                "1.20.1", mapOf(
                    "neoforge" to VariantConfig(ArtifactTransformers.artifactDashLoader),
                    "fabric" to VariantConfig(ArtifactTransformers.artifactDashLoader)
                )
            ),
            VariantMapping(
                "1.21.1", mapOf(
                    "neoforge" to VariantConfig(ArtifactTransformers.artifactDashLoader),
                    "fabric" to VariantConfig(ArtifactTransformers.artifactDashLoader)
                )
            ),
        )
    )

    maven(
        id = "curios",
        group = "top.theillusivec4.curios",
        artifact = "curios",
        mcVersionToVersion = mapOf(
            "1.20.1" to "5.14.1",
            "1.21.1" to "9.5.1"
        ),
        versionFormat = VersionFormats.versionPlusMc,
        mapping = listOf(
            VariantMapping(
                "1.20.1", mapOf(
                    "forge" to VariantConfig(ArtifactTransformers.artifactDashLoader)
                )
            ),
            VariantMapping(
                "1.21.1", mapOf(
                    "neoforge" to VariantConfig(ArtifactTransformers.artifactDashLoader)
                )
            ),
        )
    )

    modrinth(
        id = "trinkets",
        artifact = "trinkets",
        mcVersionToVersion = mapOf(
            "1.20.1" to "3.7.2",
            "1.21.1" to "3.10.0"
        ),
        mapping = listOf(
            VariantMapping("1.20.1", mapOf("fabric" to VariantConfig)),
            VariantMapping("1.21.1", mapOf("fabric" to VariantConfig)),
        )
    )

    maven(
        id = "cardinal-components",
        group = "dev.onyxstudios.cardinal-components-api",
        artifact = "cardinal-components",
        mcVersionToVersion = mapOf(
            "1.20.1" to "5.2.3",
            "1.21.1" to "6.1.2"
        ),
        mapping = listOf(
            VariantMapping(
                "1.20.1", mapOf(
                    "base" to VariantConfig(ArtifactTransformers.artifactDashLoader),
                    "entity" to VariantConfig(ArtifactTransformers.artifactDashLoader)
                )
            ),
            VariantMapping(
                "1.21.1", mapOf(
                    "base" to VariantConfig(ArtifactTransformers.artifactDashLoader),
                    "entity" to VariantConfig(ArtifactTransformers.artifactDashLoader)
                )
            )
        )
    )
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "CalypsosAfflatus"