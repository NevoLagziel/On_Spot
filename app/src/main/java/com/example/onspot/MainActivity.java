package com.example.onspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.onspot.Adapters.TypeAdapter;
import com.example.onspot.Enums.StainType;
import com.example.onspot.interfaces.TypeClicked_CallBack;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private boolean oneBackPress = false;
    public static final String TYPE = "TYPE";
    TextInputEditText main_ET_search;

    ShapeableImageView main_IMG_logout;

    MaterialButton main_BTN_saved;

    RecyclerView main_LST_types;

    TypeAdapter typeAdapter;

    ArrayList<StainType> allTypes = new ArrayList<>(Arrays.asList(StainType.values()));;

    ArrayList<StainType> filteredTypes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
    }

    private void initViews() {
        main_IMG_logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,LaunchActivity.class);
            startActivity(intent);
            finish();
        });

        main_BTN_saved.setOnClickListener(v -> {
            goToSavedScreen();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_types.setLayoutManager(linearLayoutManager);
        typeAdapter = new TypeAdapter(allTypes);
        main_LST_types.setAdapter(typeAdapter);

        typeAdapter.setTypeClicked_callBack((stainType) -> MainActivity.this.openMethodsList(stainType));

        main_ET_search.addTextChangedListener(new TextWatcher() {
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

    private void goToSavedScreen() {
        Intent intent = new Intent(this,SavedMethodsActivity.class);
        startActivity(intent);
    }

    private void Filter(String str){
        for(StainType stainType : allTypes){
            if(stainType.toString().contains(str.toUpperCase())){
                filteredTypes.add(stainType);
            }
        }
        typeAdapter.setStainTypes(filteredTypes);
    }

    private void findViews() {
        main_BTN_saved = findViewById(R.id.main_BTN_saved);
        main_ET_search = findViewById(R.id.main_ET_search);
        main_LST_types = findViewById(R.id.main_LST_types);
        main_IMG_logout = findViewById(R.id.main_IMG_logout);
    }

    private void openMethodsList(StainType stainType){
        Intent intent = new Intent(this,MethodsActivity.class);
        intent.putExtra(TYPE,stainType.toString());
        startActivity(intent);
    }

    public void onBackPressed() {
        if(oneBackPress){
            super.onBackPressed();
            return;
        }
        SignalGenerator.getInstance().toast(Constants.ToastMsg.PRESS_AGAIN_TO_EXIT, Toast.LENGTH_SHORT);
        oneBackPress = true;
        new Handler().postDelayed(() -> oneBackPress = false,2000);
    }
}