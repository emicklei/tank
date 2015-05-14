package com.philemonworks.games.tank;

import java.awt.image.*;
/**
 * Insert the type's description here.
 * Creation date: (05-06-2000 16:48:51)
 * @author: Ernest Micklei
 */
import java.awt.*;
public class CanvasWithImage extends java.awt.Canvas {

	Image image = null;
	int width;
	int height;
	
/**
 * CanvasWithImage constructor comment.
 */
public CanvasWithImage(String imageURL) {
	super();
	initializeImageFrom(imageURL);
}
public void initializeImageFrom(String imageURL) {
	
	image = loadImage(imageURL);
	MediaTracker tracker = new MediaTracker(this);
	tracker.addImage(image, 0);
	try {
		tracker.waitForAll();
	} catch (Exception e) {
	}
	if (image != null) {
		width = image.getWidth(this);
		height = image.getHeight(this);
	} else {
		width = 20;
		height = 20;
	}
	setSize(width, height);
}
public Image loadImage(String name) {
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	try {
		java.net.URL url = getClass().getResource(name);
		return toolkit.createImage((ImageProducer) url.getContent());
	} catch (Exception ex) {
	}
	return null;
}
public void paint(Graphics g) {
	
	if (image != null)
		g.drawImage(image, 0, 0, width, height, this);
}
}
