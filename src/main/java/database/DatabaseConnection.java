package com.linkedIn.chat.chatinterface;
import com.arangodb.ArangoDB;
import com.linkedIn.chat.ConfigReader;

import java.io.IOException;

class DatabaseConnection {
    private ArangoDB arangoDriver;
    private ConfigReader config;

    private volatile static DatabaseConnection dbConnection;

    private DatabaseConnection() throws IOException {
        config = ConfigReader.getInstance();

        initializeArangoDB();
    }

    private void initializeArangoDB() {
        arangoDriver = new ArangoDB.Builder()
                .user(config.getArangoConfig("arangodb.user"))
                .password(config.getArangoConfig("arangodb.password"))
                .build();
    }

  
    static DatabaseConnection getDBConnection() throws IOException {
        if(dbConnection == null) {
            synchronized (DatabaseConnection.class) {
                if (dbConnection == null)
                    dbConnection = new DatabaseConnection();
            }
        }
        return dbConnection;
    }


    ArangoDB getArangoDriver() {
        return arangoDriver;
    }
}