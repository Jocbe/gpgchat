/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.pack.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
}
