package com.example.user.medcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.user.medcare.adapter.AtmListRecyclerAdapter;
import com.example.user.medcare.adapter.OnClickListenerRecyclerView;
import com.example.user.medcare.model.Bank;
import com.example.user.medcare.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MedViewActivity extends AppCompatActivity {

    private int mSelectedBankId;
    private Bank mBank = null;
    TextView address;
    private AtmListRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerAtms;
    TextView mViewAllAtms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_view);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        initView();
        setActions();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+mBank.getLat()+","+mBank.getLng()+""));
                startActivity(intent);

            }
        });
    }


    private void setActions() {
//        mViewAllAtms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), AtmMapActivity.class);
//                i.putExtra(Bank.EXTRA_ID, mSelectedBankId);
//                startActivity(i);
//            }
//        });
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();

        address = (TextView) findViewById(R.id.address);
        mViewAllAtms = (TextView) findViewById(R.id.viewatms);



        mRecyclerAtms = (RecyclerView) findViewById(R.id.mRecycler);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerAtms.setLayoutManager(mLayoutManager);

//        mRecyclerAtms.addOnItemTouchListener(new OnClickListenerRecyclerView(getApplicationContext(), new OnClickListenerRecyclerView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent i = new Intent(getApplicationContext(), AtmMapActivity.class);
//                i.putExtra(Bank.EXTRA_ID, mSelectedBankId);
//                i.putExtra(Bank.EXTRA_ATM_ID, mAdapter.getAll().get(position).getId());
//                startActivity(i);
//            }
//        }));




        if(bundle != null)
        {
            mSelectedBankId = bundle.getInt(Item.EXTRA_ID);
            mBank = getBankDetails();
//            Log.d("bank", String.valueOf(mBank.getmAtms().size()));
            setTitle(mBank.getName());
            address.setText(mBank.getAddress());


            mAdapter = new AtmListRecyclerAdapter(mBank.getmAtms(), getApplicationContext());
            mRecyclerAtms.setAdapter(mAdapter);
        }
    }



    private Bank getBankDetails() {

        Bank mb = null;

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());


            JSONArray array = obj.getJSONArray("banks");


            for (int i = 0; i <array.length() ; i++)
            {
                JSONObject o = array.getJSONObject(i);


                if (o.getInt("id") == mSelectedBankId) {

                    mb = new Bank(o.getString("name"), o.getString("address"));
                    mb.setImage(o.getString("logo"));
                    mb.setLat(o.getDouble("lat"));
                    mb.setLng(o.getDouble("lng"));
                    mb.setId(o.getInt("id"));
                    mb.setDecodeAtms(o);
                    return mb;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_video:
                startActivity(new Intent(getApplicationContext(), PlayerViewActivity.class));
                break;

        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bank_view, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);

    }
}
