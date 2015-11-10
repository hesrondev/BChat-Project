package com.hesrondev.bchat.server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	// Constructor
	
	public Server() {
		super("BChat Server");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(	new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						String text = event.getActionCommand();
						
						sendMessage(output, (new ServerMessage(ServerMessage.MSG, text)).toString());
						
						userText.setText("");
					}
		});
		
		chatWindow = new JTextArea();
		
		this.add(userText, BorderLayout.NORTH);
		this.add(new JScrollPane(chatWindow));
		setSize(300, 150);
		setVisible(true);
	}
	
	// Set up and run the server
	
	public void startServer() {
		try {
			server = new ServerSocket(6789, 100);
			
			while(true) {
				try {
					waitForConnection();
					setupStreams();
					whileChatting();					
				} catch (EOFException e) {
					System.err.println("\nSERVER: Server ended connection !");
				}finally {
					closeCrap();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		showMessage("SERVER: waiting for someone to connect...\n");
		connection = server.accept();
		showMessage("SERVER: now connected to "+ connection.getInetAddress().getHostName());
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
		
		String message = "SERVER : You are now connected !";
		sendMessage(output, (new ServerMessage(ServerMessage.MSG, message)).toString());
		
		ableToType(true);
		
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
			
			System.out.println("Server RECEIVES : "+ type +" : "+ senderName +" : "+ senderID +" : "+ receivedMsg);
			
			// Test the type of message cases
			
			String message = "";
				
			switch(type) {
					
				case "LOGOUT":			
					showMessage("\n"+senderName + " : " + receivedMsg);
					message = setServerResponse(type, senderName, senderID);					
					sendMessage(output, message);
					break;
					
				case "SERVER_REQUEST":		
					showMessage("\n"+senderName + " : " + receivedMsg);
					message = setServerResponse(type, senderName, senderID);					
					sendMessage(output, message);
					break;
					
				case "MSG":
					showMessage("\n"+senderName + " : " + receivedMsg);					
					sendMessage(output, jsonMessage);
					// BROADCASTING  !!!
					// broadcast(jsonMessage);
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
	
	// close streams and sockets
	private void closeCrap(){
		showMessage("\n Closing connections...");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// configure a server response ---> Methode in clientProcess class
	private String setServerResponse(String type, String clientName, String clientID){
		
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
		sm.setReceiverName(clientName);
		sm.setReceiverID(clientID);
		
		return sm.toString();
		
	}
	
	// send a message to client
	private void sendMessage(ObjectOutputStream output, String message){
		try{
			output.writeObject(message);
			output.flush();
		}catch(IOException ioe){
			chatWindow.append("\n ERROR: I can't send that message");
		}
	}
	
	// update chatWindow
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
				}
			}
		);
	} 
	
	// enable to type things
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}
			);
	}
}
