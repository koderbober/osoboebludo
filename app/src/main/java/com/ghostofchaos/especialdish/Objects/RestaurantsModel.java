package com.ghostofchaos.especialdish.Objects;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ghost on 29.03.2016.
 */
public class RestaurantsModel extends RealmObject implements Serializable {

    @PrimaryKey
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
    private String SPb_metro;
    private String moscow_metro;
    private String restaurants_type;
    private String restaurants_cuisine;         //Кухня
    private String restaurants_special;         //Особенное место
    private String restaurants_entertainment;   //Развлечения
    private String restaurants_music;           //Музыка
    private String restaurants_misc;            //Дополнительные услуги
    private RestaurantsLocation location;

    private String search_title;
    private String search_intro;
    private String search_description;
    private String search_address;
    private String search_website;
    private String search_email;
    private String search_city;
    private String search_SPb_metro;
    private String search_moscow_metro;
    private String search_restaurants_type;
    private String search_restaurants_cuisine;         //Кухня
    private String search_restaurants_special;         //Особенное место
    private String search_restaurants_entertainment;   //Развлечения
    private String search_restaurants_music;           //Музыка
    private String search_restaurants_misc;            //Дополнительные услуги
    private String search_all;

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

    public String getSPb_metro() {
        return SPb_metro;
    }

    public void setSPb_metro(String SPb_metro) {
        this.SPb_metro = SPb_metro;
    }

    public String getMoscow_metro() {
        return moscow_metro;
    }

    public void setMoscow_metro(String moscow_metro) {
        this.moscow_metro = moscow_metro;
    }

    public String getRestaurants_cuisine() {
        return restaurants_cuisine;
    }

    public void setRestaurants_cuisine(String restaurants_cuisine) {
        this.restaurants_cuisine = restaurants_cuisine;
    }

    public String getRestaurants_special() {
        return restaurants_special;
    }

    public void setRestaurants_special(String restaurants_special) {
        this.restaurants_special = restaurants_special;
    }

    public String getRestaurants_entertainment() {
        return restaurants_entertainment;
    }

    public void setRestaurants_entertainment(String restaurants_entertainment) {
        this.restaurants_entertainment = restaurants_entertainment;
    }

    public String getRestaurants_music() {
        return restaurants_music;
    }

    public void setRestaurants_music(String restaurants_music) {
        this.restaurants_music = restaurants_music;
    }

    public String getRestaurants_misc() {
        return restaurants_misc;
    }

    public void setRestaurants_misc(String restaurants_misc) {
        this.restaurants_misc = restaurants_misc;
    }

    public RestaurantsLocation getLocation() {
        return location;
    }

    public void setLocation(RestaurantsLocation location) {
        this.location = location;
    }

    public String getSearch_title() {
        return search_title;
    }

    public void setSearch_title(String search_title) {
        this.search_title = search_title;
    }

    public String getSearch_intro() {
        return search_intro;
    }

    public void setSearch_intro(String search_intro) {
        this.search_intro = search_intro;
    }

    public String getSearch_description() {
        return search_description;
    }

    public void setSearch_description(String search_description) {
        this.search_description = search_description;
    }

    public String getSearch_address() {
        return search_address;
    }

    public void setSearch_address(String search_address) {
        this.search_address = search_address;
    }

    public String getSearch_website() {
        return search_website;
    }

    public void setSearch_website(String search_website) {
        this.search_website = search_website;
    }

    public String getSearch_email() {
        return search_email;
    }

    public void setSearch_email(String search_email) {
        this.search_email = search_email;
    }

    public String getSearch_city() {
        return search_city;
    }

    public void setSearch_city(String search_city) {
        this.search_city = search_city;
    }

    public String getSearch_SPb_metro() {
        return search_SPb_metro;
    }

    public void setSearch_SPb_metro(String search_SPb_metro) {
        this.search_SPb_metro = search_SPb_metro;
    }

    public String getSearch_moscow_metro() {
        return search_moscow_metro;
    }

    public void setSearch_moscow_metro(String search_moscow_metro) {
        this.search_moscow_metro = search_moscow_metro;
    }

    public String getSearch_restaurants_type() {
        return search_restaurants_type;
    }

    public void setSearch_restaurants_type(String search_restaurants_type) {
        this.search_restaurants_type = search_restaurants_type;
    }

    public String getSearch_restaurants_cuisine() {
        return search_restaurants_cuisine;
    }

    public void setSearch_restaurants_cuisine(String search_restaurants_cuisine) {
        this.search_restaurants_cuisine = search_restaurants_cuisine;
    }

    public String getSearch_restaurants_special() {
        return search_restaurants_special;
    }

    public void setSearch_restaurants_special(String search_restaurants_special) {
        this.search_restaurants_special = search_restaurants_special;
    }

    public String getSearch_restaurants_entertainment() {
        return search_restaurants_entertainment;
    }

    public void setSearch_restaurants_entertainment(String search_restaurants_entertainment) {
        this.search_restaurants_entertainment = search_restaurants_entertainment;
    }

    public String getSearch_restaurants_music() {
        return search_restaurants_music;
    }

    public void setSearch_restaurants_music(String search_restaurants_music) {
        this.search_restaurants_music = search_restaurants_music;
    }

    public String getSearch_restaurants_misc() {
        return search_restaurants_misc;
    }

    public void setSearch_restaurants_misc(String search_restaurants_misc) {
        this.search_restaurants_misc = search_restaurants_misc;
    }

    public String getSearch_all() {
        return search_all;
    }

    public void setSearch_all(String search_all) {
        this.search_all = search_all;
    }
}
