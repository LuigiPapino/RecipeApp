package net.dragora.recipeapp.base.data.repository

/**
 * Created by luigipapino on 25/02/2018.
 */

sealed class RecipeModelFilter {
    class Query(val value: String) : RecipeModelFilter()
    class Difficulty(val value: RecipeModel.Difficulty) : RecipeModelFilter()
    class Duration(val value: IntRange) : RecipeModelFilter()

}

fun RecipeModel.matchFilters(filters: List<RecipeModelFilter>): Boolean {
    return filters.all { this.matchFilter(it) }
}

private fun RecipeModel.matchFilter(filter: RecipeModelFilter): Boolean {
    return when (filter) {
        is RecipeModelFilter.Query -> this.matchQuery(filter.value)
        is RecipeModelFilter.Difficulty -> this.difficulty == filter.value
        is RecipeModelFilter.Duration -> this.duration in filter.value
    }
}

private fun RecipeModel.matchQuery(query: String): Boolean {
    return when {
        name.contains(query, true) -> true
        ingredients.any { it.matchQuery(query) } -> true
        steps.any { it.contains(query, true) } -> true
        else -> false
    }
}

private fun IngredientModel.matchQuery(query: String?): Boolean {
    query ?: return true
    return this.name.contains(query, true) || this.type.contains(query, true)

}
