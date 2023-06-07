package com.example.onspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.onspot.Enums.StainType;
import com.example.onspot.Fragments.MainFragments.AddMethodFragment;
import com.example.onspot.Fragments.MainFragments.MethodListFragment;
import com.example.onspot.Fragments.MainFragments.TypeListFragment;
import com.example.onspot.interfaces.AddMethodClicked_CallBack;
import com.example.onspot.interfaces.SavedClicked_CallBack;
import com.example.onspot.interfaces.TypeClicked_CallBack;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static final String TYPE = "TYPE";

    public static final String LIST = "LIST";
    private boolean oneBackPress = false;

    TypeListFragment typeListFragment;

    MethodListFragment methodListFragment;

    AddMethodFragment addMethodFragment;

    FragmentManager fragmentManager;


    private TypeClicked_CallBack typeClicked_callBack = new TypeClicked_CallBack() {
        @Override
        public void stainTypeClicked(StainType stainType) {
            openMethodsList(stainType);
        }
    };

    private SavedClicked_CallBack savedClicked_callBack = new SavedClicked_CallBack() {
        @Override
        public void savedBTNClicked() {
            openSavedMethodsScreen();
        }

        @Override
        public void logOutClicked() {
            openLaunchActivity();
        }
    };

    private AddMethodClicked_CallBack addMethodClicked_callBack = new AddMethodClicked_CallBack() {
        @Override
        public void addMethodClicked(StainType stainType){
            openAddMethodScreen(stainType);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }


    private void findViews(){
        typeListFragment = new TypeListFragment();
        typeListFragment.setTypeClicked_callBack(typeClicked_callBack);
        typeListFragment.setSavedClicked_callBack(savedClicked_callBack);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.methods_FRM_layout,typeListFragment).commit();
    }


    private void openMethodsList(StainType stainType) {
        methodListFragment = new MethodListFragment();
        methodListFragment.setAddMethodClicked_callBack(addMethodClicked_callBack);

        Bundle bundle = new Bundle();
        bundle.putBoolean(LIST,true);                            //true for stain type list
        bundle.putString(TYPE,stainType.toString());
        methodListFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.methods_FRM_layout,methodListFragment ).addToBackStack( "back" ).commit();
    }


    private void openSavedMethodsScreen(){
        methodListFragment = new MethodListFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(LIST,false);                            // false for saved list
        methodListFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.methods_FRM_layout,methodListFragment ).addToBackStack( "back" ).commit();
    }

    private void openAddMethodScreen(StainType stainType){
        addMethodFragment = new AddMethodFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TYPE,stainType.toString());
        addMethodFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.methods_FRM_layout,addMethodFragment ).addToBackStack( "back" ).commit();
    }

    private void openLaunchActivity(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,LaunchActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        if(!typeListFragment.isVisible()){
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
