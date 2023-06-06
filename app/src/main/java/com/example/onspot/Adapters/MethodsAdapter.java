package com.example.onspot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onspot.App;
import com.example.onspot.Models.Method;
import com.example.onspot.R;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MethodsAdapter extends RecyclerView.Adapter {

    private ArrayList<Method> methods;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public MethodsAdapter(ArrayList<Method> methods){
        this.methods = methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_method_item, parent, false);
            return new FullMethodViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.method_item, parent, false);
            return new MethodViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Method method = getItem(position);

        if(method.isFull()){
            ((FullMethodViewHolder)holder).method_LBL_type.setText(method.getStainType().getString());

            if(auth.getCurrentUser() != null) {

                if (method.getSaves().containsKey(auth.getCurrentUser().getUid())) {
                    ((FullMethodViewHolder) holder).method_IMG_favorite.setImageResource(R.drawable.bookmark);
                } else {
                    ((FullMethodViewHolder) holder).method_IMG_favorite.setImageResource(R.drawable.save);
                }

                if (method.getStars().containsKey(auth.getCurrentUser().getUid())) {
                    Float rating = method.getStars().get(auth.getCurrentUser().getUid());
                    if(rating != null) {
                        ((FullMethodViewHolder) holder).method_RTG_rate.setRating(rating);
                    }else{
                        ((FullMethodViewHolder) holder).method_RTG_rate.setRating(0);
                    }
                } else {
                    ((FullMethodViewHolder) holder).method_RTG_rate.setRating(0);
                }
            }

            ((FullMethodViewHolder)holder).method_LBL_supplies.setText(R.string.supplies);
            ((FullMethodViewHolder)holder).method_LBL_supplies.append(method.getSupplies());

            ((FullMethodViewHolder)holder).method_LBL_recommended.setText(R.string.recommended_for);
            ((FullMethodViewHolder)holder).method_LBL_recommended.append(method.getRecommended());

            ((FullMethodViewHolder)holder).method_LBL_description.setText(R.string.description);
            ((FullMethodViewHolder)holder).method_LBL_description.append(method.getDescription());

            ((FullMethodViewHolder)holder).method_LBL_username.setText(method.getUserName());

            ((FullMethodViewHolder)holder).method_LBL_ratings.setText(String.valueOf(method.getNumberOfVotes()));
            ((FullMethodViewHolder)holder).method_LBL_ratings.append(App.res.getText(R.string.ratings));

        }else{
            ((MethodViewHolder)holder).method_LBL_type.setText(method.getStainType().getString());

            if(auth.getCurrentUser() != null) {
                if (method.getSaves().containsKey(auth.getCurrentUser().getUid())) {
                    ((MethodViewHolder) holder).method_IMG_favorite.setImageResource(R.drawable.bookmark);
                } else {
                    ((MethodViewHolder) holder).method_IMG_favorite.setImageResource(R.drawable.save);
                }
            }

            ((MethodViewHolder)holder).method_LBL_supplies.setText(R.string.supplies);
            ((MethodViewHolder)holder).method_LBL_supplies.append(method.getSupplies());

            ((MethodViewHolder)holder).method_LBL_recommended.setText(R.string.recommended_for);
            ((MethodViewHolder)holder).method_LBL_recommended.append(method.getRecommended());

            ((MethodViewHolder)holder).method_LBL_username.setText(method.getUserName());

            ((MethodViewHolder)holder).method_LBL_ratings.setText(String.valueOf(method.getNumberOfVotes()));
            ((MethodViewHolder)holder).method_LBL_ratings.append(App.res.getText(R.string.ratings));

            ((MethodViewHolder)holder).method_RTG_rating.setRating(method.getRating());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).isFull())
            return 1;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return this.methods == null ? 0 : this.methods.size();
    }

    private Method getItem(int position){
        return this.methods.get(position);
    }


    public class MethodViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView method_LBL_type;
        private ShapeableImageView method_IMG_favorite;
        private MaterialTextView method_LBL_supplies;
        private MaterialTextView method_LBL_recommended;
        private MaterialTextView method_LBL_username;
        private MaterialTextView method_LBL_ratings;
        private RatingBar method_RTG_rating;

        public MethodViewHolder(@NonNull View itemView) {
            super(itemView);

            method_LBL_type = itemView.findViewById(R.id.method_LBL_type);
            method_IMG_favorite = itemView.findViewById(R.id.method_IMG_favorite);
            method_LBL_supplies = itemView.findViewById(R.id.method_LBL_supplies);
            method_LBL_recommended = itemView.findViewById(R.id.method_LBL_recommended);
            method_LBL_username = itemView.findViewById(R.id.method_LBL_username);
            method_LBL_ratings = itemView.findViewById(R.id.method_LBL_ratings);
            method_RTG_rating = itemView.findViewById(R.id.method_RTG_rating);

            itemView.setOnClickListener(v -> {
                changeViewType(getAdapterPosition());
            });

            method_IMG_favorite.setOnClickListener(v -> {
                SignalGenerator.getInstance().vibrate(100);
                onSaveClicked(dbRef.child(Constants.DBKeys.TYPES).child(getItem(getAdapterPosition()).getStainType().toString()).child(getItem(getAdapterPosition()).getmId()));
            });
        }
    }


    public class FullMethodViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView method_LBL_type;
        private ShapeableImageView method_IMG_favorite;
        private MaterialTextView method_LBL_supplies;
        private MaterialTextView method_LBL_recommended;
        private MaterialTextView method_LBL_description;
        private MaterialTextView method_LBL_username;
        private MaterialTextView method_LBL_ratings;
        private RatingBar method_RTG_rate;

        public FullMethodViewHolder(@NonNull View itemView) {
            super(itemView);

            method_LBL_type = itemView.findViewById(R.id.method_LBL_type);
            method_IMG_favorite = itemView.findViewById(R.id.method_IMG_favorite);
            method_LBL_supplies = itemView.findViewById(R.id.method_LBL_supplies);
            method_LBL_recommended = itemView.findViewById(R.id.method_LBL_recommended);
            method_LBL_description = itemView.findViewById(R.id.method_LBL_description);
            method_LBL_username = itemView.findViewById(R.id.method_LBL_username);
            method_LBL_ratings = itemView.findViewById(R.id.method_LBL_ratings);
            method_RTG_rate = itemView.findViewById(R.id.method_RTG_rate);

            itemView.setOnClickListener(v -> {
                changeViewType(getAdapterPosition());
            });

            method_IMG_favorite.setOnClickListener(v -> {
                SignalGenerator.getInstance().vibrate(100);
                onSaveClicked(dbRef.child(Constants.DBKeys.TYPES).child(getItem(getAdapterPosition()).getStainType().toString()).child(getItem(getAdapterPosition()).getmId()));
            });

            method_RTG_rate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                float newRating = method_RTG_rate.getRating();
                if(newRating != 0) {
                    Method method = getItem(getAdapterPosition());
                    DatabaseReference mRef = dbRef.child(Constants.DBKeys.TYPES).child(method.getStainType().toString()).child(method.getmId());
                    onStarClicked(mRef, newRating);
                }
            });
        }
    }

    private void onSaveClicked(DatabaseReference methRef){
        methRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Method method = snapshot.getValue(Method.class);
                if(method != null) {
                    if(auth.getCurrentUser() != null) {
                        Map<String,Boolean> map = method.getSaves();
                        if(map == null){
                            map = new HashMap<>();
                        }
                        if (map.containsKey(auth.getCurrentUser().getUid())){
                            map.remove(auth.getCurrentUser().getUid());
                            method.setSaves(map);
                            dbRef.child(Constants.DBKeys.USERS_FAVORITES).child(auth.getCurrentUser().getUid()).child(method.getmId()).removeValue();
                        }else{
                            map.put(auth.getCurrentUser().getUid(),true);
                            method.setSaves(map);
                            dbRef.child(Constants.DBKeys.USERS_FAVORITES).child(auth.getCurrentUser().getUid()).child(method.getmId()).setValue(true);
                        }
                    }
                    methRef.setValue(method);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void changeViewType(int position){
        Method method = getItem(position);
        method.setFull(!method.isFull());
        notifyItemChanged(position);
    }


    private void onStarClicked(DatabaseReference postRef, float newRating) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Method method = mutableData.getValue(Method.class);

                if (method == null) {
                    return Transaction.success(mutableData);
                }

                if(auth.getCurrentUser() != null) {
                    Map<String, Float> stars = method.getStars();
                    if(stars == null){
                        stars = new HashMap<>();
                    }
                    if (stars.containsKey(auth.getCurrentUser().getUid())) {
                        Float oldRating = stars.get(auth.getCurrentUser().getUid());
                        if(oldRating != null) {
                            if (oldRating != newRating) {
                                stars.replace(auth.getCurrentUser().getUid(), newRating);
                                method.setRatingSum((method.getRatingSum() - oldRating) + newRating);
                                method.setRating(method.getRatingSum() / method.getNumberOfVotes());
                                method.setStars(stars);
                            }
                        }
                    } else {
                        stars.put(auth.getCurrentUser().getUid(),newRating);
                        method.setRatingSum(method.getRatingSum() + newRating);
                        method.setNumberOfVotes(method.getNumberOfVotes() + 1);
                        method.setRating(method.getRatingSum()/method.getNumberOfVotes());
                        method.setStars(stars);
                    }
                    mutableData.setValue(method);
                    return Transaction.success(mutableData);
                }
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
            }
        });
    }


}


