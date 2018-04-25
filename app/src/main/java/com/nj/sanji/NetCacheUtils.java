package com.nj.sanji;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2018-04-25.
 */

class NetCacheUtils {
    private static final String TAG = "NetCacheUtils";

    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
    }

    public void getBitmapFromNet(ImageView ivShow, String url) {
        new BitmapTask().execute(ivShow, url);
    }

    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView mIvPic;
        private String mUrl;

        @Override
        protected Bitmap doInBackground(Object... objects) {
            mIvPic = (ImageView) objects[0];
            mUrl = (String) objects[1];
            return downloadBitmap(mUrl);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                mIvPic.setImageBitmap(result);
                Log.d(TAG, "onPostExecute: " + "load from net");
                //从网络加载完毕之后缓存到本地和内存
                mLocalCacheUtils.setBitmapToLocal(mUrl, result);
                mMemoryCacheUtils.setBitmapToMemory(mUrl, result);
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                byte[] data = readStreamToByteArray(connection.getInputStream());
                //计算采样率的值，网上好多的不正确
                int inSampleSize = 1;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeStream(connection.getInputStream(), null, options);
                BitmapFactory.decodeByteArray(data, 0, data.length, options);
                int width = options.outWidth;
                int height = options.outHeight;
                while (width / inSampleSize >= 200 || height / inSampleSize >= 200) {
                    inSampleSize = inSampleSize * 2;
                }
                Log.d(TAG, "inSampleSize: " + inSampleSize);
                options.inSampleSize = inSampleSize;
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                //注意要把这个变量设置为false，否则获取的bitmap为null
                options.inJustDecodeBounds = false;
//                connection.getInputStream().reset(); //用这个方法直接就宝异常了
//                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream(), null, options);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    private byte[] readStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        return os.toByteArray();
    }
}
