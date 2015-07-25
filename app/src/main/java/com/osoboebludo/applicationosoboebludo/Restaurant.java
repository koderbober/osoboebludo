package com.osoboebludo.applicationosoboebludo;

import java.io.Serializable;

/**
 * Created by Ghost on 09.05.2015.
 */
public class Restaurant implements Serializable {
    
    private String title;
    private String intro;
    private String description;
    private String smallPhoto;
    private String largePhoto;
    private String addressInstructions;
    private String address;
    private String city;
    private String phone;
    private String website;
    private String averageCheck;
    private String idRestaurant;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddressInstructions() {
        return addressInstructions;
    }

    public void setAddressInstructions(String addressInstructions) {
        this.addressInstructions = addressInstructions;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAverageCheck() {
        return averageCheck;
    }

    public void setAverageCheck(String averageCheck) {
        this.averageCheck = averageCheck;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
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

    public String getLargePhoto() {
        return largePhoto;
    }

    public void setLargePhoto(String largePhoto) {
        this.largePhoto = largePhoto;
    }

    @Override
    public String toString() {
        return title;
    }
}