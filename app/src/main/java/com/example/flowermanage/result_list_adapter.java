package com.example.flowermanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class result_list_adapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<flowerData> sample;

    public result_list_adapter(Context context, ArrayList<flowerData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public flowerData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.result_list, null);

        TextView probability = (TextView)view.findViewById(R.id.probability);
        TextView commonName = (TextView)view.findViewById(R.id.commonName);
        TextView scientificName = (TextView)view.findViewById(R.id.scientificName);
        ImageView exPic = (ImageView)view.findViewById(R.id.exPic);

        probability.setText(sample.get(position).getProbability());
        commonName.setText(sample.get(position).getCommonName());
        scientificName.setText(sample.get(position).getScientificName());
        Glide.with(mContext).load(sample.get(position).getImageURL()).into(exPic);

        return view;
    }
}
