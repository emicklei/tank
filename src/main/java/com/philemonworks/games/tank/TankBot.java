package com.philemonworks.games.tank;

import java.io.*;
/**
 * Insert the type's description here.
 * Creation date: (19-06-2000 14:02:06)
 * @author: Ernest Micklei
 */
public class TankBot implements Runnable {

	Thread simulator;
	public TankClient client = new TankClient();
	public boolean isActive = true;
public Player closestPlayer() {
	
	Player closest = null;
	double distance = Double.MAX_VALUE;
	
	for (int i = 0; i < client.players.size(); i++) {
		Player eachPlayer = (Player) client.players.elementAt(i);
		if (eachPlayer != client.player && !isBot(eachPlayer)) {		
			double eachDistance = this.distanceFromTo(client.player, eachPlayer);
			if (eachDistance < distance) {
				closest = eachPlayer;
				distance = eachDistance;
			}
		}
	}
	return closest;
}
public int degreesFromTo(Player thisPlayer, Player thatPlayer) {
	// answer the angle in degrees that thisPlayer should follow	to encounter thatPlayer

	int degrees;
	
	if (thatPlayer == null) return thisPlayer.degrees;	
	double dx = thatPlayer.x - thisPlayer.x;
	double dy = thatPlayer.y - thisPlayer.y;
	if (dx == 0)
		if (dy > 0) 
			return 0;
		else
			return 180;
	else {
		degrees = (int)Math.round(Math.atan(dy / dx) / Math.PI * 180);
		if (dx < 0) degrees = degrees + 180;
		if (degrees < 0) degrees = degrees + 360;
	}
	return (degrees / Moveable.ROTATEDELTA) * Moveable.ROTATEDELTA;
}
public void destroy() {

	this.stop();
	client.destroy();
}
public double distanceFromTo(Player thisPlayer, Player thatPlayer) {

	int dx = thatPlayer.x - thisPlayer.x;
	int dy = thatPlayer.y - thisPlayer.y;
	return Math.sqrt(dx * dx + (dy * dy));
	
}
public void handlePlayerMessage(DataInputStream in) throws IOException {

	System.out.println("Bot received: "+in.readUTF());
}
public void init() {

	client.init();
	client.start();	
	this.start();	
}
public boolean isBot(Player aPlayer) {

	return aPlayer.id > 1; //aPlayer.name.indexOf("robot") != 0;
}
public void run() {

	while (isActive) {
		if (client.needsRepaint) {
			this.test();
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
				// bummer
			}
		}
	}
}
public void start() {

	if (simulator == null){
		simulator = new Thread(this);
		simulator.start();
	}
}
public void stop() {

	if (simulator != null){
		simulator.destroy();
		simulator = null;
	}
}
public void test() {
	int deg;
			deg = this.degreesFromTo(client.player, this.closestPlayer());
			client.player.degrees = deg;
			client.player.computeDxDy();
			client.tryPlayerGoForward();
			//client.playerShoot();
			client.sendPlayerUpdate();
			client.needsRepaint = false;
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
				// bummer
			}
			
}
}
