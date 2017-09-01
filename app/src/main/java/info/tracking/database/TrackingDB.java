package info.tracking.database;

/**
 * Created by Asus on 03-05-2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrackingDB extends SQLiteOpenHelper {

    public static final String TABLE_TRACKING = "tacking";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRACK = "latLng";

    private static final String DATABASE_NAME = "tacking.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRACKING + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TRACK
            + " text not null);";

    public TrackingDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TrackingDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKING);
        onCreate(db);
    }

}