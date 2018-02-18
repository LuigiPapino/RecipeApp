package net.dragora.recipeapp.base.data.network

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by luigipapino on 18/02/2018.
 */

interface RecipeApiService {
    @GET("sampleapifortest/recipes.json")
    fun getRecipes(): Single<List<RecipePayload>>
}