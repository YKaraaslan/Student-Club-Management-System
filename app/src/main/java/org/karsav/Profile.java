package org.karsav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Profile extends Fragment {

    private static final String SETTINGS_PREF_NAME = "textSize";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button update;
    DataBase karsav = new DataBase();
    SharedPreferences settings;
    int textSize;
    private TextView postAmount;
    private TextView likeAmount;
    private ReadMoreTextView about;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        TextView authorizations = view.findViewById(R.id.authorizations);
        TextView myPosts = view.findViewById(R.id.myPosts);
        TextView myProjects = view.findViewById(R.id.myProjects);
        TextView myAssignments = view.findViewById(R.id.myAssignments);

        TextView name = view.findViewById(R.id.name);
        TextView position = view.findViewById(R.id.position);
        postAmount = view.findViewById(R.id.postAmount);
        likeAmount = view.findViewById(R.id.likeAmount);
        TextView last_login = view.findViewById(R.id.last_login);
        TextView meetupsAmount = view.findViewById(R.id.meetupsAmount);
        TextView projectsAmount = view.findViewById(R.id.projectsAmount);
        TextView assignmentsAmount = view.findViewById(R.id.assignmentsAmount);
        TextView myInformation = view.findViewById(R.id.myInformation);
        LinearLayout assignmentsLinear = view.findViewById(R.id.assignmentsLinear);
        LinearLayout projectsLinear = view.findViewById(R.id.projectsLinear);
        LinearLayout meetupsLinear = view.findViewById(R.id.meetupsLinear);
        LinearLayout postsLinear = view.findViewById(R.id.postsLinear);
        LinearLayout likesLinear = view.findViewById(R.id.likesLinear);

        about = view.findViewById(R.id.about);
        LinearLayout aboutLinear = view.findViewById(R.id.aboutLinear);
        TextView aboutTitle = view.findViewById(R.id.aboutTitle);

        LinearLayout authorizationLinear = view.findViewById(R.id.authorizationLinear);
        LinearLayout myAssignmentLinear = view.findViewById(R.id.myAssignmentLinear);
        LinearLayout myProjectsLinear = view.findViewById(R.id.myProjectsLinear);
        LinearLayout myPostsLinear = view.findViewById(R.id.myPostsLinear);

        TextView student_no = view.findViewById(R.id.student_no);
        TextView phone = view.findViewById(R.id.phone);
        TextView mail = view.findViewById(R.id.mail);
        TextView explanation = view.findViewById(R.id.explanation);


        TextView aviation = view.findViewById(R.id.aviation);
        TextView ground = view.findViewById(R.id.ground);
        TextView marine = view.findViewById(R.id.marine);
        TextView cyber = view.findViewById(R.id.cyber);

        ImageView aviation_check = view.findViewById(R.id.aviation_check);
        ImageView ground_check = view.findViewById(R.id.ground_check);
        ImageView marine_check = view.findViewById(R.id.marine_check);
        ImageView cyber_check = view.findViewById(R.id.cyber_check);

        update = view.findViewById(R.id.update);

        CircleImageView karsav_image = view.findViewById(R.id.karsav_image);

        setProfile();
        setAbout();

        String nameSurname = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();

        settings = Objects.requireNonNull(getContext()).getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize + 5);
        position.setTextSize(textSize - 1);
        postAmount.setTextSize(textSize);
        likeAmount.setTextSize(textSize);
        last_login.setTextSize(textSize - 1);
        meetupsAmount.setTextSize(textSize);
        projectsAmount.setTextSize(textSize);
        assignmentsAmount.setTextSize(textSize);
        student_no.setTextSize(textSize);
        phone.setTextSize(textSize);
        mail.setTextSize(textSize);
        explanation.setTextSize(textSize);
        aviation.setTextSize(textSize);
        ground.setTextSize(textSize);
        cyber.setTextSize(textSize);
        marine.setTextSize(textSize);
        myInformation.setTextSize(textSize + 5);
        update.setTextSize(textSize);
        authorizations.setTextSize(textSize);
        myPosts.setTextSize(textSize);
        myProjects.setTextSize(textSize);
        myAssignments.setTextSize(textSize);
        about.setTextSize(textSize);
        aboutTitle.setTextSize(textSize + 2);

        String postAmountString = String.valueOf(ProfileAdapter.getPostAmount());
        String likeAmountString = String.valueOf(ProfileAdapter.getLikeAmount());
        String meetupsAmountString = String.valueOf(ProfileAdapter.getMeetupsAmount());
        String projectsAmountString = String.valueOf(ProfileAdapter.getProjectsAmount());
        String assignmentsAmountString = String.valueOf(ProfileAdapter.getAssignmentsAmount());

        name.setText(nameSurname);
        position.setText(UserAdapter.getPozisyon());
        last_login.setText(ProfileAdapter.getStatus());

        postAmount.setText(postAmountString);
        likeAmount.setText(likeAmountString);
        meetupsAmount.setText(meetupsAmountString);
        projectsAmount.setText(projectsAmountString);
        assignmentsAmount.setText(assignmentsAmountString);

        assignmentsLinear.setOnClickListener(v -> alertDialog(getString(R.string.assignmentsLinear, String.valueOf(ProfileAdapter.getAssignmentsAmount()))));

        projectsLinear.setOnClickListener(v -> alertDialog(getString(R.string.projectsLinear, String.valueOf(ProfileAdapter.getProjectsAmount()))));

        meetupsLinear.setOnClickListener(v -> alertDialog(getString(R.string.meetupsLinear, String.valueOf(ProfileAdapter.getMeetupsAmount()))));

        postsLinear.setOnClickListener(v -> alertDialog(getString(R.string.postsLinear, String.valueOf(ProfileAdapter.getPostAmount()))));

        likesLinear.setOnClickListener(v -> alertDialog(getString(R.string.likesLinear, String.valueOf(ProfileAdapter.getLikeAmount()))));

        authorizationLinear.setOnClickListener(v -> startActivity(new Intent(getContext(), ProfileAuthorizations.class)));

        myAssignmentLinear.setOnClickListener(v -> {
            if (AssignmentsMineGet.getName().get(1) != null)
                startActivity(new Intent(getContext(), ProfileAssignments.class));
            else
                Toast.makeText(getContext(), getString(R.string.no_assignment_found), Toast.LENGTH_SHORT).show();
        });

        myProjectsLinear.setOnClickListener(v -> {
            if (ProjectsMyProjectsGet.getAdmin().get(1) != null)
                startActivity(new Intent(getContext(), ProfileProjects.class));
            else
                Toast.makeText(getContext(), getString(R.string.no_project_found), Toast.LENGTH_SHORT).show();
        });

        myPostsLinear.setOnClickListener(v -> {
            if (ProfileAdapter.getPostAmount() == 0) {
                Toast.makeText(getContext(), getString(R.string.no_post_found), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getContext(), ProfilePosts.class);
                intent.putExtra("user", UserAdapter.getUserId());
                startActivity(intent);
            }
        });

        aboutLinear.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UserProfileAboutUpdate.class);
            intent.putExtra("about", String.valueOf(about.getText()));
            intent.putExtra("id", UserAdapter.getUserId());
            startActivity(intent);
        });

        update.setOnClickListener(v -> startActivity(new Intent(getContext(), ProfileUpdate.class)));

        karsav_image.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            alertDialog.setMessage(getString(R.string.your_image));

            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(UserAdapter.getUserImage());
            imageView.setBackgroundColor(Color.TRANSPARENT);

            alertDialog.setView(imageView).create().show();
        });

        student_no.setText(UserAdapter.getStudentNo());
        phone.setText(UserAdapter.getPhone());
        mail.setText(UserAdapter.getMail());
        explanation.setText(UserAdapter.getDepartment());

        if (UserAdapter.getAviation().equalsIgnoreCase("VAR"))
            aviation_check.setBackgroundResource(R.drawable.check);

        if (UserAdapter.getGround().equalsIgnoreCase("VAR"))
            ground_check.setBackgroundResource(R.drawable.check);

        if (UserAdapter.getMarine().equalsIgnoreCase("VAR"))
            marine_check.setBackgroundResource(R.drawable.check);

        if (UserAdapter.getCyber().equalsIgnoreCase("VAR"))
            cyber_check.setBackgroundResource(R.drawable.check);

        karsav_image.setImageBitmap(UserAdapter.getUserImage());

        db.collection(getString(R.string.firestore_database_for_status)).document(String.valueOf(UserAdapter.getUserId())).get().addOnSuccessListener(documentSnapshot -> {
            String status = (String) documentSnapshot.get("status");
            if (status == null || status.equalsIgnoreCase("")) {
                about.setText(getString(R.string.status_empty));
            } else {
                about.setText(status);
            }
        });

        return view;
    }

    private void setAbout() {
        about.setText(UserProfileAdapter.getStatus());
    }

    private void setProfile() {
        final int[] postAmountInt = {0};
        final int[] likeAmountInt = {0};
        db.collection(getString(R.string.firestore_database_for_posts)).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    long id = (long) documentSnapshot.get("userId");
                    if (id == UserAdapter.getUserId()) {
                        postAmountInt[0]++;
                    }
                }
                postAmount.setText(String.valueOf(postAmountInt[0]));
                ProfileAdapter.setPostAmount(postAmountInt[0]);
            }
        });

        db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(UserAdapter.getUserId()))
                .collection(getString(R.string.firestore_database_for_likedby)).get().addOnSuccessListener(queryDocumentSnapshots -> {
            likeAmountInt[0] = queryDocumentSnapshots.size();
            likeAmount.setText(String.valueOf(likeAmountInt[0]));
            ProfileAdapter.setLikeAmount(likeAmountInt[0]);
        });
    }

    private void alertDialog(String text) {
        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext())).setMessage(text)
                .setPositiveButton(getString(R.string.ok), (dialog1, which) -> {

                }).show();
        TextView textView = dialog.findViewById(android.R.id.message);
        Objects.requireNonNull(textView).setTextSize(textSize);
    }
}
