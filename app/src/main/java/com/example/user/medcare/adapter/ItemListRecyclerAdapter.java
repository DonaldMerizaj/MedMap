package com.example.user.medcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.medcare.R;
import com.example.user.medcare.model.Item;

import java.util.ArrayList;

public class ItemListRecyclerAdapter extends RecyclerView.Adapter<ItemListRecyclerViewHolder> {


    ArrayList<Item> mItem;
    LayoutInflater mInflater;
    Context mContext;

    public ItemListRecyclerAdapter(ArrayList<Item> mItem, Context mContext) {
        this.mItem = mItem;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ItemListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mInflater.inflate(R.layout.row_item_list, parent, false);

        return new ItemListRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemListRecyclerViewHolder holder, int position){

        holder.bind(mItem.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    public ArrayList<Item> getAll() {
        return mItem;
    }
}
