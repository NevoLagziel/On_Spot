package com.example.onspot.LaunchFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onspot.R;
import com.example.onspot.interfaces.LogIn_CallBack;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupFragment extends Fragment {


    private FirebaseAuth mAuth;
    TextInputEditText signup_ET_email;
    TextInputEditText signup_ET_password;
    TextInputEditText signup_ET_firstname;
    TextInputEditText signup_ET_lastname;
    MaterialButton signup_BTN_signup;
    String email;
    String password;
    String firstname;
    String lastname;

    private LogIn_CallBack logIn_callBack;
    public void setLogIn_CallBack (LogIn_CallBack logIn_callBack){
        this.logIn_callBack = logIn_callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mAuth = FirebaseAuth.getInstance();
        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        signup_BTN_signup.setOnClickListener(v -> {
            signUp();
        });
    }

    private void signUp() {
        email = String.valueOf(signup_ET_email.getText());
        password = String.valueOf(signup_ET_password.getText());
        firstname = String.valueOf(signup_ET_firstname.getText());
        lastname = String.valueOf(signup_ET_lastname.getText());

        if(TextUtils.isEmpty(email)){
            SignalGenerator.getInstance().toast(Constants.ToastMsg.EMAIL_ERR, Toast.LENGTH_SHORT);
            return;
        }

        if(TextUtils.isEmpty(password)){
            SignalGenerator.getInstance().toast(Constants.ToastMsg.PASSWORD_ERR, Toast.LENGTH_SHORT);
            return;
        }

        if(TextUtils.isEmpty(firstname)){
            SignalGenerator.getInstance().toast(Constants.ToastMsg.FIRST_NAME_ERR, Toast.LENGTH_SHORT);
            return;
        }

        if(TextUtils.isEmpty(lastname)){
            SignalGenerator.getInstance().toast(Constants.ToastMsg.LAST_NAME_ERR, Toast.LENGTH_SHORT);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user != null) {
                            user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(
                                    firstname.substring(0, 1).toUpperCase() + "" + firstname.substring(1).toLowerCase() + " " +
                                            lastname.substring(0, 1).toUpperCase() + "" + lastname.substring(1).toLowerCase()).build());
                            logIn_callBack.logInToApp(user);
                        }
                    } else {
                        SignalGenerator.getInstance().toast(task.getException().getMessage(),Toast.LENGTH_SHORT);
                    }
                });
    }

    private  void findViews(View view){
        signup_ET_email = view.findViewById(R.id.signup_ET_email);
        signup_ET_password  = view.findViewById(R.id.signup_ET_password);
        signup_ET_firstname = view.findViewById(R.id.signup_ET_firstname);
        signup_ET_lastname = view.findViewById(R.id.signup_ET_lastname);
        signup_BTN_signup = view.findViewById(R.id.signup_BTN_signup);
    }

}