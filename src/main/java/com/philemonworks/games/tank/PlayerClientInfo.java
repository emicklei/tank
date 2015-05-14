package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.net.*;
public class PlayerClientInfo extends Player {

	DatagramSocket toClientSocket;
	InetAddress toClientAddress;
	int toClientPort;
public void exit()  {

	if (toClientSocket != null) {
		try {
			toClientSocket.close();
		} catch (Exception ex) {
			System.out.println("Unable to close <toClientSocket>"+ex.toString());
		}
	}
	toClientSocket = null;
}
public void init() {
	
	try {
		toClientSocket = new DatagramSocket();
	} catch (Exception ex) {
		System.out.println("Error creating <toClientSocket>"+ex.toString());
	}
}
public String toString() {
	
	return "player name:"+name+" id:"+id+" address:"+toClientAddress+" port:"+toClientPort;
}
}
