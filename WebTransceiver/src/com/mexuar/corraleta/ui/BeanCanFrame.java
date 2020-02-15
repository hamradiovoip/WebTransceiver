// NAME
//      $RCSfile: BeanCanFrame.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.9 $
// CREATED
//      $Date: 2006/02/10 15:42:47 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//

package com.mexuar.corraleta.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.Desktop;



/**
 * Swing phone UI
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.9 $ $Date: 2006/02/10 15:42:47 $
 *
 */
public class BeanCanFrame extends JFrame implements ActionListener {
	private static final String version_id =
			"@(#)$Id: BeanCanFrame.java,v 1.9 2006/02/10 15:42:47 uid6102 Exp $ Copyright Mexuar Technologies Ltd";
	private JMenuBar menuBar; 
	private JMenu fileMenu;
	private JMenu nodeMenu;
	private JMenu nodeListMenu;
	private JMenu helpMenu;
	private JMenu settingsMenu;
	private JMenuItem openMenuItem;
	private boolean newCode = false; // for testing
	JTabbedPane tabbedPane; // for new tabs for future

	Boolean haveDialString = false;
	BorderLayout rootBorderLayout = new BorderLayout();
	JTextField dialString = new JTextField();
	public JTextArea connField = new JTextArea();
	public JTextArea rxmsgField = new JTextArea();
	public JTextField txmsgField = new JTextField();
	GridLayout keypadGridLayout = new GridLayout();
	JButton oneButton = new JButton();
	JButton twoButton = new JButton();
	JButton threeButton = new JButton();
	JButton fourButton = new JButton();
	JButton fiveButton = new JButton();
	JButton sixButton = new JButton();
	JButton sevenButton = new JButton();
	JButton eightButton = new JButton();
	JButton nineButton = new JButton();
	JButton zeroButton = new JButton();
	JButton starButton = new JButton();
	JButton poundButton = new JButton();
	JLabel status = new JLabel();
	JButton toggleConnect = new JButton();
	JButton xmit = new JButton();
	JPanel connectionStatusPanel = new JPanel();
	BorderLayout connectionStatusLayout = new BorderLayout();
	JLabel blankLabel = new JLabel(" ");
	JLabel connectionStatus = new JLabel("Node Connection Status");
	JLabel recieveMessageLabel = new JLabel("Sent And Received Messages");
	JLabel sendMessageLabel = new JLabel("Enter Message Here (Press <Enter> to send)");
	public String keyradiotext = "Key (Transmit)";
	public String unkeyradiotext = "Unkey";
	public String calltext = "Connect";
	public String hanguptext = "Disconnect";
	public Color rxcolor,txcolor = Color.red;
	public Color bgcolor = new Color((float)0.928,(float)0.928,(float)0.928);
	final JPanel keypadPanel = new JPanel(keypadGridLayout);


