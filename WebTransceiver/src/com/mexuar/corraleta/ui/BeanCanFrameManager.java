// NAME
//      $RCSfile: BeanCanFrameManager.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.24 $
// CREATED
//      $Date: 2006/11/14 16:46:37 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//

package com.mexuar.corraleta.ui;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.BorderFactory;
import com.mexuar.corraleta.protocol.*;
import com.mexuar.corraleta.protocol.netse.*;
import com.mexuar.corraleta.audio.AudioInterface;

/**
 * Place to put code specific to the protocol - allowing BeanCanFrame to
 * keep the UI code only
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.24 $ $Date: 2006/11/14 16:46:37 $
 *
 */

public class BeanCanFrameManager extends BeanCanFrame implements ProtocolEventListener, CallManager {
	private static final String version_id =
			"@(#)$Id: BeanCanFrameManager.java,v 1.24 2006/11/14 16:46:37 uid100 Exp $ Copyright Mexuar Technologies Ltd";

	private Boolean standalone_debugging = true;
	private String dialno = "s";
	private Call _ca = null;
	private Friend _peer = null;
	public String _username = "";
	public String _password = "";
	public String _callingno = "";
	public String _callingname = "";
	public String _callsign = "";
	public String _host = "";
	private String dtmfkey = null;
	private boolean myshift = false;
	private Binder _bind = null;
	public boolean _isApplet = false;
	private AudioInterface _audioBase = null;
	private boolean _keyed = false;
	private boolean _gotkeyed = false;
	public Color keyedcolor = new Color((float)0.8,(float)1.0,(float)0.8);
	Integer debug = 0;
	
	/**
	 * 
	 */
	public BeanCanFrameManager()	
	{

		init();

	}
	
	
	/**
	 * 
	 */
	public void init() {
		//validate();
		/*
		try {
			_host = getParameter("host");
		}
		catch (Exception e) {
		}
		try {
			_username = getParameter("user");
		}
		catch (Exception e) {
		}
		try {
			_password = getParameter("pass");
		}
		catch (Exception e) {
		}
		try {
			_callingno = getParameter("callingNo");
		}
		catch (Exception e) {
		}
		try {
			_callingname = getParameter("callingName");
		}
		catch (Exception e) {
		}
		try {
			_callsign = getParameter("callSign");
		}
		catch (Exception e) {
		}
		Integer debug = 0;
		try {
			debug = new Integer(getParameter("debug"));
		}
		catch (Exception e) {
		}
		 */
		
		if (standalone_debugging) 
		{
			_host = "208.93.198.218:4569"; // 75.60.195.129:4569,  
			_username = "myiphone";
			_password = "kd3su";
			_callingno = "27265"; //"27372"; 7061ff6961f7 27299
			_callingname = "kd3su";
			_callsign = "KD3SU";
			debug = 1;
		}
	/*
		     * @param username Username (peer or user) for authentication
     * @param secret Password for authentication
     * @param calledNo Number/extension to call
     * @param callingNo Number/extension we call from
     * @param callingName Name of the person calling
		if (standalone_debugging) {
			_host = "64.118.102.141:4569"; // 75.60.195.129:4569 208.93.198.219 86.30.239.87
			_username = "allstar-public";
			_password = "allstar";
			_callingno = "2008"; //"27372"; 7061ff6961f7 27299
			_callingname = "xoxoxoxoxo";
			_callsign = "WB6NIL";
			debug = 2;}
*/
		_isApplet = false;
		Log.setLevel(debug);
		register();

		keypadPanel.addKeyListener(new MyKeyListener()); //TODO add key listener back to keypad
		keypadPanel.requestFocus();
		txmsgField.addKeyListener(new BeanCanFrame_txmsgField_keyAdapter());

		_audioBase = new com.mexuar.corraleta.audio.javasound.Audio8k();
		
		try {
			_bind = new BinderSE(_host, _audioBase);
		}
		catch (SocketException ex) {
			status.setText(ex.getMessage());
		}

	}//init 



	public void stop() {
		dokey(false,false);
		if (_ca != null) _ca.hangup();
		if (_bind != null) _bind.stop();
		this.setVisible(false);
		status.setText("Stopped");
		_bind = null;
	}

