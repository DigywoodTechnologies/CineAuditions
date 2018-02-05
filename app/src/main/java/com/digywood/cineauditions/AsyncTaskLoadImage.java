package com.digywood.cineauditions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by prasa on 2018-02-05.
 */

public class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {

    private final static String TAG = "AsyncTaskLoadImage";
    private ImageView imageView;
    private URL url=null;
    public AsyncTaskLoadImage(ImageView imageView,URL url) {
        this.imageView = imageView;
        this.url=url;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try {
//            URL url = new URL(params[0]);
            bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}
