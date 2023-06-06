package com.example.onspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.onspot.Enums.StainType;
import com.example.onspot.LaunchFragments.LaunchFragment;
import com.example.onspot.LaunchFragments.SignupFragment;
import com.example.onspot.MainFragments.TypeListFragment;
import com.example.onspot.interfaces.TypeClicked_CallBack;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.textview.MaterialTextView;

public class MethodActivity extends AppCompatActivity {

    private boolean oneBackPress = false;

    TypeListFragment typeListFragment;

    FragmentManager fragmentManager;

    MaterialTextView methods_MTV_pagename;

    private TypeClicked_CallBack typeClicked_callBack = new TypeClicked_CallBack() {
        @Override
        public void stainTypeClicked(StainType stainType) {
            openMethodsList();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method);

        findViews();
    }

    private void findViews(){
        typeListFragment = new TypeListFragment();
        typeListFragment.setTypeClicked_callBack(typeClicked_callBack);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.methods_FRM_layout,typeListFragment).commit();
    }

    private void openMethodsList() {
        signupFragment = new SignupFragment();
        signupFragment.setLogIn_CallBack(logIn_callBack);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.launch_FRM_layout,signupFragment ).addToBackStack( "back" ).commit();
    }

    public void onBackPressed() {         // dont forget to fix what to do in every fragment
        if(oneBackPress){
            super.onBackPressed();
            return;
        }
        SignalGenerator.getInstance().toast(Constants.ToastMsg.PRESS_AGAIN_TO_EXIT, Toast.LENGTH_SHORT);
        oneBackPress = true;
        new Handler().postDelayed(() -> oneBackPress = false,2000);
    }
}
