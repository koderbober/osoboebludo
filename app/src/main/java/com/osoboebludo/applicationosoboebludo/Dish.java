package com.osoboebludo.applicationosoboebludo;

import java.io.Serializable;

/**
 * Created by Ghost on 20.06.2015.
 */
public class Dish implements Serializable {
    private String title;
    private String intro;
    private String restaurantsName;
    private String restaurantsAddress;
    private String smallPhoto;
    private String largePhoto;
    private String idDish;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLargePhoto() {
        return largePhoto;
    }

    public void setLargePhoto(String largePhoto) {
        this.largePhoto = largePhoto;
    }

    public String getRestaurantsName() {
        return restaurantsName;
    }

    public void setRestaurantsName(String restaurantsName) {
        this.restaurantsName = restaurantsName;
    }

    public String getRestaurantsAddress() {
        return restaurantsAddress;
    }

    public void setRestaurantsAddress(String restaurantsAddress) {
        this.restaurantsAddress = restaurantsAddress;
    }

    public String getIdDish() {
        return idDish;
    }

    public void setIdDish(String idDish) {
        this.idDish = idDish;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSmallPhoto() {
        return smallPhoto;
    }

    public void setSmallPhoto(String smallPhoto) {
        this.smallPhoto = smallPhoto;
    }
    @Override
    public String toString() {
        return title;
    }
}
