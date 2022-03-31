package com.csi.technodrugs.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.csi.technodrugs.R;

import java.util.ArrayList;

/**
 * Created by Jahid on 13-11-2018.
 */

public class SQLiteListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> id;
    ArrayList<String> productName;
    ArrayList<String> quantity;
    ArrayList<String> pack;
    ArrayList<String> tradePrice ;
    ArrayList<String> netAmount ;

    public SQLiteListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> productName,
            ArrayList<String> quantity,
            ArrayList<String> pack,
            ArrayList<String> tradePrice ,
            ArrayList<String> netAmount )
    {
        this.context = context2;
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.pack = pack;
        this.tradePrice = tradePrice ;
        this.netAmount = netAmount;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return id.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.order_card, null);

            holder = new Holder();

            holder.textViewProductName = (TextView) child.findViewById(R.id.textViewProductName);
            holder.textViewQuantity = (TextView) child.findViewById(R.id.textViewQuantity);
            holder.textViewPack = (TextView) child.findViewById(R.id.textViewPack);
            holder.textViewTradePrice = (TextView) child.findViewById(R.id.textViewTradePrice);
            holder.textViewNetAmount = (TextView) child.findViewById(R.id.textViewNetAmount);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }

        holder.textViewProductName.setText(productName.get(position));
        holder.textViewQuantity.setText(quantity.get(position));
        holder.textViewPack.setText(pack.get(position));
        holder.textViewTradePrice.setText(tradePrice.get(position));
        holder.textViewNetAmount.setText(netAmount.get(position));

        return child;
    }

    public class Holder {
        TextView textViewProductName,textViewQuantity,textViewPack,textViewTradePrice,textViewNetAmount;

    }

}
