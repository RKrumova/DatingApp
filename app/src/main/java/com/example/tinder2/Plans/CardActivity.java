package com.example.tinder2.Plans;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.tinder2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CardActivity extends AppCompatActivity {
    private DatabaseReference dateReference = FirebaseDatabase.getInstance().getReference().child("plan");

    private TextView userTV;
    private TextView userInfoTV;
    private ImageView userPic;

    private EditText editTDate;
    private  EditText editAddress;
    private Button rescheduleButton;
    private Button cancel_date_button;
    private Button yes_date_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        userTV.findViewWithTag(2131296855);
        //userTV.findViewById(R.id.userTV);
        userInfoTV.findViewById(R.id.userInfoTV);
        userPic.findViewById(R.id.imageViewPopUp);
        editTDate.findViewById(R.id.editTDate);
        editAddress.findViewById(R.id.editAddress);
        rescheduleButton.findViewById(R.id.reschedule_button);
        cancel_date_button.findViewById(R.id.cancel_date_button);
        yes_date_button.findViewById(R.id.yes_date_button);

        //

    }

    /**What is better:
     *  one method being flexible but more hard to read
     *  two method but easier to red
     *  */

    public void somethingHappening(final  View view,String username, String user){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.activity_card, null);
        final  PopupWindow popupWindow = new PopupWindow(popUpView);
        View swipeView = inflater.inflate(R.layout.activity_swipe, null);
        popupWindow.showAtLocation(swipeView, Gravity.CENTER, 0,0); //Location on screen

        //dont crash
        //setContentView(R.layout.activity_card);
        userTV.findViewWithTag(2131296855);
        //userTV.findViewById(R.id.userTV);
        userInfoTV.findViewById(R.id.userInfoTV);
        userPic.findViewById(R.id.imageViewPopUp);
        editTDate.findViewById(R.id.editTDate);
        editAddress.findViewById(R.id.editAddress);
        rescheduleButton.findViewById(R.id.reschedule_button);
        cancel_date_button.findViewById(R.id.cancel_date_button);
        yes_date_button.findViewById(R.id.yes_date_button);
        //
        cancel_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no(username, user);
            }
        });
        yes_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes(username, user);
            }
        });
    }
    // DB Logic
    private static void yes(String username, String user){

    };
    private static void no(String username, String user){

    };
             /**RESCHEDULE*/
    private static void maybe(String username, String user){

    };
}