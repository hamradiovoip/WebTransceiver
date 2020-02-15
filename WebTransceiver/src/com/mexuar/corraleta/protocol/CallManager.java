// NAME
//      $RCSfile: CallManager.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.4 $
// CREATED
//      $Date: 2006/02/23 12:54:47 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//
package com.mexuar.corraleta.protocol;

public interface CallManager {
  /**
   * Returns if we can deal with this call or not. Pretends to be a
   * small PBX. Calls can be rejected if (for example) there isn't a
   * telephone plugged into this extension.
   */
  public boolean accept(Call ca);
}
