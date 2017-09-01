package info.tracking.list.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.tracking.R;
import info.tracking.database.bean.TrackBean;
import info.tracking.list.holder.LatLngViewHolder;

/**
 * Created by Asus on 05-05-2017.
 */

public class LatLng_Adapter extends RecyclerView.Adapter<LatLngViewHolder> {

    List<TrackBean> latLngList;
    Activity context;
    View layoutView;
    public LatLng_Adapter(Activity context, List<TrackBean> latLngList){
        this.latLngList = latLngList;
        this.context = context;
    }


    @Override
    public LatLngViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        layoutView = LayoutInflater.from(context).inflate(R.layout.latlng_item, null);
        LatLngViewHolder rcv = new LatLngViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(LatLngViewHolder holder, final int position) {
        holder.latLngTxt.setText(latLngList.get(position).getLatLng());
    }

    @Override
    public int getItemCount() {
        return this.latLngList.size();
    }

    }
