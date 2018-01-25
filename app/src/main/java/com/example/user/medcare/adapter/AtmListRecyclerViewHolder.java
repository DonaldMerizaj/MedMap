package com.example.user.medcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.user.medcare.R;
import com.example.user.medcare.model.Atm;

public class AtmListRecyclerViewHolder extends RecyclerView.ViewHolder {


    TextView address;

    public AtmListRecyclerViewHolder(View itemView) {
        super(itemView);

        address = (TextView) itemView.findViewById(R.id.item_address);
    }

    public void bind(Atm atm, Context mContext)
    {

        address.setText(atm.getAddress());


    }


}
