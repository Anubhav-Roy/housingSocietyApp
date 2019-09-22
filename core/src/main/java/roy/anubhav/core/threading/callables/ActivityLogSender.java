package roy.anubhav.core.threading.callables;

import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Callable;

import roy.anubhav.core.db.dao.ActivityLogDao;
import roy.anubhav.core.db.entity.ActivityLog;

import static roy.anubhav.core.utils.FirebaseHelper.pushToDB;
import static roy.anubhav.core.utils.FirebaseHelper.writeToDB;


/**
 *
 * Used to send data to the server as well as store in the local device asynchronously .
 *
 */
public class ActivityLogSender implements Callable {

    //Required to store in the local db
    private ActivityLogDao dao;
    //Reference to the position where it must be store on the server.
    private DatabaseReference ref;
    //The object to be stored.
    private ActivityLog activityLog;

    public ActivityLogSender(DatabaseReference ref, ActivityLog activityLog, ActivityLogDao dao){
        this.dao = dao;
        this.activityLog = activityLog;
        this.ref = ref;
    }

    @Override
    public Object call() throws Exception {

        try {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            //Sending data to the server.
            pushToDB(ref, activityLog,null);

            //Storing data in the local device.
            dao.insert(activityLog);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
