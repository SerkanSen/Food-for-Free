package de.hsh.f4.mobilecomputing.foodforfree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class AdAdapter extends FirestoreRecyclerAdapter<Ad, AdAdapter.AdHolder> {

    private OnItemClickListener listener;
    private ProgressBar progressBarAdItemPhoto;

    public AdAdapter(@NonNull FirestoreRecyclerOptions<Ad> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdHolder holder, int position, @NonNull Ad model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewAmount.setText(model.getAmount());
        holder.textViewPickupLocation.setText(model.getPickupLocation());
        holder.textViewFilter.setText(model.getFilterOptions());

        progressBarAdItemPhoto.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(model.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageViewAdPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBarAdItemPhoto.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @NonNull
    @Override
    public AdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false);
        return new AdHolder(view);
    }

    class AdHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewPickupLocation, textViewAmount, textViewFilter;
        ImageView imageViewAdPhoto;

        public AdHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.ad_item_title);
            textViewPickupLocation = itemView.findViewById(R.id.ad_item_pickuplocation);
            textViewAmount = itemView.findViewById(R.id.ad_item_amount);
            textViewFilter = itemView.findViewById(R.id.ad_item_filter);
            imageViewAdPhoto = itemView.findViewById(R.id.ad_item_image);
            progressBarAdItemPhoto = itemView.findViewById(R.id.ad_item_progressBar);

            //OnClick for each item(card)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }

    //helps sending information from adapter to underlying activity
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
