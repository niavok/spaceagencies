package com.spaceagencies.i3d;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Handler {

    Queue<Message> messages = new LinkedBlockingQueue<Message>();
    
    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public Message getMessage() {
        return messages.poll();
    }
    
    public void send(Message message) {
        messages.add(message);
    }

    public Message obtainMessage(int what) {
        Message message =  new Message();
        message.handler = this;
        message.what = what;
        message.obj = null;
        return message;
    }

    public Message obtainMessage(int what, Object obj) {
        Message message =  new Message();
        message.handler = this;
        message.what = what;
        message.obj = obj;
        return message;
    }

    public void removeMessages(int updateWhat) {
        for(Message message: messages) {
            if(message.what == updateWhat) {
                messages.remove(message);
            }
        }
    }
}
