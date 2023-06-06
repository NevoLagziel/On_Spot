package com.example.onspot.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onspot.Enums.StainType;
import com.example.onspot.R;
import com.example.onspot.interfaces.TypeClicked_CallBack;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {

    private ArrayList<StainType> types;

    private TypeClicked_CallBack typeClicked_callBack;

    public void setTypeClicked_callBack(TypeClicked_CallBack typeClicked_callBack){
        this.typeClicked_callBack = typeClicked_callBack;
    }

    public TypeAdapter(ArrayList<StainType> types){
        this.types = types;
    }

    public void setStainTypes(ArrayList<StainType> types){
        this.types = types;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stain_type, parent,false);
        TypeViewHolder typeViewHolder = new TypeViewHolder(view);
        return typeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.TypeViewHolder holder, int position) {
        StainType stainType = getItem(position);
        holder.main_LBL_type.setText(stainType.getString());
    }

    @Override
    public int getItemCount() {
        return this.types == null ? 0 : this.types.size();
    }

    private StainType getItem(int position){
        return this.types.get(position);
    }


    public class TypeViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView main_LBL_type;
        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);

            main_LBL_type = itemView.findViewById(R.id.main_LBL_type);
            itemView.setOnClickListener(v -> {
                if(typeClicked_callBack != null)
                    typeClicked_callBack.stainTypeClicked(getItem(getAdapterPosition()));
            });
        }
    }
}
