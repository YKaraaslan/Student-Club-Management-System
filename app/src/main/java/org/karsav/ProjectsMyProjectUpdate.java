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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProjectsMyProjectUpdate extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextInputEditText explanation, members;
    AutoCompleteTextView manager;
    EditText name;
    ProjectsMyProjectItem projectsItem;
    String nameString, managerString, explanationString, membersString, idString;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    Toolbar toolbar;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> officialItems = new ArrayList<>();
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_add);
        Slidr.attach(this);
        init();
    }

    private void init() {
        explanation = findViewById(R.id.projects_explanation);
        members = findViewById(R.id.projects_members);
        manager = findViewById(R.id.project_admin);
        name = findViewById(R.id.project_name);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.update_projects));
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        projectsItem = (ProjectsMyProjectItem) getIntent().getSerializableExtra("UpdateMyProject");

        idString = projectsItem.getId();
        name.setText(projectsItem.getName());
        explanation.setText(projectsItem.getDescription());
        manager.setText(projectsItem.getMembers());
        members.setText(projectsItem.getAdmin());

        members.setOnClickListener(v -> {
            Map map = RegisterOfficialGet.getNameSurname();
            listItems = (String[]) map.values().toArray(new String[0]);
            checkedItems = new boolean[listItems.length];

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProjectsMyProjectUpdate.this);
            mBuilder.setTitle(R.string.officials);
            mBuilder.setMultiChoiceItems(listItems, checkedItems, (dialogInterface, position, isChecked) -> {
                if (isChecked) {
                    if (!officialItems.contains(position)) {
                        officialItems.add(position);
                    }
                } else if (officialItems.contains(position)) {
                    officialItems.remove(Integer.valueOf(position));
                }
            });

            mBuilder.setPositiveButton(R.string.ok, (dialogInterface, which) -> {
                StringBuilder item = new StringBuilder();
                for (int i = 0; i < officialItems.size(); i++) {
                    item.append(listItems[officialItems.get(i)]);
                    if (i != officialItems.size() - 1) {
                        item.append("\n");
                    }
                }
                members.setText(item.toString());
            });

            mBuilder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());

            mBuilder.setNeutralButton(R.string.clear_all, (dialogInterface, which) -> {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    officialItems.clear();
                    members.setText("");
                }
            });


            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });

        Map map = RegisterMemberGet.getFullName();
        Collection<String> values = map.values();
        ArrayList<String> listOfValues = new ArrayList<>(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_for_users, R.id.users_list_item, listOfValues);
        manager.setAdapter(adapter);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        explanation.setTextSize(textSize);
        members.setTextSize(textSize);
        manager.setTextSize(textSize);
        name.setTextSize(textSize);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            done();
        }
        return super.onOptionsItemSelected(item);
    }

    private void done() {
        nameString = name.getText().toString();
        explanationString = Objects.requireNonNull(explanation.getText()).toString();
        managerString = manager.getText().toString();
        membersString = Objects.requireNonNull(members.getText()).toString();
        if (nameString.equalsIgnoreCase("null") || nameString.isEmpty() || managerString.equalsIgnoreCase("null") || managerString.isEmpty()) {
            Toast.makeText(ProjectsMyProjectUpdate.this, getString(R.string.name_manager_empty), Toast.LENGTH_SHORT).show();
        } else {
            String text = getString(R.string.project_update_text);
            String yes = getString(R.string.update);
            String no = getString(R.string.cancel);
            AlertDialog.Builder builder = new AlertDialog.Builder(ProjectsMyProjectUpdate.this);
            builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> updateProject()).setNegativeButton(no, (dialog, which) -> {

            }).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void updateProject() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.project_update_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String updated = getString(R.string.project_updated);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(ProjectsMyProjectUpdate.this);
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
                    karsav.ProjectsUpdate(nameString, explanationString, membersString, managerString, idString);
                    karsav.ProjectsDeleteUserList(idString);
                    try {
                        String[] split = membersString.split("\n");
                        for (String s : split) {
                            String[] nameSurname = s.split((" "), 3);
                            karsav.ProjectsAddUserList(nameSurname, nameString, explanationString, membersString, managerString);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    karsav.ProjectsAddManagerToTheUserList(nameString, explanationString, membersString, managerString);
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
                        Toast.makeText(ProjectsMyProjectUpdate.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(ProjectsMyProjectUpdate.this, HomePage.class));
                        Toast.makeText(ProjectsMyProjectUpdate.this, updated, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("updateMyProject");
    }
}
