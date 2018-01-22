package com.example.user.medcare;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
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

import com.example.user.medcare.adapter.AtmListRecyclerAdapter;
import com.example.user.medcare.adapter.OnClickListenerRecyclerView;
import com.example.user.medcare.model.Atm;
import com.example.user.medcare.model.Bank;
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

public class AtmMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 100;
    private GoogleMap mMap;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private View bottomSheet;
    private AtmListRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerAtm;

    private int selectedBank;
    private Toolbar mToolbar;
    private int selectedAtmBank = -1;
    private int selectedAtmPosition = 0;
    private int lastPosition = 0;
    private ArrayList<Marker> mMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_map);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mMarkers = new ArrayList<>();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            selectedBank = bundle.getInt(Bank.EXTRA_ID);
            selectedAtmBank = bundle.getInt(Bank.EXTRA_ATM_ID);
        }



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mRecyclerAtm.smoothScrollToPosition(Integer.parseInt(marker.getSnippet()) - 1 );
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

        mRecyclerAtm = (RecyclerView) findViewById(R.id.mRecycler);
//        mRecyclerAtm.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerAtm.setLayoutManager(mLayoutManager);

        mRecyclerAtm.addOnItemTouchListener(new OnClickListenerRecyclerView(getApplicationContext(), new OnClickListenerRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent i = new Intent(getApplicationContext(), MedViewActivity.class);
//                i.putExtra(Bank.EXTRA_ID, mAdapter.getAll().get(position).getId());
//                startActivity(i);
                mRecyclerAtm.smoothScrollToPosition(position);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mAdapter.getAll().get(position).getLatLng(), 15));

                mMarkers.get(position).showInfoWindow();
            }
        }));

        mRecyclerAtm.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int pos = mLayoutManager.findFirstVisibleItemPosition();
                Log.d("LIST", String.valueOf(pos));
                if (lastPosition != pos) {
                    mRecyclerAtm.smoothScrollToPosition(pos);
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


        mAdapter = new AtmListRecyclerAdapter(getAllAtms(), getApplicationContext());
        mRecyclerAtm.setAdapter(mAdapter);


        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (selectedAtmBank != -1){
                    mRecyclerAtm.smoothScrollToPosition(selectedAtmPosition);
                }
            }
        }, 1500);





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

    private ArrayList<Atm> getAllAtms() {

        ArrayList<Atm> mb = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());


            JSONArray array = obj.getJSONArray("banks");


            for (int i = 0; i <array.length() ; i++)
            {
                JSONObject o = array.getJSONObject(i);

                if (o.getInt("id") == selectedBank) {

                    Bank b = new Bank(o.getString("name"), o.getString("address"));
                    b.setImage(o.getString("logo"));
                    b.setLat(o.getDouble("lat"));
                    b.setLng(o.getDouble("lng"));
                    b.setId(o.getInt("id"));
                    b.setDecodeAtms(o);


                    mToolbar.setSubtitle(b.getName());


                    for (int j = 0; j < b.getmAtms().size(); j++) {
                        if (mMap != null) {
                            LatLng l = b.getmAtms().get(j).getLatLng();
                            MarkerOptions mo = new MarkerOptions().position(l).title(b.getmAtms().get(j).getAddress()).snippet(String.valueOf(b.getmAtms().get(j).getId()));
                            Marker marker = mMap.addMarker(mo);

                            mMarkers.add(marker);

                            if (selectedAtmBank != -1) {
                               if (b.getmAtms().get(i).getId() == selectedAtmBank) {
                                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
                                   selectedAtmPosition = j;
                               }

                           }
                        }
                    }

                    if (selectedAtmBank == -1) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(b.getmAtms().get(0).getLatLng(), 15));
                    }


                    return b.getmAtms();
                }



            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mb;
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("medcare.json");
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
        return true;
    }



    private void requestLocationPremission() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), AtmMapActivity.this)) {
        } else {
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), AtmMapActivity.this);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
