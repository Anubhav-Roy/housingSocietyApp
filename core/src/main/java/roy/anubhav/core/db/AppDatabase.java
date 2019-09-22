package roy.anubhav.core.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import roy.anubhav.core.db.dao.ActivityLogDao;
import roy.anubhav.core.db.entity.ActivityLog;

@Database(entities = {ActivityLog.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    //Exposing the activityLogDao
    public abstract ActivityLogDao activityLogDao();
}
