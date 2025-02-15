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
    }
}

rootProject.name = "sohae"
include(":app")
include(":domain:utils")
include(":presentation:home")
include(":data:repositoryimpl:myinformation")
include(":data:repositoryimpl:jobinformation")
include(":presentation:community")
include(":presentation:houseaccount")
include(":presentation:jobreview")
include(":presentation:myprofile")
include(":presentation:post")
include(":presentation:profile")
include(":presentation:searchjob")
include(":presentation:searchpost")
include(":presentation:settingoptions")
include(":presentation:writejobreview")
include(":presentation:writepost")
include(":data:model:local")
include(":data:model:remote")
project(":data:model:remote").projectDir = File(rootProject.projectDir, "data/model/remote")
include(":data:datasource:myinformation")
include(":domain:home")
include(":domain:myinformation")
include(":presentation:jobnavgraph")
include(":presentation:jobinformation")
include(":common:ui:custom")
include(":navigation:homenavgraph")
include(":controller:mainnavgraph")
include(":controller:barcolor")
include(":navigation:mainnavgraph")
include(":domain:jobreview")
