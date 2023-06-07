package com.example.onspot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.onspot.Fragments.LaunchFragments.LaunchFragment;
import com.example.onspot.Fragments.LaunchFragments.LoginFragment;
import com.example.onspot.Fragments.LaunchFragments.SignupFragment;
import com.example.onspot.interfaces.Launch_CallBack;
import com.example.onspot.interfaces.LogIn_CallBack;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchActivity extends AppCompatActivity {

    private boolean oneBackPress = false;
    LoginFragment loginFragment;

    SignupFragment signupFragment;

    LaunchFragment launchFragment;
    
    FragmentManager fragmentManager;

    private LogIn_CallBack logIn_callBack = user -> goToMainActivity();

    private Launch_CallBack launch_callBack = new Launch_CallBack() {
        @Override
        public void logIn() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null)
                goToMainActivity();
            else
                switchToLogIn();
        }

        @Override
        public void signUp() {
            switchToSignUp();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        findViews();
    }

    private void findViews() {
        launchFragment = new LaunchFragment();
        launchFragment.setLaunch_CallBack(launch_callBack);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.launch_FRM_layout,launchFragment).commit();
    }

    private void switchToLogIn(){
        loginFragment = new LoginFragment();
        loginFragment.setLogIn_CallBack(logIn_callBack);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.launch_FRM_layout,loginFragment ).addToBackStack( "back" ).commit();
    }

    private void switchToSignUp(){
        signupFragment = new SignupFragment();
        signupFragment.setLogIn_CallBack(logIn_callBack);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.launch_FRM_layout,signupFragment ).addToBackStack( "back" ).commit();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if(!launchFragment.isVisible()){
            super.onBackPressed();
            return;
        }
        if(oneBackPress){
            super.onBackPressed();
            return;
        }
        SignalGenerator.getInstance().toast(Constants.ToastMsg.PRESS_AGAIN_TO_EXIT, Toast.LENGTH_SHORT);
        oneBackPress = true;
        new Handler().postDelayed(() -> oneBackPress = false,2000);

    }
}