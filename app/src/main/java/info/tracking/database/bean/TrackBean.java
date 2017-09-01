package info.tracking.database.bean;

/**
 * Created by Asus on 03-05-2017.
 */

public class TrackBean {

    private long id;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLatLng() {
        return comment;
    }

    public void setLatLng(String comment) {
        this.comment = comment;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return comment;
    }
}
