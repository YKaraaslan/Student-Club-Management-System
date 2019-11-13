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
public class ProjectsMyProject extends Fragment {
    TextView text;
    EditText search;
    ProjectsMyProjectsAdapter adapter;
    DataBase karsav = new DataBase();
    SwipeRefreshLayout swipeRefreshLayout;
    private List<ProjectsMyProjectItem> projectsMyProjectItems;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_projects_myprojects, container, false);
        text = view.findViewById(R.id.no_myprojects);
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
                        karsav.ProjectsMyProjectDb();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (projectsMyProjectItems.isEmpty()) {
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
        ArrayList<ProjectsMyProjectItem> filteredList = new ArrayList<>();
        for (ProjectsMyProjectItem item : projectsMyProjectItems) {
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

    private void fillExampleList() {
        projectsMyProjectItems = new ArrayList<>();
        Map mine = ProjectsMyProjectsGet.getName();
        Map exp = ProjectsMyProjectsGet.getExplanation();
        Map members = ProjectsMyProjectsGet.getMembers();
        Map admins = ProjectsMyProjectsGet.getAdmin();
        Map id = ProjectsMyProjectsGet.getId();

        if (mine != null) {
            for (int i = mine.size(); i > 0; i--) {
                String title = String.valueOf(mine.get(i));
                String desc = String.valueOf(exp.get(i));
                String member = String.valueOf(members.get(i));
                String admin = String.valueOf(admins.get(i));
                String idString = String.valueOf(id.get(i));
                projectsMyProjectItems.add(new ProjectsMyProjectItem(R.drawable.ic_karsav, title, desc, member, admin, idString));
            }
        }
        if (!projectsMyProjectItems.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProjectsMyProjectsAdapter(Objects.requireNonNull(getContext()), projectsMyProjectItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
