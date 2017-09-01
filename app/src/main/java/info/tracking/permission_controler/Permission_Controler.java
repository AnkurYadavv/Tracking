package info.tracking.permission_controler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Asus on 18-10-2016.
 */


public class Permission_Controler {

    Activity context;
    public Permission_Controler(Activity context) {

        this.context=context;
    }

    public boolean getPermission(Activity context) {

        boolean permission=false;

        if(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)  && PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA ) && PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) && PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)){

            permission=true;
        }else{
            requestPermission(context);
        }

        return permission;
    }

    public static int  REQUEST_WRITE_EXTERNAL_STORAGE=1;
    private static void requestPermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS},REQUEST_WRITE_EXTERNAL_STORAGE);
        }else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public boolean getLocationPermission(Activity activity) {
        boolean permission=false;

        if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)){

            permission=true;
        }else{
            requestLocationPermission(context);
        }

        return permission;
    }


    public static int  REQUEST_LOCATION=1;
    private static void requestLocationPermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions((Activity) context,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_LOCATION);

        }
        else
        {

            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity)context,
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_LOCATION);


        }
        }


    public static boolean location_permission=false;
    public boolean getOnlyLocationPermission(Activity activity) {


        if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)){

            location_permission=true;
        }else{
            requestOnlyLocationPermission(activity);
        }

        return location_permission;
    }


    public static int  REQUEST_ONLY_LOCATION=1;
    private static void requestOnlyLocationPermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_ONLY_LOCATION);

        }
        else
        {

            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_ONLY_LOCATION);


        }
    }

    public static boolean camera_permission=false;
    public boolean getCameraPhotoPermission(Activity activity) {


        if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)  && PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA )){

            camera_permission=true;
        }else{
            requestOnlyCameraPermission(activity);
        }

        return camera_permission;

    }

    public static int  REQUEST_ONLY_CAMERA=1;
    private static void requestOnlyCameraPermission(final Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},REQUEST_ONLY_CAMERA);

        }
        else
        {

            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_ONLY_CAMERA);


        }
    }




    public static boolean contact_permission=false;
    public boolean getContactPermission(Activity activity) {


        if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)){

            contact_permission=true;
        }else{
            requestContactPermission(activity);
        }

        return contact_permission;

    }

    public static int  REQUEST_ONLY_CONTACT=1;
    private static void requestContactPermission(final Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.READ_CONTACTS)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_CONTACTS},REQUEST_ONLY_CONTACT);

        }
        else
        {

            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_ONLY_CONTACT);


        }
    }


}
