package com.linkedin.replica.chat.realtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.models.Message;
import com.linkedin.replica.chat.services.ChatService;

public class BufferWatcher implements Runnable {
	private final int MAX_BUFFER_SIZE = Integer.parseInt(Configuration
			.getInstance().getAppConfigProp("max.buffer.size"));
	private ArrayList<Message> buffer;
	
	private BlockingQueue<Message> consumerQueue;
	private ChatService chatService;

	public BufferWatcher(BlockingQueue<Message> consumerQueue, ChatService chatService) {
		this.consumerQueue = consumerQueue;
		this.chatService = chatService;
		this.buffer = new ArrayList<Message>(MAX_BUFFER_SIZE);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Message message = consumerQueue.take(); // block or take new message to be added to buffer
				if (buffer.size() <= MAX_BUFFER_SIZE) // check that buffer is not full
					buffer.add(message);
				else {
					ArrayList<Message> bufferClone = (ArrayList<Message>) buffer.clone(); // clone buffer for writing 
					String commandName = Configuration.getInstance().getCommandConfigProp("chat.insert"); // get command name
					HashMap<String, Object> args= new HashMap<String, Object>();  // construct arguments to command
					args.put("messages", bufferClone);
					chatService.serve(commandName, args); // invoke a worker thread to write cloned buffer to disk
					buffer = new ArrayList<Message>(MAX_BUFFER_SIZE); // flush the current buffer
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
