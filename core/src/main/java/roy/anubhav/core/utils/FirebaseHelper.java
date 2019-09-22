package roy.anubhav.core.utils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 *
 * Helper class for firebase transaction to make sure that we don't
 * leak listeners .
 *
 */
public class FirebaseHelper {

    public static void writeToDB(DatabaseReference ref, Object object, OnCompleteListener onCompleteListener){
        ref.setValue(object).addOnCompleteListener(onCompleteListener);
    }

    public static void pushToDB(DatabaseReference ref, Object object, OnCompleteListener onCompleteListener){
        String key = ref.push().getKey();
        ref = ref.child(key);
        ref.setValue(object).addOnCompleteListener(onCompleteListener);
    }

    public static void readFromDB(DatabaseReference ref,ValueEventListener listener){
        ref.addListenerForSingleValueEvent(listener);
    }
}
