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
public class RegisterReference extends Fragment {
    TextView text;
    EditText search;
    RegisterReferenceAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    DataBase karsav = new DataBase();
    private List<RegisterReferenceItem> registerUserItemList;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register3, container, false);
        text = view.findViewById(R.id.fragment_register_text3);
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
                        karsav.RegisterReferenceDb();
                        karsav.Disconnect();
                        fillExampleList();
                        setUpRecyclerView(view);
                        if (registerUserItemList.isEmpty()) {
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
        ArrayList<RegisterReferenceItem> filteredList = new ArrayList<>();
        for (RegisterReferenceItem item : registerUserItemList) {
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
        registerUserItemList = new ArrayList<>();
        Map id = RegisterReferenceGet.getId();
        Map name = RegisterReferenceGet.getName();
        Map surname = RegisterReferenceGet.getSurname();
        Map institution = RegisterReferenceGet.getInstitution();
        Map profession = RegisterReferenceGet.getProfession();
        Map phone = RegisterReferenceGet.getPhone();
        Map mail = RegisterReferenceGet.getMail();
        Map explanation = RegisterReferenceGet.getExplanation();
        Map reference = RegisterReferenceGet.getReference();
        if (name != null) {
            for (int i = name.size(); i > 0; i--) {
                String idString = String.valueOf(id.get(i));
                String nameString = String.valueOf(name.get(i));
                String surnameString = String.valueOf(surname.get(i));
                String institutionString = String.valueOf(institution.get(i));
                String professionString = String.valueOf(profession.get(i));
                String phoneString = String.valueOf(phone.get(i));
                String mailString = String.valueOf(mail.get(i));
                String explanationString = String.valueOf(explanation.get(i));
                String referenceString = String.valueOf(reference.get(i));
                registerUserItemList.add(new RegisterReferenceItem(R.drawable.ic_karsav, idString, nameString, surnameString, institutionString, professionString, phoneString, mailString, explanationString, referenceString));
            }
        }
        if (!registerUserItemList.isEmpty())
            text.setVisibility(View.GONE);
        else
            search.setVisibility(View.GONE);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new RegisterReferenceAdapter(Objects.requireNonNull(getContext()), registerUserItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
