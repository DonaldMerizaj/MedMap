package com.example.user.medcare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.medcare.PlayerViewActivity;
import com.example.user.medcare.R;
import com.example.user.medcare.model.Item;


public class ItemListRecyclerViewHolder extends RecyclerView.ViewHolder {


    ImageView youtube;
    ImageView logo;
    TextView name, address;

    public ItemListRecyclerViewHolder(View itemView) {
        super(itemView);

        logo = (ImageView) itemView.findViewById(R.id.image);
        name = (TextView) itemView.findViewById(R.id.item_name);
        address = (TextView) itemView.findViewById(R.id.item_address);
        youtube = (ImageView) itemView.findViewById(R.id.youtubeItem);
    }

    public void bind(Item item, final Context mContext)
    {

        name.setText(item.getName());
        address.setText(item.getAddress());
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayerViewActivity.class);
                mContext.startActivity(intent);
            }
        });
    }


}
