package info.tracking.list;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import info.tracking.MainActivity;
import info.tracking.R;
import info.tracking.database.TrackingSource;
import info.tracking.database.bean.TrackBean;
import info.tracking.list.adapter.LatLng_Adapter;
import info.tracking.showroute.Map_Activity;

/**
 * Created by Asus on 05-05-2017.
 */

public class Activity_LatLngList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latlnglist);
        ButterKnife.bind(this);

        setTypeface();
        objectInitialization();
        setRecyclerView();

    }

    TrackingSource datasource;
    int counter=0;
    List<TrackBean> values;
    private void objectInitialization() {
        datasource = new TrackingSource(Activity_LatLngList.this);

    }

    Typeface Montserrat;
    private void setTypeface() {

        Montserrat = Typeface.createFromAsset(Activity_LatLngList.this.getAssets(), "Montserrat-Regular.ttf");

    }

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    private void setRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.isInEditMode();
        layoutManager = new LinearLayoutManager(Activity_LatLngList.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true);


        datasource.open();
        values = datasource.getAllLatLng();

        if (values.size()>0){
            LatLng_Adapter latlng_adapter=new LatLng_Adapter(Activity_LatLngList.this,values);
            recyclerView.setAdapter(latlng_adapter);

        }else {
            Toast.makeText(Activity_LatLngList.this,"No Route Data",Toast.LENGTH_SHORT).show();
        }

    }



}
