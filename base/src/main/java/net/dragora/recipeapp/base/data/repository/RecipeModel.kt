package net.dragora.recipeapp.base.data.repository

import net.dragora.recipeapp.base.data.network.RecipePayload
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty.Hard
import net.dragora.recipeapp.base.data.repository.RecipeModel.Difficulty.Medium
import net.dragora.recipeapp.base.tools.Loggy

/**
 * Created by luigipapino on 21/02/2018.
 */
data class RecipeModel(
        val id: Int,
        val name: String,
        val ingredients: List<IngredientModel>,
        val steps: List<String>,
        val timers: List<Int>,
        val imageURL: String,
        val originalURL: String?,
        val difficulty: Difficulty,
        val duration: Int
) {
    enum class Difficulty { Easy, Medium, Hard }
}

data class IngredientModel(
        val quantity: String,
        val name: String,
        val type: String)

internal fun List<RecipePayload>?.toModels(): List<RecipeModel> {
    return this?.mapIndexed { index, it -> it.toModel(index) } ?: emptyList()
}

/**
 * This is useless at the moment.
 * It's just to protect the presentation layer from changes in the domain layer
 */
internal fun RecipePayload.toModel(recipeId: Int): RecipeModel {
    val ingredients =
            this.ingredients
                    .map { IngredientModel(it.quantity, it.name, it.type) }
    val steps = this.steps
    val timers = this.timers
    val difficulty = when (steps.size) {
        in 0..4 -> Difficulty.Easy
        in 5..6 -> Medium
        else -> Hard
    }
    val totalTime = this.timers.reduce { acc, i -> acc + i }

    Loggy.d("toModel() steps=${steps.size} duration=$totalTime")

    val model = RecipeModel(recipeId, name, ingredients, steps, timers, imageURL,
            originalURL,
            difficulty, totalTime)
    return model
}