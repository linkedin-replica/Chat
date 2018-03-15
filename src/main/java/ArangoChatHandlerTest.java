import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.arangodb.ArangoDBException;

import database.handlers.ChatHandler;
import database.handlers.impl.ArangoChatHandler;
import models.Message;

import static org.junit.Assert.assertEquals;

public class ArangoChatHandlerTest {
	private DatabaseSeed dbSeed;
	static int counter = 0;

	@BeforeClass
	public void setup() throws ClassNotFoundException, IOException, SQLException {
		Chat.start();
		this.dbSeed = new DatabaseSeed();
		counter = this.dbSeed.insertMessages();
	}

	@Test
	public void testMarkAsRead() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		String searchKey = "1";
		ChatHandler dbHandler = new ArangoChatHandler();
		dbHandler.markAsRead(searchKey);
		Message result = dbHandler.getMessage(searchKey);
		boolean check = false;

		if (result.isRead())
			check = true;

		assertEquals("Message was not marked as read properly.", true, check);

	}

	@Test
	public void testInsertMessage() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {

		Message msg = new Message(counter + "", "1", "2", new Date(), false, "jojojojoojjojojojojo");
		ChatHandler dbHandler = new ArangoChatHandler();
		dbHandler.insertMessage(msg);
		Message res = dbHandler.getLatestMessage();
		boolean check = false;
		if (msg.getMessageId().equals(res.getMessageId()))
			check = true;

		assertEquals("Not inserted", true, check);

	}

	@Test
	public void testGetChatHistory() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {

		ChatHandler dbHandler = new ArangoChatHandler();
		List<Message> msgs = dbHandler.getChatHistory("0", "2");
		assertEquals("History not retrieved", 3, msgs.size());

	}

	@Test
	public void testGetChatHistoryWithOffsetLimit()
			throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {

		ChatHandler dbHandler = new ArangoChatHandler();
		List<Message> msgs = dbHandler.getChatHistory("0", "2", 1, 2);
		assertEquals("History not retrieved", 2, msgs.size());

	}

	@AfterClass
	public static void tearDown()
			throws ArangoDBException, FileNotFoundException, ClassNotFoundException, IOException, SQLException {

		Chat.shutdown();
	}

}