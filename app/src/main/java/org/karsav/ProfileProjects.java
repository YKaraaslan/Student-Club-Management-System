package org.karsav;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProfileProjects extends AppCompatActivity {
    EditText search;
    ProjectsMyProjectsAdapter adapter;
    DataBase karsav = new DataBase();
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    private List<ProjectsMyProjectItem> projectsMyProjectItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_projects);
        Slidr.attach(this);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        swipeRefreshLayout = findViewById(R.id.swipe);
        search = findViewById(R.id.search);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

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
                setUpRecyclerView();
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
                        Toast.makeText(getApplicationContext(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                    else {
                        karsav.ProjectsMyProjectDb();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView();
                        if (projectsMyProjectItems.isEmpty()) {
                            search.setVisibility(View.GONE);
                        } else {
                            search.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.refreshed), Toast.LENGTH_SHORT).show();
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
        setUpRecyclerView();
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
        if (projectsMyProjectItems.isEmpty())
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new ProjectsMyProjectsAdapter(getApplicationContext(), projectsMyProjectItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
