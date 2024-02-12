package com.roughterr.players;

/**
 * Represents a message.
 */
public class Message {
    /**
     * Senders of the message.
     */
    private final Player sender;
    /**
     * Receiver of the message.
     */
    private final Player receiver;
    /**
     * The content of the message.
     */
    private final String messageContent;

    public Message(Player sender, Player receiver, String messageContent) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageContent = messageContent;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
