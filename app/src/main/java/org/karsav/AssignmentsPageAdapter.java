package org.karsav;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsPageAdapter extends FragmentPagerAdapter {
    private final List<String> getmFragmentTitleList = new ArrayList<>();
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public AssignmentsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragmentMeetUps(Fragment fragment, String title) {
        this.mFragmentList.add(fragment);
        this.getmFragmentTitleList.add(title);
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return getmFragmentTitleList.get(position);
    }

    @NonNull
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    public int getCount() {
        return this.mFragmentList.size();
    }
}
