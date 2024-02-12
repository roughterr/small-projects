package com.roughterr.players.example;

import com.roughterr.players.MessageManagerMultiThread;
import com.roughterr.players.Player;

/**
 * Run a multi-threaded players example.
 */
public class MultiThreadExample {
	public static void main(String[] args) {
		MessageManagerMultiThread player1MessageManager = new MessageManagerMultiThread();
		Player initiator =
			new Player("initiator", ExampleConstants.SENT_MESSAGES_LIMIT, ExampleConstants.RECEIVED_MESSAGES_LIMIT,
				player1MessageManager);
		MessageManagerMultiThread player2MessageManager = new MessageManagerMultiThread();
		Player secondPlayer = new Player("secondPlayer", -1, -1, player2MessageManager);
		initiator.sendMessage("hello", secondPlayer);
		// Wait for 3 seconds to make sure that nothing actually happens before we start the threads.
		System.out.println("Wait for 2 seconds to make sure that nothing actually happens before we start the threads");
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// Prepare the threads.
		Thread player1Thread = new Thread(player1MessageManager);
		Thread player2Thread = new Thread(player2MessageManager);
		// Start the threads.
		player1Thread.start();
		player2Thread.start();
		try {
			player1Thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// We don't wait for the second player because his process never ends in this example (he has no limits).
		// but we finalize gracefully
		player2MessageManager.finalizeGracefully();
	}
}
