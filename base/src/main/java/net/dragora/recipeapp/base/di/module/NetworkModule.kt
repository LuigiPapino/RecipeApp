package net.dragora.recipeapp.base.di.module

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import net.dragora.recipeapp.BuildConfig
import net.dragora.recipeapp.base.BuildType.DEBUG
import net.dragora.recipeapp.base.data.network.RecipeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by luigipapino on 18/02/2018.
 */

@Singleton
@Module
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideOkHttp(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (DEBUG.isCurrentBuild) {
            BODY
        } else {
            NONE
        }
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
                .baseUrl(BuildConfig.BASE_API_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}