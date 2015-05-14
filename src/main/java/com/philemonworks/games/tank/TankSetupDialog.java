package com.philemonworks.games.tank;

/**
 * Insert the type's description here.
 * Creation date: (05-06-2000 16:33:54)
 * @author: Ernest Micklei
 */
public class TankSetupDialog extends java.awt.Dialog {
	private java.awt.Panel ivjContentsPane = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private java.awt.Button ivjButton1 = null;
	private java.awt.TextField ivjTextField1 = null;
	private java.awt.TextField ivjTextField2 = null;
	private java.awt.TextField ivjTextField3 = null;
	public TankClient client;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.WindowListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == TankSetupDialog.this.getButton1()) 
				connEtoC2(e);
		};
		public void windowActivated(java.awt.event.WindowEvent e) {};
		public void windowClosed(java.awt.event.WindowEvent e) {};
		public void windowClosing(java.awt.event.WindowEvent e) {
			if (e.getSource() == TankSetupDialog.this) 
				connEtoC1(e);
		};
		public void windowDeactivated(java.awt.event.WindowEvent e) {};
		public void windowDeiconified(java.awt.event.WindowEvent e) {};
		public void windowIconified(java.awt.event.WindowEvent e) {};
		public void windowOpened(java.awt.event.WindowEvent e) {};
	};
/**
 * TankSetupDialog constructor comment.
 * @param parent java.awt.Frame
 */
public TankSetupDialog(java.awt.Frame parent) {
	super(parent);
	initialize();
}
/**
 * TankSetupDialog constructor comment.
 * @param parent java.awt.Frame
 * @param title java.lang.String
 */
public TankSetupDialog(java.awt.Frame parent, String title) {
	super(parent, title);
}
/**
 * TankSetupDialog constructor comment.
 * @param parent java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
public TankSetupDialog(java.awt.Frame parent, String title, boolean modal) {
	super(parent, title, modal);
}
/**
 * TankSetupDialog constructor comment.
 * @param parent java.awt.Frame
 * @param modal boolean
 */
public TankSetupDialog(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
}
/**
 * connEtoC1:  (TankSetupDialog.window.windowClosing(java.awt.event.WindowEvent) --> TankSetupDialog.dispose()V)
 * @param arg1 java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.WindowEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dispose();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (Button1.action.actionPerformed(java.awt.event.ActionEvent) --> TankSetupDialog.enterGame(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.enterGame(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Comment
 */
public void enterGame(java.awt.event.ActionEvent actionEvent) {
	int port;
	
	// check values
	String name = this.getTextField1().getText();
	if (name.length() == 0) return;
	String host = this.getTextField2().getText();
	if (host.length() == 0) return;	
	String portString = this.getTextField3().getText();
	if (portString.length() == 0) return;
 	try {
	 	port = new Integer(portString).intValue();
 	} catch (Exception ex) {
	 	return;
 	}
 	client.player.name = name;
	client.toServerHostName = host;
	client.toServerPort = port;
	this.dispose();
	return;
}
/**
 * Return the Button1 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getButton1() {
	if (ivjButton1 == null) {
		try {
			ivjButton1 = new java.awt.Button();
			ivjButton1.setName("Button1");
			ivjButton1.setBackground(java.awt.Color.black);
			ivjButton1.setBounds(22, 180, 118, 23);
			ivjButton1.setForeground(java.awt.Color.yellow);
			ivjButton1.setLabel("Enter");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButton1;
}
/**
 * Return the ContentsPane property value.
 * @return java.awt.Panel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Panel getContentsPane() {
	if (ivjContentsPane == null) {
		try {
			ivjContentsPane = new java.awt.Panel();
			ivjContentsPane.setName("ContentsPane");
			ivjContentsPane.setLayout(null);
			getContentsPane().add(getButton1(), getButton1().getName());
			getContentsPane().add(getTextField1(), getTextField1().getName());
			getContentsPane().add(getTextField2(), getTextField2().getName());
			getContentsPane().add(getTextField3(), getTextField3().getName());
			// user code begin {1}
			ivjContentsPane.add(new CanvasWithImage("/tank.jpg"));			
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjContentsPane;
}
/**
 * Return the TextField1 property value.
 * @return java.awt.TextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.TextField getTextField1() {
	if (ivjTextField1 == null) {
		try {
			ivjTextField1 = new java.awt.TextField();
			ivjTextField1.setName("TextField1");
			ivjTextField1.setText("Your nickname");
			ivjTextField1.setBackground(java.awt.Color.black);
			ivjTextField1.setBounds(22, 31, 116, 23);
			ivjTextField1.setForeground(java.awt.Color.white);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTextField1;
}
/**
 * Return the TextField2 property value.
 * @return java.awt.TextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public java.awt.TextField getTextField2() {
	if (ivjTextField2 == null) {
		try {
			ivjTextField2 = new java.awt.TextField();
			ivjTextField2.setName("TextField2");
			ivjTextField2.setText("localhost");
			//ivjTextField2.setBackground(java.awt.Color.black);
			ivjTextField2.setBounds(22, 60, 116, 23);
			//ivjTextField2.setForeground(java.awt.Color.white);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTextField2;
}
/**
 * Return the TextField3 property value.
 * @return java.awt.TextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public java.awt.TextField getTextField3() {
	if (ivjTextField3 == null) {
		try {
			ivjTextField3 = new java.awt.TextField();
			ivjTextField3.setName("TextField3");
			ivjTextField3.setText("9000");
			//ivjTextField3.setBackground(java.awt.Color.black);
			ivjTextField3.setBounds(22, 90, 116, 23);
			//ivjTextField3.setForeground(java.awt.Color.white);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTextField3;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	this.addWindowListener(ivjEventHandler);
	getButton1().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("TankSetupDialog");
		setLayout(new java.awt.BorderLayout());
		setSize(320, 240);
		add(getContentsPane(), "Center");
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		TankSetupDialog aTankSetupDialog = new TankSetupDialog(new java.awt.Frame());
		aTankSetupDialog.setModal(true);
		aTankSetupDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aTankSetupDialog.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.awt.Dialog");
		exception.printStackTrace(System.out);
	}
}
}
