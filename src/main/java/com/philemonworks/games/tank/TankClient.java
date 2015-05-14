package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.io.*;
import java.net.*;
import java.util.Vector;
public class TankClient extends TankCommunication {
	
	DatagramSocket toServerSocket;
	String toServerHostName;
	InetAddress toServerAddress;
	int toServerPort;

	DatagramSocket fromServerSocket;
	int fromServerPort;

	public Player player = new Player();
	public Vector players = new Vector();
	public Vector bullets = new Vector();
	public Vector bots = new Vector();	
	public boolean needsRepaint = false;
	public int autoRun = 0; // -1 = back, 0 = still, 1 = fore
	public String message;
	public int messageDisplayCount = 0;
public DatagramSocket acceptingSocket ()	{

	return fromServerSocket;
}
public void addBot() {

	TankBot bot = new TankBot();
 	bot.client.player.name = this.player.name + "'s robot";
	bot.client.toServerHostName = this.toServerHostName;
	bot.client.toServerPort = this.toServerPort;
	bot.init();
	bots.addElement(bot);

}
/**
 * Called when a key has been typed.
 * @param e the received event
 */
synchronized public void addBullet(Bullet bullet) {

	bullets.addElement(bullet);
}
/**
 * Called when a key has been typed.
 * @param e the received event
 */
synchronized public void addPlayer(Player player) {

	players.addElement(player);
}
public void destroy() {

	this.sendDisconnectRequest();
	this.stop();
	close(fromServerSocket);
	close(toServerSocket);
	fromServerSocket = null;
	toServerSocket = null;
	for (int i=0; i<bots.size(); ++i) ((TankBot)bots.elementAt(i)).destroy();
}
public void handleBulletCreate(DataInputStream in) throws IOException {
	// format: state
	
	Bullet newBullet = new Bullet();
	newBullet.read(in);
	this.addBullet(newBullet);
	// repaint is auto
}
public void handlePlayerCreate(DataInputStream in) throws IOException {
	// format id, name, x , y , degrees
	
	Player playerToCreate = new Player();
	playerToCreate.setID(in.readByte());
	playerToCreate.read(in);
	this.addPlayer(playerToCreate);
	needsRepaint = true;
}
public void handlePlayerDelete(DataInputStream in) throws IOException {
	// format: id
	
	Player playerToDelete;
	byte id = in.readByte();
	playerToDelete = this.playerWithID(id);
	this.removePlayer(playerToDelete);
	needsRepaint = true;	
}
public void handlePlayerMessage(DataInputStream in) throws IOException {

	this.setMessage(in.readUTF());
}
public void handlePlayerSetID(DataInputStream in) throws IOException {
	
	player.setID(in.readByte());
}
public void handlePlayerUpdate(DataInputStream in) throws IOException {
	// If the player was not created yet then do so
	// format: id, state
	
	byte id = in.readByte();
	Player playerToUpdate = this.playerWithID(id);
	if (playerToUpdate == null) {
		playerToUpdate = new Player();
		playerToUpdate.setID(id);
		this.addPlayer(playerToUpdate);
	}
	playerToUpdate.read(in);
	needsRepaint = true;
}
public boolean hitAnyPlayerBy(Bullet bullet) {
	// detects whether one of other players is hit by bullet and handles it

	java.awt.Rectangle bounds = bullet.bounds();
	
	for (int i = 0; i < players.size(); i++) {
		Player playerToHit = (Player) (players.elementAt(i));
		if (bounds.intersects(playerToHit.bounds())) {
			if ((playerToHit.id != player.id) && (bullet.shooter == player.id)) {
				// I fragged him
				this.sendPlayerMessage(playerToHit.name+" was fragged by "+player.name);
				this.sendPlayerFragged(playerToHit);
				player.frags++;				
			}
			return true;
		}
	}
	return false;
}
public void init() 
{
	map = this.map0();
	this.setMessage("PhilemonWorks presents: Multi-Tank");
	this.initPlayer();
	try {
		toServerAddress = InetAddress.getByName(toServerHostName);
	} catch (Exception ex) {
		System.out.println("Server address creation error:"+ex.toString()+":"+toServerHostName);
		return;
	}
	try {
		this. initFromServerSocket (8000,10);
	} catch (Exception ex) {
		System.out.println("Socket <fromServer> creation error:"+ex.toString());
		return;
	}
	try {
		toServerSocket = new DatagramSocket();
		sendPacket.setAddress(toServerAddress);
		sendPacket.setPort(toServerPort);
		} catch (Exception ex) {
		System.out.println("Socket <toServer> creation error:"+ex.toString());
		return;
	}
}
public void initFromServerSocket (int startPort, int maxTrials) throws IOException {

	fromServerPort = startPort;
	int trials = maxTrials;

	while (fromServerSocket == null & trials > 0) {	
		try {
			fromServerSocket = new DatagramSocket(fromServerPort);
		} catch (Exception ex) {
			System.out.println("Client <fromServerSocket> creation error:"+ex.toString()+" for port:"+fromServerPort);
			fromServerSocket = null;
			fromServerPort++;
			trials--;
		}
	}
	if (fromServerSocket == null) {
		throw new IOException("No port available to create <fromServerSocket>");
	}
}
public void initPlayer() {
	
	player.id = 0; // trigger ID assignment
	this.addPlayer(player);
}
public void playerBackward() {

	if (autoRun > -1) 
		autoRun	= autoRun - 1;
	this.playerMove();
}
public void playerForward() {

	if (autoRun < 1) 
		autoRun = autoRun + 1;
	this.playerMove();
}
public void playerMove() {

	if (autoRun == 0) return;
	if (autoRun == 1) {
		if (this.tryPlayerGoForward()) {
			this.sendPlayerUpdate();
			needsRepaint = true;
		} else {
			autoRun = 0;
		}
	} else { // autoRun < 1
		if (this.tryPlayerGoBackward()) {
			this.sendPlayerUpdate();
			needsRepaint = true;			
		} else {
			autoRun = 0;
		}
	}
}
public void playerRotateLeft() {

	player.rotateLeft();
	this.sendPlayerUpdate();
	needsRepaint = true;
}
public void playerRotateRight() {

	player.rotateRight();
	this.sendPlayerUpdate();
	needsRepaint = true;
}
public void playerShoot() {
	
	if (!(map.contourIntersects(player.boundsAfterForward()))) {
		Bullet newBullet = player.bulletToShoot();
		this.addBullet(newBullet);
		this.sendBulletCreate(newBullet);
	}
}
public Player playerWithID(byte id) {
	// Answer null if the player was not yet created here
	
	synchronized (players) {
		int size = players.size();
		for (int i=0; i < size;i++) {
			Player eachPlayer = (Player)(players.elementAt(i));
			if (eachPlayer.id == id) return eachPlayer;
		}
	}
	return null;
}
/**
 * Called when a key has been typed.
 * @param e the received event
 */
synchronized public void removeBullet(Bullet bullet) {

	bullets.removeElement(bullet);
}
/**
 * Called when a key has been typed.
 * @param e the received event
 */
synchronized public void removePlayer(Player player) {

	players.removeElement(player);
}
public void run()	{

	this.loopReceiveAndProcessFrom(this.acceptingSocket());
}
public void sendBulletCreate(Bullet newBullet) {

	try {
		this.prepareForBulletCreate(newBullet);
		toServerSocket.send(sendPacket);
	} catch (Exception ex) {
		System.out.println("Bullet create send error:"+ex.toString());
	}
}
public void sendConnectRequest() {
	// format: tag , client port, player state

	ByteArrayOutputStream out = new ByteArrayOutputStream(32);
	DataOutputStream writeStream = new DataOutputStream(out);
	
	try { 
		writeStream.writeByte(CONNECT);
		writeStream.writeInt(fromServerPort);
		player.write(writeStream);
		// send request
		sendPacket.setData(out.toByteArray());
		sendPacket.setLength(out.size());
		toServerSocket.send(sendPacket);
	} catch (Exception ex) {
		System.out.println("Connection request error:" + ex.toString());
	}
}
public void sendDisconnectRequest() {
	// only send request if client was registered at server

	if (player.id == 0) return;
		
	byte[] dataSend = new byte[2];
	dataSend[0] = DISCONNECT;
	dataSend[1] = player.id;
	sendPacket.setData(dataSend);
	sendPacket.setLength(2);
	try {
		toServerSocket.send(sendPacket);
	} catch (Exception ex) {
		System.out.println("Failed to send disconnect request:"+ex.toString());
	}	
}
public void sendPlayerFragged(Player fraggedPlayer) {
	
	byte[] dataSend = new byte[2];
	dataSend[0] = PLAYER_FRAGGED;
	dataSend[1] = fraggedPlayer.id;
	sendPacket.setData(dataSend);
	sendPacket.setLength(2);
	try {
		toServerSocket.send(sendPacket);
	} catch (Exception ex) {
		System.out.println("Failed to send player fragged:"+ex.toString());
	}	
}
public void sendPlayerMessage(String newMessage) {

	try { 
		this.prepareForPlayerMessage(newMessage);
		toServerSocket.send(sendPacket);
	} catch (Exception ex) {
		System.out.println("Player message send error:"+ex.toString());
	}
}
public void sendPlayerUpdate() {

	try { 
		this.prepareForPlayerUpdate(player);
		toServerSocket.send(sendPacket);
	} catch (Exception ex) {
		System.out.println("Player update send error:"+ex.toString());
	}
}
public void setMessage(String newMessage) {

	message = newMessage;
	if (newMessage == null)
		messageDisplayCount = 0;
	else
		messageDisplayCount = 30;
	needsRepaint = true;
}
public void start() {

	super.start();
	this.sendConnectRequest();
}
public boolean tryPlayerGoBackward () {

	java.awt.Rectangle bounds = player.boundsAfterBackward();
	
	// will it collide with another player ?
	for (int i = 0; i < players.size(); i++) {
		Player playerToCollide = (Player) (players.elementAt(i));
		if (playerToCollide != player) {
			if (bounds.intersects(playerToCollide.bounds())) {
				return false;
			}
		}
	}
	// will it hit the map contouit ?
	if (map.contourIntersects(bounds)) {
		// may be can slide along the contour
		if (player.dx == 0 || player.dy == 0 || player.dx == player.dy) return false;
		if (Math.abs(player.dx) < Math.abs(player.dy))
			if (map.contourIntersects(player.boundsAt(player.x,player.y-player.dy)))
				return false;
			else
				player.y = player.y - player.dy;
		else
			if (map.contourIntersects(player.boundsAt(player.x-player.dx,player.y)))
				return false;
			else			
				player.x = player.x - player.dx;
		return true;	
	} else {
		player.backward();
		return true;
	}
}
public boolean tryPlayerGoForward () {

	java.awt.Rectangle bounds = player.boundsAfterForward();
	
	// will it collide with another player ?
	for (int i = 0; i < players.size(); i++) {
		Player playerToCollide = (Player) (players.elementAt(i));
		if (playerToCollide != player) {
			if (bounds.intersects(playerToCollide.bounds())) {
				return false;
			}
		}
	}
	// will it hit the map contouit ?
	if (map.contourIntersects(bounds)) {
		// may be can slide along the contour
		if (player.dx == 0 || player.dy == 0 || player.dx == player.dy) return false;
		if (Math.abs(player.dx) < Math.abs(player.dy))
			if (map.contourIntersects(player.boundsAt(player.x,player.y+player.dy)))
				return false;
			else
				player.y = player.y + player.dy;
		else
			if (map.contourIntersects(player.boundsAt(player.x+player.dx,player.y)))
				return false;
			else			
				player.x = player.x + player.dx;
		return true;	
	} else {
		player.forward();
		return true;
	}
}
}
