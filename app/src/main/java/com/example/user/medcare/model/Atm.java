package com.example.user.medcare.model;

import com.google.android.gms.maps.model.LatLng;

public class Atm {

    String address;
    double lat;
    double lng;
    int id;


    public Atm(String address, double lat, double lng, int id) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return new LatLng(lat,lng);
    }
}
