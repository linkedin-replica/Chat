import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.arangodb.ArangoDatabase;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.DatabaseConnection;
import org.junit.*;

import com.linkedin.replica.chat.database.handlers.impl.ArangoChatHandler;
import com.linkedin.replica.chat.models.Message;

import static org.junit.Assert.assertEquals;

public class ArangoChatHandlerTest {
	private static ArangoDatabase arangoDb;
	private static Configuration config;
	private static ArangoChatHandler arangoChatHandler;

	@BeforeClass
	public static void setup() throws IOException {
		String rootPath = "src/main/resources/";
		Configuration.init(rootPath+"app.config",
				rootPath+ "arango.config",
				rootPath+ "commands.config",
				rootPath+ "controller.config");

		config = Configuration.getInstance();
		DatabaseConnection.init();
		arangoChatHandler = new ArangoChatHandler();
		arangoDb = DatabaseConnection.getInstance().getArangoDriver().db(
				config.getArangoConfigProp("db.name")
		);
	}

	@Before
	public void initBeforeTest() {
		arangoDb.createCollection(
				config.getArangoConfigProp("collection.messages.name")
		);
	}

	@Test
	public void testInsertMessage() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {

		ArrayList<Message> messages =new ArrayList<>();
		Message msg = new Message();
		msg.setId("34");
		msg.setSender("1");
		msg.setReceiver("2");
		msg.setTimestamp(new Date());
		msg.setMsg("jojojojoojjojojojojo");

		Message msg1 = new Message();
		msg1.setId("35");
		msg1.setSender("1");
		msg1.setReceiver("2");
		msg1.setTimestamp(new Date());
		msg1.setMsg("jojojojo");

		messages.add(msg);
		messages.add(msg1);
		arangoChatHandler.insertMessages(messages);

		Message res = arangoChatHandler.getLatestMessage();

		assertEquals("Id of last item matches the Inserted Item", msg1.getId(), res.getId());

	}

	@Test
	public void testGetChatHistory(){
		ArrayList<Message> messages =new ArrayList<>();
		Message msg = new Message();
		msg.setId("34");
		msg.setSender("1");
		msg.setReceiver("2");
		msg.setTimestamp(new Date());
		msg.setMsg("jojojojoojjojojojojo");

		Message msg1 = new Message();
		msg1.setId("35");
		msg1.setSender("1");
		msg1.setReceiver("2");
		msg1.setTimestamp(new Date());
		msg1.setMsg("jojojojo");

		messages.add(msg);
		messages.add(msg1);
		arangoDb.collection(config.getArangoConfigProp("collection.messages.name")).insertDocuments(messages);

		List<Message> msgs = arangoChatHandler.getChatHistory("1", "2");
		assertEquals("History retrieved", 2, msgs.size());

	}

	@Test
	public void testGetChatHistoryWithOffsetLimit() throws  IOException {

		ArrayList<Message> messages =new ArrayList<>();
		Message msg = new Message();
		msg.setId("34");
		msg.setSender("1");
		msg.setReceiver("2");
		msg.setTimestamp(new Date());
		msg.setMsg("jojojojoojjojojojojo");

		Message msg1 = new Message();
		msg1.setId("35");
		msg1.setSender("1");
		msg1.setReceiver("2");
		msg1.setTimestamp(new Date());
		msg1.setMsg("jojojojo");

		messages.add(msg);
		messages.add(msg1);
		arangoDb.collection(config.getArangoConfigProp("collection.messages.name")).insertDocuments(messages);

		List<Message> msgs = arangoChatHandler.getChatHistory("1", "2", 1, 2);
		assertEquals("History chat retrieved with offset 1 and limit of 2, i.e. 1 msg", 1, msgs.size());

	}

	@After
	public void cleanAfterTest() {
		arangoDb.collection(
				config.getArangoConfigProp("collection.messages.name")
		).drop();
	}

	@AfterClass
	public static void clean() throws SQLException {
		DatabaseConnection.getInstance().closeConnections();
	}
}