	public void register() {
		try {
			boolean reg = false;
			_bind.register(_username, _password, this, reg);
		}
		catch (Exception ex) {
			System.out.println("Error with register : "+ex.getMessage());
			status.setText(ex.getMessage());
		}
		if (dialno != null) 
			docall(dialno);
	}

	/**
	 * newCall
	 *
	 * @param c Call
	 */
	public void newCall(Call c) {
		Log.debug("in newCall ");
		if (_ca == null) {
			_ca = c;
			Log.debug("_ca == null :" + _ca.getStatus());
			this.status.setText(c.getStatus());
			if (_ca.getIsInbound()) {
				toggleConnect.setText("Answer");
			}
			else {
				toggleConnect.setText(hanguptext);
			}
		}
		else {
			Log.debug("_ca != null :" + _ca.getStatus());
			this.status.setText("Ignoring second call");
		}
	}

	/**
	 * registered
	 *
	 * @param f Friend
	 * @param s boolean
	 */
	public void registered(Friend f, boolean s) {
		_peer = f;
		this.status.setText(_peer.getStatus());
	}

	public void mySetText(String str) {
		this.status.setText(str);
	}

	/**
	 * hungUp
	 *
	 * @param c Call
	 */
	public void hungUp(Call c) {
		_ca = null;
		status.setText("idle");
		connField.setText(null);
		rxmsgField.setText(null);
		txmsgField.setText(null);
		txmsgField.setBackground(Color.lightGray);
		txmsgField.setEnabled(false);
		connField.setBackground(Color.white);
		toggleConnect.setText(calltext);
	}

	/**
	 * ringing
	 *
	 * @param c Call
	 */
	public void ringing(Call c) {
		status.setText("Ringing");
	}

	/**
	 * Lets us know that the call we made is answered (or
	 * not).
	 *
	 * @param c Call
	 * @see ProtocolEventListener#answered(Call)
	 */
	public void answered(Call c) {
		status.setText("Answered");
		txmsgField.setBorder(BorderFactory.createLineBorder(Color.lightGray,2));
		txmsgField.setBackground(Color.white);
		txmsgField.setEnabled(true);
		if (_gotkeyed == true) connField.setBackground(keyedcolor);
	}

	/**
	 * Called when it is known whether or not friend can reach its host
	 * (PBX).
	 *
	 * @param f Friend
	 * @param b Whether friend can reach its host
	 * @param roundtrip The round trip (ms) of the request
	 * @todo implement
	 */
	public void setHostReachable(Friend f, boolean b, int roundtrip) {
		Log.warn("setHostReachable " + b + ", roundtrip " + roundtrip);
	}

	public void handleMessage(String fromnode,String str) {
		rxmsgField.append("Node " + fromnode + ": " + str + "\n");
	}

	private boolean isAllstarNode(String str) {

		char ch;
		if (str.length() < 3) return false;
		for(int i = 2; i < str.length(); i++)
		{
			ch = str.charAt(i);
			if (Character.isDigit(ch) == false) return false;
		}
		String s0 = str.substring(1,2);
		if (s0.compareTo("5") >= 0) return true;
		if (s0.equals("2")) return true;
		return false;
	}

	private boolean isAllstarPortal(String str) {
		char ch;
		if (str.length() < 3) return false;
		for(int i = 2; i < str.length(); i++)
		{
			ch = str.charAt(i);
			if (Character.isDigit(ch) == false) return true;
		}
		return false;
	}

	private boolean isEchoLink(String str) {
		char ch;
		if (str.length() != 8) return false;
		for(int i = 2; i < str.length(); i++)
		{
			ch = str.charAt(i);
			if (Character.isDigit(ch) == false) return false;
		}
		String s0 = str.substring(1,2);
		if (s0.equals("3")) return true;
		return false;
	}

	private boolean isIRLP(String str) {
		char ch;
		if (str.length() != 6) return false;
		for(int i = 2; i < str.length(); i++)
		{
			ch = str.charAt(i);
			if (Character.isDigit(ch) == false) return false;
		}
		String s0 = str.substring(1,2);
		if (s0.equals("4")) return true;
		return false;
	}

