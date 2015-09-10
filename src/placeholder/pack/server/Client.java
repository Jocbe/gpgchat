/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.pack.server;

/**
 *
 * @author jocbe
 */
public class Client {
    
    // TOOD: proof for concurrent use
    
    private boolean authenticated = false;
    private String userid;
    private String authToken;
    
    public void authenticate(String userid, String authToken) {
        this.userid = userid;
        this.authToken = authToken;
        authenticated = true;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public String getUserId() {
        return userid;
    }
    
    public String getAuthToken() {
        return authToken;
    }
}
