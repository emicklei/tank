package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.net.*;
import java.util.Vector;
import java.io.*;
import java.awt.*;
public class TankServer extends TankCommunication {

	DatagramSocket fromClientSocket;
	Vector clientInfos = new Vector();
public DatagramSocket acceptingSocket() {
	
	return fromClientSocket;
}
public void addInfo (PlayerClientInfo info) {

	synchronized (clientInfos) {
		clientInfos.addElement(info);
	}
}
public void broadcastBulletCreatesFrom(Bullet newBullet) {

	this.prepareForBulletCreate(newBullet);
	synchronized (clientInfos) {
		int size = clientInfos.size();
		for (int i=0; i < size;i++) {
			PlayerClientInfo info = (PlayerClientInfo)(clientInfos.elementAt(i));
			if (info.id != newBullet.shooter) {
				sendPacket.setAddress(info.toClientAddress);
				sendPacket.setPort(info.toClientPort);
				try {
					info.toClientSocket.send(sendPacket);
				} catch (Exception ex) {
					System.out.println("Error sending bullet create"+ex.toString()+"to:"+info);
				}
			}
		}
	}
}
public void broadcastPlayerChangesFrom(PlayerClientInfo info, boolean sendBack) {
	// pre: sendPacket contains correct data

	boolean canSend;
	
	synchronized (clientInfos) {
		int size = clientInfos.size();
		for (int i=0; i < size;i++) {
			PlayerClientInfo eachInfo = (PlayerClientInfo)(clientInfos.elementAt(i));
			canSend = false;
			if (sendBack) { 
				canSend = true;
			} else { 
				if (info == null) {
					canSend = true;
				} else {
					canSend = eachInfo.id != info.id;
				}
			}
			if (canSend) {
				sendPacket.setAddress(eachInfo.toClientAddress);
				sendPacket.setPort(eachInfo.toClientPort);
				try {
					eachInfo.toClientSocket.send(sendPacket);
				} catch (Exception ex) {
					System.out.println("Error sending player change"+ex.toString()+"to:"+eachInfo);
				}
			}
		}
	}
}
public void destroy() {

	this.stop();
	this.close(fromClientSocket);
	fromClientSocket = null;
	this.destroyClientInfos();
}
public void destroyClientInfos() {

	while (clientInfos.size() > 0) {
		PlayerClientInfo info = (PlayerClientInfo)(clientInfos.elementAt(0));
		this.close(info.toClientSocket);
		this.removeInfo(info);
	}
}
public void handleBulletCreate(DataInputStream in) throws IOException {
	// format: tag , bullet state

	Bullet newBullet = new Bullet();

	newBullet.read(in);
	this.broadcastBulletCreatesFrom(newBullet);
}
public void handleConnectRequest(DataInputStream in) throws IOException {
	// format: tag , client port, player state
	
	PlayerClientInfo info = new PlayerClientInfo();
	Point location;
	
	info.init(); // creates toClientSocket
	info.toClientAddress = receivePacket.getAddress();
	info.toClientPort = in.readInt();
	info.id = in.readByte(); // uninitialized id
	info.read(in);
	info.id = this.nextID();
	location = this.locationToSpawn(info);
	info.x = location.x;
	info.y = location.y;
	// echo
	System.out.println(info.name + " wants to join the game");
	// send back the assigned id
	this.sendClientInitializationTo(info);
	// propagate change
	this.prepareForPlayerCreate(info);
	this.broadcastPlayerChangesFrom(info,false);
	// register
	this.addInfo(info);	
}
public void handleDisconnectRequest(DataInputStream in) throws IOException {

	byte id = in.readByte();
	PlayerClientInfo info = this.infoWithID(id);
	this.removeInfo(info);
	// echo
	System.out.println(info.name + " wants to leave the game");	
	// prepare send packet	-> format: id
	ByteArrayOutputStream out = new ByteArrayOutputStream(32);
	DataOutputStream writeStream = new DataOutputStream(out);
	writeStream.writeByte(PLAYER_DELETE);
	writeStream.writeByte(id);
	writeStream.writeByte(PLAYER_MESSAGE);
	writeStream.writeUTF(info.name+ " has left the game");
	sendPacket.setData(out.toByteArray());
	sendPacket.setLength(out.size());	
	this.broadcastPlayerChangesFrom(info,false);
}
public void handlePlayerFragged(DataInputStream in) throws IOException {
	
	byte id = in.readByte();
	PlayerClientInfo info = this.infoWithID(id);
	Point newLocation = this.locationToSpawn(info);
	info.x = newLocation.x;
	info.y = newLocation.y;
	info.degrees = 0;
	this.prepareForPlayerUpdate((Player) info);
	this.broadcastPlayerChangesFrom(null, true);
}
public void handlePlayerMessage(DataInputStream in) throws IOException {
	// format message text

	String message = in.readUTF();
	this.prepareForPlayerMessage(message);
	this.broadcastPlayerChangesFrom(null,true);
}
public void handlePlayerUpdate(DataInputStream in) throws IOException {

	byte id = in.readByte();
	PlayerClientInfo info = this.infoWithID(id);

	if (info == null) {
		// try find info with matching address
		info = this.infoWithAddress(receivePacket.getAddress());
		if (info == null) {
			// no luck then consume input and notify problem
			info = new PlayerClientInfo();
			info.read(in);
			System.out.println("Unregistered player: "+info.id+":"+info.name);
			return;
		} else {
			// re-initialize its id
			id = info.id; // save id
			info.read(in);  // update rest of state
			info.id = id;  // restore id
			this.sendClientInitializationTo(info);
		}
	} else { 
		info.read(in); 
	}

	this.prepareForPlayerUpdate((Player)info);
	this.broadcastPlayerChangesFrom(info,false);
}
public PlayerClientInfo infoWithAddress(InetAddress clientAddress) {
	// Answer the info that matches clientAddress

	synchronized (clientInfos) {
		for (int i=0; i < clientInfos.size(); i++) {
			PlayerClientInfo eachInfo = (PlayerClientInfo)(clientInfos.elementAt(i));
			if (eachInfo.toClientAddress.equals(clientAddress))
				return eachInfo;
		}
	}
	return null;
}
public PlayerClientInfo infoWithID(byte id) {

	// find the info for which the id equals id
	// or answer null if player was not properly connected
	
	synchronized (clientInfos) {
		for (int i=0; i < clientInfos.size(); i++) {
			PlayerClientInfo eachInfo = (PlayerClientInfo)(clientInfos.elementAt(i));
			if (eachInfo.id == id)
				return eachInfo;
		}
	}
	return null;
}
public void init(String host, int port) {

	map = this.map0();
	this.initFromClientSocket(host,port, 10);
	this.start();
}
public void initFromClientSocket (String hostNameOrNull, int startPort, int maxTrials) {

	int port = startPort;
	int trials = maxTrials;
	String ipName = "";

	while (fromClientSocket == null & trials > 0) {	
		try {
			if (hostNameOrNull == null) {
				fromClientSocket = new DatagramSocket(port);
				ipName = this.getHostName();
			} else {
				fromClientSocket = new DatagramSocket(port,InetAddress.getByName(hostNameOrNull));
				ipName = hostNameOrNull;
			}
		} catch (Exception ex) {
			System.out.println("Server <fromClientSocket> creation error:"+ex.toString()+" for port:"+port);
			fromClientSocket = null;
			port++;
			trials--;
		}
	}
	if (fromClientSocket !=null) {
		System.out.println("Server <fromClientSocket> address:"+ipName);
		System.out.println("Server <fromClientSocket> port:"+port);
	}
}
public Point locationToSpawn(PlayerClientInfo info) {
	// Answer a location to spawn a new player

	boolean  free = false;
	Point location = new Point(0,0);
	
	synchronized (clientInfos) {
		int maxClients = clientInfos.size();
		int maxPoints = map.spawnLocations.length;
		for (int j = 0; j < maxPoints ; j++) {
			location = map.spawnLocations[j];
			Rectangle bounds = info.boundsAt(location.x, location.y);
			free = true;
			for (int i = 0; i < maxClients; i++) {
				PlayerClientInfo eachInfo = (PlayerClientInfo) (clientInfos.elementAt(i));
				if (eachInfo != info) {
					free = !bounds.intersects(eachInfo.bounds());
					if (!free) i = maxClients;
				}
			}
			if (free) j = maxPoints;
		}
	}
	if (!free)
		System.out.println("Unable to find free location to spawn" + info);
	return location;
}

public static void main(String args[]) throws Exception {
	// host port , e.q.   localhost 2000
	TankServer server = new TankServer();
	server.init(args[0], Integer.parseInt(args[1]));
	server.start();

}
public byte nextID() {

	int maxID = 0;
	synchronized (clientInfos) {
		for (int i=0; i < clientInfos.size(); i++) {
			maxID = Math.max (maxID,((PlayerClientInfo)(clientInfos.elementAt(i))).id);
		}
	}
	return (byte)(maxID + 1);
}
public void removeInfo (PlayerClientInfo info) {

	synchronized (clientInfos) {
		clientInfos.removeElement(info);
	}
}
public void sendClientInitializationTo(PlayerClientInfo info) throws IOException {
	
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream(32);
		DataOutputStream writeStream = new DataOutputStream(out);
		writeStream.writeByte(PLAYER_SETID);
		writeStream.writeByte(info.id);
		writeStream.writeByte(PLAYER_UPDATE);
		info.write(writeStream);
		sendPacket.setData(out.toByteArray());
		sendPacket.setLength(out.size());
		sendPacket.setAddress(info.toClientAddress);
		sendPacket.setPort(info.toClientPort);
		info.toClientSocket.send(sendPacket);
	} catch (Exception ex) {
		System.out.println("Error preparing for player update:" + ex + info);
	}
}
}
