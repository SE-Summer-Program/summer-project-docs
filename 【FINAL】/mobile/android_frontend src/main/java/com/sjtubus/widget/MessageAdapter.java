package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> messages;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView messageSender;
        TextView messageContent;
        TextView messageTitle;
        TextView messageTime;

        ViewHolder(View view){
            super(view);
            messageTitle = view.findViewById(R.id.message_title);
            messageContent = view.findViewById(R.id.message_content);
            messageSender = view.findViewById(R.id.message_sender);
            messageTime = view.findViewById(R.id.message_time);
        }
    }

    public void setDataList(List<Message> list) {
        messages = list;
        notifyDataSetChanged();
    }

    public MessageAdapter(Context context){
        this.context = context;
        this.messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTitle.setText(message.getMessageTitle());
        holder.messageContent.setText(message.getMessageContent());
        holder.messageSender.setText(message.getMessageType());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        String time=format.format(message.getStartDate());
        holder.messageTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
