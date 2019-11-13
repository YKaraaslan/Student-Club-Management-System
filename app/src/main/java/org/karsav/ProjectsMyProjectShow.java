package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProjectsMyProjectShow extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextView projects_name, projects_explanation, projects_members, projects_admin;
    ProjectsMyProjectItem projectsItem;
    String name, explanation, members, admins, idString;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    Toolbar toolbar;
    TextView explanationTitle, projectManagerTitle, officialTitle;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_show);
        Slidr.attach(this);
        init();
    }

    private void init() {
        projects_admin = findViewById(R.id.projects_admin);
        projects_explanation = findViewById(R.id.projects_explanation);
        projects_members = findViewById(R.id.projects_members);
        projects_name = findViewById(R.id.projects_name);
        toolbar = findViewById(R.id.toolbar);

        explanationTitle = findViewById(R.id.explanationTitle);
        projectManagerTitle = findViewById(R.id.projectManagerTitle);
        officialTitle = findViewById(R.id.officialTitle);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.header_for_my_projects));
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        projectsItem = (ProjectsMyProjectItem) getIntent().getSerializableExtra("ProjectsMyProjects");

        idString = projectsItem.getId();
        name = projectsItem.getName().trim();
        explanation = projectsItem.getDescription().trim();
        members = projectsItem.getMembers().trim();
        admins = projectsItem.getAdmin().trim();

        projects_admin.setText(members);
        projects_explanation.setText(explanation);
        projects_members.setText(admins);
        projects_name.setText(name);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        projects_admin.setTextSize(textSize - 1);
        projects_explanation.setTextSize(textSize - 1);
        projects_members.setTextSize(textSize - 1);
        projects_name.setTextSize(textSize + 15);
        explanationTitle.setTextSize(textSize + 5);
        projectManagerTitle.setTextSize(textSize + 5);
        officialTitle.setTextSize(textSize + 5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_delete, menu);
        if (UserAdapter.getProjeKay() != null && UserAdapter.getProjeKay().equalsIgnoreCase("VAR")) {
            menu.findItem(R.id.update).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
        } else {
            menu.findItem(R.id.update).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.update:
                Intent intent = new Intent(ProjectsMyProjectShow.this, ProjectsMyProjectUpdate.class);
                intent.putExtra("UpdateMyProject", projectsItem);
                startActivity(intent);
                finish();
                break;

            case R.id.delete:
                String title = getString(R.string.projects_delete_dialog_title);
                String text = getString(R.string.projects_delete_dialog_message);
                String yes = getString(R.string.reference_yes);
                String no = getString(R.string.reference_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectsMyProjectShow.this);
                builder.setTitle(title).setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_delete()).setNegativeButton(no, (dialog, which) -> {

                }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void foo_delete() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.register_reference_delete_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String deleted = getString(R.string.projects_deleted);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(ProjectsMyProjectShow.this);
                progressDialog.setTitle(progress_title);
                progressDialog.setMessage(progress_message);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    if (karsav.Begin()) {
                        return "wrong";
                    }
                    karsav.ProjectsDeleteFromDatabase(idString);
                    karsav.Disconnect();
                    return "done";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "false";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();
                switch (s) {
                    case "false":
                        Toast.makeText(ProjectsMyProjectShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(ProjectsMyProjectShow.this, HomePage.class));
                        Toast.makeText(ProjectsMyProjectShow.this, deleted, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }
}
