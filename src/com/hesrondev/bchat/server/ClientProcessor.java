package com.hesrondev.bchat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientProcessor implements Runnable{
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Server serverListener;
	private Socket connection;
	
	private String connectionIP;
	private String connectionName = "";
	
	protected ClientProcessor(Socket socket) {
		this.connection = socket;
		this.connectionIP = socket.getInetAddress().toString();
	}

	@Override
	public void run() {
		
		try {
			setupStreams();
			whileChatting();			
		} catch (IOException e) {
			System.err.println("[ClientProcess] : Cannot setup the connection with the client!");
			e.printStackTrace();
		}
		
		closeCrap();
	}
	
	// get stream to send and receive data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		
		showMessage("SERVER: Streams are now setup ! \n");
	}
	
	// during the chat conversation
	private void whileChatting() throws IOException{
			
		String message = "You are now connected !";
		sendData(setServerMessage(message));
			
		do{
			// have a conversation
			try{
				message = (String) input.readObject();
				messageProcessing(message);
			}catch(ClassNotFoundException classNotFound) {
				showMessage("\nSERVER: Server can't read the Client message !");
			}
		}while(connection.isConnected());
	}

	// close streams and sockets
	private void closeCrap(){
		showMessage("\n Closing connections...");
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// Parse the received message from JSON to String and treat according to the type of message
	private void messageProcessing(String jsonMessage) {
		
		JSONParser parser = new JSONParser();
			
		try{
			Object obj = parser.parse(jsonMessage);
			JSONObject json = (JSONObject)obj;
			
			String type = (String)json.get("typeMessage");
			String senderName = (String)json.get("senderName");	
			String senderID =  (String)json.get("senderID");
			String receivedMsg = (String)json.get("message");
			
			String message = "";
			
			System.out.println("Server RECEIVES : "+ type +" : "+ senderName +" : "+ senderID +" : "+ receivedMsg);
			
			// If the connection name isn't initialized yet
			if (this.connectionName.isEmpty())
				this.connectionName = senderName;
			
			// Test the type of message cases
			switch(type) {
					
				case "LOGOUT":			
					showMessage("\n"+senderName + " : " + receivedMsg);
					message = setServerResponse(type);					
					sendData(message);
					break;
					
				case "SERVER_REQUEST":		
					showMessage("\n"+senderName + " : " + receivedMsg);
					message = setServerResponse(type);					
					sendData(message);
					break;
				
				case "MSG":
					showMessage("\n"+senderName + " : " + receivedMsg);					
					serverListener.broadcast(jsonMessage);
					break;
						
				default:
					showMessage("Incorrect ClientMessage type!\n");
					break;						
			}
			
		}catch(ParseException pe){
			System.err.println("position : "+ pe.getPosition());
			System.err.println(pe);
		}
	}
	
	// configure a server response ---> Methode in clientProcess class
	private String setServerResponse(String type){
		
		String tm = "";
		String msg = "";
		
		switch(type) {
								
			case "LOGOUT":			
				tm = ServerMessage.DISCONNECT;
				msg = "You have been disconnected!";				
				break;
					
			case "SERVER_REQUEST":		
				tm = ServerMessage.MSG;
				msg = "Server Request here! Not implemented yet... sorry.";
				break;
				
			default:
				showMessage("Incorrect ClientMessage type!\n");
				break;						
		}
		
		ServerMessage sm = new ServerMessage(tm, msg);
		sm.setSenderName("Server-Chat");
		sm.setSenderID(connection.getInetAddress().getHostAddress());
		sm.setReceiverName(connectionName);
		sm.setReceiverID(connectionIP);
		
		return sm.toString();
	}
	
	//
	private String setServerMessage(String message) {
		
		ServerMessage sm = new ServerMessage(ServerMessage.MSG, message);
		sm.setSenderName("Server-Chat");
		sm.setSenderID(connection.getInetAddress().getHostAddress());
		sm.setReceiverName(connectionName);
		sm.setReceiverID(connectionIP);
		
		return sm.toString();
	}

	
	// Send a message to the client
	protected void sendData(String message){
		try{
			output.writeObject(message);
			output.flush();
		}catch(IOException ioe){
			showMessage("\n ERROR: I can't send that message");
		}
	}
	
	private void showMessage(String msg) {
		
		System.out.println("SERVER : "+ msg);
		serverListener.showMessage(msg);
	}

	// Add serverListener
	protected void addServerListener(Server server) {
		if (server != null) {
			System.out.println("SERVER ADDED "+ server.name);
			this.serverListener = server;
		}
	}
	
	
	// Getters and Setters

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public Server getServerListener() {
		return serverListener;
	}

	public void setServerListener(Server serverListener) {
		this.serverListener = serverListener;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public String getConnectionIP() {
		return connectionIP;
	}

	public void setConnectionIP(String connectionIP) {
		this.connectionIP = connectionIP;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	
}
