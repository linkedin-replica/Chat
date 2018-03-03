

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseConnection;
import utils.ConfigReader;


public class Chat {
	
	public static void start() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
//		if(args.length != 3)
//			throw new IllegalArgumentException("Expected three arguments. 1-database_config file path "
//					+ "2- command_config file path  3- arango_name file path");
		
		// create singleton instance of Configuration class that will hold configuration files paths
		ConfigReader.getInstance();
		
		// create singleton instance of DatabaseConnection class that is responsible for intiating connections
		// with databases
		DatabaseConnection.getDBConnection().getArangoDriver();
	}
	
	public static void shutdown() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException{
		DatabaseConnection.closeConnections();
	}
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		Chat.start();
	}
}