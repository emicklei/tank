package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.awt.Rectangle;
import java.io.*;
public class Bullet extends Moveable {

		public byte shooter;
/**
 * Bullet constructor comment.
 */
public Bullet() {
	super();
}
public Rectangle bounds() {

	int rx = Math.min(x+dx,x);
	int ry = Math.min(y+dy,y);
	return new Rectangle (rx,ry,Math.abs(dx),Math.abs(dy));		
}
public boolean canGoForward(Map map) {

	return !(map.contourIntersects(bounds()));
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public void paint(java.awt.Graphics g) {

	setColor(g,shooter);
	g.drawLine(x,y,x+dx,y+dy);
		
}
public void read(DataInputStream in) throws IOException {

	shooter = in.readByte();
	super.read(in);
	dx = in.readInt();
	dy = in.readInt();
}
public void write(DataOutputStream out)  throws IOException {

	out.writeByte(shooter);
	super.write(out);
	out.writeInt(dx);
	out.writeInt(dy);
}
}
