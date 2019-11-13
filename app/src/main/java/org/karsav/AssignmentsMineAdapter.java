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
public class AssignmentsMineAdapter extends Adapter<AssignmentsMineAdapter.AssignmentsMineViewHolder> {
    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private Context mCtx;
    private List<AssignmentsItem> assignmentsItemList;

    public AssignmentsMineAdapter(Context mCtx, List<AssignmentsItem> exampleList) {
        this.mCtx = mCtx;
        this.assignmentsItemList = exampleList;
        settings = mCtx.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    public AssignmentsMineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignmentsMineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false), this.mCtx, this.assignmentsItemList);
    }

    public void onBindViewHolder(@NonNull AssignmentsMineViewHolder holder, int position) {
        AssignmentsItem currentItem = assignmentsItemList.get(position);
        String content = currentItem.getContent().trim() + "\n\n" + currentItem.getResponsibleName() + " - " + currentItem.getDateSaved();
        holder.textView1.setText(currentItem.getName());
        holder.textView2.setText(content);
        holder.image.setImageResource(currentItem.getImageResource());
        holder.textView1.setTextSize(textSize + 3);
        holder.textView2.setTextSize(textSize);
    }

    public int getItemCount() {
        return this.assignmentsItemList.size();
    }

    public void filterList(ArrayList<AssignmentsItem> filteredList) {
        assignmentsItemList = filteredList;
        notifyDataSetChanged();
    }

    public class AssignmentsMineViewHolder extends ViewHolder {
        ImageView image;
        TextView textView1;
        TextView textView2;
        Context context;
        List<AssignmentsItem> assignmentsItemsList;

        public AssignmentsMineViewHolder(@NonNull View itemView, final Context context, final List<AssignmentsItem> assignmentsItemsList) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.text_view1);
            this.textView2 = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.image_view);
            this.context = context;
            this.assignmentsItemsList = assignmentsItemsList;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                AssignmentsItem assignmentsItem = assignmentsItemsList.get(position);
                Intent intent = new Intent(context, AssignmentsDoneShow.class);
                intent.putExtra("Assignments", assignmentsItem);
                context.startActivity(intent);
            });
        }
    }
}
