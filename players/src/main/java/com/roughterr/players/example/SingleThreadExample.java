package com.roughterr.players.example;

import com.roughterr.players.MessageManagerSingleThread;
import com.roughterr.players.Player;

/**
 * Run a single-threaded players example.
 */
public class SingleThreadExample {
	public static void main(String[] args) {
		MessageManagerSingleThread messageManager = new MessageManagerSingleThread();
		Player initiator =
			new Player("initiator", ExampleConstants.SENT_MESSAGES_LIMIT, ExampleConstants.RECEIVED_MESSAGES_LIMIT,
				messageManager);
		Player secondPlayer = new Player("secondPlayer", -1, -1, messageManager);
		initiator.sendMessage("hello", secondPlayer);
	}
}
