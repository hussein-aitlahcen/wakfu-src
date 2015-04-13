package org.apache.tools.ant.taskdefs.condition;

import java.util.*;
import org.apache.tools.ant.*;
import java.io.*;
import java.net.*;

public class Http extends ProjectComponent implements Condition
{
    private static final int ERROR_BEGINS = 400;
    private static final String DEFAULT_REQUEST_METHOD = "GET";
    private String spec;
    private String requestMethod;
    private int errorsBeginAt;
    
    public Http() {
        super();
        this.spec = null;
        this.requestMethod = "GET";
        this.errorsBeginAt = 400;
    }
    
    public void setUrl(final String url) {
        this.spec = url;
    }
    
    public void setErrorsBeginAt(final int errorsBeginAt) {
        this.errorsBeginAt = errorsBeginAt;
    }
    
    public void setRequestMethod(final String method) {
        this.requestMethod = ((method == null) ? "GET" : method.toUpperCase(Locale.ENGLISH));
    }
    
    public boolean eval() throws BuildException {
        if (this.spec == null) {
            throw new BuildException("No url specified in http condition");
        }
        this.log("Checking for " + this.spec, 3);
        try {
            final URL url = new URL(this.spec);
            try {
                final URLConnection conn = url.openConnection();
                if (conn instanceof HttpURLConnection) {
                    final HttpURLConnection http = (HttpURLConnection)conn;
                    http.setRequestMethod(this.requestMethod);
                    final int code = http.getResponseCode();
                    this.log("Result code for " + this.spec + " was " + code, 3);
                    return code > 0 && code < this.errorsBeginAt;
                }
            }
            catch (ProtocolException pe) {
                throw new BuildException("Invalid HTTP protocol: " + this.requestMethod, pe);
            }
            catch (IOException e2) {
                return false;
            }
        }
        catch (MalformedURLException e) {
            throw new BuildException("Badly formed URL: " + this.spec, e);
        }
        return true;
    }
}
