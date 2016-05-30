package com.ghostofchaos.especialdish.Objects;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;

/**
 * Created by Ghost on 30.05.2016.
 */
public class RestaurantsLocation extends RealmObject {
    // Duplicate all parameters required to initialise a android Location
    private double longitude;
    private double latitude;

    // Public static method for converting object to android Location
    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
