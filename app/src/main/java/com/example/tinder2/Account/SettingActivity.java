package com.example.tinder2.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tinder2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class SettingActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String username;
    private EditText birthDateTV;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private RadioButton otherRadioButton;
    private AutoCompleteTextView addressTV;
    private Spinner sexualitySpinner;
    private CheckBox showSexualOrientationCheckBox;
    private CheckBox showLocationCheckBox;
    private CheckBox showGenderCheckBox;
    private ProgressDialog mLoadingBar;
     private ImageView profile_imageView; //
    private Button saveChangesButton;
    String birthDate;
    boolean isMaleChecked;
    boolean isFemaleChecked;
    boolean isOtherChecked;
    String location;
    String selectedSexuality;
    boolean isSexualOrientationChecked;
//    boolean isLocationChecked = showLocationCheckBox.isChecked();
//    boolean isGenderChecked = showGenderCheckBox.isChecked();
    String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =getIntent();
        username = intent.getStringExtra("username");

        setContentView(R.layout.activity_setting_profile);
        mLoadingBar = new ProgressDialog(SettingActivity.this);
        profile_imageView = findViewById(R.id.profilePicCurrentUser);
        birthDateTV = findViewById(R.id.birthDateTV);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        otherRadioButton = findViewById(R.id.otherRadioButton);
        addressTV = findViewById(R.id.address_TV);
        sexualitySpinner = findViewById(R.id.sexualitySpinner);
        showSexualOrientationCheckBox = findViewById(R.id.showSexualOrientationCheckBox);
        showLocationCheckBox = findViewById(R.id.showLocationCheckBox);
        showGenderCheckBox = findViewById(R.id.showGenderCheckBox);
        saveChangesButton = findViewById(R.id.SaveChangesbutton);
        //sexuality
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sexuality_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexualitySpinner.setAdapter(adapter);


        //if account already register and set up
        if(!MemoryData.getData(this).isEmpty()){
            databaseReference.child("users").child(username);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        // DO profile pic
                        gender = snapshot.child("gender").getValue(String.class);
                        selectedSexuality = snapshot.child("sexual_orientation").getValue(String.class);
                        location = snapshot.child("location").getValue(String.class);
                        birthDate = snapshot.child("birthdate").getValue(String.class);
                        loadUserData(gender, selectedSexuality, location, birthDate);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        saveChangesButton.setOnClickListener(view -> checkCredentials());

    }

    private void loadUserData(String gender, String sexuality, String location, String birthdate) {
        if(gender.equals("male")){
            maleRadioButton.setChecked(true);
        } else if(gender.equals("female")){
            femaleRadioButton.setChecked(true);
        } else{
            otherRadioButton.setChecked(true);
        }
        int sexualityPosition = ((ArrayAdapter<String>) sexualitySpinner.getAdapter()).getPosition(sexuality);
        if(sexualityPosition != -1){
            sexualitySpinner.setSelection(sexualityPosition);
        }
        addressTV.setText(location);
        birthDateTV.setText(birthdate);
    }
//check box error

    public void checkCredentials() {

        //maybe
        birthDate = birthDateTV.getText().toString();
        isMaleChecked = maleRadioButton.isChecked();
        isFemaleChecked = femaleRadioButton.isChecked();
        isOtherChecked = otherRadioButton.isChecked();
        location = addressTV.getText().toString();
        selectedSexuality = sexualitySpinner.getSelectedItem().toString();


        if (!isAgeValid(birthDate)) {
            showError(birthDateTV, "You must be at least 18 years old");
        } else if ((!isMaleChecked && !isFemaleChecked && !isOtherChecked) //nothing is checked
                || (isMaleChecked && isFemaleChecked && !isOtherChecked)  //Male and Female
                || (!isMaleChecked && isFemaleChecked && isOtherChecked) //Female and Other
                || (isMaleChecked && !isFemaleChecked && isOtherChecked) //Male and other
                || (isMaleChecked && isFemaleChecked && isOtherChecked)) { //all three
            showGenderError();
        } else if (location.trim().isEmpty()) {
            showError(addressTV, "Please enter a valid address");
        } else if (!isSexualOrientationChecked) {
            showSexualOrientationError();
        } else {
            mLoadingBar.setTitle("Updating");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            if(profile_imageView != null){
                //for not nothing
                System.out.println();
            }
            if(isMaleChecked){
                gender =  "male";
            } else if(isFemaleChecked){
                gender = "female";
            } else{
                gender = "other";
            }
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.child("users").child(username).child("profile_pic").setValue("");
                    databaseReference.child("users").child(username).child("gender").setValue(gender);
                    databaseReference.child("users").child(username).child("sexual_orientation").setValue(selectedSexuality);
                    databaseReference.child("users").child(username).child("location").setValue(location);
                    databaseReference.child("users").child(username).child("birthdate").setValue(birthDate);
                    MemoryData.saveData(username, SettingActivity.this);
                    //????MemoryData.saveName(nameTxt, SettingActivity.this);
                    mLoadingBar.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mLoadingBar.dismiss();
                }
            });
        }
    }
    private boolean isAgeValid(String birthDate) {
        try {
            Date dateOfBirth = DateUtils.parseDate(birthDate, "yyyy-MM-dd");
            Date currentDate = new Date();

            // Calculate the age
            int age = DateUtils.toCalendar(currentDate).get(Calendar.YEAR) - DateUtils.toCalendar(dateOfBirth).get(Calendar.YEAR);
            if (DateUtils.toCalendar(currentDate).get(Calendar.DAY_OF_YEAR) <
                    DateUtils.toCalendar(dateOfBirth).get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age >= 18;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // error occurred or  age is less 18
        return false;
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    //  error message for gender
    private void showGenderError() {
        maleRadioButton.setError("Select a gender");
        femaleRadioButton.setError(null); // Remove error from femaleRadioButton if previously set
        otherRadioButton.setError(null); // Remove error from otherRadioButton if previously set
    }

    // Error message for sexual orientation
    private void showSexualOrientationError() {
        showSexualOrientationCheckBox.setError("Select your sexual orientation");
    }


}