pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven("https://repository.map.naver.com/archive/maven")
    }
}

rootProject.name = "sohae"
include(":app")
include(":common:ui:custom")
include(":common:remote")
include(":navigation:homenavgraph")
include(":controller:mainnavgraph")
include(":controller:barcolor")
include(":navigation:mainnavgraph")
include(":common:di")
include(":common:resource")
include(":feature")
include(":data")
include(":domain")
