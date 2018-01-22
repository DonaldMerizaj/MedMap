package com.example.user.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.medcare.adapter.BankListRecyclerAdapter;
import com.example.user.medcare.adapter.OnClickListenerRecyclerView;
import com.example.user.medcare.model.Bank;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListBanksActivity extends AppCompatActivity {

    private RecyclerView mRecyclerBanks;
    private BankListRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_banks);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        mRecyclerBanks = (RecyclerView) findViewById(R.id.mRecycler);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerBanks.setLayoutManager(mLayoutManager);

        mRecyclerBanks.addOnItemTouchListener(new OnClickListenerRecyclerView(getApplicationContext(), new OnClickListenerRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), MedViewActivity.class);
                i.putExtra(Bank.EXTRA_ID, mAdapter.getAll().get(position).getId());
                startActivity(i);
            }
        }));



        mAdapter = new BankListRecyclerAdapter(getAllBanks(), getApplicationContext());
        mRecyclerBanks.setAdapter(mAdapter);
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


            }


        } catch (JSONException e) {
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
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
