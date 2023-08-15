package com.example.tinder2.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinder2.Account.MemoryData;
import com.example.tinder2.R;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {
    private List<ConversationList> conversationLists;
    private final Context context;
    private String user;

    public ConversationAdapter(List<ConversationList> conversationLists, Context context) {
        this.conversationLists = conversationLists;
        this.context = context;
        this.user = MemoryData.getData(context);
    }

    @NonNull
    @Override
    public ConversationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversation_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.MyViewHolder holder, int position) {
        ConversationList convoList = conversationLists.get(position);
        if(convoList.getUsername().equals(user)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myMessage.setText(convoList.getMessage());
            holder.myTime.setText(convoList.getDate() + " " + convoList.getTime());
        } else{
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.oppoMessage.setText(convoList.getMessage());
            holder.oppoTime.setText(convoList.getDate() + " " + convoList.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return conversationLists.size();
    }
    public void updateConvoList(List<ConversationList> conversationLists){
        this.conversationLists = conversationLists;

    }
    static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout oppoLayout;
        private LinearLayout myLayout;
        private TextView oppoMessage;
        private TextView myMessage;
        private TextView oppoTime;
        private  TextView myTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oppoLayout = itemView.findViewById(R.id.opposideLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            oppoTime  = itemView.findViewById(R.id.oppoMessageTime);
            myLayout = itemView.findViewById(R.id.myLayout);
            myMessage  = itemView.findViewById(R.id.myMessage);
            myTime = itemView.findViewById(R.id.myMessageTime);


        }
    }
}
