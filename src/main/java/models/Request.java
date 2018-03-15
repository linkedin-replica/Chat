package models;

public class Request {
	 
	private RequestType type;
	private String searchKey;
	
	public RequestType getType() {
		return type;
	}
	
	public void setType(RequestType type) {
		this.type = type;
	}
	
	public String getSearchKey() {
		return searchKey;
	}
	
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	
	@Override
	public String toString() {
		return "Request [type=" + type + ", searchKey=" + searchKey + "]";
	}
}