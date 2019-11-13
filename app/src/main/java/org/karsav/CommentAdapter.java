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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class CommentAdapter extends FirestoreRecyclerAdapter<CommentItems, CommentAdapter.CommentViewHolder> {

    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    private OnItemLongClickListener listener;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<CommentItems> options, Context applicationContext) {
        super(options);
        settings = applicationContext.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentAdapter.CommentViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_recycler_view_item, viewGroup, false));
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommentAdapter.CommentViewHolder holder, int position, @NonNull CommentItems model) {
        String time = model.getDate();
        DateFormatter dateFormatter = new DateFormatter();
        String date = dateFormatter.format(time);
        holder.commentName.setText(model.getName());
        holder.commentText.setText(model.getText());
        holder.commentDate.setText(date);

        holder.commentName.setTextSize(textSize + 3);
        holder.commentText.setTextSize(textSize - 1);
        holder.commentDate.setTextSize(textSize - 1);

        StorageReference referenceSecond = storageReference.child("ProfilePhotos/" + model.getUserId() + ".jpg");
        referenceSecond.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.imageView.setImageBitmap(bmp);
        });
    }

    public interface OnItemLongClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, View view);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentName, commentText, commentDate;
        CircleImageView imageView;

        public CommentViewHolder(@NonNull final View itemView) {
            super(itemView);
            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);
            commentDate = itemView.findViewById(R.id.commentDate);
            imageView = itemView.findViewById(R.id.postImage);

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), itemView);
                }
                return false;
            });
        }
    }
}
