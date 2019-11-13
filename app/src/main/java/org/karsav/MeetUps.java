package org.karsav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MeetUps extends Fragment {

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meet_ups, container, false);
        ViewPager mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);
        ((TabLayout) view.findViewById(R.id.tabs)).setupWithViewPager(mViewPager);
        view.setOnTouchListener((v, event) -> true);
        FloatingActionButton addMeetUps = view.findViewById(R.id.add_meetups);
        if (UserAdapter.getTopKay() != null && UserAdapter.getTopKay().equalsIgnoreCase("VAR")) {
            addMeetUps.setVisibility(View.VISIBLE);
        }
        addMeetUps.setOnClickListener(v -> startActivity(new Intent(getActivity(), MeetUpsAdd.class)));
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        MeetUpsPageAdapter adapter = new MeetUpsPageAdapter(getChildFragmentManager());
        if (UserAdapter.getTumTopGo() != null) {
            adapter.addFragmentMeetUps(new MeetUpsFirst(), getString(R.string.tab_meetup_text_2));
        }
        if (UserAdapter.getTopGo() != null) {
            adapter.addFragmentMeetUps(new MeetUpsSecond(), getString(R.string.tab_meetup_text_3));
        }
        viewPager.setAdapter(adapter);
    }
}
