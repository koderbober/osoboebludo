package com.osoboebludo.applicationosoboebludo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Ghost on 30.06.2015.
 */
public class DishBitmap {

    private Bitmap bitmapSmallPhoto;
    private Bitmap bitmapLargePhoto;
    private String idDish;

    public String getIdDish() {
        return idDish;
    }

    public void setIdDish(String idDish) {
        this.idDish = idDish;
    }

    public Bitmap getBitmapSmallPhoto() {
        return bitmapSmallPhoto;
    }

    public void setBitmapSmallPhoto(String smallPhoto) {
        if (smallPhoto != "") {
            Bitmap bitmapSmallPhoto = Utils.loadBitmap("http://osoboebludo.com/uploads/content/" + smallPhoto);
            this.bitmapSmallPhoto = bitmapSmallPhoto;
        } else {
            bitmapSmallPhoto = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher);
        }
    }

    public Bitmap getBitmapLargePhoto() {
        return bitmapLargePhoto;
    }

    public void setBitmapLargePhoto(String largePhoto) {
        if (largePhoto != "") {
            Bitmap bitmapLargePhoto = Utils.loadBitmap("http://osoboebludo.com/uploads/content/" + largePhoto);
            this.bitmapLargePhoto = bitmapLargePhoto;
        } else {
            bitmapLargePhoto = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher);
        }
    }

    @Override
    public String toString() {
        if (getBitmapSmallPhoto() != null){
            return "Bitmap";
        }
        return idDish;
    }
}
