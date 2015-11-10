package com.hesrondev.bchat.server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;



class Server extends JFrame{
	
	// Serial ID
	private static final long serialVersionUID = -1957904804599314189L;
	
	private JTextField userText;
	private JTextArea chatWindow;
	
	private ServerSocket server;
	protected String name;
	
	// List of connections
	
	// Constructor
	
	public Server() {
		super("BChat Server");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(	new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						//String text = event.getActionCommand();		
						//sendMessage(output, (new ServerMessage(ServerMessage.MSG, text)).toString());
						// find the client in the list of connections
						userText.setText("");
					}
		});
		
		chatWindow = new JTextArea();
		this.add(userText, BorderLayout.NORTH);
		this.add(new JScrollPane(chatWindow));
		setSize(300, 150);
		setVisible(true);
		
		//
		this.name = "Server-Chat";
	}
	
	
	/**** **** PUT THIS IN A THREAD **** */
	// Set up and run the server
	
	public void startServer() {
		try {
			server = new ServerSocket(6789, 100);		// connect the server
			
			while(true) {
				try {
					waitForConnection();													
				} catch (EOFException e) {
					System.err.println("\nSERVER: Server ended connection !");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		showMessage("SERVER: waiting for someone to connect...\n");
		
		Socket connection = server.accept();
		
		ClientProcessor clientProcessor = new ClientProcessor(connection);
		clientProcessor.addServerListener(this);
		// Add to the LIST
		
		Thread clthread = new Thread(clientProcessor);
		clthread.start();
		
		showMessage("SERVER: now connected to "+ connection.getInetAddress().getHostName());
		ableToType(true);
	}
	
	// Broadcast method
	
	
	// send a message to client
	private void sendMessage(ClientProcessor client, String message){
		client.sendData(message);
	}
	
	// update chatWindow
	protected void showMessage(final String text) {
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append("\n"+text);
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
