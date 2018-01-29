package com.example.user.medcare.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.medcare.R;
import com.example.user.medcare.adapter.ItemListRecyclerAdapter;
import com.example.user.medcare.model.AnalyticsAplication;
import com.example.user.medcare.model.Item;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QshFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QshFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QshFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerQsh;
    private Context mContext;
    private ItemListRecyclerAdapter mAdapter;
    private Tracker mTracker;

    public QshFragment() {

    }

    public static QshFragment newInstance(String param1, String param2) {
        QshFragment fragment = new QshFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qsh_list, container, false);

        mContext = getActivity();


        // Analytics
        AnalyticsAplication application = (AnalyticsAplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();


        Log.i("SEnding Hit:", "List");
        mTracker.setScreenName("Lista e QSH");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        //Inicializon view me listen e farmacive
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerQsh = (RecyclerView)view.findViewById(R.id.mRecyclerQsh);
        initView();
    }

    private void initView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerQsh.setLayoutManager(mLayoutManager);

//        mRecyclerFarmaci.addOnItemTouchListener(new OnClickListenerRecyclerView(mContext,
//                new OnClickListenerRecyclerView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent i = new Intent(mContext, MedViewActivity.class);
//                i.putExtra(Item.EXTRA_ID, mAdapter.getAll().get(position).getId());
//                startActivity(i);
//            }
//        }));

        mAdapter = new ItemListRecyclerAdapter(getAllFarmaci(), mContext);
        mRecyclerQsh.setAdapter(mAdapter);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QshFragment.OnFragmentInteractionListener) {
            mListener = (QshFragment.OnFragmentInteractionListener) context;
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


            JSONArray array = obj.getJSONArray("qsh");


            for (int i = 0; i <array.length() ; i++)
            {
                JSONObject o = array.getJSONObject(i);

                Item b = new Item(o.getString("name"), o.getString("address"));
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
