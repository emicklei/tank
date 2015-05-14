package com.philemonworks.games;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

public interface ICommunicator {
/**
 * Insert the method's description here.
 * Creation date: (6/8/2000 11:48:34 PM)
 */
void destroy();
/**
 * Insert the method's description here.
 * Creation date: (6/8/2000 11:48:34 PM)
 */
void send(byte[] data) throws Exception;
/**
 * Insert the method's description here.
 * Creation date: (6/8/2000 11:48:34 PM)
 */
void setDataProcessor(IDataProcessor processor);
/**
 * Insert the method's description here.
 * Creation date: (6/8/2000 11:48:34 PM)
 */
void start();
/**
 * Insert the method's description here.
 * Creation date: (6/8/2000 11:48:34 PM)
 */
void stop();
}
