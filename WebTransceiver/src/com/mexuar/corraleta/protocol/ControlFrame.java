// NAME
//      $RCSfile: ControlFrame.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.14 $
// CREATED
//      $Date: 2006/06/29 10:43:52 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//
package com.mexuar.corraleta.protocol;

/**
 * Represents an IAX ControlFrame.
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.14 $ $Date: 2006/06/29 10:43:52 $
 */
public class ControlFrame extends FullFrame {

    private final static String version_id =
            "@(#)$Id: ControlFrame.java,v 1.14 2006/06/29 10:43:52 uid100 Exp $ Copyright Mexuar Technologies Ltd";

    final static int HANGUP = 1;
    final static int OLD_RING = 2;
    final static int RINGING = 3;// (ringback)
    final static int ANSWER = 4;
    final static int BUSY = 5;
    final static int CONGESTION = 8;
    final static int FLASH = 9;
    final static int OLD_WINK = 10;
    final static int OPTION = 11;
    final static int KEYRADIO = 12;
    final static int UNKEYRADIO = 13;
    final static int CALLPROGRESS = 14;
    final static int CALLPROCEEDING = 15;
    final static int HOLD = 16;
    final static int UNHOLD = 17;

    static String[] controlTypes = {
        "ZERO",
        "Hangup",
        "Reserved",
        "Ringing",
        "Answer",
        "Busy",
        "Reserved",
        "Reserved",
        "Congestion",
        "Flash Hook",
        "Reserved",
        "Option",
        "Key Radio",
        "Unkey Radio",
        "Call Progress",
        "Call Proceeding",
        "Hold",
        "Unhold"
    };



    /**
     * The outbound constructor.
     *
     * @param p0 The Call object
     */
    public ControlFrame(Call p0) {
        super(p0);
        this._frametype = FullFrame.CONTROL;
    }


    /**
     * The inbound constructor.
     *
     * @param p0 The Call object
     * @param p1 The incoming message bytes
     */
    public ControlFrame(Call p0, byte[] p1) {
        super(p0, p1);
    }


    /**
     * ack is called to send any required response.
     */
    void ack() {
        switch (this._subclass) {
            case RINGING:
                this.sendAck();
                _call.setRinging();
                break;
            case ANSWER:
                this.sendAck();
                _call.setAnswered(true);
                break;
            default:
                Log.warn("Unhandled Control Frame " + _subclass);
                this.sendAck();
                break;
        }
        log("got");
    }


    /**
     * Sends an answer (ANSWER) back.
     */
    public void sendAnswer() {
        _subclass = this.ANSWER;
        Log.debug("Sending Answer");
        sendMe(this.EMPTY);
    }

    public void sendKeyRadio() {
        _subclass = this.KEYRADIO;
        Log.debug("Sending Radio Key");
        sendMe(this.EMPTY);
    }

    public void sendUnKeyRadio() {
        _subclass = this.UNKEYRADIO;
        Log.debug("Sending Radio Un-Key");
        sendMe(this.EMPTY);
    }
    
    /**
     * Logs this frame.
     *
     * @param inout Additional text to log
     */
    protected void log(String inout) {
        StringBuffer bu = new StringBuffer(inout);
        bu.append(" Control Frame ");
        if ((_subclass < controlTypes.length) && (_subclass >= 0)) {
            bu.append(controlTypes[_subclass]);
        } else {
            bu.append(_subclass);
        }
        super.log(bu.toString());
    }


    // Birgit: Why not remove this. It does less than its parent class.
    /**
     * arrived is called when a packet arrives. This method is
     * empty.
     *
     * @throws IAX2ProtocolException
     */
    void arrived() throws IAX2ProtocolException {
    }

}

