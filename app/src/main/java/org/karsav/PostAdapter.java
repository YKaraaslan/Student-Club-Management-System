package org.karsav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
public class PostAdapter extends FirestoreRecyclerAdapter<PostItems, PostAdapter.PostViewHolder> {

    private static final String SETTINGS_PREF_NAME = "textSize";
    DataBase karsav = new DataBase();
    int id;
    int amount = 0;
    SharedPreferences settings;
    int textSize;
    Context context;
    @SuppressLint("StaticFieldLeak")
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    public PostAdapter(@NonNull FirestoreRecyclerOptions<PostItems> options, Context context) {
        super(options);
        this.context = context;
        settings = context.getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PostAdapter.PostViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts_recycler_view_item, viewGroup, false));

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final PostViewHolder postViewHolder, int position, @NonNull PostItems model) {

        postViewHolder.textViewName.setTextSize(textSize + 2);
        postViewHolder.textViewTitle.setTextSize(textSize - 1);
        postViewHolder.textViewText.setTextSize(textSize - 1);
        postViewHolder.textViewTime.setTextSize(textSize - 1);
        postViewHolder.likeText.setTextSize(textSize - 1);
        postViewHolder.commentText.setTextSize(textSize - 1);

        postViewHolder.textViewName.setText(model.getName());
        postViewHolder.textViewTitle.setText(model.getTitle());
        postViewHolder.textViewText.setText(model.getText());
        String time = model.getDate();
        DateFormatter dateFormatter = new DateFormatter();
        String convTime = dateFormatter.format(time);
        postViewHolder.textViewTime.setText(convTime);

        db.collection("Posts").document(String.valueOf(model.getId())).collection("Likes")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                        int count = queryDocumentSnapshots.size();
                        postViewHolder.likeText.setText(String.valueOf(count));
                    } else {
                        postViewHolder.likeText.setText("0");
                    }
                });

        final String name = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();
        final int userId = UserAdapter.getUserId();

        db.collection("Posts").document(String.valueOf(model.getId())).collection("Likes").document(String.valueOf(userId))
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (Objects.requireNonNull(documentSnapshot).exists())
                        postViewHolder.like.setBackgroundResource(R.drawable.like_pressed);
                    else
                        postViewHolder.like.setBackgroundResource(R.drawable.like_default);
                });

        if (userId == model.getUserId() && name.equalsIgnoreCase(model.getName())) {
            postViewHolder.update.setVisibility(View.VISIBLE);
            postViewHolder.delete.setVisibility(View.VISIBLE);
        } else {
            postViewHolder.update.setVisibility(View.GONE);
            postViewHolder.delete.setVisibility(View.GONE);
        }

        db.collection("Posts").document(String.valueOf(model.getId())).collection("Comments")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                        int count = queryDocumentSnapshots.size();
                        postViewHolder.commentText.setText(String.valueOf(count));
                    } else {
                        postViewHolder.commentText.setText("0");
                    }
                });

        StorageReference referenceSecond = storageReference.child("ProfilePhotos/" + model.getUserId() + ".jpg");
        referenceSecond.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            postViewHolder.imageView.setImageBitmap(bmp);
        });

        if (model.isPhoto()) {
            postViewHolder.postedImageBackground.setVisibility(View.VISIBLE);
            StorageReference referencePhotos = storageReference.child("PostPhotos/" + model.getId() + ".jpg");
            referencePhotos.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                postViewHolder.postedImage.setImageBitmap(bmp);
                postViewHolder.postedImage.setVisibility(View.VISIBLE);
                postViewHolder.postedImageBackground.setVisibility(View.GONE);
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);

        void onLikeClick(int position, ImageButton likeButton, TextView likeText, String id, long userId);

        void onShareClick(DocumentSnapshot snapshot);

        void onDeleteClick(DocumentSnapshot documentSnapshot);

        void onUpdateClick(DocumentSnapshot documentSnapshot);

        void onCommentClick(DocumentSnapshot documentSnapshot);

        void onImageClick(DocumentSnapshot snapshot);
    }


    public interface OnItemLongClickListener {
        void onLongClick(DocumentSnapshot documentSnapshot);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ReadMoreTextView textViewText;
        TextView textViewTime;
        TextView textViewTitle;
        CircleImageView imageView;
        ImageView postedImage, postedImageBackground;

        ImageButton like, comment, share, delete, update;
        TextView likeText, commentText;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.postTitle);
            this.textViewText = itemView.findViewById(R.id.postText);
            this.textViewName = itemView.findViewById(R.id.postName);
            this.textViewTime = itemView.findViewById(R.id.postTime);
            this.imageView = itemView.findViewById(R.id.postImage);
            this.postedImage = itemView.findViewById(R.id.postedImage);
            this.postedImageBackground = itemView.findViewById(R.id.postedImageBackground);

            this.like = itemView.findViewById(R.id.likeButton);
            this.comment = itemView.findViewById(R.id.commentButton);
            this.likeText = itemView.findViewById(R.id.likeText);
            this.commentText = itemView.findViewById(R.id.commentText);
            this.share = itemView.findViewById(R.id.shareButton);
            this.delete = itemView.findViewById(R.id.binButton);
            this.update = itemView.findViewById(R.id.penButton);
            //this.textViewName.setTextColor(SupportMenu.CATEGORY_MASK);

            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onImageClick(getSnapshots().getSnapshot(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    longClickListener.onLongClick(getSnapshots().getSnapshot(position));
                }
                return true;
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position));
                }
            });
            like.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                    String id = snapshot.getId();
                    long userId = (long) snapshot.get("userId");
                    listener.onLikeClick(position, like, likeText, id, userId);
                }
            });

            share.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onShareClick(getSnapshots().getSnapshot(position));
                }
            });

            delete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(getSnapshots().getSnapshot(position));
                }
            });

            update.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onUpdateClick(getSnapshots().getSnapshot(position));
                }
            });

            comment.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCommentClick(getSnapshots().getSnapshot(position));
                }
            });

            amount = getSnapshots().size();
        }
    }
}