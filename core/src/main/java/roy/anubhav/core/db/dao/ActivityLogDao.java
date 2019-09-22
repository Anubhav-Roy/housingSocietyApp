package roy.anubhav.core.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import roy.anubhav.core.db.entity.ActivityLog;


@Dao
public interface ActivityLogDao {

    //Used to show the logs of the user
    @Query("SELECT * from activityLogs ORDER BY timestamp DESC")
    LiveData<List<ActivityLog>> getAllInOrder();

    //Used when the user logs in and the data from the server must
    //be stored in the local device
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<ActivityLog> activityLogs);

    //Used when the user adds a new activity log
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(ActivityLog activityLog);

}
