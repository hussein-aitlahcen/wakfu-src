package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import java.io.*;

public class LogOutputStream extends LineOrientedOutputStream
{
    private ProjectComponent pc;
    private int level;
    
    public LogOutputStream(final ProjectComponent pc) {
        super();
        this.level = 2;
        this.pc = pc;
    }
    
    public LogOutputStream(final Task task, final int level) {
        this((ProjectComponent)task, level);
    }
    
    public LogOutputStream(final ProjectComponent pc, final int level) {
        this(pc);
        this.level = level;
    }
    
    protected void processBuffer() {
        try {
            super.processBuffer();
        }
        catch (IOException e) {
            throw new RuntimeException("Impossible IOException caught: " + e);
        }
    }
    
    protected void processLine(final String line) {
        this.processLine(line, this.level);
    }
    
    protected void processLine(final String line, final int level) {
        this.pc.log(line, level);
    }
    
    public int getMessageLevel() {
        return this.level;
    }
}
