package com.roughterr.players;

import java.util.LinkedList;
import java.util.Queue;

import com.roughterr.players.Message;
import com.roughterr.players.MessageManager;

/**
 * Message manager (a multi threading implementation).
 */
public class MessageManagerMultiThread implements MessageManager, Runnable {
    private Queue<Message> unprocessedMessages = new LinkedList<>();
	
	private volatile boolean exit = false;

    @Override
    public void sendMessage(Message message) {
        unprocessedMessages.add(message);
    }

	@Override
	public void finalizeGracefully() {
		System.out.println("MessageManagerMultiThread.finalizeGracefully() called");
		this.exit = true;
	}

	@Override
    public void run() {
        while(!exit) {
			if (!unprocessedMessages.isEmpty()) {
				Message message = unprocessedMessages.poll();
				message.getReceiver().receiveMessage(message.getMessageContent(), message.getSender());
			}
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Finishing the thread.");
    }
}
