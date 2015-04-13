package org.apache.tools.ant;

import java.util.*;
import java.io.*;

public interface ArgumentProcessor
{
    int readArguments(String[] p0, int p1);
    
    boolean handleArg(List<String> p0);
    
    void prepareConfigure(Project p0, List<String> p1);
    
    boolean handleArg(Project p0, List<String> p1);
    
    void printUsage(PrintStream p0);
}
