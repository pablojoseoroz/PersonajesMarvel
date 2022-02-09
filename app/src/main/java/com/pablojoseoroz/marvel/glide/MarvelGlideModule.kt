package com.pablojoseoroz.marvel.glide

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.pablojoseoroz.marvel.BuildConfig
import com.pablojoseoroz.marvel.R
import java.util.*

/**
 *
 * [AppGlideModule] encargado de configurar la instancia de [GlideApp]
 * Aplica las siguientes opciones x defecto:
 *
 *  * Nivel de log [Log.DEBUG] en caso de estar en [BuildConfig.DEBUG]
 *  * Caché de memoria: 20 megas
 *  * Caché de disco: 20 megas
 *  * Opciones de petición:
 *
 *  * Sin animación
 *  * Scala de tipo [android.widget.ImageView.ScaleType.CENTER_CROP]
 *  * Calidad de imagen 50%
 *  * Formato de imagen: rgb565
 *
 */
@GlideModule
class MarvelGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        if (BuildConfig.DEBUG) builder.setLogLevel(Log.DEBUG)
        setupBitmapPool(context, builder)
        setupMemoryCache(builder)
        //        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
//        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, memoryCacheSizeBytes));
        setupDiskCache(context, builder)
        builder.setDefaultRequestOptions(requestOptions(context))
    }

    /**
     * Glide uses LruBitmapPool as the default BitmapPool. LruBitmapPool is a fixed size in memory BitmapPool
     * that uses LRU eviction. The default size is based on the screen size and density of the device
     * in question as well as the memory class and the return value of isLowRamDevice.
     * The specific calculations are done by Glide’s MemorySizeCalculator, similar to the way sizes are determined for Glide’s MemoryCache.
     *
     *
     * Applications can customize the BitmapPool size in their AppGlideModule with the applyOptions(Context, GlideBuilder) method by configuring MemorySizeCalculator:
     *
     * @param context
     * @param builder
     */
    private fun setupBitmapPool(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(3f)
            .build()
        val aux: MutableSet<Bitmap.Config> = HashSet()
        aux.add(Bitmap.Config.RGB_565)
        builder.setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong(), aux))
    }

    /**
     * You can use the GlideBuilder's setDiskCache() method to set the location and/or maximum size
     * of the disk cache. You can also disable the cache entirely using DiskCacheAdapter or replace
     * it with your own implementation of the DiskCache interface. Disk caches are built on background
     * threads to avoid strict mode violations using the DiskCache.Factory interface.
     *
     *
     * By default Glide uses the InternalCacheDiskCacheFactory class to build disk caches. The internal
     * cache factory places the disk cache in your application's internal cache directory and sets a
     * maximum size of 250MB. Using the cache directory rather than the external SD card means no other
     * applications will be able to access the images you download. See Android's Storage Options doc
     * for more details.
     *
     * @param context
     * @param builder
     */
    private fun setupDiskCache(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(InternalCacheDiskCacheFactory(context))
    }

    /**
     * Glide's memory cache is used to hold resources in memory so that they are instantly available without having to perform I/O.
     *
     *
     * You can use GlideBuilder's setMemoryCache() method to set the size and/or implementation you
     * wish to use for your memory cache. The LruResourceCache class is Glide's default implementation.
     * You can set a custom maximum in memory byte size by passing in the size you want to the LruResourceCache constructor:
     *
     * @param builder
     */
    private fun setupMemoryCache(builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 50 // 50mb
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    companion object {
        private fun requestOptions(context: Context): RequestOptions {
            return RequestOptions() //                .signature(new ObjectKey(System.currentTimeMillis() / (24 * 60 * 60 * 1000)))
                //                .dontAnimate()
                //                .centerCrop()
                //                .encodeFormat(Bitmap.CompressFormat.PNG)
                //                .encodeFormat(Bitmap.CompressFormat.WEBP)
                //                .encodeQuality(50)
                //                .encodeQuality(DeviceUtils.isHighPerformingDevice(context)
                //                        ? 50
                //                        : 30)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_baseline_person_outline_24)
                .skipMemoryCache(false)
        }

    }
}