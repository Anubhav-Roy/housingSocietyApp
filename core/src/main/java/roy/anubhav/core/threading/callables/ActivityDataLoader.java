package roy.anubhav.core.threading.callables;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import roy.anubhav.core.db.dao.ActivityLogDao;
import roy.anubhav.core.db.entity.ActivityLog;

import static roy.anubhav.core.utils.FirebaseHelper.readFromDB;

public class ActivityDataLoader implements Callable {

    private String uid;
    private ActivityLogDao dao;



    public ActivityDataLoader(String uid,ActivityLogDao dao){
        this.uid = uid;
        this.dao = dao;
    }

    @Override
    public Object call() throws Exception {

        try{

            if(Thread.interrupted()){throw  new InterruptedException();}

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            List<ActivityLog> activityLogs = new ArrayList<>(0);

            readFromDB(mDatabase.child("users/" + uid + "/activityLog"), new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        activityLogs.add(dataSnapshot1.getValue(ActivityLog.class));
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(activityLogs.size()!=0){
                                dao.insert(activityLogs);
                            }
                        }
                    }).start();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
