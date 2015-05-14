package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
public class TankApplet extends Applet implements KeyListener, Runnable {

	Thread thread;
	TankClient client;
	boolean trace = false;
	int slowFactor = 4;
	String textline = "";
	boolean editing = false;
	boolean alive = true;
public void destroy() {

	client.destroy();
}
/**
 * Returns information about this applet.
 * @return a string of information about this applet
 */
public String getAppletInfo() {
	return "GameApplet\n" + 
		"\n" + 
		"Insert the type's description here.\n" + 
		"Creation date: (29-05-2000 15:06:49)\n" + 
		"@author: Ernest Micklei\n" + 
		"";
}
/**
 * Initializes the applet.
 */
public void init() {
	super.init();
	client = new TankClient();
	
	TankSetupDialog dialog = new TankSetupDialog(new Frame());
	dialog.client = client;
	dialog.getTextField2().setText(getParameter("host"));
	dialog.getTextField3().setText(getParameter("port"));	
	dialog.setModal(true);
	dialog.setVisible(true);

	addKeyListener(this);
	client.init();
}
/**
 * Called when a key has been pressed.
 * @param e the received event
 */
public void keyPressed(KeyEvent e) {

	switch (e.getKeyCode()) {
		case KeyEvent.VK_UP : client.playerForward(); break;
		case KeyEvent.VK_DOWN : client.playerBackward(); break;
		case KeyEvent.VK_LEFT : 	client.playerRotateLeft(); break;
		case KeyEvent.VK_RIGHT : client.playerRotateRight(); break;
		case KeyEvent.VK_CONTROL : client.playerShoot(); break;
		case KeyEvent.VK_BACK_SPACE : textline = textline.substring(0,Math.max(1,textline.length()-1)); break;
	}
	if (client.needsRepaint) repaint();	
}
/**
 * Called when a key has been released.
 * @param e the received event
 */
public void keyReleased(KeyEvent e) {
}
/**
 * Called when a key has been typed.
 * @param e the received event
 */
public void keyTyped(KeyEvent e) {
	
	char ch = e.getKeyChar();

	if (!editing && (ch == 'b')) {
		client.addBot();
		return;
	}
	
	if (!editing && (ch == 't')) {
		editing = true;
		textline = ">";
		repaint();
		return;
	}
	if (editing && (ch == '\n')) {
		editing = false;
		client.sendPlayerMessage(client.player.name + " says: " + textline.substring(1));
		textline = "";
		repaint();
		return;
	}
	if (!editing)
		return;
	if (Character.isLetterOrDigit(ch) || Character.isSpaceChar(ch) )
		textline = textline + ch;
	repaint();
}
/**
 * Paints the applet.
 * If the applet does not need to be painted (e.g. if it is only a container for other
 * awt components) then this method can be safely removed.
 * 
 * @param g  the specified Graphics window
 * @see #update
 */
public void paint(Graphics g) {

	Player eachPlayer,leader = null;	
	super.paint(g);

	int w = this.getSize().width;
	int h = this.getSize().height;
		
	// status info
	g.drawString("Frags:"+ new Integer(client.player.frags).toString(),2,h -2);
	g.setColor(Color.lightGray);
	g.drawLine(0,h - 18,w,h - 18);
	if (editing) g.drawString(textline, 130, h -2 );
	
	// any message to display ?
	if (client.message != null) {
		g.setColor(Color.red);
		g.drawString(client.message,2,14);
	}
	// leader
	for (int i=0; i<client.players.size(); i++) {
		eachPlayer = (Player)client.players.elementAt(i);
		if (leader == null) 
			leader = eachPlayer;
		else if (eachPlayer.frags > leader.frags)
			leader = eachPlayer;
	}
	Moveable.setColor(g,leader.id);
	g.drawString(leader.name+" :" + new Integer(leader.frags).toString(),w-92, h-2);	
	// reduce clipping area
	g.clipRect(0, 0, w, h - 20);
		
	int offsetX = w / 2;
	int offsetY = h / 2;
	g.translate(offsetX-client.player.x,offsetY-client.player.y);

	client.map.paint(g);
	for (int i=0; i<client.players.size(); i++) ((Player)client.players.elementAt(i)).paint(g);
	for (int i=0; i<client.bullets.size(); i++) ((Bullet)client.bullets.elementAt(i)).paint(g);
}
/**
 * Contains the thread execution loop.
 */
public void run() {

	int i;
	Bullet bullet;
	Vector bulletsCopy;
	int loopCount = 0;
	
	while (alive) {
		if (client.bullets.size()!=0) {
			bulletsCopy = (Vector)client.bullets.clone();
			for (i=0; i<bulletsCopy.size(); i++) {
				bullet = (Bullet)bulletsCopy.elementAt(i);
				if (client.hitAnyPlayerBy(bullet)) {
						client.removeBullet(bullet);
				} else {
					if (bullet.canGoForward(client.map)) {
						bullet.forward();
					} else {
						client.removeBullet(bullet);
					}
				}
			}
			client.needsRepaint = true;
		}
		if (loopCount % slowFactor == 0) {
			client.playerMove();
			loopCount = 0;
		}
		if (client.message != null) {
			client.messageDisplayCount--;
			if (client.messageDisplayCount == 0) {
				client.setMessage(null);
			}
		}
		if (client.needsRepaint) {
			repaint();
			client.needsRepaint = false;
		}
		try {
			Thread.sleep(50);
		} catch (Exception ex) {
		}
		loopCount++;
	}
}
/**
 * Starts up the thread.
 */
public void start() {

	client.start();
	if (thread == null){
		thread = new Thread(this);
		thread.start();
	}
}
/**
 * Terminates the thread and leaves it for garbage collection.
 */
public void stop() {

	client.stop();
	alive = false; // this will end the loop in run()
	thread = null;
}
}
