package roy.anubhav.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import roy.anubhav.core.utils.FirebaseHelper;

import static roy.anubhav.core.utils.ActivityHelper.intentTo;
import static roy.anubhav.core.utils.FirebaseHelper.readFromDB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       navigation();
        Glide.with(this)
                .load(R.drawable.icon)
                .into((ImageView)findViewById(R.id.logo));
    }

    private void navigation() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //Checking if the user is already logged in .
        if(mAuth.getCurrentUser()==null) {
            startActivity(intentTo("login.activity.Login"));
            finish();
            return;
        }



        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        readFromDB(mDatabase.child("users/" + mAuth.getCurrentUser().getUid()), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null){
                    //This condition indicates the user has successfully registered .
                    //Sending him to the main screen
                    startActivity(intentTo("main.activity.MainScreen"));
                }else{
                    //This condition indicates the user hasn't signed up on the app.
                    //Sending him to the sign up screen.
                    startActivity(intentTo("signup.activity.SIgnUpDetails"));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
