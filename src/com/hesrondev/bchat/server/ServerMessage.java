package com.hesrondev.bchat.server;

import java.io.Serializable;

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
