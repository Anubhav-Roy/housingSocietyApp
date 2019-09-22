package roy.anubhav.signup.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import roy.anubhav.core.firebaseModels.User;
import roy.anubhav.signup.R;

import static roy.anubhav.core.utils.ActivityHelper.intentTo;
import static roy.anubhav.core.utils.FirebaseHelper.writeToDB;

public class SIgnUpDetails extends AppCompatActivity {

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.userName)
    EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.submit)
    void submitDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        User user1 = new User();
        user1.setUsername(userName.getText().toString());
        user1.setAddress(address.getText().toString());

        writeToDB(mDatabase.child("users/" + user.getUid()), user1, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                startActivity(intentTo("main.activity.MainScreen"));
                finish();
            }
        });
    }
}