	public void gotText(Friend f,String str) {
		String typ = str.substring(0,1);
		if (typ.equals("L")) {
			String str1;
			Boolean gotone = false;
			if (str.length() > 2) {
				str1 = str.substring(2) + ",T" + _callsign;
			} else {
				str1 = "T" + _callsign;
			}
			String[] strs = str1.split(",");
			Integer n = strs.length;
			connField.setText("");
			if ((n == 0) || (strs[0].length() < 1)) {
				connField.setText("NONE CONNECTED" + str1);
				return;
			} 
			Integer i,j;
			for(i = 0; i < n; i++) {
				if (strs[i].length() < 1) break;
			}
			if (i < n) return;
			Arrays.sort(strs,new NodeCompare());
			for(i = 0,j = 0; i < n; i++) {
				if (isAllstarNode(strs[i]) == true) j++;
			}
			if (j > 0) {
				connField.append("Allstar Nodes:\n");
				for(i = 0; i < n; i++) {
					if (isAllstarNode(strs[i]) == true) {
						String mode = "Transceive";
						String mchr = strs[i].substring(0,1);
						if (mchr.equals("R")) mode = "Monitor";
						else if (mchr.equals("C")) mode = "Connectingr";
						connField.append(strs[i].substring(1) + ":" + mode + "\n");
						gotone = true;
					}
				}
			}
			for(i = 0,j = 0; i < n; i++) {
				if (isAllstarPortal(strs[i]) == true) j++;
			}
			if (j > 0) {
				if (gotone == true) connField.append("\n");
				connField.append("Portal Users:\n");
				for(i = 0; i < n; i++) {
					if (isAllstarPortal(strs[i]) == true) {
						connField.append(strs[i].substring(1) + "\n");
						gotone = true;
					}
				}
			}
			for(i = 0,j = 0; i < n; i++) {
				if (isEchoLink(strs[i]) == true) j++;
			}
			if (j > 0) {
				if (gotone == true) connField.append("\n");
				connField.append("EchoLink Nodes:\n");
				for(i = 0; i < n; i++) {
					if (isEchoLink(strs[i]) == true) {
						String mode = "Transceive";
						String mchr = strs[i].substring(0,1);
						if (mchr.equals("R")) mode = "Monitor";
						else if (mchr.equals("C")) mode = "Connectingr";
						connField.append(strs[i].substring(2) + ":" + mode + "\n");
						gotone = true;
					}
				}
			}
			for(i = 0,j = 0; i < n; i++) {
				if (isIRLP(strs[i]) == true) j++;
			}
			if (j > 0) {
				if (gotone == true) connField.append("\n");
				connField.append("IRLP Nodes:\n");
				for(i = 0; i < n; i++) {
					if (isIRLP(strs[i]) == true) {
						String mode = "Transceive";
						String mchr = strs[i].substring(0,1);
						if (mchr.equals("R")) mode = "Monitor";
						else if (mchr.equals("C")) mode = "Connectingr";
						connField.append(strs[i].substring(2) + ":" + mode + "\n");
						gotone = true;
					}
				}
			}
			if (gotone == false) {
				connField.setText("NONE CONNECTED" + str1);
			}
		}
		if (typ.equals("M")) {
			String str1 = "";
			if (str.length() > 2) str1 = str.substring(2);
			Integer i;
			i = str1.indexOf(" ");
			if (i < 0) return;
			String fromnode = str1.substring(0,i);
			String str2 = str1.substring(i + 1);
			i = str2.indexOf(" ");
			if (i < 0) return;
			String tonode = str2.substring(0,i);
			String msg = str2.substring(i + 1);
			if (msg.length() < 1) return;
			if (tonode.equals(_callsign))
				handleMessage(fromnode + " (private)",msg);
			else if (tonode.equals("0"))
				handleMessage(fromnode,msg);
		}
		if (typ.equals("J")) {
			if (str.length() > 2) {
				connField.setText(str.substring(2));
			}
		}
	}

	void docall(String num) {
		if (_ca == null) {
			if (_peer != null) {
				_peer.newCall(_username, _password, num, _callingno, _callingname);
			}
		}
		else {
			if (_keyed) dokey(false,false);
			if (_ca.getIsInbound()) {
				if (_ca.isAnswered()) {
					_ca.hangup();
				}
				else {
					_ca.answer();
					toggleConnect.setText(hanguptext);
				}
			}
			else {
				_ca.hangup();
			}
		}
	}

	/**
	 */
	void dialString_actionPerformed(ActionEvent e) {
		//   String num = dialString.getText();
		if (dialno != null) docall(dialno);
		//        else docall(num);
		keypadPanel.requestFocus();
	}

