package roy.anubhav.main.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import roy.anubhav.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityLogFragment extends Fragment {


    public ActivityLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("Activity");
        return inflater.inflate(R.layout.fragment_activity_log, container, false);
    }

}
