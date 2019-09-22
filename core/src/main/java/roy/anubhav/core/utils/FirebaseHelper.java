package roy.anubhav.core.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {


    public static void writeToDB(DatabaseReference ref, Object object, OnCompleteListener onCompleteListener){
        ref.setValue(object).addOnCompleteListener(onCompleteListener);
    }

    public static void readFromDB(DatabaseReference ref,ValueEventListener listener){
        ref.addListenerForSingleValueEvent(listener);
    }
}
