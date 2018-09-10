package com.sjtubus.model.response;

import com.sjtubus.entity.Message;

import java.util.List;

public class MessageResponse extends HttpResponse {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
