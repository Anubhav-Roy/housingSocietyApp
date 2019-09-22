package roy.anubhav.home;

import android.app.Application;

import androidx.room.Room;

import roy.anubhav.core.db.AppDatabase;
import roy.anubhav.core.db.dao.ActivityLogDao;

public class ApplicationClass extends Application {

    private ActivityLogDao activityLogDao ;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initializing the ROOM database on application start.
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "yocket").build();

        //Assigning the dao
        activityLogDao = db.activityLogDao();

    }

    /**
     *
     * Provides the ActivityLogDao throughout the app for performing db transaction .
     *
     * @return
     */
    public ActivityLogDao getActivityLogDao(){
        return activityLogDao;
    }



}
