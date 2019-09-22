package roy.anubhav.core.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activityLogs")
public class ActivityLog {

    //Message to be sent to the guard
    @ColumnInfo(name = "activityDescription")
    public String activityDescription;

    //Time stamp of the activity
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    public long timestamp;

}