	void button_action(ActionEvent e) {
		if (_ca != null) {
			String t = e.getActionCommand();
			_ca.sendTEXT("D " + _callingno + " 0 0 " + t);
			status.setText("sent dtmf " + t);
		}
		keypadPanel.requestFocus();
	}

	void xmit_actionPerformed(ActionEvent e) { 	
		if ((_ca != null) && _ca.isAnswered()) {
			if (_keyed) dokey(false,true);
			else dokey(true,true);
		}
		keypadPanel.requestFocus();
	}

	public String get_host() {
		return _host;
	}

	public String get_password() {
		return _password;
	}

	public String get_username() {
		return _username;
	}

	public String get_callingno() {
		return _callingno;
	}

	public String get_callingname() {
		return _callingname;
	}

	public void set_username(String _username) {
		this._username = _username;
	}

	public void set_password(String _password) {
		this._password = _password;
	}

	public void set_callingno(String _callingno) {
		this._callingno = _callingno;
	}

	public void set_callingname(String _callingname) {
		this._callingname = _callingname;
	}

	public void set_host(String _host) {
		this._host = _host;
	}
	/*
	 * 
	 */
	public void dokey(Boolean mykey,Boolean ansmatters) 
	{
		if ((ansmatters) && (!_ca.isAnswered())) return;
		if (!mykey) 
		{
			xmit.setText(keyradiotext);
			_keyed = false;
			xmit.setBackground(rxcolor);
			if (_ca != null) {
				// _ca.unkeyradio();
				_ca.setMuted(true);
			}
		} 
		else 
		{
			xmit.setText(unkeyradiotext);
			xmit.setBackground(txcolor);
			_keyed = true;

			if (_ca != null) 
			{
				// _ca.keyradio();
				_ca.setMuted(false);
			}
		}
	}

	public boolean accept(Call ca) {
		Log.debug("in accept ");
		boolean ret = true;
		if (_ca != null) {
			ret = false;
		}
		return ret;
	}

	public void rxKeyChange(Friend f, boolean keyed) {
		_gotkeyed = keyed;
		if (keyed == true) {
			_audioBase.startPlay();
			if (txmsgField.isEnabled()) connField.setBackground(keyedcolor);
		}
		else {
			_audioBase.stopPlay();
			connField.setBackground(Color.white);
		}
		Log.debug("Rx key chage to " + keyed);
	}

	public boolean canRecord() {
		boolean ret = false;
		javax.sound.sampled.AudioPermission ap = new javax.sound.sampled.
				AudioPermission("record");
		try {
			java.security.AccessController.checkPermission(ap);
			ret = true;
			Log.debug("Have permission to access microphone");
		}
		catch (java.security.AccessControlException ace) {
			Log.debug("Do not have permission to access microphone");
			Log.warn(ace.getMessage());
		}
		return ret;
	}

	/**
	 * set_debug
	 *
	 * @param debug int
	 */

	public class BeanCanFrame_txmsgField_keyAdapter extends KeyAdapter {

		public void keyPressed(KeyEvent ke){	
			int i = ke.getKeyCode();
			if (i == KeyEvent.VK_ENTER) {
				String ms = txmsgField.getText();
				String msg = ms.trim();
				txmsgField.setText("");
				if ((msg.length() < 1) || (msg.length() > 159)) return;
				rxmsgField.append("Sent :" + msg + "\n");
				String msgbody = msg;
				String msgdst = "0";
				String mf = msg.substring(0,1);
				if (mf.equals("/")) {
					Integer j = msg.indexOf(" ");
					if (j >= 0) {
						msgdst = msg.substring(1,j);
						msgbody = msg.substring(j + 1);
					}
				}
				if (_ca != null) _ca.sendTEXT("M " + _callsign + " " + msgdst + " " + msgbody);
				//keypadPanel.requestFocus();
			}
			if (i == KeyEvent.VK_CONTROL) dokey(true,true);
		}

		public void keyReleased(KeyEvent ke){
			int i = ke.getKeyCode();
			if (i == KeyEvent.VK_CONTROL) dokey(false,true);
		}
	}

