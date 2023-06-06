package com.example.onspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.onspot.Adapters.MethodsAdapter;
import com.example.onspot.Enums.StainType;
import com.example.onspot.Models.Method;
import com.example.onspot.interfaces.MethodCallback;
import com.example.onspot.utilities.Constants;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SavedMethodsActivity extends AppCompatActivity {

    RecyclerView saved_LST_methods;

    MethodsAdapter methodsAdapter;

    ArrayList<Method> methods;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    String methodId;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_methods);

        ref = FirebaseDatabase.getInstance().getReference();
        findViews();
        initViews();
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        saved_LST_methods.setLayoutManager(linearLayoutManager);

        loadMethods();
    }

    private void loadMethods() {
        if (auth.getCurrentUser() != null) {
            methods = new ArrayList<>();
            methodsAdapter = new MethodsAdapter(methods);
            saved_LST_methods.setAdapter(methodsAdapter);
            ref.child(Constants.DBKeys.USERS_FAVORITES).child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    methods = new ArrayList<>();
                    if(!snapshot.hasChildren()){
                        methodsAdapter.setMethods(methods);
                        methodsAdapter.notifyDataSetChanged();
                    }
                    for (DataSnapshot data : snapshot.getChildren()) {
                        methodId = data.getKey();
                        findMethodById(methodId, new MethodCallback() {
                            @Override
                            public void onMethodFound(Method method) {
                                if (method != null)
                                    methods.add(method);
                                methodsAdapter.setMethods(methods);
                                methodsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onMethodNotFound() {
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void findViews() {
        saved_LST_methods = findViewById(R.id.saved_LST_methods);
    }


    private void findMethodById(String id, MethodCallback callback) {
        for (StainType type : StainType.values()) {
            Query findSaved = ref.child(Constants.DBKeys.TYPES).child(type.toString()).orderByChild(Constants.DBKeys.MID).equalTo(id);
            findSaved.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Method method = snapshot.getValue(Method.class);
                    if (method != null) {
                        callback.onMethodFound(method);
                    }
                    callback.onMethodNotFound();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Method method = snapshot.getValue(Method.class);
                    if(method != null) {
                        for (int i=0;i<methods.size();i++) {
                            if (method.getmId().equals(methods.get(i).getmId())){
                                methods.set(i,method);
                                methodsAdapter.setMethods(methods);
                                methodsAdapter.notifyItemChanged(i);
                            }
                        }
                    }
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

}