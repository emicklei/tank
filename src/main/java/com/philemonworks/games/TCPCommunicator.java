package com.philemonworks.games;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.net.*;
import java.io.*;
public class TCPCommunicator implements Runnable,ICommunicator {
	
	public Socket talkSocket;
	public String talkHostname;
	public int talkPort;
	
	public Socket listenSocket;
	public String listenHostname;
	public int listenPort;

	private Thread listenThread;
	public boolean keepListening = false;

	private IDataProcessor dataProcessor;
	private final static byte ACK = 1;
/**
 * Communicator constructor comment.
 */
public TCPCommunicator(IDataProcessor proc) {
	super();
	dataProcessor = proc;
}
public void close(Socket socket) {

	System.out.println("Closing socket:"+socket);
	if (socket != null) {
		try {
			socket.close();
		} catch (Exception ex) {
			System.out.println("Socket close error:"+ex.toString());
		}
	}
}
public void connectToListenUsingTalkHost() {

	try {
		listenSocket = new Socket ( talkHostname, talkPort + 1);
		if (!this.tryReceiveACKFrom(listenSocket))
			throw new IOException ("unable to receive ACK");
	} catch (IOException ex) {
		System.out.println("Error connecting to listen using talkHost"+ex);
		this.close(listenSocket);
		listenSocket = null;
	}
}
public void connectToTalk() {

	try {
		talkSocket = new Socket(talkHostname, talkPort);
		if (!this.tryReceiveACKFrom(talkSocket))
			throw new IOException ("unable to receive ACK");
	} catch (IOException ex) {
		System.out.println("Error connecting to talk using talkHost" + ex);
		this.close(talkSocket);
		talkSocket = null;
	}
}
public void destroy() {
	
	this.disconnect();
	this.stop();
}
public void disconnect() {

	this.close(listenSocket);
	this.close(talkSocket);
}
public boolean readyToListen() {

	return listenSocket != null;
}
public boolean readyToTalk() {

	return talkSocket != null;
}
public void run() {

	keepListening = true;

	while (keepListening) {
		try {
			InputStream binStream = listenSocket.getInputStream();
			int dataSize = binStream.read();
			if (dataSize < 0) {
				System.out.println("negative size read:"+dataSize);
			}
			byte[] data = new byte[dataSize];
			binStream.read(data);
			dataProcessor.processData(data);
		} catch (Exception ex) {
			System.out.println(dataProcessor.toString()+":Error processing incoming data:"+ex);
			keepListening = false;
		}
	}
}
public void send(byte[] data) throws IOException {
	
	OutputStream boutStream = talkSocket.getOutputStream();
	boutStream.write(data.length);
	boutStream.write(data);
	boutStream.flush();
}
public void sendACKTo(Socket aSocket) {

	try {
		// send acknowledge
		OutputStream out = aSocket.getOutputStream();
		byte[] ack = { ACK };
		out.write(ack);
		out.flush();
	} catch (IOException ex) {
		System.out.println("Error sending ack:"+ex);
	}
}
public void setDataProcessor(IDataProcessor processor) {

	dataProcessor = processor;
}
/**
 * Insert the method's description here.
 * Creation date: (6/7/2000 10:37:57 PM)
 */
public void start() {

	if (listenThread == null){
		keepListening = true;
		listenThread = new Thread(this);
		listenThread.start();
	}
}
public void stop() {

	if (listenThread != null){
		keepListening = false;
		// listenThread.stop();
		listenThread = null;
	}
}
public boolean tryReceiveACKFrom(Socket aSocket) throws IOException {
	int trials = 10;
	while (trials > 0) {
		try {
			aSocket.setSoTimeout(2000);
			InputStream in = aSocket.getInputStream();
			if (in.read() != ACK) throw new IOException("Illegal ack value");
			trials = 0;
		} catch (IOException ex) {
			System.out.println("Time out when receiving ACK: "+trials);
			trials--;
		}
	}
	aSocket.setSoTimeout(0);
	return trials > 0;
}
}
