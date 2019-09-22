package roy.anubhav.main.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import roy.anubhav.core.db.dao.ActivityLogDao;
import roy.anubhav.core.db.entity.ActivityLog;
import roy.anubhav.core.threading.callables.ActivityLogSender;
import roy.anubhav.core.threading.threadpools.NetworkThreadpool;
import roy.anubhav.home.ApplicationClass;
import roy.anubhav.main.R;
import roy.anubhav.main.adapter.ActivityLogRecyclerViewAdapter;

import static roy.anubhav.core.utils.ActivityHelper.intentTo;
import static roy.anubhav.core.utils.FirebaseHelper.writeToDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityLogFragment extends Fragment {

    private AlertDialog popup;

    private NetworkThreadpool threadpool = NetworkThreadpool.getInstance();

    private ActivityLogDao dao;

    @BindView(R.id.activityLog)
    RecyclerView recyclerView;

    public ActivityLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_activity_log, container, false);

        dao = ((ApplicationClass)getActivity().getApplicationContext()).getActivityLogDao();

        ButterKnife.bind(this,view);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {


        final ActivityLogRecyclerViewAdapter adapter = new ActivityLogRecyclerViewAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        dao.getAllInOrder().observe(this, new Observer<List<ActivityLog>>() {
            @Override
            public void onChanged(List<ActivityLog> activityLogs) {
                adapter.setData(activityLogs);
            }
        });

    }

    @OnClick(R.id.addActivity)
    void addActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.activity_log_poup, null);
        final EditText description = customView.findViewById(R.id.activityDescription);
        Button submit = customView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ActivityLog activityLog = new ActivityLog();
                activityLog.activityDescription = description.getText().toString();
                activityLog.timestamp = System.currentTimeMillis();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                ActivityLogSender sender = new ActivityLogSender(
                        mDatabase.child("users/" + user.getUid()+"/activityLog"),
                        activityLog,
                        dao
                );

                threadpool.addActivityLog(sender);

                popup.dismiss();

            }
        });

        builder.setView(customView);
        builder.create();
        popup = builder.show();
    }


}

