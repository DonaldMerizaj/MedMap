package com.example.user.medcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.medcare.R;
import com.example.user.medcare.model.Atm;

import java.util.ArrayList;


public class AtmListRecyclerAdapter extends RecyclerView.Adapter<AtmListRecyclerViewHolder> {


    ArrayList<Atm> mAtms;
    LayoutInflater mInlater;
    Context mContext;

    public AtmListRecyclerAdapter(ArrayList<Atm> mAtms, Context mContext) {
        this.mAtms = mAtms;
        this.mContext = mContext;
        this.mInlater = LayoutInflater.from(mContext);
    }

    @Override
    public AtmListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mInlater.inflate(R.layout.row_item_list, parent, false);
        return new AtmListRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AtmListRecyclerViewHolder holder, int position) {

        holder.bind(mAtms.get(position), mContext);

    }

    @Override
    public int getItemCount() {
        return mAtms.size();
    }

    public ArrayList<Atm> getAll() {
        return mAtms;
    }
}
