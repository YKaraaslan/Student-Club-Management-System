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
public class MeetUpsSecondAdapter extends Adapter<MeetUpsSecondAdapter.MeetUpsFirstViewHolder> {
    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private Context mCtx;
    private List<MeetUpsItem> meetUpsList;

    public MeetUpsSecondAdapter(Context mCtx, List<MeetUpsItem> exampleList) {
        this.mCtx = mCtx;
        this.meetUpsList = exampleList;
        settings = mCtx.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    public MeetUpsFirstViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeetUpsFirstViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false), this.mCtx, this.meetUpsList);
    }

    public void onBindViewHolder(@NonNull MeetUpsFirstViewHolder holder, int position) {
        MeetUpsItem currentItem = meetUpsList.get(position);
        String subject = "\n" + currentItem.getTopic() + "\n\n" + currentItem.getDate() + " - " + currentItem.getTime();
        holder.textView1.setText(currentItem.getName());
        holder.textView2.setText(subject);
        holder.image.setImageResource(currentItem.getImageResource());
        holder.textView1.setTextSize(textSize + 3);
        holder.textView2.setTextSize(textSize);
    }

    public int getItemCount() {
        return this.meetUpsList.size();
    }

    public void filterList(ArrayList<MeetUpsItem> filteredList) {
        meetUpsList = filteredList;
        notifyDataSetChanged();
    }

    public class MeetUpsFirstViewHolder extends ViewHolder {
        ImageView image;
        TextView textView1;
        TextView textView2;
        Context context;
        List<MeetUpsItem> meetUpsItemList;

        public MeetUpsFirstViewHolder(@NonNull View itemView, final Context context, final List<MeetUpsItem> meetUpsItemList) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.text_view1);
            this.textView2 = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.image_view);
            this.context = context;
            this.meetUpsItemList = meetUpsItemList;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                MeetUpsItem meetUpsItem = meetUpsItemList.get(position);
                Intent intent = new Intent(context, MeetUpsMineShow.class);
                intent.putExtra("MeetUps", meetUpsItem);
                context.startActivity(intent);
            });
        }
    }
}
