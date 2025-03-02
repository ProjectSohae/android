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
include(":domain:utils")
include(":presentation:home")
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
include(":domain:home")
include(":domain:myinformation")
include(":presentation:jobnavgraph")
include(":presentation:jobinformation")
include(":common:ui:custom")
include(":common:remote")
project(":common:remote").projectDir = File(rootProject.projectDir, "common/remote")
include(":navigation:homenavgraph")
include(":controller:mainnavgraph")
include(":controller:barcolor")
include(":navigation:mainnavgraph")
include(":domain:jobreview")
include(":data:myinformation")
include(":data:jobinformation")
include(":common:di")
include(":presentation:mypostlist")
include(":presentation:mycommentlist")
include(":common:resource")
include(":data:community")
include(":domain:community")
include(":presentation:selectimage")
