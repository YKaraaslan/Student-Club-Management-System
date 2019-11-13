package org.karsav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProjectsMyProjectsAdapter extends Adapter<ProjectsMyProjectsAdapter.ProjectsViewHolder> {
    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private Context mCtx;
    private List<ProjectsMyProjectItem> projectMyProjectList;

    public ProjectsMyProjectsAdapter(Context mCtx, List<ProjectsMyProjectItem> projectMyProjectList) {
        this.mCtx = mCtx;
        this.projectMyProjectList = projectMyProjectList;
        settings = mCtx.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false), this.mCtx, this.projectMyProjectList);
    }

    public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int position) {
        ProjectsMyProjectItem currentItem = projectMyProjectList.get(position);
        holder.textView1.setText(currentItem.getName());
        holder.textView2.setText(currentItem.getDescription());
        holder.image.setImageResource(currentItem.getImageResource());
        holder.textView1.setTextSize(textSize + 3);
        holder.textView2.setTextSize(textSize);
    }

    public int getItemCount() {
        return this.projectMyProjectList.size();
    }

    public void filterList(ArrayList<ProjectsMyProjectItem> filteredList) {
        projectMyProjectList = filteredList;
        notifyDataSetChanged();
    }

    public class ProjectsViewHolder extends ViewHolder {
        ImageView image;
        TextView textView1;
        TextView textView2;
        Context context;
        List<ProjectsMyProjectItem> projectMyProjectListe;

        public ProjectsViewHolder(@NonNull View itemView, final Context context, final List<ProjectsMyProjectItem> projectMyProjectListe) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.text_view1);
            this.textView2 = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.image_view);
            this.context = context;
            this.projectMyProjectListe = projectMyProjectListe;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                ProjectsMyProjectItem projectsItem = projectMyProjectListe.get(position);
                Intent intent = new Intent(context, ProjectsMyProjectShow.class);
                intent.putExtra("ProjectsMyProjects", projectsItem);
                context.startActivity(intent);
            });
        }
    }
}
