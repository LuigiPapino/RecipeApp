package net.dragora.recipeapp.base

import net.dragora.recipeapp.BuildConfig

enum class BuildType {
    DEBUG, RELEASE;

    val isCurrentBuild: Boolean
        get() = this.toString().toLowerCase().contentEquals(BuildConfig.BUILD_TYPE.toLowerCase())

    companion object {
        fun isCurrentBuild(vararg types: BuildType) =
                types.any { it.isCurrentBuild }

    }
}


