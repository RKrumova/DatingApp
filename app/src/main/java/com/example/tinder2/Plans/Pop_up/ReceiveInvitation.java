package com.example.tinder2.Plans.Pop_up;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
public class ReceiveInvitation {
    public void showPopUp(final View view){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        //Specify the length and width through constants
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;
        //final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


    }
}
