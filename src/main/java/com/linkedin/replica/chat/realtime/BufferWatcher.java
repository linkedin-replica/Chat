package com.linkedin.replica.chat.realtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.models.Message;
import com.linkedin.replica.chat.services.ChatService;
import com.linkedin.replica.chat.services.Workers;

public class BufferWatcher implements Runnable {
	private final int MAX_BUFFER_SIZE = Integer.parseInt(Configuration
			.getInstance().getAppConfigProp("buffer.size"));
	private final int TIMER_PERIOD = Integer.parseInt(Configuration
			.getInstance().getAppConfigProp("buffer.timer"));
	private ArrayList<Message> buffer;

	private BlockingQueue<Message> consumerQueue;
	private ChatService chatService;

	public BufferWatcher(BlockingQueue<Message> consumerQueue) {
		this.consumerQueue = consumerQueue;
		this.chatService = new ChatService();
		this.buffer = new ArrayList<>(MAX_BUFFER_SIZE);
	}

	public void startTimer() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {	
			@Override
			public void run() {
				try {
					flushBuffer(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, TIMER_PERIOD, TIMER_PERIOD);
		
	}

	@Override
	public void run() {
		while (true) {
			try {
				Message message = consumerQueue.take(); // block or take new message to be added to buffer
				if (buffer.size() <= MAX_BUFFER_SIZE) // check that buffer is not full
					buffer.add(message);
				else {
					flushBuffer(false);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void flushBuffer(boolean isTimer) throws Exception {
		ArrayList<Message> bufferClone = null;
		synchronized (this) {
			if (isTimer || buffer.size() >= MAX_BUFFER_SIZE) {
				bufferClone = (ArrayList<Message>) buffer.clone(); // clone buffer for writing
				buffer = new ArrayList<>(MAX_BUFFER_SIZE); // flush the  current buffer
			}
		}

		if (bufferClone != null && bufferClone.size() > 0) {
			String commandName = "chat.insert";
			HashMap<String, Object> args = new HashMap<String, Object>(); // construct arguments to command
			args.put("messages", bufferClone);
			
			Runnable runnable = () -> {
				try {
					chatService.serve(commandName, args);
				} catch (Exception e) {
					// TODO logging
					e.printStackTrace();
				} 
			};
			
			Workers.getInstance().submit(runnable);  // invoke a worker thread to write cloned buffer to disk
		}
	}

}
