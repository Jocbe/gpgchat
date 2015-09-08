/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.pack.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.jocbe.java.io.HTTPPacket;
import xyz.jocbe.java.io.UnsupportedFormatException;

/**
 *
 * This class takes care of communication directly with peers. It takes requests
 * and passes them on to an APIProvider and handles sending anything back to
 * clients.
 * 
 * @author jocbe
 */
public class CoreServer extends Thread {
    private static final Logger logger = LogManager.getLogger(CoreServer.class);
    
    private int port;
    private APIProvider apiProvider;
    private DataManager dataManager;
    
    public CoreServer(int port, APIProvider apiProvider,
            DataManager dataManager) {
        
        this.port = port;
        this.apiProvider = apiProvider;
        this.dataManager = dataManager;
    }
    
    @Override
    public void run() {
        ServerSocket ss;
        
        try {
            logger.trace("Trying to create new ServerSocket on port " + port + "...");
            ss = new ServerSocket(port);
        } catch (IOException ex) {
            logger.error("Error while trying to create ServerSocket: "
                    + ex.getMessage());
            return;
        }
        
        while(true) {
            Socket s;
            
            try {
                logger.trace("Waiting for incoming connection...");
                s = ss.accept();
            } catch (IOException ex) {
                logger.error("Error while accepting connection: "
                        + ex.getMessage());
                continue;
            }
            
            logger.trace("Incoming connection from " + s.getInetAddress().toString()
                    + ":" + s.getPort() + ", passing to worker...");
            ConnectionWorker cw = new ConnectionWorker(s);
            cw.start();
        }
        
    }
    
    private class ConnectionWorker extends Thread {
        
        private Socket socket;
        
        public ConnectionWorker(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            
            BufferedInputStream in;
            try {
                in = new BufferedInputStream(socket.getInputStream());
            } catch (IOException ex) {
                logger.error("IOException while trying to get InputStream from socket: "
                        + ex.getMessage());
                return;
            }
            
            // First we need to authenticate
            // TODO: We should probably wrap any communcation to peers into http
            boolean authenticated = false;
            // TODO: handle case where buffer size is insufficient
            byte[] b = new byte[4096];
            int len;
            while(!authenticated) {
                try {
                    len = in.read(b);                    
                } catch (IOException ex) {
                    logger.error("IOException while trying to receive authentication packet: "
                            + ex.getMessage());
                    continue;
                }
                
                HTTPPacket http;
                try {
                    http = HTTPPacket.parse(b, 0, len);
                } catch (UnsupportedFormatException ex) {
                    logger.error("UnsupportedFormatException while parsing authentication request: "
                            + ex.getMessage());
                    continue;
                }
                
                System.out.println("Request: " + http.getMethod().toString() + " -- " + http.getUri().toString() + " -- " + http.getHttpVersion());
            }
            
        }
    }
}
