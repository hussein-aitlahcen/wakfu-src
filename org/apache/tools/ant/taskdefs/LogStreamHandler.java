package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;

public class LogStreamHandler extends PumpStreamHandler
{
    public LogStreamHandler(final Task task, final int outlevel, final int errlevel) {
        this((ProjectComponent)task, outlevel, errlevel);
    }
    
    public LogStreamHandler(final ProjectComponent pc, final int outlevel, final int errlevel) {
        super(new LogOutputStream(pc, outlevel), new LogOutputStream(pc, errlevel));
    }
    
    public void stop() {
        super.stop();
        try {
            this.getErr().close();
            this.getOut().close();
        }
        catch (IOException e) {
            throw new BuildException(e);
        }
    }
}
