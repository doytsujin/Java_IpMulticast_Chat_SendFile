/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receive;

import java.io.IOException;

/**
 *
 * @author Admin
 */
public class ChatReceive extends Receive implements Runnable{

    public ChatReceive(String multicastAddress, int port) throws IOException {
        super(multicastAddress, port);
    }


    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
