package net.dragora.recipeapp.base.data.repository

/**
 * Created by luigipapino on 21/02/2018.
 */
data class RecipeModel(
        val name: String,
        val ingredients: List<IngredientModel>,
        val steps: List<String>,
        val timers: List<Int>,
        val imageURL: String,
        val originalURL: String?
)

data class IngredientModel(
        val quantity: String,
        val name: String,
        val type: String)