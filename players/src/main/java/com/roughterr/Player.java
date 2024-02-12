package com.roughterr;

/**
 * Represents a player who can send messages to other players.
 */
public class Player {
    /**
     * Player's name.
     */
    private final String name;
    /**
     * The maximum number of messages that Player can send. A negative value means no limit.
     */
    private final int sentMessagesLimit;
    /**
     * The maximum number of messages that Player can receive. A negative value means no limit.
     */
    private final int receivedMessagesLimit;
    /**
     * Message manager.
     */
    private final MessageManager messageManager;

    public Player(String name, int sentMessagesLimit, int receivedMessagesLimit, MessageManager messageManager) {
        this.name = name;
        this.sentMessagesLimit = sentMessagesLimit;
        this.receivedMessagesLimit = receivedMessagesLimit;
        this.messageManager = messageManager;
    }

    /**
     * The number of messages that this player has sent.
     */
//    private AtomicInteger messageSentCount = new AtomicInteger(0);

    /**
     * The number of messages that this player has received.
     */
//    private AtomicInteger messageReceiveCount = new AtomicInteger(0);
    /**
     * The number of messages that this player has sent.
     */
    private int sentMessagesCount = 0;
    /**
     * The number of messages that this player has received.
     */
    private int receivedMessagesCount = 0;

    /**
     * Sends a message to another player.
     *
     * @param messageContent the message content
     * @param receiver       to whom we are sending the message
     */
    public synchronized void sendMessage(String messageContent, Player receiver) {
        if (sentMessagesLimit > -1 && sentMessagesCount >= sentMessagesLimit) {
            System.out.println("Player \"" + name + "\" has reached the limit of sent messages");
        } else {
            sentMessagesCount++;
            messageManager.sendMessage(new Message(this, receiver, messageContent));
        }
    }

    /**
     * Receives the message from another player.
     *
     * @param messageContent the message content
     * @param sender         who sent the message
     */
    public synchronized void onMessage(String messageContent, Player sender) {
        if (receivedMessagesLimit > -1 && receivedMessagesCount >= receivedMessagesLimit) {
            System.out.println("Player \"" + name + "\" has has reached the limit of received messages");
            return;
        } else {
            receivedMessagesCount++;
            System.out.println("Player \"" + name + "\" has just received the following message from player \"" + sender.getName() + "\": " + messageContent);
        }
        sendMessage(messageContent + sentMessagesCount, sender);
    }

    public String getName() {
        return name;
    }
}
