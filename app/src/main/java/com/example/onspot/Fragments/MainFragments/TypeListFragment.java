package com.example.onspot.Fragments.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onspot.Adapters.TypeAdapter;
import com.example.onspot.Enums.StainType;
import com.example.onspot.R;
import com.example.onspot.interfaces.SavedClicked_CallBack;
import com.example.onspot.interfaces.TypeClicked_CallBack;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

public class TypeListFragment extends Fragment {
    TextInputEditText type_ET_search;

    ShapeableImageView type_IMG_logout;

    MaterialButton type_BTN_saved;

    RecyclerView type_LST_types;

    TypeAdapter typeAdapter;

    ArrayList<StainType> allTypes = new ArrayList<>(Arrays.asList(StainType.values()));;

    ArrayList<StainType> filteredTypes = new ArrayList<>();

    private TypeClicked_CallBack typeClicked_callBack;

    public void setTypeClicked_callBack(TypeClicked_CallBack typeClicked_callBack){
        this.typeClicked_callBack = typeClicked_callBack;
    }

    private SavedClicked_CallBack savedClicked_callBack;

    public void setSavedClicked_callBack(SavedClicked_CallBack savedClicked_callBack){
        this.savedClicked_callBack = savedClicked_callBack;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_list, container, false);

        findViews(view);
        initViews(view);

        return view;
    }

    private void findViews(View view) {
        type_BTN_saved = view.findViewById(R.id.type_BTN_saved);
        type_ET_search = view.findViewById(R.id.type_ET_search);
        type_LST_types = view.findViewById(R.id.type_LST_types);
        type_IMG_logout = view.findViewById(R.id.type_IMG_logout);
    }

    private void initViews(View view) {

        type_IMG_logout.setOnClickListener(v -> {
            savedClicked_callBack.logOutClicked();
        });

        type_BTN_saved.setOnClickListener(v -> {
            savedClicked_callBack.savedBTNClicked();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        type_LST_types.setLayoutManager(linearLayoutManager);
        typeAdapter = new TypeAdapter(allTypes);
        type_LST_types.setAdapter(typeAdapter);
        typeAdapter.setTypeClicked_callBack(typeClicked_callBack);

        activateSearchBar();
    }

    private void activateSearchBar() {
        type_ET_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // not needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                filteredTypes.clear();

                if(s.toString().isEmpty()){
                    typeAdapter.setStainTypes(allTypes);
                }else{
                    Filter(s.toString());
                }
            }
        });
    }

    private void Filter(String str){
        for(StainType stainType : allTypes){
            if(stainType.toString().contains(str.toUpperCase())){
                filteredTypes.add(stainType);
            }
        }
        typeAdapter.setStainTypes(filteredTypes);
    }

}