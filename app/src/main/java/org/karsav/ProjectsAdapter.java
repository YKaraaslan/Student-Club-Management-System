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
public class ProjectsAdapter extends Adapter<ProjectsAdapter.PojectViewHolder> {
    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private Context mCtx;
    private List<ProjectsItem> projectList;

    public ProjectsAdapter(Context mCtx, List<ProjectsItem> exampleList) {
        this.mCtx = mCtx;
        this.projectList = exampleList;
        settings = mCtx.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    public PojectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PojectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false), this.mCtx, this.projectList);
    }

    public void onBindViewHolder(@NonNull PojectViewHolder holder, int position) {
        ProjectsItem currentItem = this.projectList.get(position);
        holder.textView1.setText(currentItem.getName());
        holder.textView2.setText(currentItem.getExplanation());
        holder.image.setImageResource(currentItem.getImageResource());
        holder.textView1.setTextSize(textSize + 3);
        holder.textView2.setTextSize(textSize);
    }

    public int getItemCount() {
        return this.projectList.size();
    }

    public void filterList(ArrayList<ProjectsItem> filteredList) {
        projectList = filteredList;
        notifyDataSetChanged();
    }

    public class PojectViewHolder extends ViewHolder {
        ImageView image;
        Context context;
        List<ProjectsItem> projectsListe;
        TextView textView1;
        TextView textView2;

        public PojectViewHolder(@NonNull View itemView, final Context context, final List<ProjectsItem> projectsListe) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.text_view1);
            this.textView2 = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.image_view);
            this.context = context;
            this.projectsListe = projectsListe;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                ProjectsItem projectsItem = projectsListe.get(position);
                Intent intent = new Intent(context, ProjectsShow.class);
                intent.putExtra("Projects", projectsItem);
                context.startActivity(intent);
            });
        }
    }
}
