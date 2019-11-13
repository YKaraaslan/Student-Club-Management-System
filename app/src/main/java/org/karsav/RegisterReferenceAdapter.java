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
public class RegisterReferenceAdapter extends Adapter<RegisterReferenceAdapter.RegisterViewHolder> {
    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private Context mCtx;
    private List<RegisterReferenceItem> registerReferenceList;

    public RegisterReferenceAdapter(Context mCtx, List<RegisterReferenceItem> exampleList) {
        this.mCtx = mCtx;
        this.registerReferenceList = exampleList;
        settings = mCtx.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    public RegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RegisterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false), this.mCtx, this.registerReferenceList);
    }

    public void onBindViewHolder(@NonNull RegisterViewHolder holder, int position) {
        RegisterReferenceItem currentItem = this.registerReferenceList.get(position);
        String nameSurname = currentItem.getName() + " " + currentItem.getSurname();
        holder.textView1.setText(nameSurname);
        holder.textView2.setText(currentItem.getExplanation());
        holder.image.setImageResource(currentItem.getImageResource());
        holder.textView1.setTextSize(textSize + 3);
        holder.textView2.setTextSize(textSize);
    }

    public int getItemCount() {
        return this.registerReferenceList.size();
    }

    public void filterList(ArrayList<RegisterReferenceItem> filteredList) {
        registerReferenceList = filteredList;
        notifyDataSetChanged();
    }

    public class RegisterViewHolder extends ViewHolder {
        ImageView image;
        TextView textView1;
        TextView textView2;
        Context context;
        List<RegisterReferenceItem> referenceList;

        public RegisterViewHolder(@NonNull View itemView, final Context context, final List<RegisterReferenceItem> referenceList) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.text_view1);
            this.textView2 = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.image_view);
            this.context = context;
            this.referenceList = referenceList;
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                RegisterReferenceItem register = referenceList.get(position);
                Intent intent = new Intent(context, RegisterReferenceShow.class);
                intent.putExtra("RegisterReferenceAddToDatabase", register);
                context.startActivity(intent);
            });
        }
    }
}
