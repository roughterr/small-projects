package com.roughterr.players;

/**
 * Manages sending messages between players.
 */
public interface MessageManager {
    /**
     * Sends a message.
     * @param message message object (with the sender and the receiver).
     */
    void sendMessage(Message message);

    /**
     * Finalize (gracefully).
     */
    void finalizeGracefully();
}
