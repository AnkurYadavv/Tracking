package info.tracking.appclass;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

/**
 * Created by AnkurPC_Webdior on 10/21/2016.
 */

public class Application_Class extends Application {

    private static Application_Class Instance;
    public static volatile Handler applicationHandler = null;


    @Override
    public void onCreate() {
        super.onCreate();

        Instance=this;
        applicationHandler = new Handler(getInstance().getMainLooper());



    }

    public static Application_Class getInstance()
    {
        return Instance;
    }


    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


    //*******************GA tracker****************************
   /* public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    *//***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     *//*
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    *//***
     * Tracking exception
     *
     * @param e exception to be tracked
     *//*
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    *//***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     *//*
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }*/

}