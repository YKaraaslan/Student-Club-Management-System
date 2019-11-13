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
public class Projects extends Fragment {

    @SuppressLint("RestrictedApi")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.projects, container, false);
        ViewPager mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);
        ((TabLayout) view.findViewById(R.id.tabs)).setupWithViewPager(mViewPager);
        FloatingActionButton addProject = view.findViewById(R.id.add_project);
        if (UserAdapter.getProjeKay() != null && UserAdapter.getProjeKay().equalsIgnoreCase("VAR"))
            addProject.setVisibility(View.VISIBLE);
        addProject.setOnClickListener(v -> startActivity(new Intent(getActivity(), ProjectsAdd.class)));
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ProjectsPageAdapter adapter = new ProjectsPageAdapter(getChildFragmentManager());
        if (UserAdapter.getTumProjeGo() != null) {
            adapter.addFragmentProjects(new ProjectsProject(), getString(R.string.projects_project_list));
        }
        adapter.addFragmentProjects(new ProjectsMyProject(), getString(R.string.projects_my_project_list));
        viewPager.setAdapter(adapter);
    }
}
