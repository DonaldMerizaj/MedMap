package com.example.user.medcare.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.medcare.ListItemActivity;
import com.example.user.medcare.MedViewActivity;
import com.example.user.medcare.R;
import com.example.user.medcare.adapter.ItemListRecyclerAdapter;
import com.example.user.medcare.adapter.OnClickListenerRecyclerView;
import com.example.user.medcare.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FarmaciFragment extends Fragment {


    private RecyclerView mRecyclerFarmaci;
    private Context mContext;
    private ItemListRecyclerAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FarmaciFragment() {
    }

    public static FarmaciFragment newInstance(int columnCount) {
        FarmaciFragment fragment = new FarmaciFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmaci_list, container, false);

        mContext = ((ListItemActivity)getActivity()).getApplicationContext();

        //Inicializon view me listen e farmacive
        initView();
        return view;

    }

    private void initView() {
        mRecyclerFarmaci = (RecyclerView)getActivity().findViewById(R.id.mRecyclerFarmaci);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerFarmaci.setLayoutManager(mLayoutManager);

        mRecyclerFarmaci.addOnItemTouchListener(new OnClickListenerRecyclerView(mContext,
                new OnClickListenerRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(mContext, MedViewActivity.class);
                i.putExtra(Item.EXTRA_ID, mAdapter.getAll().get(position).getId());
                startActivity(i);
            }
        }));

        mAdapter = new ItemListRecyclerAdapter(getAllFarmaci(), mContext);
        mRecyclerFarmaci.setAdapter(mAdapter);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FarmaciFragment.OnFragmentInteractionListener) {
            mListener = (FarmaciFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    // lexon te gjitha farmcaite nga JSON me farmaci
    private ArrayList<Item> getAllFarmaci() {

        ArrayList<Item> mb = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());


            JSONArray array = obj.getJSONArray("farmaci");


            for (int i = 0; i <array.length() ; i++)
            {
                JSONObject o = array.getJSONObject(i);

                Item b = new Item(o.getString("name"), o.getString("address"));
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
