// NAME
//      $RCSfile: DtmfFrame.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.6 $
// CREATED
//      $Date: 2006/02/16 13:21:33 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//
package com.mexuar.corraleta.protocol;

import com.mexuar.corraleta.util.*;

/**
 * Represents an IAX DTMF FRAME
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.6 $ $Date: 2006/02/16 13:21:33 $
 */
class TextFrame extends FullFrame {
    private final static String version_id =
            "@(#)$Id: TextFrame.java,v 1.6 2006/02/16 13:21:33 uid1003 Exp $ Copyright Mexuar Technologies Ltd";


    /**
     * The outbound constructor.
     *
     * @param ca The Call object
     * @param c The outgoing DTMF character: 0-9, A-D, *, # 
     */
    TextFrame(Call ca, String str) {
        super(ca);
        _retry = false;
        _cbit = false;
        _frametype = FullFrame.TEXT;
        _subclass = 0;
        byte b1[] = str.getBytes();
        byte b2[] = new byte[] {0} ;
        byte[] buf = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, buf, 0, b1.length);
        System.arraycopy(b2, 0, buf, b1.length, b2.length);
        sendMe(buf);
        Log.debug("Sent Text " + str);
        this.dump();
    }
    
     /**
     * The inbound constructor.
     *
     * @param p0 The Call object
     * @param p1 The incoming message bytes
     */
    public TextFrame(Call p0, byte[] p1) {
        super(p0, p1);
    }

    /**
     * ack is called to send any required response. This method is empty
     * (for the moment?).
     */
    void ack() {
        // inbound - ignore it for now....
    }
        /**
     * arrived is called when a packet arrives.
     *
     * @throws IAX2ProtocolException
     */
    void arrived() throws IAX2ProtocolException {
        int fsz = _call.getFrameSz();
        byte[] bs = new byte[fsz];
        _data.get(bs);
        String str = new String(bs);
        Integer i = str.indexOf(0,0);
        if (i >= 0) str = str.substring(0,i);
        if (_call._peer != null) _call._peer.tellGuigotText(str);
    }
}

