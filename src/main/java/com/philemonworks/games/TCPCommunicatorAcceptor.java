package com.philemonworks.games;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.net.*;
import java.io.*;
public class TCPCommunicatorAcceptor implements Runnable {
	
	private Thread thread;
	public String acceptHostname;
	public int acceptPort;
	public ISocketAcceptor socketAcceptor;
	private ServerSocket acceptingSocket;
	private boolean isAccepting;
/**
 * Insert the method's description here.
 * Creation date: (6/9/2000 12:45:13 AM)
 * @param aSocketAcceptor com.philemonworks.games.ISocketAcceptor
 */
public TCPCommunicatorAcceptor(ISocketAcceptor aSocketAcceptor) {
	super();
	socketAcceptor = aSocketAcceptor;
}

public void destroy() {

	if (acceptingSocket != null) {
		try {
			System.out.println("Closing socket:"+acceptingSocket);
			acceptingSocket.close();
		} catch (IOException ex) {
			// bummer
		}
		acceptingSocket = null;
	}
	this.stop();
	
}
/**
 */
public void run() {

	isAccepting = true;
	Socket newSocket = null;
	
	while (isAccepting) {
		try {
			InetAddress acceptAddress = InetAddress.getByName(acceptHostname);
			acceptingSocket = new ServerSocket(acceptPort, 50, acceptAddress);
			newSocket = acceptingSocket.accept();
			socketAcceptor.acceptSocket(newSocket, this);
		} catch (IOException ex) {
			System.out.println("Error accepting socket connections:"+ex);
			isAccepting = false;
		}
	}
	try {
		if (acceptingSocket != null)
			acceptingSocket.close();
	} catch (IOException ex) {
		System.out.println(ex);
	}
}
/**
 * Starts up the thread.
 */
public void start() {

	if (thread == null){
		thread = new Thread(this);
		thread.start();
	}
}
/**
 * Terminates the thread and leaves it for garbage collection.
 */
public void stop() {

	if (thread != null){
		isAccepting = false;
		//thread.stop();
		thread = null;
	}
}
}
