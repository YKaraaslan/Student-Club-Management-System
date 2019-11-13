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
public class Assignments extends Fragment {

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assignments, container, false);
        ViewPager mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);
        ((TabLayout) view.findViewById(R.id.tabs)).setupWithViewPager(mViewPager);
        view.setOnTouchListener((v, event) -> true);
        FloatingActionButton addAssignments = view.findViewById(R.id.add_assignments);
        if (UserAdapter.getGorevKay() != null && UserAdapter.getGorevKay().equalsIgnoreCase("VAR"))
            addAssignments.setVisibility(View.VISIBLE);
        addAssignments.setOnClickListener(v -> startActivity(new Intent(getActivity(), AssignmentsAdd.class)));
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        AssignmentsPageAdapter adapter = new AssignmentsPageAdapter(getChildFragmentManager());
        if (UserAdapter.getGrvGo() != null) {
            adapter.addFragmentMeetUps(new AssignmentsNew(), getString(R.string.assignment_all));
        }
        if (UserAdapter.getTumGorevGo() != null) {
            adapter.addFragmentMeetUps(new AssignmentsDone(), getString(R.string.assignment_done));
        }
        adapter.addFragmentMeetUps(new AssignmentsMine(), getString(R.string.assignment_mine));
        viewPager.setAdapter(adapter);
    }
}
