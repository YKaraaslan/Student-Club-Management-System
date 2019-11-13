package org.karsav;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProjectsProject extends Fragment {
    String explanationString;
    TextView text;
    EditText search;
    ProjectsAdapter adapter;
    DataBase karsav = new DataBase();
    SwipeRefreshLayout swipeRefreshLayout;
    private List<ProjectsItem> projectsItemList;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_projects_projects, container, false);
        text = view.findViewById(R.id.no_projects);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        search = view.findViewById(R.id.search);
        search.setOnTouchListener((v, event) -> {
            search.setClickable(true);
            search.setFocusable(true);
            search.setFocusableInTouchMode(true);
            return false;
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setUpRecyclerView(view);
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        try {
            swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                try {
                    if (karsav.Begin())
                        Toast.makeText(getContext(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                    else {
                        karsav.Projects_db();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (projectsItemList.isEmpty()) {
                            text.setVisibility(View.VISIBLE);
                            search.setVisibility(View.GONE);
                        } else {
                            text.setVisibility(View.GONE);
                            search.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getContext(), getString(R.string.refreshed), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }, 3000));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        fillExampleList();
        setUpRecyclerView(view);
        return view;
    }

    private void filter(String text) {
        ArrayList<ProjectsItem> filteredList = new ArrayList<>();
        for (ProjectsItem item : projectsItemList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        try {

            adapter.filterList(filteredList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void fillExampleList() {
        projectsItemList = new ArrayList<>();
        Map name = ProjectsGet.getName();
        Map explanation = ProjectsGet.getExplanation();
        Map members = ProjectsGet.getMembers();
        Map admins = ProjectsGet.getAdmin();
        Map id = ProjectsGet.getId();
        if (name != null) {
            for (int i = name.size(); i > 0; i--) {
                String nameString = String.valueOf(name.get(i));
                if (nameString.isEmpty() || nameString.equalsIgnoreCase("null"))
                    continue;
                explanationString = String.valueOf(explanation.get(i));
                String memberString = String.valueOf(members.get(i));
                String adminString = String.valueOf(admins.get(i));
                String idString = String.valueOf(id.get(i));
                projectsItemList.add(new ProjectsItem(R.drawable.ic_karsav, nameString, explanationString, memberString, adminString, idString));
            }
        }
        if (!projectsItemList.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProjectsAdapter(Objects.requireNonNull(getContext()), projectsItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
