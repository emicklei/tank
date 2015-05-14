package com.philemonworks.games;

// Copyright (c) 2000, Ernest Micklei. PhilemonWorks.com

import java.net.Socket;
public interface ISocketAcceptor {
/**
 * Insert the method's description here.
 * Creation date: (6/9/2000 12:11:07 AM)
 */
void acceptSocket(Socket newSocket, TCPCommunicatorAcceptor acceptor);
}
