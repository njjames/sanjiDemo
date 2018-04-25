package com.nj.sanji;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Administrator on 2018-04-25.
 */

class MemoryCacheUtils {
    //这样写根本就从内存中获取不到，因为每次都要new
//    private LruCache<String, Bitmap> mMemoryCache;
//
//    public MemoryCacheUtils() {
//        long maxMemory = Runtime.getRuntime().maxMemory();
//        mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount();
//            }
//        };
//    }

    //用这种方式可以
    private static LruCache<String, Bitmap> mMemoryCache;

    public static MemoryCacheUtils getMemoryCacheUtils() {
        if (mMemoryCache == null) {
            long maxMemory = Runtime.getRuntime().maxMemory();
            mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }
        return new MemoryCacheUtils();
    }

    private MemoryCacheUtils() {
    }

    public Bitmap getBitmapFromMemory(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        return bitmap;
    }

    public void setBitmapToMemory(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }
}
