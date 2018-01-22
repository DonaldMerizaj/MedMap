package com.example.user.medcare.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.medcare.MainActivity;
import com.example.user.medcare.R;
import com.example.user.medcare.model.Farmaci;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

// Fragmenti i hartes se farmacive
public class Farmaci_Map extends Fragment {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100 ;
    private static boolean isEnabledLocation = false;
    Context mContext;

    private OnFragmentInteractionListener mListener;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng defaultLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Parcelable mCurrentLocation, mCameraPosition;
    private boolean mLocationPermissionGranted;

    public Farmaci_Map() {
        // Required empty public constructor
    }

    MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<Marker> mMarkers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_farmaci_map, container, false);


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMarkers = new ArrayList<>();

        //merr kontestin nga aktiviteti kryesor
        mContext = ((MainActivity) getActivity()).getApplicationContext();
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                defaultLocation = new LatLng(41.3269816, 19.8161949);
                // Do other setup activities here too, as described elsewhere in this tutorial.

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

                // Get the current location of the device and set the position of the map.
                getDeviceLocation();
                initMapPins();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public static Farmaci_Map newInstance(String param1, String param2) {
        Farmaci_Map fragment = new Farmaci_Map();
        Bundle args = new Bundle();

        return fragment;
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                requestLocationPremission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 15));
                            LatLng l = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                            Drawable dr = getActivity().getDrawable(R.drawable.ic_my_location_black_24px);
                            BitmapDescriptor bmp = getMarkerIconFromDrawable(dr);
                            Marker pos_Marker =  googleMap.addMarker(new MarkerOptions().position(l)
                                    .icon(bmp)
                                    .title("Ti je ketu").draggable(false));
                        } else {

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void initMapPins() {


        ArrayList<Farmaci> mb = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());


            JSONArray array = obj.getJSONArray("farmaci");

            for (int i = 0; i <array.length() ; i++)
            {
                JSONObject o = array.getJSONObject(i);

                Farmaci b = new Farmaci(o.getString("name"), o.getString("address"));
                b.setImage(o.getString("logo"));
                b.setLat(o.getDouble("lat"));
                b.setLng(o.getDouble("lng"));
                b.setId(o.getInt("id"));
                mb.add(b);


                if (googleMap != null) {
                    LatLng l = b.getLatLng();
                    Marker m = googleMap.addMarker(new MarkerOptions().position(l).title(b.getName()).snippet("Direction"));
                    mMarkers.add(m);
                }
            }

            googleMap.setOnInfoWindowClickListener(
                    new GoogleMap.OnInfoWindowClickListener(){
                        public void onInfoWindowClick(Marker marker){
                            String uri = String.format(Locale.ENGLISH, "https://www.google.com/maps/dir/?api=1&traveling_mode=walking&origin="
                                    +mLastKnownLocation.getLatitude()+", "+mLastKnownLocation.getLongitude()
                                    +"&destination="+marker.getPosition().latitude
                                    +", "+marker.getPosition().longitude);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            getActivity().startActivity(intent);
                        }
                    }
            );

            if (googleMap != null && defaultLocation == null)
            {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mb.get(0).getLatLng(), 15));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("medcare.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void requestLocationPremission() {
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}