package com.example.onspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.onspot.Enums.StainType;
import com.example.onspot.Models.Method;
import com.example.onspot.utilities.Constants;
import com.example.onspot.utilities.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMethodActivity extends AppCompatActivity {


    TextInputEditText add_ET_type;
    TextInputEditText add_ET_supplies;
    TextInputEditText add_ET_recommended;
    TextInputEditText add_ET_description;
    MaterialButton add_BTN_add;
    String supplies;
    String recommended;
    String description;
    StainType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_method);

        findViews();
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        type = StainType.valueOf(intent.getStringExtra(MainActivity.TYPE));

        add_ET_type.setText(type.getString());
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
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.DBKeys.TYPES).child(type.toString());
            String key = ref.push().getKey();

            Method newMet = new Method();
            newMet
                    .setmId(key)
                    .setStainType(type)
                    .setSupplies(supplies)
                    .setRecommended(recommended)
                    .setDescription(description)
                    .setUserName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            ref.child(key).setValue(newMet);
            SignalGenerator.getInstance().toast("Method added successfully", Toast.LENGTH_SHORT);
            finish();
        }
    }

    private void goBackToMethodsList() {
        Intent intent = new Intent(this,MethodsActivity.class);
        intent.putExtra(MainActivity.TYPE,type.toString());
        startActivity(intent);
        finish();
    }

    private void findViews() {
        add_ET_type = findViewById(R.id.add_ET_type);
        add_ET_supplies = findViewById(R.id.add_ET_supplies);
        add_ET_recommended = findViewById(R.id.add_ET_recommended);
        add_ET_description = findViewById(R.id.add_ET_description);
        add_BTN_add = findViewById(R.id.add_BTN_add);
    }
}