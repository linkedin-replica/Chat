package models;

public class Request {

	// type of request which is either one of the following type: USERS or COMPANIES or POSTS or JOBS.	 
	private RequestType type;
	// search key which is the only argument needed from request.
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