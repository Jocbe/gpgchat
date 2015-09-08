/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.pack.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jocbe
 */
public class Entry {
    private static final Logger logger = LogManager.getLogger(Entry.class);
    
    public static void main(String[] args) {
        CoreServer cs = new CoreServer(9901, null, null);
        cs.start();
    }
}
