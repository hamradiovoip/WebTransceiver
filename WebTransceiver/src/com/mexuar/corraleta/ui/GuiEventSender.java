// NAME
//      $RCSfile: GuiEventSender.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.1 $
// CREATED
//      $Date: 2006/05/13 13:35:10 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//
package com.mexuar.corraleta.ui;
import com.mexuar.corraleta.protocol.*;

/**
 * Decouples events from the main threads. This class it used by Friend.
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.1 $ $Date: 2006/05/13 13:35:10 $
 * @see Friend
 */

public class GuiEventSender
    implements ProtocolEventListener {

    private final static String version_id =
        "@(#)$Id: GuiEventSender.java,v 1.1 2006/05/13 13:35:10 uid100 Exp $ Copyright Mexuar Technologies Ltd";

    private ProtocolEventListener _gui;
    private Call _call;

    /**
     * Constructor for the GuiEventSender object
     *
     * @param gui The protocol event listener
     */
    public GuiEventSender(ProtocolEventListener gui) {
        _gui = gui;
    }

    /**
     * Received a new call.
     * Via invokeLater() this is passed on to the ProtocolEventListener parameter.
     *
     * @param c The call object
     */
    public void newCall(Call c) {
        _call = c;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.newCall(_call);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);
    }

    /**
     * Hung up.
     * Via invokeLater() this is passed on to the ProtocolEventListener parameter.
     *
     * @param c The call object
     */
    public void hungUp(Call c) {
        _call = c;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.hungUp(_call);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);
    }

    /**
     * Ringing.
     * Via invokeLater() this is passed on to the ProtocolEventListener parameter.
     *
     * @param c The call object
     */
    public void ringing(Call c) {
        _call = c;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.ringing(_call);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);
    }

    /**
     * Answered.
     * Via invokeLater() this is passed on to the ProtocolEventListener parameter.
     *
     * @param c The call object
     */
    public void answered(Call c) {
        _call = c;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.answered(_call);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);
    }

    /**
     * registered
     *
     * @param f Friend
     * @param s boolean
     */
    public void registered(Friend f, boolean s) {
        final Friend ff = f;
        final boolean fs = s;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.registered(ff, fs);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);
    }

    /**
     * setHostReachable
     *
     * @param f Friend
     * @param b boolean
     * @param roundtrip int
     */
    public void setHostReachable(Friend f, boolean b, int roundtrip) {
        final Friend ff = f;
        final boolean fb = b;
        final int fr = roundtrip;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.setHostReachable(ff, fb, fr);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);

    }
        /**
     * setHostReachable
     *
     * @param f Friend
     * @param b boolean
     * @param roundtrip int
     */
    public void gotText(Friend f, String str) {
        final Friend ff = f;
        final String fs = str;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.gotText(ff, fs);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);
    }

    public void rxKeyChange(Friend f, boolean keyed) {
        final Friend ff = f;
        final boolean fb = keyed;
        Runnable r = new Runnable() {
            public void run() {
                if (_gui != null) {
                    _gui.rxKeyChange(ff, fb);
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(r);
    }

}
