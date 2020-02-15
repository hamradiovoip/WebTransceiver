// NAME
//      $RCSfile: ABuffer.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.6 $
// CREATED
//      $Date: 2006/10/06 11:03:00 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//
package com.mexuar.corraleta.audio;

/**
 * class to encapsulate the concept of an audio buffer and it's state
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ABuffer {

    private final static String version_id =
        "@(#)$Id: ABuffer.java,v 1.6 2006/10/06 11:03:00 uid100 Exp $ Copyright Mexuar Technologies Ltd";

  private byte[] _buff;
  private boolean _written;
  private long _stamp;
    private long _astamp;

    public ABuffer(int sz){
    _buff = new byte[sz];
  }
  public byte [] getBuff(){
    return _buff;
  }
  public boolean isWritten(){
    return _written;
  }
  public void setWritten(){
    _written = true;
  }
  public void setRead(){
    _written = false;
  }
  public long getStamp(){
      return _stamp;
  }
  public void setStamp(long stamp){
      _stamp = stamp;
  }

  public long getAStamp(){
      return _astamp;
  }
  public void setAStamp(long as){
      _astamp = as;
  }
}
