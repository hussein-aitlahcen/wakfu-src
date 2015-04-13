package org.apache.tools.ant.taskdefs;

import java.io.*;

public interface ExecuteStreamHandler
{
    void setProcessInputStream(OutputStream p0) throws IOException;
    
    void setProcessErrorStream(InputStream p0) throws IOException;
    
    void setProcessOutputStream(InputStream p0) throws IOException;
    
    void start() throws IOException;
    
    void stop();
}
