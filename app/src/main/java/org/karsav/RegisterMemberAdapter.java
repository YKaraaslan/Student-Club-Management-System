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
public class RegisterMemberAdapter extends Adapter<RegisterMemberAdapter.RegisterViewHolder> {
    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private Context mCtx;
    private List<RegisterMemberItem> registerList;

    public RegisterMemberAdapter(Context mCtx, List<RegisterMemberItem> registerList) {
        this.mCtx = mCtx;
        this.registerList = registerList;
        settings = mCtx.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    public RegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RegisterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false), this.mCtx, this.registerList);
    }

    public void onBindViewHolder(@NonNull RegisterViewHolder holder, int position) {
        RegisterMemberItem currentItem = registerList.get(position);
        holder.textView1.setText(String.format("%s %s", currentItem.getName(), currentItem.getSurname()));
        holder.textView2.setText(currentItem.getExplanation());
        holder.image.setImageResource(currentItem.getImageResource());
        holder.textView1.setTextSize(textSize + 3);
        holder.textView2.setTextSize(textSize);
    }

    public int getItemCount() {
        return this.registerList.size();
    }

    public void filterList(ArrayList<RegisterMemberItem> filteredList) {
        registerList = filteredList;
        notifyDataSetChanged();
    }

    public class RegisterViewHolder extends ViewHolder {
        ImageView image;
        Context context;
        List<RegisterMemberItem> registerListe;
        TextView textView1;
        TextView textView2;

        public RegisterViewHolder(@NonNull View itemView, final Context context, final List<RegisterMemberItem> registerListe) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.text_view1);
            this.textView2 = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.image_view);
            this.context = context;
            this.registerListe = registerListe;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                RegisterMemberItem register = registerListe.get(position);
                Intent intent = new Intent(context, RegisterMemberShow.class);
                intent.putExtra("Register", register);
                context.startActivity(intent);
            });
        }
    }
}
