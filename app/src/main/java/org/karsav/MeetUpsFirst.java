package org.karsav;

import android.annotation.SuppressLint;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
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
public class MeetUpsFirst extends Fragment {
    TextView text;
    MeetUpsFirstAdapter adapter;
    EditText search;
    SwipeRefreshLayout swipeRefreshLayout;
    DataBase karsav = new DataBase();
    private List<MeetUpsItem> meetUpsItemList;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_meetupsfirst, container, false);
        text = view.findViewById(R.id.fragment_meetups_text);
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
                        karsav.MeetUps_db();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (meetUpsItemList.isEmpty()) {
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
        ArrayList<MeetUpsItem> filteredList = new ArrayList<>();
        for (MeetUpsItem item : meetUpsItemList) {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void fillExampleList() {
        meetUpsItemList = new ArrayList<>();
        Map name = MeetUpsGet.getName();
        Map exp = MeetUpsGet.getExplanation();
        Map content = MeetUpsGet.getContent();
        Map decisions = MeetUpsGet.getDecisions();
        Map creator = MeetUpsGet.getCreator();
        Map invitees = MeetUpsGet.getInvitees();
        Map meetupId = MeetUpsGet.getId();
        Map dateMap = MeetUpsGet.getDate();
        Map timeMap = MeetUpsGet.getTime();

        if (name != null) {
            for (int i = name.size(); i > 0; i--) {
                String isim = String.valueOf(name.get(i));
                String explanationString = "\n" + exp.get(i);
                String contentString = String.valueOf(content.get(i));
                String decisionsString = String.valueOf(decisions.get(i));
                String creatorString = String.valueOf(creator.get(i));
                String invited = String.valueOf(invitees.get(i));
                String dateString = String.valueOf(dateMap.get(i));
                String timeString = String.valueOf(timeMap.get(i));
                int id = (int) meetupId.get(i);
                meetUpsItemList.add(new MeetUpsItem(R.drawable.ic_karsav, isim, explanationString, contentString, decisionsString, creatorString, invited, dateString, timeString, id));
            }
        }
        if (!meetUpsItemList.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new MeetUpsFirstAdapter(Objects.requireNonNull(getContext()), meetUpsItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
