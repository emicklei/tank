package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;
abstract public class TankCommunication implements Runnable {

	public final static byte CONNECT = 1;
	public final static byte DISCONNECT = 2;
	public final static byte PLAYER_CREATE = 3;
	public final static byte PLAYER_UPDATE = 4;
	public final static byte PLAYER_DELETE = 5;
	public final static byte PLAYER_SETID = 6;
	public final static byte PLAYER_MESSAGE = 7;
	public final static byte PLAYER_FRAGGED = 8;	
	public final static byte BULLET_CREATE = 9;

	DatagramPacket sendPacket = new DatagramPacket(new byte[0],0);
	DatagramPacket receivePacket = new DatagramPacket(new byte[1024],1024);
	Thread thread;
	Map map;
	boolean alive = true;
public DatagramSocket acceptingSocket() {
	// should be redefined
	return null;
}
public void close(DatagramSocket socket) {

	if (socket != null) {
		try {
			socket.close();
		} catch (Exception ex) {
			System.out.println("Socket close error:"+ex.toString());
		}
	}
}
public void dispatchTag(byte tag, DataInputStream readStream) throws IOException {
	
	switch (tag) {
		case CONNECT : // request connect
			this.handleConnectRequest(readStream);
			break;
		case DISCONNECT : // request disconnect
			this.handleDisconnectRequest(readStream);
			break;
		case PLAYER_SETID :
			this.handlePlayerSetID(readStream);
			break;			
		case PLAYER_CREATE :
			this.handlePlayerCreate(readStream);
			break;
		case PLAYER_UPDATE :
			this.handlePlayerUpdate(readStream);
			break;
		case PLAYER_DELETE : 
			this.handlePlayerDelete(readStream);
			break;
		case PLAYER_MESSAGE : 
			this.handlePlayerMessage(readStream);
			break;			
		case PLAYER_FRAGGED : 
			this.handlePlayerFragged(readStream);
			break;		
		case BULLET_CREATE :
			this.handleBulletCreate(readStream);
			break;
		default :
			System.out.println("Unknown tag error:" + tag);
			break;
	}
}
public String getHostName() {

	String hostName = null;
	
	try {
		 hostName = InetAddress.getLocalHost().getHostAddress();
	} catch (Exception ex) {
		System.out.println("Error accessing hostname:"+ex.toString());
	}
	return hostName;
}
public void handleBulletCreate(DataInputStream in) throws IOException {
	
}
public void handleConnectRequest(DataInputStream in) throws IOException {
	
}
public void handleDisconnectRequest(DataInputStream in) throws IOException {
	
}
public void handlePlayerCreate(DataInputStream in) throws IOException {
	
}
public void handlePlayerDelete(DataInputStream in) throws IOException {
	
}
public void handlePlayerFragged(DataInputStream in) throws IOException {
	
}
public void handlePlayerMessage(DataInputStream in) throws IOException {
	
}
public void handlePlayerSetID(DataInputStream in) throws IOException {
	
}
public void handlePlayerUpdate(DataInputStream in) throws IOException {
}
public void loopReceiveAndProcessFrom (DatagramSocket socket)	{

	boolean proceed = true;
	
	while (proceed) {
		try {
			socket.receive(receivePacket);
			int dataSize = receivePacket.getLength();
			byte[] dataReceived = new byte[dataSize];
			byte[] buffer = receivePacket.getData();
			for (int i=0; i < dataSize; i++) dataReceived[i] = buffer[i];
			this.processData(dataReceived);
		} catch (Exception ex) {
			System.out.println("Error processing incoming data:"+ex.toString());
			proceed = false;
		}
	}
	
}
/**
 * @author EMM
 * @return work.Map
 */
public Map map0() {

	Map newMap = new Map();
	Vector segs = new Vector();
	Vector locs = new Vector();
	locs.addElement(new Point(-260,260));
	locs.addElement(new Point(-260,-260));
	locs.addElement(new Point(260,-260));	
	locs.addElement(new Point(260,260));
	
	locs.addElement(new Point(-320,0));
	locs.addElement(new Point(0,-320));
	locs.addElement(new Point(320,0));	
	locs.addElement(new Point(0,320));

	
	
	LineSegment.addSegmentsFromRectangleTo(new Rectangle(-1000,-1000,2000,2000),segs);

	segs.addElement(new LineSegment(-100,20,-100,100));
	segs.addElement(new LineSegment(-100,100,-20,100));
	segs.addElement(new LineSegment(-160,20,-160,160));
	segs.addElement(new LineSegment(-160,160,-20,160));

	segs.addElement(new LineSegment(100,20,100,100));
	segs.addElement(new LineSegment(100,100,20,100));
	segs.addElement(new LineSegment(160,20,160,160));
	segs.addElement(new LineSegment(160,160,20,160));
	
	segs.addElement(new LineSegment(-100,-20,-100,-100));
	segs.addElement(new LineSegment(-100,-100,-20,-100));
	segs.addElement(new LineSegment(-160,-20,-160,-160));
	segs.addElement(new LineSegment(-160,-160,-20,-160));

	segs.addElement(new LineSegment(100,-20,100,-100));
	segs.addElement(new LineSegment(100,-100,20,-100));
	segs.addElement(new LineSegment(160,-20,160,-160));
	segs.addElement(new LineSegment(160,-160,20,-160));

	segs.addElement(new LineSegment(-220,-160,-220,160));
	segs.addElement(new LineSegment(220,-160,220,160));
	segs.addElement(new LineSegment(-160,220,160,220));
	segs.addElement(new LineSegment(-160,-220,160,-220));

	LineSegment.addSegmentsFromRectangleTo(new Rectangle(-240,-240,40,40),segs);
	LineSegment.addSegmentsFromRectangleTo(new Rectangle(200,-240,40,40),segs);
	LineSegment.addSegmentsFromRectangleTo(new Rectangle(-240,200,40,40),segs);
	LineSegment.addSegmentsFromRectangleTo(new Rectangle(200,200,40,40),segs);

	LineSegment.addSegmentsFromRectangleTo(new Rectangle(-20,-300,40,40),segs);
	LineSegment.addSegmentsFromRectangleTo(new Rectangle(-300,-20,40,40),segs);
	LineSegment.addSegmentsFromRectangleTo(new Rectangle(260,-20,40,40),segs);
	LineSegment.addSegmentsFromRectangleTo(new Rectangle(-20,260,40,40),segs);	

	segs.addElement(new LineSegment(-40,-40,-40,40));
	segs.addElement(new LineSegment(40,-40,40,40));

	segs.addElement(new LineSegment(-280,200,-280,280));
	segs.addElement(new LineSegment(-280,280,-200,280));
	segs.addElement(new LineSegment(-340,200,-340,340));
	segs.addElement(new LineSegment(-340,340,-200,340));

	segs.addElement(new LineSegment(280,200,280,280));
	segs.addElement(new LineSegment(280,280,200,280));
	segs.addElement(new LineSegment(340,200,340,340));
	segs.addElement(new LineSegment(340,340,200,340));
	
	segs.addElement(new LineSegment(-280,-200,-280,-280));
	segs.addElement(new LineSegment(-280,-280,-200,-280));
	segs.addElement(new LineSegment(-340,-200,-340,-340));
	segs.addElement(new LineSegment(-340,-340,-200,-340));

	segs.addElement(new LineSegment(280,-200,280,-280));
	segs.addElement(new LineSegment(280,-280,200,-280));
	segs.addElement(new LineSegment(340,-200,340,-340));
	segs.addElement(new LineSegment(340,-340,200,-340));
	
	
		
	newMap.segments = new LineSegment[segs.size()];
	segs.copyInto(newMap.segments);
	newMap.spawnLocations = new Point[locs.size()];
	locs.copyInto(newMap.spawnLocations);
	return newMap;
}
public void prepareForBulletCreate(Bullet newBullet) {
	
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream (32);
		DataOutputStream writeStream = new DataOutputStream(out);
		writeStream.writeByte(BULLET_CREATE);
		newBullet.write(writeStream);
		sendPacket.setData(out.toByteArray());
		sendPacket.setLength(out.size());
	} catch (Exception ex) {
		System.out.println("Error preparing for bullet create:"+ex+newBullet);
	}
}
public void prepareForPlayerCreate(Player player) {
	
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream (32);
		DataOutputStream writeStream = new DataOutputStream(out);
		writeStream.writeByte(PLAYER_CREATE);
		player.write(writeStream);
		writeStream.writeByte(PLAYER_MESSAGE);
		writeStream.writeUTF(player.name+" joins the game");
		sendPacket.setData(out.toByteArray());
		sendPacket.setLength(out.size());
	} catch (Exception ex) {
		System.out.println("Error preparing for player create:"+ex+player);
	}
}
public void prepareForPlayerMessage(String message) {
	
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream (32);
		DataOutputStream writeStream = new DataOutputStream(out);
		writeStream.writeByte(PLAYER_MESSAGE);
		writeStream.writeUTF(message);
		sendPacket.setData(out.toByteArray());
		sendPacket.setLength(out.size());
	} catch (Exception ex) {
		System.out.println("Error preparing for player message:"+ex);
	}
}
public void prepareForPlayerUpdate(Player info) {
	
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream (32);
		DataOutputStream writeStream = new DataOutputStream(out);
		writeStream.writeByte(PLAYER_UPDATE);
		info.write(writeStream);
		sendPacket.setData(out.toByteArray());
		sendPacket.setLength(out.size());
	} catch (Exception ex) {
		System.out.println("Error preparing for player update:"+ex+info);
	}
}
public void processData(byte[] data) {

	try {
		DataInputStream readStream = new DataInputStream (new ByteArrayInputStream(data));
		while (true) {
			byte tag = readStream.readByte();
			this.dispatchTag(tag,readStream);
		}
	} catch (IOException ex) {
		// end of stream
	}
}
public void run()	{

	this.loopReceiveAndProcessFrom(this.acceptingSocket());
}
public void start() {

	if (thread == null){
		thread = new Thread(this);
		thread.start();
	}
}
public void stop() {

	alive = false; // this will end the loop in run()
}
}
