package roy.anubhav.login.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import roy.anubhav.core.firebaseModels.User;
import roy.anubhav.core.threading.threadpools.NetworkThreadpool;
import roy.anubhav.home.ApplicationClass;
import roy.anubhav.login.R;
import roy.anubhav.login.viewmodel.LoginViewModel;

import static roy.anubhav.core.utils.ActivityHelper.intentTo;
import static roy.anubhav.core.utils.FirebaseHelper.readFromDB;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_CODE_SENT;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_INITIALIZED;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_LOGIN;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_SIGNIN_FAILED;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_SIGNIN_SUCCESS;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_SIGNUP;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_VERIFY_FAILED;
import static roy.anubhav.login.viewmodel.LoginViewModel.STATE_VERIFY_SUCCESS;

public class Login extends AppCompatActivity  implements
        View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private LoginViewModel mViewModel ;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;


    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;

    // [START declare_auth]
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Assign views
        mPhoneNumberViews = findViewById(R.id.phoneAuthFields);
        mSignedInViews = findViewById(R.id.signedInButtons);

        mPhoneNumberField = findViewById(R.id.fieldPhoneNumber);
        mVerificationField = findViewById(R.id.fieldVerificationCode);

        mStartButton = findViewById(R.id.buttonStartVerification);
        mVerifyButton = findViewById(R.id.buttonVerifyPhone);
        mResendButton = findViewById(R.id.buttonResend);

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mViewModel.uiState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateUI(integer,mAuth.getCurrentUser());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        // [END start_phone_auth]


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    private void updateUI(int uiState, FirebaseUser user) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                break;
            case STATE_SIGNUP:
                startActivity(intentTo("signup.activity.SIgnUpDetails"));
                finish();
                break;
            case STATE_LOGIN:
                mViewModel.fetchData(((ApplicationClass)getApplicationContext()).getActivityLogDao());

                startActivity(intentTo("main.activity.MainScreen"));
                finish();

                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);


        } else {
            // Signed in
            mPhoneNumberViews.setVisibility(View.GONE);
            mSignedInViews.setVisibility(View.VISIBLE);

            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStartVerification:
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.buttonVerifyPhone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mViewModel.mVerificationId, code);
                break;
            case R.id.buttonResend:

                if(mPhoneNumberField.getText().toString()!=null && mViewModel.mResendToken!=null)
                    resendVerificationCode(mPhoneNumberField.getText().toString(), mViewModel.mResendToken);
                break;
        }
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            mViewModel.checkUserDetails();
                        } else {

                            mViewModel.uiState.postValue(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    // Initialize phone auth callbacks
    // [START phone_auth_callbacks]
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            Log.d(TAG, "onVerificationCompleted:" + credential);

            // [END_EXCLUDE]
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            // [START_EXCLUDE silent]

            // [END_EXCLUDE]
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            } else if (e instanceof FirebaseTooManyRequestsException) {

            }

            // Show a message and update the UI
            // [START_EXCLUDE]
            mViewModel.uiState.postValue(STATE_VERIFY_FAILED);
            // [END_EXCLUDE]
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mViewModel.mVerificationId = verificationId;
            mViewModel.mResendToken = token;

            // [START_EXCLUDE]
            // Update UI
            mViewModel.uiState.postValue(STATE_CODE_SENT);
            // [END_EXCLUDE]
        }
    };

}
