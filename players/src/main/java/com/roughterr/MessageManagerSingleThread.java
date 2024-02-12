package com.roughterr;

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
            //pollMessage.getSender().sendMessage(pollMessage.getMessageContent(), pollMessage.getReceiver());
            pollMessage.getReceiver().onMessage(pollMessage.getMessageContent(), pollMessage.getSender());
            messageQueue.remove(pollMessage);
        }
    }
}