	//Construct the frame
	public BeanCanFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}



	//Component initialization
	private void jbInit() throws Exception  
	{
		this.setLayout(rootBorderLayout);
		if (haveDialString)
		{
			dialString.setText("600");
			dialString.addActionListener(new BeanCanFrame_dialString_actionAdapter(this));
		}
		//Create the keypad
		keypadGridLayout.setColumns(3);
		keypadGridLayout.setRows(4);
		oneButton.setMnemonic('1');
		oneButton.setText("1");
		oneButton.addActionListener(new BeanCanFrame_jButton1_actionAdapter(this));
		twoButton.setMnemonic('2');
		twoButton.setText("2");
		twoButton.addActionListener(new BeanCanFrame_jButton2_actionAdapter(this));
		threeButton.setMnemonic('3');
		threeButton.setText("3");
		threeButton.addActionListener(new BeanCanFrame_jButton3_actionAdapter(this));
		fourButton.setText("4");
		fourButton.setMnemonic('4');
		fourButton.addActionListener(new BeanCanFrame_jButton4_actionAdapter(this));
		fiveButton.setText("5");
		fiveButton.setMnemonic('5');
		fiveButton.addActionListener(new BeanCanFrame_jButton5_actionAdapter(this));
		sixButton.setText("6");
		sixButton.setMnemonic('6');
		sixButton.addActionListener(new BeanCanFrame_jButton6_actionAdapter(this));
		sevenButton.setText("7");
		sevenButton.setMnemonic('7');
		sevenButton.addActionListener(new BeanCanFrame_jButton7_actionAdapter(this));
		eightButton.setText("8");
		eightButton.setMnemonic('8');
		eightButton.addActionListener(new BeanCanFrame_jButton8_actionAdapter(this));
		nineButton.setText("9");
		nineButton.setMnemonic('9');
		nineButton.addActionListener(new BeanCanFrame_jButton9_actionAdapter(this));
		zeroButton.setText("0");
		zeroButton.setMnemonic('0');
		zeroButton.addActionListener(new BeanCanFrame_jButton0_actionAdapter(this));
		starButton.setText("*");
		starButton.addActionListener(new BeanCanFrame_jButton11_actionAdapter(this));
		poundButton.setAction(null);
		poundButton.setText("#");
		poundButton.addActionListener(new BeanCanFrame_jButton12_actionAdapter(this));

		JPanel outerKeypad = new JPanel(new GridBagLayout());
		GridBagConstraints ock = new GridBagConstraints();
		ock.gridx = 0;
		ock.gridy = 0;

		ock.anchor = GridBagConstraints.LINE_START;
		outerKeypad.add(xmit, ock);

		ock.gridx = 2;
		ock.anchor = GridBagConstraints.LINE_END;
		outerKeypad.add(toggleConnect, ock);

		ock.gridx = 0;
		ock.gridy++;
		ock.gridwidth = 3;
		ock.fill = GridBagConstraints.BOTH;
		outerKeypad.add(keypadPanel, ock);

		status.setText("notconnected");
		toggleConnect.setText(calltext);
		toggleConnect.addActionListener(new BeanCanFrame_act_actionAdapter(this));
		//contentPane.setActionMap(null);
		xmit.setText(keyradiotext);
		xmit.addActionListener(new BeanCanFrame_xmit_actionAdapter(this));

		if (haveDialString)
		{
			ock.gridx = 1;
			ock.gridwidth = 1;
			ock.fill = GridBagConstraints.NONE;
			outerKeypad.add(dialString, ock);
			dialString.setBackground(bgcolor);
		}
		keypadPanel.add(oneButton, null);
		oneButton.setBackground(bgcolor);
		keypadPanel.add(twoButton, null);
		twoButton.setBackground(bgcolor);
		keypadPanel.add(threeButton, null);
		threeButton.setBackground(bgcolor);
		keypadPanel.add(fourButton, null);
		fourButton.setBackground(bgcolor);
		keypadPanel.add(fiveButton, null);
		fiveButton.setBackground(bgcolor);
		keypadPanel.add(sixButton, null);
		sixButton.setBackground(bgcolor);
		keypadPanel.add(sevenButton, null);
		sevenButton.setBackground(bgcolor);
		keypadPanel.add(eightButton, null);
		eightButton.setBackground(bgcolor);
		keypadPanel.add(nineButton, null);
		nineButton.setBackground(bgcolor);
		keypadPanel.add(starButton, null);
		starButton.setBackground(bgcolor);
		keypadPanel.add(zeroButton, null);
		zeroButton.setBackground(bgcolor);
		keypadPanel.add(poundButton, null);
		poundButton.setBackground(bgcolor);
		toggleConnect.setBackground(bgcolor);
		xmit.setBackground(bgcolor);
		rxcolor = xmit.getBackground();

		connectionStatusPanel.setLayout(connectionStatusLayout);
		connectionStatusPanel.add(connField, BorderLayout.SOUTH);
		connectionStatusPanel.add(connectionStatus,BorderLayout.NORTH);
		connectionStatus.setOpaque(true);
		connectionStatus.setBackground(bgcolor);
		recieveMessageLabel.setOpaque(true);
		recieveMessageLabel.setBackground(bgcolor);
		sendMessageLabel.setOpaque(true);
		sendMessageLabel.setBackground(bgcolor);
		blankLabel.setOpaque(true);
		blankLabel.setBackground(bgcolor);
		connField.setLineWrap(true);
		connField.setWrapStyleWord(true);
		JScrollPane connFieldScrollPane = new JScrollPane(connField);
		connectionStatusPanel.add(connFieldScrollPane, BorderLayout.CENTER);
		connField.setEditable(false);
		connField.setBackground(Color.white);
		rxmsgField.setRows(10);
		rxmsgField.setMaximumSize(rxmsgField.getSize());
		rxmsgField.setLineWrap(true);
		rxmsgField.setWrapStyleWord(true);
		JScrollPane rxmsgFieldScrollPane = new JScrollPane(rxmsgField);
		rxmsgField.setText("");
		rxmsgField.setEditable(false);
		rxmsgField.setBackground(Color.white);
		txmsgField.setText("");
		txmsgField.setEditable(true);
		txmsgField.setBackground(Color.lightGray);
		txmsgField.setEnabled(false);
		status.setOpaque(true);
		status.setBackground(bgcolor);

		JPanel rxmsgPanel = new JPanel(new BorderLayout());
		rxmsgPanel.add(recieveMessageLabel, BorderLayout.PAGE_START);
		rxmsgPanel.add(rxmsgFieldScrollPane, BorderLayout.CENTER);

		JPanel sendMessagePanel = new JPanel(new BorderLayout());
		sendMessagePanel.add(sendMessageLabel, BorderLayout.PAGE_START);
		sendMessagePanel.add(txmsgField, BorderLayout.CENTER);

		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;

		this.add(outerKeypad, gc);
		gc.gridy++;

		this.add(rxmsgPanel, gc);
		gc.gridy++;

		this.add(sendMessagePanel, gc);
		gc.gridy++;

		gc.gridy = 0;
		gc.gridx++;
		gc.gridheight = 3;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.BOTH;
		this.add(connectionStatusPanel, gc);


		if(newCode)
		{
			tabbedPane = new JTabbedPane();		

			JPanel settingsPanel = new JPanel();
			JPanel mainPanel = new JPanel();
			Button test = new Button("test");
			mainPanel.add(test);

			tabbedPane.addTab("Settings", null, settingsPanel,   "Settings");
			tabbedPane.addTab("Main", null, mainPanel,   "Main");

			this.add(tabbedPane);
		}
		menuBar = new JMenuBar();		

		// file menu
		fileMenu = new JMenu("File");

		// build the File menu
		fileMenu = new JMenu("File");		
		JMenuItem exitMenu = new JMenuItem("Exit");	
		fileMenu.add(exitMenu);
		exitMenu.addActionListener(this);

		// build the node menu
		nodeMenu = new JMenu("Node");

		nodeMenu.add(new JMenuItem("Connect"));
		nodeMenu.add(new JMenuItem("Disconnect"));

		nodeListMenu  = new JMenu("Node List");
		JMenuItem activeMenu = new JMenuItem("Display Active System Nodes");
		nodeListMenu.add(activeMenu);

		activeMenu.addActionListener(this);			

		helpMenu = new JMenu("Help");
		helpMenu.add(new JMenuItem("Help Index"));
		helpMenu.add(new JMenuItem("About"));

		settingsMenu = new JMenu("Settings");
		settingsMenu.add(new JMenuItem("IAX Password"));
		settingsMenu.add(new JMenuItem("Host Name"));
		settingsMenu.add(new JMenuItem("Username"));
		settingsMenu.add(new JMenuItem("IAX Port"));
		settingsMenu.add(new JMenuItem("Caller ID"));
		settingsMenu.add(new JMenuItem("Node"));
		settingsMenu.add(new JMenuItem("Sound Level"));
		settingsMenu.add(new JMenuItem("Misc"));

		// add menus to menubar
		menuBar.add(fileMenu);
		menuBar.add(nodeMenu);
		menuBar.add(settingsMenu);
		menuBar.add(nodeListMenu);
		menuBar.add(helpMenu);

		// put the menubar on the frame
		this.setJMenuBar(menuBar);
		this.setVisible(true);
	}


	void button_action(ActionEvent e) {
		if (haveDialString) {
			String t = e.getActionCommand();
			String s = this.dialString.getText();
			s = s+t;
			dialString.setText(s);
		}
	}

	void dialString_actionPerformed(ActionEvent e) {
		String num = dialString.getText();
		status.setText("Dialing: "+num);
	}

	void clear_actionPerformed(ActionEvent e) {
		dialString.setText("");
	}

	void xmit_actionPerformed(ActionEvent e) {
	}

	void act_actionPerformed(ActionEvent e) {
		dialString_actionPerformed(e);
	}

	void list_actionPerformed(ActionEvent e) throws URISyntaxException {
		System.out.println("URL"); 
		try{
			if(Desktop.isDesktopSupported())
				Desktop.getDesktop().browse(new URI("http://stats.allstarlink.org/"));
		}
		catch(Exception ex){

		}


	}

	class BeanCanFrame_jButton1_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton1_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton2_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton2_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton6_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton6_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton12_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton12_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton3_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton3_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton4_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton4_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton5_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton5_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton7_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton7_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton8_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton8_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton9_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton9_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton11_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton11_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_jButton0_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_jButton0_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.button_action(e);
		}
	}

	class BeanCanFrame_dialString_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_dialString_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.dialString_actionPerformed(e);
		}
	}


	class BeanCanFrame_xmit_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_xmit_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.xmit_actionPerformed(e);
		}
	}

	class BeanCanFrame_act_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		BeanCanFrame_act_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.act_actionPerformed(e);
		}
	}

	class list_menu_actionAdapter implements java.awt.event.ActionListener {
		BeanCanFrame adaptee;

		public list_menu_actionAdapter(BeanCanFrame beanCanFrame) {
			// TODO Auto-generated constructor stub
		}
		void BeanCanFrame_act_actionAdapter(BeanCanFrame adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.act_actionPerformed(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("arg = "+arg0);


		if(arg0.getActionCommand().equals("Display Active System Nodes"))
			if(Desktop.isDesktopSupported())
				try {
					Desktop.getDesktop().browse(new URI("http://stats.allstarlink.org/maps/allstarNodesMap.php"));
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		if(arg0.getActionCommand().equals("Exit"))
		{
			dispose();		
			System.exit(0);
		}

	}
}



