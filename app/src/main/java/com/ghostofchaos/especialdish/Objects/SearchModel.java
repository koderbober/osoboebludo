package com.ghostofchaos.especialdish.Objects;

import java.io.Serializable;

/**
 * Created by Ghost on 29.03.2016.
 */
public class SearchModel implements Serializable {

    private int id;
    private String title;
    private String intro;
    private String description;
    private String photo_large_path;
    private String photo_small_path;
    private String phone;
    private String address;
    private String website;
    private String email;
    private String city;
    private String restaurants_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto_large_path() {
        return photo_large_path;
    }

    public void setPhoto_large_path(String photo_large_path) {
        this.photo_large_path = photo_large_path;
    }

    public String getPhoto_small_path() {
        return photo_small_path;
    }

    public void setPhoto_small_path(String photo_small_path) {
        this.photo_small_path = photo_small_path;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRestaurants_type() {
        return restaurants_type;
    }

    public void setRestaurants_type(String restaurants_type) {
        this.restaurants_type = restaurants_type;
    }
}
