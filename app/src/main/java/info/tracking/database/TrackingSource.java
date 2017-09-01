package info.tracking.database;

/**
 * Created by Asus on 03-05-2017.
 */

 import android.content.ContentValues;
 import android.content.Context;
 import android.database.Cursor;
 import android.database.SQLException;
 import android.database.sqlite.SQLiteDatabase;
 import java.util.ArrayList;
 import java.util.List;
 import info.tracking.database.bean.TrackBean;

public class TrackingSource {

    // Database fields
    private SQLiteDatabase database;
    private TrackingDB dbHelper;
    private String[] allColumns = { TrackingDB.COLUMN_ID,
            TrackingDB.COLUMN_TRACK };

    public TrackingSource(Context context) {
        dbHelper = new TrackingDB(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public TrackBean storeLatLng(String latLng) {
        ContentValues values = new ContentValues();
        values.put(TrackingDB.COLUMN_TRACK, latLng);
        long insertId = database.insert(TrackingDB.TABLE_TRACKING, null,
                values);
        Cursor cursor = database.query(TrackingDB.TABLE_TRACKING,
                allColumns, TrackingDB.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TrackBean newLatLng = cursorToLatLng(cursor);
        cursor.close();
        return newLatLng;
    }


    public List<TrackBean> getAllLatLng() {
        List<TrackBean> latLngs = new ArrayList<TrackBean>();

        Cursor cursor = database.query(TrackingDB.TABLE_TRACKING,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TrackBean latLng = cursorToLatLng(cursor);
            latLngs.add(latLng);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return latLngs;
    }

    private TrackBean cursorToLatLng(Cursor cursor) {
        TrackBean latlng = new TrackBean();
        latlng.setId(cursor.getLong(0));
        latlng.setLatLng(cursor.getString(1));
        return latlng;
    }

    public void removeAll()
    {
        // remove all lat lng from DB
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TrackingDB.TABLE_TRACKING, null, null);

    }

}