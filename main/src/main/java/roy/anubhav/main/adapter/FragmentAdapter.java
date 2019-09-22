package roy.anubhav.main.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import roy.anubhav.main.fragments.ActivityLogFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private ActivityLogFragment mActivityLogFragment;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                if (mActivityLogFragment == null) {
                    mActivityLogFragment = new ActivityLogFragment();
                }
                return mActivityLogFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 1;
    }
}