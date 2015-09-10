/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.pack.server;

import java.net.URI;
import javax.xml.ws.http.HTTPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.jocbe.java.io.HTTPPacket;

/**
 *
 * The APIProvider class handles requests to the server. It parses them and
 * processes them however necessary. Requests should be passed here by the
 * server.
 * 
 * @author jocbe
 */
public class APIProvider {
    
    private static final Logger logger = LogManager.getLogger(APIProvider.class);
    
    public static void handle(Client c, HTTPPacket hp) {
        URI uri = hp.getUri();
        String path = uri.getPath().toLowerCase();
        
        switch(path) {
            case "/api/authenticate": authenticate(c, hp);
            default: throw new HTTPException(404);
        }
    }
    
    public static void authenticate(Client client, HTTPPacket hp) {
        HTTPPacket.RequestMethod rm = hp.getMethod();
        
        if(rm != HTTPPacket.RequestMethod.POST) {
            throw new HTTPException(405);
        }
        
        // TODO: Authenticate
        
        
    }
    
}
