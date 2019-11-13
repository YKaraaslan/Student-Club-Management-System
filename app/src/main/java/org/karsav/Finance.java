package org.karsav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Finance extends Fragment {

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.finance, container, false);
        ViewPager mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);
        ((TabLayout) view.findViewById(R.id.tabs)).setupWithViewPager(mViewPager);
        view.setOnTouchListener((v, event) -> true);
        FloatingActionButton addFinance = view.findViewById(R.id.add_finance);
        if (UserAdapter.getMaliyeKay() != null && UserAdapter.getMaliyeKay().equalsIgnoreCase("VAR"))
            addFinance.setVisibility(View.VISIBLE);
        addFinance.setOnClickListener(v -> startActivity(new Intent(getActivity(), FinanceAdd.class)));
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        FinancePageAdapter adapter = new FinancePageAdapter(getChildFragmentManager());
        adapter.addFragmentFinance(new FinanceFirst(), getString(R.string.finance_report));
        adapter.addFragmentFinance(new FinanceSecond(), getString(R.string.finance_log));
        viewPager.setAdapter(adapter);
    }
}
