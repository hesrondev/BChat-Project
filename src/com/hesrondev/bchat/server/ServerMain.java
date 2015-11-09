package com.hesrondev.bchat.server;

import javax.swing.JFrame;


public class ServerMain {
	public static void main(String[] args){
		
		/*Server bchatServer = new Server();
		bchatServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bchatServer.startServer();*/
		
		ServerMessage msg = new ServerMessage(ServerMessage.MSG, "You are connected");
		msg.setReceiverID("64646");
		msg.setSenderID("SERVER-XG5968");
		msg.setReceiverName("Horgtusi");
		msg.setSenderName("SERVER-CHAT");
		
		System.out.println(msg);
	}
}
