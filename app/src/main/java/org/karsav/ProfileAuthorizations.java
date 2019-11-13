package org.karsav;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProfileAuthorizations extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    LinearLayout kayit_go, proje_go, top_go, gorev_go, uye_go, uye_kay, grv_go, grv_kay, ref_go, ref_kay, tum_proje_go, proje_kay;
    LinearLayout tum_top_go, top_kay, tum_gorev_go, gorev_kay, maliye_go, maliye_kay;
    View kayit_goView, proje_goView, top_goView, gorev_goView, uye_goView, uye_kayView, grv_goView, grv_kayView, ref_goView, ref_kayView, tum_proje_goView, proje_kayView;
    View tum_top_goView, top_kayView, tum_gorev_goView, gorev_kayView, maliye_goView, maliye_kayView;
    TextView karsav, registerDisplay, projectsDisplay, displayMeetups, displayAssignments, displayMembers, displayOfficial, displayReference;
    TextView displayAllProjects, displayAllMeetups, displayAllAssignments, displayFinance, registerMember, registerOfficial, registerReference;
    TextView registerProject, registerMeetups, registerAssignments, registerFinance;
    Toolbar toolbar;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorizations);
        Slidr.attach(this);
        init();
    }

    private void init() {
        kayit_go = findViewById(R.id.kayit_go);
        proje_go = findViewById(R.id.proje_go);
        top_go = findViewById(R.id.top_go);
        gorev_go = findViewById(R.id.gorev_go);
        uye_go = findViewById(R.id.uye_go);
        uye_kay = findViewById(R.id.uye_kay);
        grv_go = findViewById(R.id.grv_go);
        grv_kay = findViewById(R.id.grv_kay);
        ref_go = findViewById(R.id.ref_go);
        ref_kay = findViewById(R.id.ref_kay);
        tum_proje_go = findViewById(R.id.tum_proje_go);
        proje_kay = findViewById(R.id.proje_kay);
        tum_top_go = findViewById(R.id.tum_top_go);
        top_kay = findViewById(R.id.top_kay);
        tum_gorev_go = findViewById(R.id.tum_gorev_go);
        gorev_kay = findViewById(R.id.gorev_kay);
        maliye_go = findViewById(R.id.maliye_go);
        maliye_kay = findViewById(R.id.maliye_kay);

        karsav = findViewById(R.id.karsav);

        kayit_goView = findViewById(R.id.kayit_goView);
        proje_goView = findViewById(R.id.proje_goView);
        top_goView = findViewById(R.id.top_goView);
        gorev_goView = findViewById(R.id.gorev_goView);
        uye_goView = findViewById(R.id.uye_goView);
        uye_kayView = findViewById(R.id.uye_kayView);
        grv_goView = findViewById(R.id.grv_goView);
        grv_kayView = findViewById(R.id.grv_kayView);
        ref_goView = findViewById(R.id.ref_goView);
        ref_kayView = findViewById(R.id.ref_kayView);
        tum_proje_goView = findViewById(R.id.tum_proje_goView);
        proje_kayView = findViewById(R.id.proje_kayView);
        tum_top_goView = findViewById(R.id.tum_top_goView);
        top_kayView = findViewById(R.id.top_kayView);
        tum_gorev_goView = findViewById(R.id.tum_gorev_goView);
        gorev_kayView = findViewById(R.id.gorev_kayView);
        maliye_goView = findViewById(R.id.maliye_goView);
        maliye_kayView = findViewById(R.id.maliye_kayView);

        karsav = findViewById(R.id.karsav);
        registerDisplay = findViewById(R.id.registerDisplay);
        projectsDisplay = findViewById(R.id.projectsDisplay);
        displayMeetups = findViewById(R.id.displayMeetups);
        displayAssignments = findViewById(R.id.displayAssignments);
        displayMembers = findViewById(R.id.displayMembers);
        displayOfficial = findViewById(R.id.displayOfficial);
        displayReference = findViewById(R.id.displayReference);
        displayAllProjects = findViewById(R.id.displayAllProjects);
        displayAllMeetups = findViewById(R.id.displayAllMeetups);
        displayAllAssignments = findViewById(R.id.displayAllAssignments);
        displayFinance = findViewById(R.id.displayFinance);
        registerMember = findViewById(R.id.registerMember);
        registerOfficial = findViewById(R.id.registerOfficial);
        registerReference = findViewById(R.id.registerReference);
        registerProject = findViewById(R.id.registerProject);
        registerMeetups = findViewById(R.id.registerMeetups);
        registerAssignments = findViewById(R.id.registerAssignments);
        registerFinance = findViewById(R.id.registerFinance);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        karsav.setTextSize(textSize + 15);
        registerDisplay.setTextSize(textSize);
        projectsDisplay.setTextSize(textSize);
        displayMeetups.setTextSize(textSize);
        displayAssignments.setTextSize(textSize);
        displayMembers.setTextSize(textSize);
        displayOfficial.setTextSize(textSize);
        displayReference.setTextSize(textSize);
        displayAllProjects.setTextSize(textSize);
        displayAllMeetups.setTextSize(textSize);
        displayAllAssignments.setTextSize(textSize);
        displayFinance.setTextSize(textSize);
        registerMember.setTextSize(textSize);
        registerOfficial.setTextSize(textSize);
        registerReference.setTextSize(textSize);
        registerProject.setTextSize(textSize);
        registerMeetups.setTextSize(textSize);
        registerAssignments.setTextSize(textSize);
        registerFinance.setTextSize(textSize);

        if (UserAdapter.getKayitGo() != null && UserAdapter.getKayitGo().equalsIgnoreCase("VAR")) {
            kayit_go.setVisibility(View.VISIBLE);
            kayit_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getProjeGo() != null && UserAdapter.getProjeGo().equalsIgnoreCase("VAR")) {
            proje_go.setVisibility(View.VISIBLE);
            proje_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getTopGo() != null && UserAdapter.getTopGo().equalsIgnoreCase("VAR")) {
            top_go.setVisibility(View.VISIBLE);
            top_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getGorevGo() != null && UserAdapter.getGorevGo().equalsIgnoreCase("VAR")) {
            gorev_go.setVisibility(View.VISIBLE);
            gorev_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getUyeGo() != null && UserAdapter.getUyeGo().equalsIgnoreCase("VAR")) {
            uye_go.setVisibility(View.VISIBLE);
            uye_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getUyeKay() != null && UserAdapter.getUyeKay().equalsIgnoreCase("VAR")) {
            uye_kay.setVisibility(View.VISIBLE);
            uye_kayView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getGrvGo() != null && UserAdapter.getGrvGo().equalsIgnoreCase("VAR")) {
            grv_go.setVisibility(View.VISIBLE);
            grv_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getGrvKay() != null && UserAdapter.getGrvKay().equalsIgnoreCase("VAR")) {
            grv_kay.setVisibility(View.VISIBLE);
            grv_kayView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getRefGo() != null && UserAdapter.getRefGo().equalsIgnoreCase("VAR")) {
            ref_go.setVisibility(View.VISIBLE);
            ref_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getRefKay() != null && UserAdapter.getRefKay().equalsIgnoreCase("VAR")) {
            ref_kay.setVisibility(View.VISIBLE);
            ref_kayView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getTumProjeGo() != null && UserAdapter.getTumProjeGo().equalsIgnoreCase("VAR")) {
            tum_proje_go.setVisibility(View.VISIBLE);
            tum_proje_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getProjeKay() != null && UserAdapter.getProjeKay().equalsIgnoreCase("VAR")) {
            proje_kay.setVisibility(View.VISIBLE);
            proje_kayView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getTumTopGo() != null && UserAdapter.getTumTopGo().equalsIgnoreCase("VAR")) {
            tum_top_go.setVisibility(View.VISIBLE);
            tum_top_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getTopKay() != null && UserAdapter.getTopKay().equalsIgnoreCase("VAR")) {
            top_kay.setVisibility(View.VISIBLE);
            top_kayView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getTumGorevGo() != null && UserAdapter.getTumGorevGo().equalsIgnoreCase("VAR")) {
            tum_gorev_go.setVisibility(View.VISIBLE);
            tum_gorev_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getGorevKay() != null && UserAdapter.getGorevKay().equalsIgnoreCase("VAR")) {
            gorev_kay.setVisibility(View.VISIBLE);
            gorev_kayView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getMaliyeGo() != null && UserAdapter.getMaliyeGo().equalsIgnoreCase("VAR")) {
            maliye_go.setVisibility(View.VISIBLE);
            maliye_goView.setVisibility(View.VISIBLE);
        }

        if (UserAdapter.getMaliyeKay() != null && UserAdapter.getMaliyeKay().equalsIgnoreCase("VAR")) {
            maliye_kay.setVisibility(View.VISIBLE);
            maliye_kayView.setVisibility(View.VISIBLE);
        }
    }
}
