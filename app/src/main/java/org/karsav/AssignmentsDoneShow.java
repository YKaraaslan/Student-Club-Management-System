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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsDoneShow extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextView assignments_name, assignments_creator, assignments_content, assignments_returns, assignments_date_of_issue;
    TextView assignments_date_due, assignments_date_saved;
    AssignmentsItem assignmentsItem;
    String name, creator, content, returns, date_of_issue, date_due, date_saved;
    LinearLayout linear_creator, linear_content, linear_dates;
    ProgressDialog progressDialog;
    DataBase karsav = new DataBase();
    TextView creatorHeader, contentHeader, datesHeader, returnsHeader;
    int id;
    Toolbar toolbar;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_done_show);
        Slidr.attach(this);
        init();
    }

    private void init() {
        assignments_name = findViewById(R.id.assignments_name);
        assignments_creator = findViewById(R.id.assignments_creator);
        assignments_content = findViewById(R.id.assignments_content);
        assignments_returns = findViewById(R.id.assignments_returns);
        assignments_date_of_issue = findViewById(R.id.assignments_date_of_issue);
        assignments_date_due = findViewById(R.id.assignments_date_due);
        assignments_date_saved = findViewById(R.id.assignments_date_saved);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        linear_creator = findViewById(R.id.linear_creator);
        linear_content = findViewById(R.id.linear_content);
        linear_dates = findViewById(R.id.linear_dates);

        creatorHeader = findViewById(R.id.creatorHeader);
        contentHeader = findViewById(R.id.contentHeader);
        datesHeader = findViewById(R.id.datesHeader);
        returnsHeader = findViewById(R.id.returnsHeader);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        assignments_name.setTextSize(textSize + 10);
        assignments_creator.setTextSize(textSize);
        assignments_content.setTextSize(textSize);
        assignments_returns.setTextSize(textSize);
        assignments_date_of_issue.setTextSize(textSize);
        assignments_date_due.setTextSize(textSize);
        assignments_date_saved.setTextSize(textSize);
        creatorHeader.setTextSize(textSize + 5);
        contentHeader.setTextSize(textSize + 5);
        datesHeader.setTextSize(textSize + 5);
        returnsHeader.setTextSize(textSize + 5);

        assignmentsItem = (AssignmentsItem) getIntent().getSerializableExtra("Assignments");

        name = assignmentsItem.getName().trim();
        creator = assignmentsItem.getCreator().trim();
        content = assignmentsItem.getContent().trim();
        date_of_issue = assignmentsItem.getDateOfIssue();
        date_due = assignmentsItem.getDueDate();
        date_saved = assignmentsItem.getDateSaved();
        returns = assignmentsItem.getReturns();
        id = assignmentsItem.getId();

        if (creator.isEmpty() || creator.equalsIgnoreCase("null")) {
            linear_creator.setVisibility(View.GONE);
        }
        if (content.isEmpty() || content.equalsIgnoreCase("null")) {
            linear_content.setVisibility(View.GONE);
        }
        if (date_of_issue.isEmpty() || date_of_issue.equalsIgnoreCase("null")) {
            assignments_date_of_issue.setVisibility(View.GONE);
        }
        if (date_due.isEmpty() || date_due.equalsIgnoreCase("null")) {
            assignments_date_due.setVisibility(View.GONE);
        }
        if (date_saved.isEmpty() || date_saved.equalsIgnoreCase("null")) {
            assignments_date_saved.setVisibility(View.GONE);
        }

        assignments_name.setText(name);
        assignments_creator.setText(creator);
        assignments_content.setText(content);
        assignments_date_of_issue.setText(getString(R.string.given_date, date_of_issue));
        assignments_date_due.setText(getString(R.string.due_dated, date_due));
        assignments_date_saved.setText(getString(R.string.date_saved, date_saved));
        assignments_returns.setText(returns);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_delete, menu);
        if (UserAdapter.getGorevKay() != null && UserAdapter.getGorevKay().equalsIgnoreCase("VAR")) {
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
                Intent intent = new Intent(AssignmentsDoneShow.this, AssignmentsDoneUpdate.class);
                intent.putExtra("UpdateAssignment", assignmentsItem);
                startActivity(intent);
                finish();
                break;

            case R.id.delete:
                String title = getString(R.string.assignment_delete_dialog_title);
                String text = getString(R.string.assignment_delete_dialog_message);
                String yes = getString(R.string.assignment_delete_yes);
                String no = getString(R.string.assignment_delete_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentsDoneShow.this);
                builder.setTitle(title).setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_delete()).setNegativeButton(no, (dialog, which) -> {

                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void foo_delete() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.assignment_delete_progress_title);
            final String progress_message = getString(R.string.assignment_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String deleted = getString(R.string.assignment_deleted);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AssignmentsDoneShow.this);
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
                    karsav.AssignmentDeleteFromDatabase(id);
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
                        Toast.makeText(AssignmentsDoneShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(AssignmentsDoneShow.this, HomePage.class));
                        Toast.makeText(AssignmentsDoneShow.this, deleted, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }
}
