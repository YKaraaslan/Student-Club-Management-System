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
public class FinanceSecondAdapter extends Adapter<FinanceSecondAdapter.FinanceSecondViewHolder> {
    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private Context mCtx;
    private List<FinanceItem> financeList;

    public FinanceSecondAdapter(Context mCtx, List<FinanceItem> exampleList) {
        this.mCtx = mCtx;
        this.financeList = exampleList;
        settings = mCtx.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    public FinanceSecondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FinanceSecondViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false), this.mCtx, this.financeList);
    }

    public void onBindViewHolder(@NonNull FinanceSecondViewHolder holder, int position) {
        FinanceItem currentItem = financeList.get(position);
        holder.textView1.setText(currentItem.getName());
        holder.textView2.setText(currentItem.getDescription());
        holder.image.setImageResource(currentItem.getImageResource());
        holder.textView1.setTextSize(textSize + 3);
        holder.textView2.setTextSize(textSize);
    }

    public int getItemCount() {
        return this.financeList.size();
    }

    public void filterList(ArrayList<FinanceItem> filteredList) {
        financeList = filteredList;
        notifyDataSetChanged();
    }

    public class FinanceSecondViewHolder extends ViewHolder {
        ImageView image;
        TextView textView1;
        TextView textView2;
        Context context;
        List<FinanceItem> financeItemList;

        public FinanceSecondViewHolder(@NonNull View itemView, final Context context, final List<FinanceItem> financeItemList) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.text_view1);
            this.textView2 = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.image_view);
            this.context = context;
            this.financeItemList = financeItemList;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                FinanceItem financeItem = financeItemList.get(position);
                Intent intent = new Intent(context, FinanceSecondShow.class);
                intent.putExtra("Finance", financeItem);
                context.startActivity(intent);
            });
        }
    }
}
