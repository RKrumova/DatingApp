package com.example.tinder2.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
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

import com.example.tinder2.ChatActivity;
import com.example.tinder2.R;
import com.example.tinder2.SwipeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class SettingActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String username;
    boolean isNew;
    //profile oic

    private ImageView profile_imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    //
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
    private Button saveChangesButton;
    private String birthDate;
    private boolean isMaleChecked;
    private boolean isFemaleChecked;
    private boolean isOtherChecked;
    private String gender;
    private String location;
    private String selectedSexuality;
    private boolean isSexualOrientationChecked;
    //    boolean isLocationChecked = showLocationCheckBox.isChecked();
//    boolean isGenderChecked = showGenderCheckBox.isChecked();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Request no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set flags for full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_setting_profile);
        mLoadingBar = new ProgressDialog(SettingActivity.this);
        profile_imageView = findViewById(R.id.profilePicCurrentUser);
        birthDateTV = findViewById(R.id.birthDateTV);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        otherRadioButton = findViewById(R.id.otherRadioButton);
        addressTV = findViewById(R.id.address_TV);
        sexualitySpinner = findViewById(R.id.sexualitySpinner);
        showLocationCheckBox = findViewById(R.id.showLocationCheckBox);
        showGenderCheckBox = findViewById(R.id.showGenderCheckBox);
        saveChangesButton = findViewById(R.id.SaveChangesbutton);

        Button messagesButton = findViewById(R.id.messagesButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button datesButton = findViewById(R.id.datesButton);
        Button swipesButton = findViewById(R.id.swipesButton);
        //sexuality
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sexuality_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexualitySpinner.setAdapter(adapter);
        //
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        isNew = intent.getBooleanExtra("isNew", false);
        if(isNew != true){
            //loadUserData();
        }
        //upload pic
        profile_imageView.setOnClickListener(view -> {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, PICK_IMAGE_REQUEST);
            });

        saveChangesButton.setOnClickListener(view -> checkCredentials());
        //
        messagesButton.setOnClickListener(v->{
            startActivity(new Intent(SettingActivity.this, ChatActivity.class));
        });
        swipesButton.setOnClickListener(v->{
            startActivity(new Intent(SettingActivity.this, SwipeActivity.class));
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profile_imageView.setImageURI(imageUri);
        }
    }
    private void saveImage(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users");
        UploadTask uploadTask = storageReference.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Store the image URL in the user's profile data
                        databaseReference.child("users").child(username).child("profile_pic").setValue(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    Log.e("Upload", "Image upload failed: " + e.getMessage());
                });
    }
    //
    private void loadUserData(){
        if (!MemoryData.getData(this).isEmpty()) {
            databaseReference.child("users").child(username);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // DO profile pic
                        gender = snapshot.child("gender").getValue(String.class);
                        selectedSexuality = snapshot.child("sexual_orientation").getValue(String.class);
                        location = snapshot.child("location").getValue(String.class);
                        birthDate = snapshot.child("birthdate").getValue(String.class);
                        applyData(gender, selectedSexuality, location, birthDate);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    private void applyData(String gender, String sexuality, String location, String birthdate) {
        if (gender.equals("male")) {
            maleRadioButton.setChecked(true);
        } else if (gender.equals("female")) {
            femaleRadioButton.setChecked(true);
        } else {
            otherRadioButton.setChecked(true);
        }
        int sexualityPosition = ((ArrayAdapter<String>) sexualitySpinner.getAdapter()).getPosition(sexuality);
        if (sexualityPosition != -1) {
            sexualitySpinner.setSelection(sexualityPosition);
        }
        if(location != null && birthdate != null) {
            addressTV.setText(location);
            birthDateTV.setText(birthdate);
        }
    }

    public void checkCredentials() {

        //maybe
        birthDate = birthDateTV.getText().toString();
        isMaleChecked = maleRadioButton.isChecked();
        isFemaleChecked = femaleRadioButton.isChecked();
        isOtherChecked = otherRadioButton.isChecked();
        location = addressTV.getText().toString();
        selectedSexuality = sexualitySpinner.getSelectedItem().toString();
        //check here
        if(imageUri != null){
            saveImage();
        }
        if (!isAgeValid(birthDate)) {
            showError(birthDateTV, "You must be at least 18 years old");
        } else if (!isMaleChecked && !isFemaleChecked && !isOtherChecked || isMaleChecked && isFemaleChecked && !isOtherChecked ||
                   !isMaleChecked && isFemaleChecked && isOtherChecked || isMaleChecked && !isFemaleChecked && isOtherChecked
                    || isMaleChecked && isFemaleChecked && isOtherChecked) {
            showGenderError();
        } else if (location.trim().isEmpty()) {
            showError(addressTV, "Please enter a valid address");
        } else {
            mLoadingBar.setTitle("Updating");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            if (profile_imageView != null) {
                //for not nothing
                System.out.println();
            }
            if (isMaleChecked) {
                gender = "male";
            } else if (isFemaleChecked) {
                gender = "female";
            } else {
                gender = "other";
            }
            saveData();
        }
    }

    private void saveData(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("users").child(username).child("gender").setValue(gender);
                databaseReference.child("users").child(username).child("sexual_orientation").setValue(selectedSexuality);
                databaseReference.child("users").child(username).child("location").setValue(location);
                databaseReference.child("users").child(username).child("birthdate").setValue(birthDate);
                MemoryData.saveData(username, SettingActivity.this);
                //MemoryData.saveData(username, SettingActivity.this);

                mLoadingBar.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mLoadingBar.dismiss();
            }
        });
        Intent intent = new Intent(SettingActivity.this, SwipeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(username, "username");
        startActivity(intent);
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