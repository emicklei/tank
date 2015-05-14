package com.philemonworks.games.tank;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.awt.*;
import java.util.Vector; 
public class LineSegment {

	public int fromX,fromY,toX,toY;
	
/**
 * LineSegment constructor comment.
 */
public LineSegment() {
	super();
}
/**
 * LineSegment constructor comment.
 */
public LineSegment(int fx, int fy, int tx, int ty) {
	super();
	fromX = Math.min(fx,tx);
	fromY = Math.min(fy,ty);
	toX = Math.max(fx,tx);
	toY = Math.max(fy,ty);
}
public static void addSegmentsFromRectangleTo(Rectangle box, Vector segments){

	segments.addElement(new LineSegment(box.x,box.y,box.x+box.width,box.y));
	segments.addElement(new LineSegment(box.x+box.width,box.y,box.x+box.width,box.y+box.height));
	segments.addElement(new LineSegment(box.x,box.y+box.height,box.x+box.width,box.y+box.height));
	segments.addElement(new LineSegment(box.x,box.y,box.x,box.y+box.height));		
}
/**
 * @author EMM
 */
public Rectangle bounds() {
 
	return new Rectangle (fromX,fromY,toX - fromX,fromY - toY);
}
/**
 * @author EMM
 * @return boolean
 */
public boolean intersects(LineSegment line) {

	if (fromY == toY) {
		//horizontal line
		if (line.fromY == line.toY) {
			//both horizontal
			if (fromY != line.fromY) return false;
			if (toX < line.fromX) return false;
			if (line.toX < fromX) return false;
			return true;
		} else {
			if (line.fromX == line.toX) {
				//vertical line
				if (fromX < line.fromX && line.fromX < toX && line.fromY < fromY && fromY < line.toY) return true;
				return false;
			}
		} 
	}
	
	if (fromX == toX) {
		//vertical line
		if (line.fromX == line.toX) {
			//both vertital
			if (fromX != line.fromX) return false;
			if (toY < line.fromY) return false;
			if (line.toY < fromY) return false;
			return true;
		} else {
			if (line.fromY == line.toY) {
				//horizontal line
				if (fromY < line.fromY && line.fromY < toY && line.fromX < fromX && fromX < line.toX) return true;
				return false;
			}
		} 
	}
	
	//slope line
	return true;
}
/**
 * @author EMM
 * @return boolean
 */
public boolean intersects(Rectangle box) {

	if (fromY == toY) {
		//horizontal line
		if (fromY < box.y) return false;
		if (fromY > (box.y+box.height)) return false;
		if (fromX > (box.x+box.width)) return false;
		if (toX < box.x) return false;
		if (fromX < box.x && toX > box.x) return true;
		if (fromX < (box.x+box.width) && toX > (box.x+box.width)) return true;
		if (fromX > box.x && toX < (box.x + box.width)) return true;
		return false;
	}
	if (fromX == toX) {
		//vertical line
		if (fromX < box.x) return false;
		if (fromX > (box.x+box.width)) return false;
		if (fromY > (box.y+box.height)) return false;
		if (toY < box.y) return false;
		if (fromY < box.y && toY > box.y) return true;
		if (fromY < (box.y+box.height) && toY > (box.y+box.height)) return true;		
		if (fromY > box.y && toY < (box.y + box.height)) return true;
		return false;
	}
	//slope line
	Rectangle bounds = this.bounds();
	return bounds.intersects(box);
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public void paint(java.awt.Graphics g) {

	g.drawLine(fromX,fromY,toX,toY);
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public Point pointRotatedBy(Point point, double angle) {

	double beta = Math.atan(point.x / point.y);
	double r = Math.sqrt(point.x * point.x + (point.y * point.y));
	
	return new Point((int)Math.round(r * Math.cos(beta + angle)), (int)Math.round(r * Math.sin(beta + angle)));
}
/**
 * @author EMM
 * @param g java.awt.Graphics
 */
public LineSegment rotatedBy(double angle) {

	Point newFrom = this.pointRotatedBy(new Point(fromX,fromY),angle);
	Point newTo = this.pointRotatedBy(new Point(toX,toY),angle);
	
	return new LineSegment(newFrom.x,newFrom.y,newTo.x,newTo.y);
}
public static LineSegment[] segmentsFromData (int[] xyData) {

	int i,j,fromX,toX,fromY,toY;
	LineSegment[] newSegments = new LineSegment[(xyData.length - 2) / 2];

	i = 0;
	j = 0;
	fromX = xyData[i];
	fromY = xyData[i+1];
	i = i + 2;
	while (i < xyData.length) {		
		toX = xyData[i];
		toY = xyData[i+1];
		newSegments[j] = new LineSegment(fromX,fromY,toX,toY);
		fromX = toX;
		fromY = toY;
		j = j + 1;
		i = i + 2;
	}
	return newSegments;		
}
/**
 * @author EMM
 */
public String toString() {
 
	return "("+fromX+","+fromY+":"+toX+","+toY+")";
}
}
