package com.example.user.medcare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.medcare.adapter.BankListRecyclerAdapter;
import com.example.user.medcare.adapter.OnClickListenerRecyclerView;
import com.example.user.medcare.model.Bank;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MedMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 100;
    private GoogleMap mMap;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private View bottomSheet;
    private BankListRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerBanks;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private BottomSheetDialog mBottomSheetDialog;
    private int lastPosition = 0;
    private ArrayList<Marker> mMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMarkers = new ArrayList<>();

        bottomSheet = findViewById(R.id.bottom_sheet);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mRecyclerBanks.smoothScrollToPosition(Integer.parseInt(marker.getSnippet()) - 1 );
            }
        });


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        showBottomList();
    }


    public void showBottomList() {


//        mBottomSheetDialog = new BottomSheetDialog(this);
//        final View view = getLayoutInflater().inflate(R.layout.bottom_sheet_map_list, null);

//        mBottomSheetDialog.setContentView(view);

        mRecyclerBanks = (RecyclerView) findViewById(R.id.mRecycler);
//        mRecyclerBanks.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerBanks.setLayoutManager(mLayoutManager);

        mRecyclerBanks.addOnItemTouchListener(new OnClickListenerRecyclerView(getApplicationContext(), new OnClickListenerRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), MedViewActivity.class);
                i.putExtra(Bank.EXTRA_ID, mAdapter.getAll().get(position).getId());
                startActivity(i);
            }
        }));

        mRecyclerBanks.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int pos = mLayoutManager.findFirstVisibleItemPosition();
                Log.d("LIST", String.valueOf(pos));
                if (lastPosition != pos) {

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mAdapter.getAll().get(pos).getLatLng(), 15));
                    lastPosition = pos;
                    mMarkers.get(pos).showInfoWindow();

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });


        mAdapter = new BankListRecyclerAdapter(getAllBanks(), getApplicationContext());
        mRecyclerBanks.setAdapter(mAdapter);



//        mBottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
//        mBottomSheetBehavior.setPeekHeight(260);


//        mBottomSheetDialog.setCancelable(false);
//        mBottomSheetDialog.show();


//        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//            }
//        });
    }

    private ArrayList<Bank> getAllBanks() {

        ArrayList<Bank> mb = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());


            JSONArray array = obj.getJSONArray("banks");


            for (int i = 0; i <array.length() ; i++)
            {
                JSONObject o = array.getJSONObject(i);

                Bank b = new Bank(o.getString("name"), o.getString("address"));
                b.setImage(o.getString("logo"));
                b.setLat(o.getDouble("lat"));
                b.setLng(o.getDouble("lng"));
                b.setId(o.getInt("id"));
                mb.add(b);


                if (mMap != null) {
                    LatLng l = b.getLatLng();
                    Marker m = mMap.addMarker(new MarkerOptions().position(l).title(b.getName()).snippet(String.valueOf(b.getId())));
                    mMarkers.add(m);
                }

            }


            if (mMap != null)
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mb.get(0).getLatLng(), 15));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mb;
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("banks.json");
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

    @Override
    public void onStart() {
        super.onStart();
        requestLocationPremission();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bank_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_list)
        {
            Intent i = new Intent(getApplicationContext(), ListBanksActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }


    private void requestLocationPremission() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), MedMapActivity.this)) {
        } else {
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), MedMapActivity.this);
        }
    }

    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            Toast.makeText(getApplicationContext(), "GPS permission is required.", Toast.LENGTH_SHORT).show();
        } else {

            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }

    public static boolean checkPermission(String strPermission, Context _c, Activity _a) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied, Enable Location.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
