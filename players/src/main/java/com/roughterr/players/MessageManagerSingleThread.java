package com.roughterr.players;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Message manager (a single thread implementation).
 */
public class MessageManagerSingleThread implements MessageManager {
    private Queue<Message> messageQueue = new LinkedList<>();

    @Override
    public void sendMessage(Message message) {
        messageQueue.add(message);
        while (!messageQueue.isEmpty()) {
            Message pollMessage = messageQueue.poll();
            pollMessage.getReceiver().receiveMessage(pollMessage.getMessageContent(), pollMessage.getSender());
            messageQueue.remove(pollMessage);
        }
    }

    @Override
    public void finalizeGracefully() {
        System.out.println("MessageManagerSingleThread.finalizeGracefully() called");
    }
}
