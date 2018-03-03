package models;

import java.util.HashMap;

public abstract class Command {

	public HashMap<String,String> hMap;

	public Command(HashMap<String, String> hMap) {
		this.hMap=hMap;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

    public abstract String execute();
}
