package net.dragora.recipeapp.base.data.network

/**
 * Created by luigipapino on 18/02/2018.
 */
internal data class RecipePayload(
        val name: String,
        val ingredients: List<IngredientPayload>,
        val steps: List<String>,
        val timers: List<Int>,
        val imageURL: String,
        val originalURL: String?
)

internal data class IngredientPayload(val quantity: String,
        val name: String,
        val type: String)