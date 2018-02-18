package net.dragora.recipeapp.base.di.module

import android.app.Application
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER

@Target(FUNCTION, PROPERTY_GETTER)
annotation class InitAction

@Module
internal object InitModule {

    @Provides
    @IntoSet
    @InitAction
    @JvmStatic
    internal fun initActionFresco(app: Application): () -> Unit = {
        val config = ImagePipelineConfig.newBuilder(app)
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(app).build())
                .setSmallImageDiskCacheConfig(
                        DiskCacheConfig.newBuilder(app).build())
                .setProgressiveJpegConfig(
                        SimpleProgressiveJpegConfig())
                .experiment()
                .setMediaVariationsIndexEnabled { true }
                .build()
        Fresco.initialize(app, config)
    }

}