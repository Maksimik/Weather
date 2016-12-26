package com.maksimik.weather.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.utils.ContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private MemoryCache memoryCache = new MemoryCache();
    private ExecutorService executorService;
    private Map<String, ArrayList<ImageView>> waitingImages = new HashMap<>();
    private final Handler handler = new Handler();
    private Context context;

    public ImageLoader() {
        executorService = Executors.newCachedThreadPool();
        context = ContextHolder.getInstance().getContext();

    }

    public void displayImage(String url, ImageView imageView) {

        imageView.setScaleType(ImageView.ScaleType.CENTER);
        //TODO remove
//        imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_business_black_24dp));

        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.getBitmap(url);

        if (bitmap != null) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);
        } else if (waitingImages.containsKey(url)) {
            waitingImages.get(url).add(imageView);
        } else {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            ArrayList<ImageView> temp = new ArrayList<>();
            temp.add(imageView);
            waitingImages.put(url, temp);
            queueImage(url);
        }
    }

    private void queueImage(String url) {

        executorService.submit(new LoadingImage(url));

    }

    private Bitmap getBitmap(String url) {
        try {

            return BitmapFactory.decodeResource(context.getResources(),
                    context.getResources().getIdentifier(Constants.IMG + url, "drawable", context.getPackageName()));

        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError) {
                memoryCache.clear();
            }
            return null;
        }
    }

    private class LoadingImage implements Runnable {

        private String url;

        LoadingImage(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {

                Bitmap bmp = getBitmap(url);
                if (bmp == null) {
                    return;
                }

                memoryCache.putBitmap(url, bmp);


                BitmapDisplayer bd = new BitmapDisplayer(bmp, url);
                handler.post(bd);

            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private class BitmapDisplayer implements Runnable {
        private Bitmap bitmap;
        private String url;

        BitmapDisplayer(Bitmap b, String p) {
            bitmap = b;
            url = p;
        }

        public void run() {

            if (bitmap != null) {

                for (ImageView iv : waitingImages.get(url)) {

                    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    iv.setImageBitmap(bitmap);
                }

                waitingImages.remove(url);
            }
        }
    }
}