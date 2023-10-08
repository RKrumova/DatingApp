package com.example.tinder2.messages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinder2.R;
import com.example.tinder2.conversation.ConversationActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private List<MessagesList> messagesLists;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MessagesAdapter", "onCreateViewHolder called");
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, parent, false));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        Log.d("MessagesAdapter", "Binding view for position: " + position);
        MessagesList list = messagesLists.get(position);
        if (list.getProfilePic() != null && !list.getProfilePic().isEmpty()) {
            Picasso.get().load(list.getProfilePic()).into(holder.profilePic);
        }

        holder.username.setText(list.getUsername());
        holder.lastMessage.setText(list.getLastMessage());
        holder.lastMessage.setVisibility(View.VISIBLE);
        holder.username.setVisibility(View.VISIBLE);
        if(list.getUnseenMessages() == 0){
            holder.unseenMessages.setVisibility(View.GONE);
            holder.unseenMessages.setText(String.format("%d", list.getUnseenMessages()));
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list.getUnseenMessages()+"");
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.bg_color));

        }
        holder.rootLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ConversationActivity.class);
            intent.putExtra("username", list.getUsername());
            intent.putExtra("user2", list.getUser_2());
            intent.putExtra("profile_pic", list.getProfilePic());
            intent.putExtra("convo_key", list.getConvoKey());
            context.startActivity(intent);
        });
        Log.d("MessagesAdapter", "Binding view for position: " + position);

    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<MessagesList> messagesLists){
        Log.d("MessagesAdapter", "updateData called with " + messagesLists.size() + " messages.");
        this.messagesLists = messagesLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }
    static class MyViewHolder extends  RecyclerView.ViewHolder {
        private final CircleImageView profilePic;
        private final TextView username;
        private final TextView lastMessage;
        private final TextView unseenMessages;
        private final LinearLayout rootLayout ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            username = itemView.findViewById(R.id.name_tv);
            lastMessage = itemView.findViewById(R.id.lastMessage_tv);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }
    }
}