	public class MyKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent ke){
			System.out.println("key event");
			int i = ke.getKeyCode();
			if (i == KeyEvent.VK_CONTROL) dokey(true,true);
			if (i == KeyEvent.VK_SHIFT) myshift = true;
			String t = null;
			if (myshift == false) {
				if (i == KeyEvent.VK_0) t = "0";
				else if (i == KeyEvent.VK_1) t = "1";
				else if (i == KeyEvent.VK_2) t = "2";
				else if (i == KeyEvent.VK_3) t = "3";
				else if (i == KeyEvent.VK_4) t = "4";
				else if (i == KeyEvent.VK_5) t = "5";
				else if (i == KeyEvent.VK_6) t = "6";
				else if (i == KeyEvent.VK_7) t = "7";
				else if (i == KeyEvent.VK_8) t = "8";
				else if (i == KeyEvent.VK_9) t = "9";
			} else {
				if (i == KeyEvent.VK_3) t = "#";
				else if (i == KeyEvent.VK_8) t = "*";
			}
			if (i == KeyEvent.VK_NUMPAD0) t = "0";
			else if (i == KeyEvent.VK_NUMPAD1) t = "1";
			else if (i == KeyEvent.VK_NUMPAD2) t = "2";
			else if (i == KeyEvent.VK_NUMPAD3) t = "3";
			else if (i == KeyEvent.VK_NUMPAD4) t = "4";
			else if (i == KeyEvent.VK_NUMPAD5) t = "5";
			else if (i == KeyEvent.VK_NUMPAD6) t = "6";
			else if (i == KeyEvent.VK_NUMPAD7) t = "7";
			else if (i == KeyEvent.VK_NUMPAD8) t = "8";
			else if (i == KeyEvent.VK_NUMPAD9) t = "9";
			else if (i == KeyEvent.VK_MULTIPLY) t = "*";
			else if (i == KeyEvent.VK_SUBTRACT) t = "#";
			if (t != null) {
				dtmfkey = t;
			}
		}

		public void keyReleased(KeyEvent ke){
			int i = ke.getKeyCode();
			if (i == KeyEvent.VK_CONTROL) dokey(false,true);
			boolean wasshift = myshift;
			if (i == KeyEvent.VK_SHIFT) myshift = false;
			String t = null;
			if (wasshift == false) {
				if (i == KeyEvent.VK_0) t = "0";
				else if (i == KeyEvent.VK_1) t = "1";
				else if (i == KeyEvent.VK_2) t = "2";
				else if (i == KeyEvent.VK_3) t = "3";
				else if (i == KeyEvent.VK_4) t = "4";
				else if (i == KeyEvent.VK_5) t = "5";
				else if (i == KeyEvent.VK_6) t = "6";
				else if (i == KeyEvent.VK_7) t = "7";
				else if (i == KeyEvent.VK_8) t = "8";
				else if (i == KeyEvent.VK_9) t = "9";
			} else {
				if (i == KeyEvent.VK_3) t = "#";
				else if (i == KeyEvent.VK_8) t = "*";
			}
			if (i == KeyEvent.VK_NUMPAD0) t = "0";
			else if (i == KeyEvent.VK_NUMPAD1) t = "1";
			else if (i == KeyEvent.VK_NUMPAD2) t = "2";
			else if (i == KeyEvent.VK_NUMPAD3) t = "3";
			else if (i == KeyEvent.VK_NUMPAD4) t = "4";
			else if (i == KeyEvent.VK_NUMPAD5) t = "5";
			else if (i == KeyEvent.VK_NUMPAD6) t = "6";
			else if (i == KeyEvent.VK_NUMPAD7) t = "7";
			else if (i == KeyEvent.VK_NUMPAD8) t = "8";
			else if (i == KeyEvent.VK_NUMPAD9) t = "9";
			else if (i == KeyEvent.VK_MULTIPLY) t = "*";
			else if (i == KeyEvent.VK_SUBTRACT) t = "#";
			if ((t != null) && (dtmfkey != null)) {
				if (dtmfkey == t) {
					if ((_ca != null) && (_callingno != null))  {
						_ca.sendTEXT("D " + _callingno + " 0 0 " + t);
						status.setText("sent dtmf " + t);
					}
					dtmfkey = null;
				}
			}
		}
	}

	class NodeCompare implements Comparator<String> {
		public int compare(String aa, String bb) {
			String sa = aa.substring(1);
			String sb = bb.substring(1);
			return sa.compareTo(sb);
		}
	}

}


