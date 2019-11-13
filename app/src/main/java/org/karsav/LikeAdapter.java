package org.karsav;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class LikeAdapter extends FirestoreRecyclerAdapter<LikeShowItems, LikeAdapter.LikeViewHolder> {

    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    public LikeAdapter(@NonNull FirestoreRecyclerOptions<LikeShowItems> options, Context applicationContext) {
        super(options);
        settings = applicationContext.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    @Override
    public LikeAdapter.LikeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LikeAdapter.LikeViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.likes_recycler_view_item, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final LikeViewHolder holder, int position, @NonNull LikeShowItems model) {
        holder.textViewName.setText(model.getName());
        holder.textViewText.setText(model.getTitle());

        holder.textViewName.setTextSize(textSize + 3);
        holder.textViewText.setTextSize(textSize);

        StorageReference referenceSecond = storageReference.child("ProfilePhotos/" + model.getId() + ".jpg");
        referenceSecond.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.imageView.setImageBitmap(bmp);
        });
    }

    public class LikeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewText;
        CircleImageView imageView;

        public LikeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.commentName);
            this.textViewText = itemView.findViewById(R.id.commentText);
            this.imageView = itemView.findViewById(R.id.postImage);
        }
    }
}
