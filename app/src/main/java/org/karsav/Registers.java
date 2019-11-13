package org.karsav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Registers extends Fragment {

    ListView list;
    private TextView itemText;
    private String[] items = {};

    @SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registers, container, false);
        ViewPager mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);
        ((TabLayout) view.findViewById(R.id.tabs)).setupWithViewPager(mViewPager);
        view.setOnTouchListener((v, event) -> true);
        FloatingActionButton fab = view.findViewById(R.id.add_member);
        if (UserAdapter.getRefKay().equalsIgnoreCase("VAR") || UserAdapter.getUyeKay().equalsIgnoreCase("VAR"))
            fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(v -> {
            final String addUser = getString(R.string.add_member);
            final String addReference = getString(R.string.add_reference);
            list = new ListView(getContext());
            if (UserAdapter.getUyeKay() != null && UserAdapter.getUyeKay().equalsIgnoreCase("VAR")
                    && UserAdapter.getRefKay() != null && UserAdapter.getRefKay().equalsIgnoreCase("VAR"))
                items = new String[]{addUser, addReference};
            else if (UserAdapter.getUyeKay() != null && UserAdapter.getUyeKay().equalsIgnoreCase("VAR"))
                items = new String[]{addUser};
            else if (UserAdapter.getRefKay() != null && UserAdapter.getRefKay().equalsIgnoreCase("VAR"))
                items = new String[]{addReference};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.list_item, R.id.item_text, items);
            list.setAdapter(adapter);
            list.setOnItemClickListener((parent, view1, position, id) -> {
                ViewGroup viewGroup = (ViewGroup) view1;
                itemText = viewGroup.findViewById(R.id.item_text);
                String selected = itemText.getText().toString();
                if (selected.equalsIgnoreCase(addReference)) {
                    startActivity(new Intent(getActivity(), RegisterReferenceAdd.class));
                } else if (selected.equalsIgnoreCase(addUser)) {
                    startActivity(new Intent(getActivity(), RegisterMemberAdd.class));
                }
            });
            try {
                showDialogListView();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return view;
    }

    public void showDialogListView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setCancelable(true);
        builder.setView(list);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        RegisterPageAdapter adapter = new RegisterPageAdapter(getChildFragmentManager());
        String admin = UserAdapter.getUyeGo();
        String admin2 = UserAdapter.getRefGo();
        String admin3 = UserAdapter.getKayitGo();
        if (admin != null || admin3 != null) {
            adapter.addFragment(new RegisterMember(), getString(R.string.register_user_list));
        }
        if (admin3 != null) {
            adapter.addFragment(new RegisterOfficial(), getString(R.string.register_official_list));
        }
        if (admin2 != null) {
            adapter.addFragment(new RegisterReference(), getString(R.string.register_references));
        }
        viewPager.setAdapter(adapter);
    }
}
