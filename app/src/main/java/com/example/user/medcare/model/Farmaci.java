package com.example.user.medcare.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User on 1/22/2018.
 */

public class Farmaci {
    String name , address, image;
    double lat, lng;
    int id;
    ArrayList<Farmaci> mFarmaci;

    public Farmaci(String name, String address){
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


    public ArrayList<Farmaci> getFarmaci() {
        return mFarmaci;
    }

    public void setmmFarmaci(ArrayList<Farmaci> mFarmaci) {
        this.mFarmaci = mFarmaci;
    }

//    public void setDecodeFarmaci(JSONObject o) throws Exception {
//        JSONArray array = o.getJSONArray("farmaci");
//        ArrayList<Farmaci> mFarmaci = new ArrayList<>();
//
//        for (int i = 0 ; i< array.length(); i++)
//        {
//            JSONObject oo = array.getJSONObject(i);
//            Farmaci a = new Farmaci(
//                    oo.getString("address"),
//                    oo.getDouble("lat"),
//                    oo.getDouble("lng"),
//                    oo.getInt("id")
//            );
//            mFarmaci.add(a);
//        }
//
//        setFarmaci(mFarmaci);
//    }
}
