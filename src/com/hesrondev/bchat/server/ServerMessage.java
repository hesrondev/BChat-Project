package com.hesrondev.bchat.server;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class ServerMessage {
	
	private final String typeMessage;
	private String message;
	private String senderName = "no-name";
	private String senderID = "no-id";
	private String receiverName = "no-name";
	private String receiverID = "no_id";
	
	// Type of messages to the client
	public static String ID="IDResponse", MSG="MSG"; 
	
	// Constructor
	
	public ServerMessage(String type, String message) {
		this.typeMessage = type;
		this.message = message;
	}
	
	// convert message to JSON-String format
	
	@SuppressWarnings("unchecked")
	public String toString() {
		String jsonString = "";
		JSONObject json = new JSONObject();
	
		json.put("typeMessage", typeMessage);
		json.put("senderName", senderName);
		json.put("senderID", senderID);
		json.put("receiverName", receiverName);
		json.put("receiverID", receiverID);
		json.put("message", message);
		
		jsonString = json.toString();
		
		return jsonString;
	}

	
	// Getters and Setters
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}

	public String getTypeMessage() {
		return typeMessage;
	}

	
}
