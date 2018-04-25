package com.nj.sanji;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 自定义三级缓存加载工具
 * Created by Administrator on 2018-04-25.
 */

public class MyBitmapLoadUtils {
    private static final String TAG = "MyBitmapLoadUtils";

    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapLoadUtils() {
        mLocalCacheUtils = new LocalCacheUtils();
        mMemoryCacheUtils = MemoryCacheUtils.getMemoryCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
    }

    /**
     * 利用三级缓存从访问地址中加载图片
     * @param ivShow
     * @param url
     */
    public void display(Context context, ImageView ivShow, String url) {
        //设置默认显示的图片
        ivShow.setImageResource(R.mipmap.ic_launcher);
        //从内存加载
        Bitmap bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap != null) {
            ivShow.setImageBitmap(bitmap);
            Log.d(TAG, "display: " + "load from memory");
            Toast.makeText(context, "load from memory", Toast.LENGTH_SHORT).show();
            return;
        }
        //从本地加载
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if (bitmap != null) {
            ivShow.setImageBitmap(bitmap);
            Log.d(TAG, "display: " + "load from local");
            Toast.makeText(context, "load from local", Toast.LENGTH_SHORT).show();
            //从本地加载成功后，要存储到内存中
            mMemoryCacheUtils.setBitmapToMemory(url, bitmap);
            return;
        }
        //从网络加载
        mNetCacheUtils.getBitmapFromNet(ivShow, url);
    }
}
