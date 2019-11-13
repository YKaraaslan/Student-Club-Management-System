package org.karsav;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class UsersAdapter extends FirestoreRecyclerAdapter<UsersItems, UsersAdapter.UsersViewHolder> {

    private static final String SETTINGS_PREF_NAME = "textSize";
    SharedPreferences settings;
    int textSize;
    Context context;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnItemClickListener listener;

    public UsersAdapter(@NonNull FirestoreRecyclerOptions<UsersItems> options, Context applicationContext) {
        super(options);
        settings = applicationContext.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
        context = applicationContext;
    }

    @NonNull
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UsersViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_recycler_view_item, viewGroup, false));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int position, @NonNull UsersItems model) {
        holder.textViewName.setText(model.getName());
        holder.textViewText.setText(model.getPosition());

        holder.textViewName.setTextSize(textSize + 3);
        holder.textViewText.setTextSize(textSize);

        StorageReference referenceSecond = storageReference.child("ProfilePhotos/" + model.getId() + ".jpg");
        referenceSecond.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.imageView.setImageBitmap(bmp);
        });

        final DocumentReference documentReference = db.collection(context.getString(R.string.firestore_database_for_users))
                .document(String.valueOf(model.getId()));

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            try {
                String date = (String) documentSnapshot.get("status");
                if (!Objects.requireNonNull(date).equalsIgnoreCase("Online")) {
                    DateFormatter dateFormatter = new DateFormatter();
                    String status = dateFormatter.format(date);
                    holder.textViewLastLogin.setText(status);
                } else {
                    holder.textViewLastLogin.setText(context.getString(R.string.online));
                    holder.onlineImage.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                holder.textViewLastLogin.setText(context.getString(R.string.offline));
                holder.onlineImage.setVisibility(View.GONE);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewText;
        TextView textViewLastLogin;
        CircleImageView imageView;
        ImageView onlineImage;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.commentName);
            this.textViewText = itemView.findViewById(R.id.commentText);
            this.textViewLastLogin = itemView.findViewById(R.id.last_login);
            this.imageView = itemView.findViewById(R.id.postImage);
            this.onlineImage = itemView.findViewById(R.id.onlineImage);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position));
                }
            });
        }
    }
}
