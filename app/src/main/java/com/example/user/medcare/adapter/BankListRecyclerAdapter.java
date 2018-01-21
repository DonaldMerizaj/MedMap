package com.example.user.medcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.medcare.R;
import com.example.user.medcare.model.Bank;

import java.util.ArrayList;

public class BankListRecyclerAdapter extends RecyclerView.Adapter<BankListRecyclerViewHolder> {


    ArrayList<Bank> mBanks;
    LayoutInflater mInlater;
    Context mContext;

    public BankListRecyclerAdapter(ArrayList<Bank> mBanks, Context mContext) {
        this.mBanks = mBanks;
        this.mContext = mContext;
        this.mInlater = LayoutInflater.from(mContext);
    }

    @Override
    public BankListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mInlater.inflate(R.layout.row_bank_list, parent, false);
        return new BankListRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BankListRecyclerViewHolder holder, int position) {

        holder.bind(mBanks.get(position), mContext);

    }

    @Override
    public int getItemCount() {
        return mBanks.size();
    }

    public ArrayList<Bank> getAll() {
        return mBanks;
    }
}
