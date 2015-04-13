package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.*;
import java.net.*;
import java.io.*;

public class Socket extends ProjectComponent implements Condition
{
    private String server;
    private int port;
    
    public Socket() {
        super();
        this.server = null;
        this.port = 0;
    }
    
    public void setServer(final String server) {
        this.server = server;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public boolean eval() throws BuildException {
        if (this.server == null) {
            throw new BuildException("No server specified in socket condition");
        }
        if (this.port == 0) {
            throw new BuildException("No port specified in socket condition");
        }
        this.log("Checking for listener at " + this.server + ":" + this.port, 3);
        java.net.Socket s = null;
        try {
            s = new java.net.Socket(this.server, this.port);
        }
        catch (IOException e) {
            return false;
        }
        finally {
            if (s != null) {
                try {
                    s.close();
                }
                catch (IOException ex) {}
            }
        }
        return true;
    }
}
