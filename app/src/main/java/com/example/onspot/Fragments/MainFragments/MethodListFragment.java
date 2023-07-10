package com.example.onspot.Fragments.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.onspot.Adapters.MethodsAdapter;
import com.example.onspot.Enums.StainType;
import com.example.onspot.MainActivity;
import com.example.onspot.Models.Method;
import com.example.onspot.R;
import com.example.onspot.interfaces.AddMethodClicked_CallBack;
import com.example.onspot.interfaces.MethodCallback;
import com.example.onspot.utilities.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MethodListFragment extends Fragment {

    RecyclerView methodlist_LST_methodlist;

    MaterialTextView methodlist_MTV_title;

    FloatingActionButton methodlist_FAB_add;

    MethodsAdapter methodsAdapter;

    StainType stainType;

    ArrayList<Method> methods;

    private boolean listType;        // saved or stain type list
    DatabaseReference ref;

    FirebaseAuth auth;

    LottieAnimationView methodlist_ANIM_lottie;

    private AddMethodClicked_CallBack addMethodClicked_callBack;

    public void setAddMethodClicked_callBack(AddMethodClicked_CallBack addMethodClicked_callBack){
        this.addMethodClicked_callBack = addMethodClicked_callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_method_list, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        findViews(view);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        methodlist_ANIM_lottie.setVisibility(View.VISIBLE);
        methodlist_ANIM_lottie.resumeAnimation();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        methodlist_LST_methodlist.setLayoutManager(linearLayoutManager);

        Bundle bundle = getArguments();
        if(bundle != null) {
            listType = bundle.getBoolean(MainActivity.LIST);          // determine what list to open

            if (listType) {
                String type = bundle.getString(MainActivity.TYPE);
                stainType = StainType.valueOf(type);                  // what stain type list to open
                startListTypePage(stainType);
            } else {
                startSavedPage();
            }
        }
    }

    private void startListTypePage(StainType stainType){
        methodlist_MTV_title.setText(stainType.getString());
        methodlist_FAB_add.setVisibility(View.VISIBLE);
        methodlist_FAB_add.setOnClickListener(v -> {
            addMethodClicked_callBack.addMethodClicked(stainType);
        });
        checkForData();
    }

    private void checkForData(){
        ref.child(Constants.DBKeys.TYPES).child(stainType.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loadMethods();
                }else {
                    methodlist_ANIM_lottie.cancelAnimation();
                    methodlist_ANIM_lottie.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void startSavedPage(){
        methodlist_MTV_title.setText(R.string.saved_methods);
        loadSavedMethods();
    }

    private void loadMethods(){
        methods = new ArrayList<>();
        methodsAdapter = new MethodsAdapter(methods);
        methodlist_LST_methodlist.setAdapter(methodsAdapter);

        Query orderMethods = ref.child(Constants.DBKeys.TYPES).child(stainType.toString()).orderByChild(Constants.DBKeys.RATING);
        orderMethods.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Method method = snapshot.getValue(Method.class);
                methods.add(0,method);
                methodsAdapter.setMethods(methods);
                methodlist_ANIM_lottie.cancelAnimation();
                methodlist_ANIM_lottie.setVisibility(View.INVISIBLE);
                methodsAdapter.notifyItemInserted(0);
                methodlist_LST_methodlist.scrollToPosition(0);
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

    private void findViews(View view) {
        methodlist_LST_methodlist = view.findViewById(R.id.methodlist_LST_methodlist);
        methodlist_MTV_title = view.findViewById(R.id.methodlist_MTV_title);
        methodlist_ANIM_lottie = view.findViewById(R.id.methodlist_ANIM_lottie);
        methodlist_FAB_add = view.findViewById(R.id.methodlist_FAB_add);
    }

    private void loadSavedMethods() {
        if (auth.getCurrentUser() != null) {
            methods = new ArrayList<>();
            methodsAdapter = new MethodsAdapter(methods);
            methodlist_LST_methodlist.setAdapter(methodsAdapter);
            ref.child(Constants.DBKeys.USERS_FAVORITES).child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    methods = new ArrayList<>();
                    if(!snapshot.hasChildren()) {
                        methodsAdapter.setMethods(methods);
                        methodsAdapter.notifyDataSetChanged();
                        methodlist_ANIM_lottie.cancelAnimation();
                        methodlist_ANIM_lottie.setVisibility(View.INVISIBLE);
                    }
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String methodId = data.getKey();
                        findMethodById(methodId, new MethodCallback() {
                            @Override
                            public void onMethodFound(Method method) {
                                if (method != null) {
                                    methods.add(method);
                                    methodlist_ANIM_lottie.cancelAnimation();
                                    methodlist_ANIM_lottie.setVisibility(View.INVISIBLE);
                                }
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