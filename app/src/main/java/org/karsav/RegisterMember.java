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
public class RegisterMember extends Fragment {
    TextView text;
    EditText search;
    RegisterMemberAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    DataBase karsav = new DataBase();
    private List<RegisterMemberItem> register1UserItemList;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register1, container, false);
        text = view.findViewById(R.id.fragment_register_text);
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
                    if (karsav.Begin()) {
                        Toast.makeText(getContext(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        karsav.RegisterUserDb();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (register1UserItemList.isEmpty()) {
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
            }, 5000));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        fillExampleList();
        setUpRecyclerView(view);
        return view;
    }

    private void filter(String text) {
        ArrayList<RegisterMemberItem> filteredList = new ArrayList<>();
        for (RegisterMemberItem item : register1UserItemList) {
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
        register1UserItemList = new ArrayList<>();
        Map id = RegisterMemberGet.getId();
        Map name = RegisterMemberGet.getName();
        Map surname = RegisterMemberGet.getSurname();
        Map department = RegisterMemberGet.getDepartment();
        Map phone = RegisterMemberGet.getPhone();
        Map mail = RegisterMemberGet.getMail();
        Map studentNo = RegisterMemberGet.getStudentNo();
        Map aviation = RegisterMemberGet.getAviation();
        Map ground = RegisterMemberGet.getGround();
        Map marine = RegisterMemberGet.getMarine();
        Map cyber = RegisterMemberGet.getCyber();
        Map explanation = RegisterMemberGet.getExplanation();

        if (name != null) {
            for (int i = 1; i <= name.size(); i++) {
                String idString = String.valueOf(id.get(i));
                String nameString = String.valueOf(name.get(i));
                String surnameString = String.valueOf(surname.get(i));
                String row = String.valueOf(i);
                String departmentString = String.valueOf(department.get(i));
                String telephone = String.valueOf(phone.get(i));
                String mailString = String.valueOf(mail.get(i));
                String studentNoString = String.valueOf(studentNo.get(i));
                String aviationString = String.valueOf(aviation.get(i));
                String groundString = String.valueOf(ground.get(i));
                String marineString = String.valueOf(marine.get(i));
                String cyberString = String.valueOf(cyber.get(i));
                String explanationString = String.valueOf(explanation.get(i));

                if (explanation.isEmpty() || explanationString.equalsIgnoreCase("")) {
                    explanationString = getString(R.string.member);
                }
                register1UserItemList.add(new RegisterMemberItem(R.drawable.ic_karsav, nameString, surnameString, row, departmentString,
                        telephone, mailString, studentNoString, aviationString, groundString, marineString, cyberString, explanationString, idString));
            }
        }
        if (!register1UserItemList.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new RegisterMemberAdapter(Objects.requireNonNull(getContext()), register1UserItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
