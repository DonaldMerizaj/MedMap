package com.example.user.medcare.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by User on 1/25/2018.
 */

public class Item {
    public static final String EXTRA_ID = "extra_id";

    String name , address, image;
    double lat, lng;
    int id;
    ArrayList<Item> mItem;

    public Item(String name, String address){
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


    public ArrayList<Item> getItem() {
        return mItem;
    }

    public void setmItem(ArrayList<Item> mItem) {
        this.mItem = mItem;
    }

}
