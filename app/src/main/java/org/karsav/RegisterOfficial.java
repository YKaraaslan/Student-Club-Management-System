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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class RegisterOfficial extends Fragment {
    TextView text;
    EditText search;
    RegisterOfficialAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    DataBase karsav = new DataBase();
    private List<RegisterOfficialItem> register2UserItemList;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register2, container, false);
        text = view.findViewById(R.id.fragment_register_text2);
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
                        karsav.RegisterUserDb();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (register2UserItemList.isEmpty()) {
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
        ArrayList<RegisterOfficialItem> filteredList = new ArrayList<>();
        for (RegisterOfficialItem item : register2UserItemList) {
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
        register2UserItemList = new ArrayList<>();
        Map name = RegisterOfficialGet.getName();
        Map surname = RegisterOfficialGet.getSurname();
        Map id = RegisterOfficialGet.getId();

        Map department = RegisterMemberGet.getDepartment();
        Map phone = RegisterMemberGet.getPhone();
        Map mail = RegisterMemberGet.getMail();
        Map studentNo = RegisterMemberGet.getStudentNo();
        Map aviation = RegisterMemberGet.getAviation();
        Map ground = RegisterMemberGet.getGround();
        Map marine = RegisterMemberGet.getMarine();
        Map cyber = RegisterMemberGet.getCyber();

        if (name != null) {
            for (int i = 1; i <= name.size(); i++) {
                int ourId = (int) id.get(i);
                String row = String.valueOf(i);
                String departmentString = String.valueOf(department.get(i));
                String phoneString = String.valueOf(phone.get(i));
                String mailString = String.valueOf(mail.get(i));
                String studentNoString = String.valueOf(studentNo.get(i));
                String aviationString = String.valueOf(aviation.get(i));
                String groundString = String.valueOf(ground.get(i));
                String marineString = String.valueOf(marine.get(i));
                String cyberString = String.valueOf(cyber.get(i));
                String positionString = (String) RegisterOfficialGet.getPosition().get(ourId);
                if (Objects.requireNonNull(positionString).isEmpty())
                    positionString = getString(R.string.member);
                register2UserItemList.add(new RegisterOfficialItem(R.drawable.ic_karsav, String.valueOf(name.get(i)), String.valueOf(surname.get(i)), row, departmentString, phoneString, mailString, studentNoString, aviationString, groundString, marineString, cyberString, positionString, ourId));
            }
        }
        if (!register2UserItemList.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new RegisterOfficialAdapter(Objects.requireNonNull(getContext()), register2UserItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
