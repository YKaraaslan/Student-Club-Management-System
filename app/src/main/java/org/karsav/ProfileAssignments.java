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
public class ProfileAssignments extends AppCompatActivity {

    Toolbar toolbar;
    AssignmentsMineAdapter adapter;
    EditText search;
    SwipeRefreshLayout swipeRefreshLayout;
    DataBase karsav = new DataBase();
    private List<AssignmentsItem> assignmentsItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_assignments);
        Slidr.attach(this);
        init();
    }

    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
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
                        karsav.AssignmentsMine_db();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView();
                        if (assignmentsItemList.isEmpty()) {
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
        ArrayList<AssignmentsItem> filteredList = new ArrayList<>();
        for (AssignmentsItem item : assignmentsItemList) {
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
        assignmentsItemList = new ArrayList<>();
        Map name = AssignmentsMineGet.getName();
        Map content = AssignmentsMineGet.getContent();
        Map creator = AssignmentsMineGet.getFromWhom();
        Map dateOfIssue = AssignmentsMineGet.getDateOfIssue();
        Map dueDate = AssignmentsMineGet.getDueDate();
        Map dateSaved = AssignmentsMineGet.getDateSaved();
        Map returns = AssignmentsMineGet.getReport();
        Map id = AssignmentsMineGet.getId();
        Map category = AssignmentsMineGet.getCategory();
        Map responsibleName = AssignmentsMineGet.getResponsibleNameSurname();
        Map responsibleId = AssignmentsMineGet.getResponsibleId();

        if (name != null) {
            for (int i = name.size(); i > 0; i--) {
                String title = String.valueOf(name.get(i));
                if (!title.isEmpty()) {
                    String text = String.valueOf(content.get(i)).trim();
                    String create = String.valueOf(creator.get(i));
                    String date_of_issue = String.valueOf(dateOfIssue.get(i));
                    String due_date = String.valueOf(dueDate.get(i));
                    String date_saved = String.valueOf(dateSaved.get(i));
                    String ourReturns = String.valueOf(returns.get(i));
                    String categoryString = String.valueOf(category.get(i));
                    String responsibleNameString = String.valueOf(responsibleName.get(i)).trim();
                    int responsibleIdInt = Integer.valueOf(String.valueOf(responsibleId.get(i)));
                    int idInt = Integer.valueOf(String.valueOf(id.get(i)));
                    assignmentsItemList.add(new AssignmentsItem(R.drawable.ic_karsav, title, text, create, date_of_issue, due_date, date_saved, ourReturns, idInt, responsibleNameString, responsibleIdInt, categoryString));
                }
            }
        }
        if (assignmentsItemList.isEmpty())
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new AssignmentsMineAdapter(getApplicationContext(), assignmentsItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
