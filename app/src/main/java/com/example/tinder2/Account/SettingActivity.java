package com.example.tinder2.Account;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import com.example.tinder2.R;
import org.apache.commons.lang3.time.DateUtils;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;



public class SettingActivity extends AppCompatActivity {
    private ImageView profile_imageView;
    private static EditText birthDateTV;
    private static RadioButton maleRadioButton;
    private static RadioButton femaleRadioButton;
    private static RadioButton otherRadioButton;
    private static AutoCompleteTextView addressTV;
    private static Spinner sexualitySpinner;
    private static CheckBox showSexualOrientationCheckBox;
    private static CheckBox showLocationCheckBox;
    private static CheckBox showGenderCheckBox;
    private Button saveChangesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
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
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

    }
    public static void checkCredentials(){
        String birthDate = birthDateTV.getText().toString();
        boolean isMaleChecked = maleRadioButton.isChecked();
        boolean isFemaleChecked = femaleRadioButton.isChecked();
        boolean isOtherChecked = otherRadioButton.isChecked();
        String address = addressTV.getText().toString();
        String selectedSexuality = sexualitySpinner.getSelectedItem().toString();
        boolean isSexualOrientationChecked = showSexualOrientationCheckBox.isChecked();
        boolean isLocationChecked = showLocationCheckBox.isChecked();
        boolean isGenderChecked = showGenderCheckBox.isChecked();

        if (!isAgeValid(birthDate)) {
            showError(birthDateTV, "You must be at least 18 years old");
        } else if (!isMaleChecked && !isFemaleChecked && !isOtherChecked) {
            showGenderError();
        } else if (address.trim().isEmpty()) {
            showError(addressTV, "Please enter a valid address");
        } else if (!isSexualOrientationChecked) {
            showSexualOrientationError();
        } else {
        }
    }
    private static boolean isAgeValid(String birthDate) {
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

        // if an error occurred or  age is less 18
        return false;
    }
    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    //  error message for gender
    private static void showGenderError() {
        maleRadioButton.setError("Select a gender");
        femaleRadioButton.setError(null); // Remove error from femaleRadioButton if previously set
        otherRadioButton.setError(null); // Remove error from otherRadioButton if previously set
    }

    // Error message for sexual orientation
    private static void showSexualOrientationError() {
        showSexualOrientationCheckBox.setError("Select your sexual orientation");
    }

}