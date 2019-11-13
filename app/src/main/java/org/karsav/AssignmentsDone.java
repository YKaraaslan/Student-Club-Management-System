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
public class AssignmentsDone extends Fragment {
    TextView text;
    AssignmentsDoneAdapter adapter;
    EditText search;
    SwipeRefreshLayout swipeRefreshLayout;
    DataBase karsav = new DataBase();
    private List<AssignmentsItem> assignmentsItemList;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_assignments_done, container, false);
        text = view.findViewById(R.id.fragment_assignments_done_text);
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
                        karsav.AssignmentsDone_db();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (assignmentsItemList.isEmpty()) {
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

    @SuppressLint("UseSparseArrays")
    private void fillExampleList() {
        assignmentsItemList = new ArrayList<>();
        Map name = AssignmentsDoneGet.getName();
        Map content = AssignmentsDoneGet.getContent();
        Map responsibleName = AssignmentsDoneGet.getResponsibleNameSurname();
        Map responsibleId = AssignmentsDoneGet.getResponsibleId();
        Map creator = AssignmentsDoneGet.getFromWhom();
        Map dateOfIssue = AssignmentsDoneGet.getDateOfIssue();
        Map dueDate = AssignmentsDoneGet.getDueDate();
        Map dateSaved = AssignmentsDoneGet.getDateSaved();
        Map returns = AssignmentsDoneGet.getReport();
        Map id = AssignmentsDoneGet.getId();
        Map category = AssignmentsDoneGet.getCategory();
        if (name != null) {
            for (int i = name.size(); i > 0; i--) {
                String title = String.valueOf(name.get(i));
                if (!title.isEmpty()) {
                    String text = String.valueOf(content.get(i)).trim();
                    String responsibleNameString = String.valueOf(responsibleName.get(i)).trim();
                    String create = String.valueOf(creator.get(i));
                    String date_of_issue = String.valueOf(dateOfIssue.get(i));
                    String due_date = String.valueOf(dueDate.get(i));
                    String date_saved = String.valueOf(dateSaved.get(i));
                    String ourReturns = String.valueOf(returns.get(i));
                    String categoryString = String.valueOf(category.get(i));
                    int responsibleIdInt = Integer.valueOf(String.valueOf(responsibleId.get(i)));
                    int idInt = Integer.valueOf(String.valueOf(id.get(i)));
                    assignmentsItemList.add(new AssignmentsItem(R.drawable.ic_karsav, title, text, create, date_of_issue, due_date, date_saved, ourReturns, idInt, responsibleNameString, responsibleIdInt, categoryString));
                }
            }
        }
        if (!assignmentsItemList.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new AssignmentsDoneAdapter(Objects.requireNonNull(getContext()), assignmentsItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
