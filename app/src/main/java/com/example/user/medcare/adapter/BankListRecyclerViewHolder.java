package com.example.user.medcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.medcare.R;
import com.example.user.medcare.model.Bank;
import com.squareup.picasso.Picasso;


public class BankListRecyclerViewHolder extends RecyclerView.ViewHolder {


    ImageView logo;
    TextView name, address;

    public BankListRecyclerViewHolder(View itemView) {
        super(itemView);

        logo = (ImageView) itemView.findViewById(R.id.image);
        name = (TextView) itemView.findViewById(R.id.bank_name);
        address = (TextView) itemView.findViewById(R.id.bank_address);
    }

    public void bind(Bank bank, Context mContext)
    {

        name.setText(bank.getName());
        address.setText(bank.getAddress());

        Picasso.with(mContext)
                .load(bank.getImage())
                .into(logo);

    }


}
