package com.example.user.medcare.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by User on 1/22/2018.
 */

public class Qsh {
    String name , address, image;
    double lat, lng;
    int id;
    ArrayList<Qsh> mQsh;

    public Qsh(String name, String address){
        this.address = address;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        return new LatLng(getLat(), getLng());
    }


    public ArrayList<Qsh> getQsh() {
        return mQsh;
    }

    public void setmQsh(ArrayList<Qsh> mFarmaci) {
        this.mQsh = mQsh;
    }
}
