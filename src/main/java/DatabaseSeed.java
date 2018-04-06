import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.linkedin.replica.chat.database.*;

public class DatabaseSeed {
	private static Properties properties;

	public DatabaseSeed() throws FileNotFoundException, IOException {
		properties = new Properties();
		properties.load(new FileInputStream("src/main/resources/arango.config"));
	}

	public int insertMessages() throws IOException, ClassNotFoundException, SQLException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/resources/messages"));
		ArangoDB arangoDB = DatabaseConnection.getDBConnection().getArangoDriver();
		String dbName = properties.getProperty("db.name");
		String collectionName = properties.getProperty("collection.messages.name");

		try {
			//TODO check if collection exists 
			arangoDB.db(dbName).createCollection(collectionName);

		} catch (ArangoDBException ex) {
			// check if an exception was raised as a result of the com.linkedin.replica.chat.database not being
			// created
			if (ex.getErrorNum() == 1228) {
				arangoDB.createDatabase(dbName);
			} else {
				throw ex;
			}
		}
		int counter = 0;
		BaseDocument newDoc;
		String[] arr;
		for (String text : lines) {
			arr = text.split(" ");
			newDoc = new BaseDocument();
			newDoc.addAttribute("messageId", counter + "");
			newDoc.addAttribute("sentFrom", arr[0]);
			newDoc.addAttribute("sentTo", arr[1]);
			newDoc.addAttribute("sent_date", new Date());
			newDoc.addAttribute("read", Boolean.parseBoolean(arr[3]));
			newDoc.addAttribute("msg", arr[4]);
			arangoDB.db(dbName).collection(collectionName).insertDocument(newDoc);
			counter++;
		}
		return counter;
	}

	public void closeDBConnection()
			throws ArangoDBException, FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		DatabaseConnection.getDBConnection().getArangoDriver().shutdown();
	}
}