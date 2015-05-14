package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.awt.*;
public class Map {

	public LineSegment[] segments = {};
	public Point[] spawnLocations = {};
	
/**
 * Map constructor comment.
 */
public Map() {
	super();
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public boolean contourIntersects(LineSegment line) {

	int i = 0;
	while (i<segments.length) {
		if (segments[i].intersects(line)) return true;
		i++;
	}
	return false;
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public boolean contourIntersects(Rectangle box) {

	int i = 0;
	while (i<segments.length) {
		if (segments[i].intersects(box)) return true;
		i++;
	}
	return false;
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public void paint(java.awt.Graphics g) {

	LineSegment segment;

	g.setColor(Color.black);
	for (int i = 0; i < segments.length; i++) {
		segment = segments[i];
		g.drawLine(segment.fromX, segment.fromY, segment.toX, segment.toY);
	}
}
}
