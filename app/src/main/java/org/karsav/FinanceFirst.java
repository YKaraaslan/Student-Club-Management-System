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
public class FinanceFirst extends Fragment {
    TextView text;
    FinanceFirstAdapter adapter;
    EditText search;
    SwipeRefreshLayout swipeRefreshLayout;
    DataBase karsav = new DataBase();
    private List<FinanceItem> financeItemList;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.finance_first, container, false);
        text = view.findViewById(R.id.finance_nothing_found);
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
                        karsav.FinanceList();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (financeItemList.isEmpty()) {
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
        ArrayList<FinanceItem> filteredList = new ArrayList<>();
        for (FinanceItem item : financeItemList) {
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
        this.financeItemList = new ArrayList<>();
        Map tour = FinanceGet.getTour();
        Map content = FinanceGet.getExplanation();
        Map amount = FinanceGet.getAmount();
        Map responsible = FinanceGet.getResponsible();
        Map id = FinanceGet.getId();
        if (tour != null) {
            for (int i = tour.size(); i > 0; i--) {
                String title = String.valueOf(tour.get(i)).trim() + " (" + String.valueOf(amount.get(i)).trim() + " TL)";
                String tourString = String.valueOf(tour.get(i));
                String explanationString = "\n" + String.valueOf(content.get(i)).trim();
                String creatorString = String.valueOf(responsible.get(i));
                String idString = String.valueOf(id.get(i));
                String amountString = String.valueOf(amount.get(i));
                financeItemList.add(new FinanceItem(R.drawable.ic_karsav, title, explanationString, creatorString, idString, amountString, tourString));
            }
        }
        if (!financeItemList.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new FinanceFirstAdapter(Objects.requireNonNull(getContext()), financeItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
