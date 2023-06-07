package com.example.onspot.Fragments.LaunchFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.onspot.R;
import com.example.onspot.interfaces.LogIn_CallBack;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    TextInputEditText login_ET_email;
    TextInputEditText login_ET_password;
    MaterialButton login_BTN_login;
    String email;
    String password;

    private LogIn_CallBack logIn_callBack;

    public void setLogIn_CallBack (LogIn_CallBack logIn_callBack){
        this.logIn_callBack = logIn_callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        login_BTN_login.setOnClickListener(v -> {
            logIn();
        });


    }

    private void logIn() {
        email = String.valueOf(login_ET_email.getText());
        password = String.valueOf(login_ET_password.getText());

        if(TextUtils.isEmpty(email)){
            SignalGenerator.getInstance().toast(Constants.ToastMsg.EMAIL_ERR, Toast.LENGTH_SHORT);
            return;
        }

        if(TextUtils.isEmpty(password)){
            SignalGenerator.getInstance().toast(Constants.ToastMsg.PASSWORD_ERR, Toast.LENGTH_SHORT);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        logIn_callBack.logInToApp(user);
                    } else {
                        SignalGenerator.getInstance().toast(task.getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }


    private void findViews(View view) {
        login_BTN_login = view.findViewById(R.id.login_BTN_login);
        login_ET_email = view.findViewById(R.id.login_ET_email);
        login_ET_password = view.findViewById(R.id.login_ET_password);
    }


}