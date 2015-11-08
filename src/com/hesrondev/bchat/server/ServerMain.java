package com.hesrondev.bchat.server;

import javax.swing.JFrame;

public class ServerMain {
	public static void main(String[] args){
		
		Server bchatServer = new Server();
		bchatServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bchatServer.startServer();
	}
}
