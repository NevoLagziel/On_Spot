package com.example.onspot.LaunchFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onspot.R;
import com.example.onspot.interfaces.Launch_CallBack;
import com.google.android.material.button.MaterialButton;


public class LaunchFragment extends Fragment {

    MaterialButton launch_BTN_login;
    MaterialButton launch_BTN_signup;

    private Launch_CallBack launch_callBack;

    public void setLaunch_CallBack(Launch_CallBack launch_callBack){
        this.launch_callBack = launch_callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_launch, container, false);

        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        launch_BTN_login.setOnClickListener(v -> {
            launch_callBack.logIn();
        });

        launch_BTN_signup.setOnClickListener(v -> {
            launch_callBack.signUp();
        });
    }

    private void findViews(View view) {
        launch_BTN_login = view.findViewById(R.id.launch_BTN_login);
        launch_BTN_signup = view.findViewById(R.id.launch_BTN_signup);
    }


}