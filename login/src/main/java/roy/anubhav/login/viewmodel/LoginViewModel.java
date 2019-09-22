package roy.anubhav.login.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import roy.anubhav.core.db.dao.ActivityLogDao;
import roy.anubhav.core.threading.callables.ActivityDataLoader;
import roy.anubhav.core.threading.threadpools.NetworkThreadpool;

import static roy.anubhav.core.utils.FirebaseHelper.readFromDB;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    public MutableLiveData<Integer> uiState = new MutableLiveData<>();

    private NetworkThreadpool threadpool = NetworkThreadpool.getInstance();

    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_CODE_SENT = 2;
    public static final int STATE_VERIFY_FAILED = 3;
    public static final int STATE_VERIFY_SUCCESS = 4;
    public static final int STATE_SIGNIN_FAILED = 5;
    public static final int STATE_SIGNIN_SUCCESS = 6;
    public static final int STATE_SIGNUP = 7;
    public static final int STATE_LOGIN = 8;

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public String mVerificationId;

    public PhoneAuthProvider.ForceResendingToken mResendToken;


    public LoginViewModel(){}


    /**
     *
     * Checks if the user has already signed up or is creating a new account.
     *
     */
    public void checkUserDetails(){

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        readFromDB(mDatabase.child("users/" + mAuth.getCurrentUser().getUid()), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    uiState.postValue(STATE_LOGIN);
                }else{

                    uiState.postValue(STATE_SIGNUP);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * Fetches data from server , previously added activities
     *
     * @param dao   Required for making db transactions
     */
    public void fetchData(ActivityLogDao dao) {
        ActivityDataLoader dataLoader = new ActivityDataLoader(mAuth.getCurrentUser().getUid(),dao);
        threadpool.fetchDataFromServer(dataLoader);
    }
}
