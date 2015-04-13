package org.apache.tools.ant;

import java.io.*;

public interface BuildLogger extends BuildListener
{
    void setMessageOutputLevel(int p0);
    
    void setOutputPrintStream(PrintStream p0);
    
    void setEmacsMode(boolean p0);
    
    void setErrorPrintStream(PrintStream p0);
}
