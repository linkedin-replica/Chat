package commands;

import java.util.HashMap;

public abstract class Command {

	public HashMap<String, String> hMap;

	public Command(HashMap<String, String> hMap) {
		this.hMap = hMap;
	}

	public abstract String execute() throws Exception;
}
