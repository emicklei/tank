package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.awt.Color;
import java.awt.Rectangle;
import java.io.*;
public class Player extends Moveable {

	public String name = "player";
	public byte id = 0;
	int degrees = 0;
	public int frags = 0;
/**
 * Player constructor comment.
 */
public Player() {
	super();
	init();
}
public void backward() {

	x = x - dx;
	y = y - dy;
}
/**
 * @author EMM
 */
public Rectangle bounds() {

	return boundsAt(x,y);

}
/**
 * @author EMM
 */
public Rectangle boundsAfterBackward() {
	
	int rx = Math.min(x-dx,x);
	int ry = Math.min(y-dy,y);
	
	return boundsAt(rx,ry);
}
/**
 * @author EMM
 */
public Rectangle boundsAfterForward() {
	
	int rx = Math.min(x+dx,x);
	int ry = Math.min(y+dy,y);
	
	return boundsAt(rx,ry);
}
/**
 * @author EMM
 */
public Rectangle boundsAt(int newX, int newY) {

	return new Rectangle (newX-radius(),newY-radius(),radius()*2,radius()*2);

}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public Bullet bulletToShoot () {

	Bullet bullet = new Bullet();
	bullet.x = x+dx+(dx/2);
	bullet.y = y+dy+(dy/2);
	bullet.dx = dx*3 / 2;
	bullet.dy = dy*3 / 2;
	bullet.shooter = id;
	return bullet;	
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public void computeDxDy() {

	double radians = degrees / 180.0 * Math.PI;
	dx = (int)Math.round(Math.cos (radians) * MOVEDELTA);
	dy = (int)Math.round(Math.sin (radians) * MOVEDELTA);
}
/**
 * @author EMM
 */
public void init() {
	
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public void paint(java.awt.Graphics g) {

	g.setColor(Color.red);
	Rectangle bounds = bounds();
	g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height	);

	setColor(g,id); //static
	g.fillArc(x-6,y-6,12,12,0,360);
	g.setColor(Color.black);
	g.drawLine(x,y,x+(dx*2),y+(dy*2));

}
/**
 * @author EMM
 */
public int radius() {

	return 10;
}
public void read(DataInputStream in)   throws IOException {
	// format id name, x , y , degrees
	// id must have been read to find the receiver
		
	name = in.readUTF();
	super.read(in);
	degrees = in.readInt();
	computeDxDy();
	frags = in.readInt();
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public void rotateLeft() {

	degrees = degrees - ROTATEDELTA;
	if (degrees < 0) degrees = degrees + 360;
	computeDxDy();
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public void rotateRight() {

	degrees = degrees + ROTATEDELTA;
	if (degrees > 360) degrees = degrees - 360;	
	computeDxDy();
}
/**
 * @author EMM
 */
public void setID(byte newID) {

	id = newID;
	// update color
}
public void write(DataOutputStream out)  throws IOException {
	// format id, name, x , y , degrees

	out.writeByte(id);
	out.writeUTF(name);
	super.write(out);
	out.writeInt(degrees);
	out.writeInt(frags);
}
}
