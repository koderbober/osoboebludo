package com.osoboebludo.applicationosoboebludo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ghost on 20.06.2015.
 */
public class Utils {

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        URL urlPicture = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            urlPicture = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try (
                InputStream inputStream = urlPicture.openStream();
                InputStream in = new BufferedInputStream(inputStream);
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                BufferedOutputStream out = new BufferedOutputStream(dataStream);) {

            int byte_;
            while ((byte_ = in.read()) != -1) {
                out.write(byte_);
            }

            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } catch (IOException e) {
            Log.e("", "Could not load Bitmap from: " + url);
        }
        return bitmap;
    }

    public static String getPageHtml(String url) {
        String html = "";

        try {
            URL siteUrl = new URL(url);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection httpURLConnection = (HttpURLConnection) siteUrl.openConnection();
            httpURLConnection.connect();

            try (InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8");) {
                html = scanner.useDelimiter("\\A").next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
}
