package com.example.onspot.MainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onspot.Enums.StainType;
import com.example.onspot.MainActivity;
import com.example.onspot.MethodActivity;
import com.example.onspot.MethodsActivity;
import com.example.onspot.Models.Method;
import com.example.onspot.R;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMethodFragment extends Fragment {

    TextInputEditText add_ET_type;
    TextInputEditText add_ET_supplies;
    TextInputEditText add_ET_recommended;
    TextInputEditText add_ET_description;
    MaterialButton add_BTN_add;
    String supplies;
    String recommended;
    String description;
    StainType stainType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_method, container, false);

        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            String type = bundle.getString(MethodActivity.TYPE);
            stainType = StainType.valueOf(type);
        }
        add_ET_type.setText(stainType.getString());
        add_ET_type.setEnabled(false);


        add_BTN_add.setOnClickListener(v -> {
            supplies = String.valueOf(add_ET_supplies.getText());
            supplies = supplies.replaceAll("\n", " ").replaceAll("\\s+", " ");

            description = String.valueOf(add_ET_description.getText()).trim();
            description = description.replaceAll("\n", " ").replaceAll("\\s+", " ");

            recommended = String.valueOf(add_ET_recommended.getText()).trim();
            recommended = recommended.replaceAll("\n", " ").replaceAll("\\s+", " ");

            if(TextUtils.isEmpty(supplies)){
                SignalGenerator.getInstance().toast("Enter supplies", Toast.LENGTH_SHORT);
                return;
            }
            if(TextUtils.isEmpty(description)){
                SignalGenerator.getInstance().toast("Enter description", Toast.LENGTH_SHORT);
                return;
            }
            if(TextUtils.isEmpty(recommended)){
                recommended = getString(R.string.all);
            }
            addNewMethod();
        });
    }

    private void addNewMethod() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.DBKeys.TYPES).child(stainType.toString());
            String key = ref.push().getKey();

            Method newMet = new Method();
            newMet
                    .setmId(key)
                    .setStainType(stainType)
                    .setSupplies(supplies)
                    .setRecommended(recommended)
                    .setDescription(description)
                    .setUserName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            ref.child(key).setValue(newMet);
            SignalGenerator.getInstance().toast("Method added successfully", Toast.LENGTH_SHORT);
            if(getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void findViews(View view) {
        add_ET_type = view.findViewById(R.id.add_ET_type);
        add_ET_supplies = view.findViewById(R.id.add_ET_supplies);
        add_ET_recommended = view.findViewById(R.id.add_ET_recommended);
        add_ET_description = view.findViewById(R.id.add_ET_description);
        add_BTN_add = view.findViewById(R.id.add_BTN_add);
    }
}