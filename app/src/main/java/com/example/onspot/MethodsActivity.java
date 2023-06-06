package com.example.onspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.onspot.Adapters.MethodsAdapter;
import com.example.onspot.Enums.StainType;
import com.example.onspot.Models.Method;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MethodsActivity extends AppCompatActivity {

    RecyclerView methods_LST_methods;

    MethodsAdapter methodsAdapter;

    FloatingActionButton methods_FAB_add;

    StainType stainType;

    ArrayList<Method> methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_methods);

        findViews();
        initViews();
    }


    private void initViews() {
        Intent intent = getIntent();
        stainType = StainType.valueOf(intent.getStringExtra(MainActivity.TYPE));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        methods_LST_methods.setLayoutManager(linearLayoutManager);

        loadMethods();

        methods_FAB_add.setOnClickListener(v -> {
            Intent addIntent = new Intent(this,AddMethodActivity.class);
            addIntent.putExtra(MainActivity.TYPE,stainType.toString());
            startActivity(addIntent);
        });
    }

    private void loadMethods(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.DBKeys.TYPES).child(stainType.toString());
        methods = new ArrayList<>();
        methodsAdapter = new MethodsAdapter(methods);
        methods_LST_methods.setAdapter(methodsAdapter);

        Query orderMethods = ref.orderByChild(Constants.DBKeys.RATING);
        orderMethods.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Method method = snapshot.getValue(Method.class);
                methods.add(0,method);
                methodsAdapter.setMethods(methods);
                methodsAdapter.notifyItemInserted(0);
                methods_LST_methods.scrollToPosition(0);
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

    private void findViews() {
        methods_LST_methods = findViewById(R.id.methods_LST_methods);
        methods_FAB_add = findViewById(R.id.methods_FAB_add);
    }

}