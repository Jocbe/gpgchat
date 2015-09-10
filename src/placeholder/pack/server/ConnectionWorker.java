/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.pack.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.jocbe.java.io.HTTPPacket;
import xyz.jocbe.java.io.UnsupportedFormatException;

/**
 *
 * @author jocbe
 */
class ConnectionWorker extends Thread {
        
        private Socket socket;
        private static final Logger logger = LogManager.getLogger(ConnectionWorker.class);
        
        public ConnectionWorker(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            
            Client client = new Client();
            
            BufferedInputStream in;
            try {
                in = new BufferedInputStream(socket.getInputStream());
            } catch (IOException ex) {
                logger.error("IOException while trying to get InputStream from socket: "
                        + ex.getMessage());
                return;
            }
            
            // TODO: handle case where buffer size is insufficient
            byte[] b = new byte[4096];
            int len;
            
            while(true) {
                try {
                    len = in.read(b);                    
                } catch (IOException ex) {
                    logger.error("IOException while trying to receive authentication packet: "
                            + ex.getMessage());
                    continue;
                }
                
                // Return if -1 bytes are read (end of stream)
                if(len < 0) {return;}
                
                HTTPPacket hp;
                try {
                    hp = HTTPPacket.parse(b, 0, len);
                } catch (UnsupportedFormatException ex) {
                    logger.error("UnsupportedFormatException while parsing authentication request: "
                            + ex.getMessage());
                    continue;
                }
                
                APIProvider.handle(client, hp);
                
            }
            
        }
    }
