package info.tracking.list.holder;

/**
 * Created by Asus on 05-05-2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import info.tracking.R;

public class LatLngViewHolder extends RecyclerView.ViewHolder{

   public TextView latLngTxt;
    public LatLngViewHolder(View itemView) {
        super(itemView);

        latLngTxt=(TextView)itemView.findViewById(R.id.latLngTxt);
    }



}