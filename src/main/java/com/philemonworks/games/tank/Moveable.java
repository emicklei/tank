package com.philemonworks.games.tank;

import java.awt.*;
// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.io.*;
public class Moveable {

	public static int MOVEDELTA = 10;
	public static int ROTATEDELTA = 30;	
	
	int x = 0;
	int y = 0;
	int dx = MOVEDELTA;
	int dy = 0;

/**
 * Moveable constructor comment.
 */
public Moveable() {
	super();
}
public void backward() {

	x = x - dx;
	y = y - dy;
}
public void forward() {

	x = x + dx;
	y = y + dy;
}
public void read(DataInputStream in) throws IOException {
	
	x = in.readInt();
	y = in.readInt();
}
public static void setColor(Graphics g, byte code) {

	switch (code) {
		case 1: g.setColor(Color.blue); break;
		case 2: g.setColor(Color.gray); break;
		case 3: g.setColor(Color.green); break;
		case 4: g.setColor(Color.magenta); break;	
		case 5: g.setColor(Color.darkGray); break;
		case 6: g.setColor(Color.cyan); break;
		case 7: g.setColor(Color.pink); break;
		case 8: g.setColor(Color.orange); break;	
		case 9: g.setColor(Color.yellow); break;
		default: g.setColor(Color.black); break;
	}
}
public void write(DataOutputStream out)  throws IOException {
	
	out.writeInt(x);
	out.writeInt(y);
}
}
