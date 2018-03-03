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
import database.*;

public class DatabaseSeed {
	private static Properties properties;

	public DatabaseSeed() throws FileNotFoundException, IOException{
		properties = new Properties();
		properties.load(new FileInputStream("src/main/resources/arango_names"));
	}
	
	
	public int insertMessages() throws IOException, ClassNotFoundException, SQLException{
		List<String> lines = Files.readAllLines(Paths.get("src/test/resources/messages"));
		ArangoDB arangoDB = DatabaseConnection.getDBConnection().getArangoDriver();
		System.out.println("arangodriver "+arangoDB);
		String dbName = properties.getProperty("db.name");
		String collectionName = properties.getProperty("collection.messages.name");
		
		try{
			System.out.println(arangoDB);
			arangoDB.db(dbName).createCollection(collectionName);
			
		}catch(ArangoDBException ex){
			// check if exception was raised because that database was not created
			if(ex.getErrorNum() == 1228){
				arangoDB.createDatabase(dbName);
			}else{
				throw ex;
			}
		}
		int counter = 0;
		BaseDocument newDoc;
		String[] arr;
		for(String text : lines){
			arr = text.split(" ");
			newDoc = new BaseDocument();
			newDoc.addAttribute("messageId", counter+"");
			newDoc.addAttribute("sentFrom", arr[0]);
			newDoc.addAttribute("sentTo", arr[1]);
			newDoc.addAttribute("sent_date", new Date());
			newDoc.addAttribute("read", Boolean.parseBoolean(arr[3]));
			newDoc.addAttribute("msg", arr[4]);
			arangoDB.db(dbName).collection(collectionName).insertDocument(newDoc);		
			System.out.println("New msg document insert with key = " + newDoc.getId());
			counter++;
		}
		return counter;
	}
	

	
	public void closeDBConnection() throws ArangoDBException, FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		DatabaseConnection.getDBConnection().getArangoDriver().shutdown();	
	}
}