


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.net.*;
import java.text.*;
import java.util.*;
import com.mexuar.corraleta.ui.BeanCanFrameManager;

public class WebTransceiver extends BeanCanFrameManager {

    Boolean go_ahead = false;
    static BorderLayout  mainBorderLayout = new BorderLayout();
    public WebTransceiver() {

      }
    
    public static void main(String[] args )
    {
    	BeanCanFrameManager f = new BeanCanFrameManager();
    	f.setSize(500, 600);
    	f.setVisible(true);
    	f.setLayout(mainBorderLayout);      	
    }
    

}