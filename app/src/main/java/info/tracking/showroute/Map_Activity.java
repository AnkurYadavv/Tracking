package info.tracking.showroute;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import info.tracking.R;
import info.tracking.database.TrackingSource;
import info.tracking.database.bean.TrackBean;
import info.tracking.showroute.fragment.TouchSupportMapFragment;

/**
 * Created by Asus on 03-05-2017.
 */

public class Map_Activity extends FragmentActivity implements OnMapReadyCallback {

    TouchSupportMapFragment mapFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        mapFragment= (TouchSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(Map_Activity.this);
        mapTouchListner();

    }


    GoogleMap mMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

        }

        mapZoomOnCurrentLocation();
    }

    TrackingSource datasource;
    int counter=0;
    List<TrackBean> values;
    private void mapZoomOnCurrentLocation() {

        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {


                                datasource = new TrackingSource(getApplicationContext());
                                datasource.open();
                                values = datasource.getAllLatLng();

                                if (values.size()>0){

                                    for (int i=0;i<1;i++){

                                        counter++;
                                        String startingLatLang=values.get(i).getLatLng();
                                        String endingLatLang=values.get(counter).getLatLng();
                                        String sLatLngs[] =startingLatLang.split(",");
                                        String eLatLngs[] =endingLatLang.split(",");
                                        callRouteAPI(new LatLng(Double.parseDouble(sLatLngs[0]),Double.parseDouble(sLatLngs[1])),new LatLng(Double.parseDouble(eLatLngs[0]),Double.parseDouble(eLatLngs[1])));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(sLatLngs[0]),Double.parseDouble(sLatLngs[1]))));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(sLatLngs[0]),Double.parseDouble(sLatLngs[1])), 12.5f));

                                        Marker startMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(sLatLngs[0]),Double.parseDouble(sLatLngs[1])))
                                                .title("Starting point")
                                                .snippet("TRACKING"));

                                        String endingPoint=values.get(values.size()-1).getLatLng();
                                        String endPointLatLng[] =endingPoint.split(",");

                                        Marker endMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(endPointLatLng[0]),Double.parseDouble(endPointLatLng[1])))
                                                .title("Ending point")
                                                .snippet("TRACKING"));

                                    }


                                }else {
                                    Toast.makeText(Map_Activity.this,"No Route Data",Toast.LENGTH_SHORT).show();
                                }

                            }catch (NullPointerException e){

                            }

                        }
                    });


                } catch (Exception e) {

                }
            }

        };

        // start thread
        background.start();

    }

    private void mapTouchListner() {

        mapFragment.setNonConsumingTouchListener(new TouchSupportMapFragment.NonConsumingTouchListener() {
            @Override
            public boolean onTouch(MotionEvent motionEvent) {
                return true;
            }
        });
    }


    //map route directions
    private void callRouteAPI(LatLng slatLang,LatLng elatLang) {

        String url = getDirectionsUrl(slatLang,elatLang);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        Log.w("route_url",url);

        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if(result.size()<1){
                //Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    addMarkerToMap(position);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLACK);
            }

            // tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
            // Drawing polyline in the Google Map for the i-th route

            mMap.addPolyline(lineOptions);

            againRouteDraw();

        }
    }

    private void againRouteDraw() {

        try {

            for (int i=counter;i<counter+1;i++){


                counter++;
                String startingLatLang=values.get(i).getLatLng();
                String endingLatLang=values.get(counter).getLatLng();
                String sLatLngs[] =startingLatLang.split(",");
                String eLatLngs[] =endingLatLang.split(",");
                callRouteAPI(new LatLng(Double.parseDouble(sLatLngs[0]),Double.parseDouble(sLatLngs[1])),new LatLng(Double.parseDouble(eLatLngs[0]),Double.parseDouble(eLatLngs[1])));

            }



        }catch (Exception e){

        }

    }

    List<LatLng> routeList=new ArrayList<>();
    private void addMarkerToMap(LatLng position) {

        routeList.add(position);

    }

    private void splitLatLng(String latLang) {



    }

    /** A method to download json data from url */
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
