package com.example.user.medcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.medcare.R;
import com.example.user.medcare.model.Bank;
import com.example.user.medcare.model.Item;
import com.squareup.picasso.Picasso;


public class ItemListRecyclerViewHolder extends RecyclerView.ViewHolder {


    ImageView logo;
    TextView name, address;

    public ItemListRecyclerViewHolder(View itemView) {
        super(itemView);

        logo = (ImageView) itemView.findViewById(R.id.image);
        name = (TextView) itemView.findViewById(R.id.item_name);
        address = (TextView) itemView.findViewById(R.id.item_address);
    }

    public void bind(Item item, Context mContext)
    {

        name.setText(item.getName());
        address.setText(item.getAddress());

        Picasso.with(mContext)
                .load(item.getImage())
                .into(logo);
    }


}
