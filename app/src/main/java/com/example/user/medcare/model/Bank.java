package com.example.user.medcare.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Bank {


    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_ATM_ID = "extra_atm_id";
    String name;
    String address;
    String image;
    double lat;
    double lng;
    int id;
    ArrayList<Atm> mAtms;


    public Bank(String name, String address) {
        this.name = name;
        this.address = address;
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


    public ArrayList<Atm> getmAtms() {
        return mAtms;
    }

    public void setmAtms(ArrayList<Atm> mAtms) {
        this.mAtms = mAtms;
    }

    public void setDecodeAtms(JSONObject o) throws Exception {
        JSONArray array = o.getJSONArray("atm");
        ArrayList<Atm> mAtms = new ArrayList<>();

        for (int i = 0 ; i< array.length(); i++)
        {
            JSONObject oo = array.getJSONObject(i);
            Atm a = new Atm(
                    oo.getString("address"),
                    oo.getDouble("lat"),
                    oo.getDouble("lng"),
                    oo.getInt("id")
            );
            mAtms.add(a);
        }

        setmAtms(mAtms);
    }
}
