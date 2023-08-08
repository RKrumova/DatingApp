package com.example.tinder2.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinder2.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private List<MessagesList> messagesLists;
    private Context context;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list = messagesLists.get(position);
        if(!list.getProfilePic().isEmpty()){
            Picasso.get().load(list.getProfilePic()).into(holder.profilePic);
        }
        holder.username.setText(list.getUsername());
        holder.lastMessage.setText(list.getLastMessage());
        if(list.getUnseenMessages() == 0){
            holder.unseenMessages.setVisibility(View.GONE);
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }
    static class MyViewHolder extends  RecyclerView.ViewHolder {
        private CircleImageView profilePic;
        private TextView username;
        private TextView lastMessage;
        private TextView unseenMessages;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            username = itemView.findViewById(R.id.name_tv);
            lastMessage = itemView.findViewById(R.id.lastMessage_tv);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
        }
    }
}
