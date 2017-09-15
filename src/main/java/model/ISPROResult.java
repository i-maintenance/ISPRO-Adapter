package model;

import java.util.List;

public class ISPROResult {
	private int id;
	private boolean success;
	private List<StandardMessage> messages;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<StandardMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<StandardMessage> messages) {
		this.messages = messages;
	}
	

}
