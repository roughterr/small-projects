package com.roughterr;

import org.junit.jupiter.api.Test;

class MessageManagerSingleThreadTest {
    /**
     * The maximum number of messages that Player can send.
     */
    public static final int SENT_MESSAGES_LIMIT = 10;
    /**
     * The maximum number of messages that Player can receive.
     */
    public static final int RECEIVED_MESSAGES_LIMIT = 10;

    private MessageManagerSingleThread messageManager = new MessageManagerSingleThread();

    @Test
    public void test1() {
        Player initiator = new Player("initiator", SENT_MESSAGES_LIMIT, RECEIVED_MESSAGES_LIMIT, messageManager);
        Player secondPlayer = new Player("secondPlayer", -1, -1, messageManager);
        initiator.sendMessage("hello", secondPlayer);
    }
}