package net.dragora.recipeapp.base.di.module

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import net.dragora.recipeapp.base.LokiApplication
import net.dragora.recipeapp.base.data.network.RecipeApiService
import net.dragora.recipeapp.base.data.repository.RecipeModelStorage
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
    internal fun provideRecipeRepository(recipeApiService: RecipeApiService,
            storage: RecipeModelStorage): RecipeRepository {
        return RecipeRepository(recipeApiService, storage)
    }

    @get:Provides
    @JvmStatic
    @Singleton
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    @JvmStatic
    internal fun providePrefs(app: LokiApplication): SharedPreferences {
        return app.getSharedPreferences("main-prefs", Context.MODE_PRIVATE)
    }

}