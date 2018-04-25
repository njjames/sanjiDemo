package com.nj.sanji;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2018-04-25.
 */

class LocalCacheUtils {
    private static final String CACHE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath() + "/sanjilocal";

    public Bitmap getBitmapFromLocal(String url) {
        try {
            File file = new File(CACHE_PATH, url);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            File file = new File(CACHE_PATH, url);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
