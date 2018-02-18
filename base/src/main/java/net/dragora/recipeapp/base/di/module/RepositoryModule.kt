package net.dragora.recipeapp.base.di.module

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import net.dragora.recipeapp.base.data.network.RecipeApiService
import net.dragora.recipeapp.base.data.repository.RecipeRepository
import javax.inject.Singleton

/**
 * Created by luigipapino on 18/02/2018.
 */

@Singleton
@Module
object RepositoryModule {

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRecipeRepository(recipeApiService: RecipeApiService): RecipeRepository {
        return RecipeRepository(recipeApiService)
    }

    @get:Provides
    @JvmStatic
    @Singleton
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

}