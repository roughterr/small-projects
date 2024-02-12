package com.roughterr;

import java.util.LinkedList;

public class PlayerThread implements Runnable {
    private final Player player;

    public PlayerThread(Player player) {
        this.player = player;
    }

    private LinkedList<Message> messageQueue = new LinkedList<>();

    @Override
    public void run() {
       while(true) {
           if (!messageQueue.isEmpty()) {
               Message message = messageQueue.poll();
//               message.getReceiver().onMessage();
           }
       }
    }
}
