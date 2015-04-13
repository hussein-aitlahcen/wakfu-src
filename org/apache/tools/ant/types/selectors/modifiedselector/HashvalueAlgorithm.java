package org.apache.tools.ant.types.selectors.modifiedselector;

import org.apache.tools.ant.util.*;
import java.io.*;

public class HashvalueAlgorithm implements Algorithm
{
    public boolean isValid() {
        return true;
    }
    
    public String getValue(final File file) {
        Reader r = null;
        try {
            if (!file.canRead()) {
                final String s = null;
                FileUtils.close(r);
                return s;
            }
            r = new FileReader(file);
            final int hash = FileUtils.readFully(r).hashCode();
            final String string = Integer.toString(hash);
            FileUtils.close(r);
            return string;
        }
        catch (Exception e) {
            final String s2 = null;
            FileUtils.close(r);
            return s2;
        }
        finally {
            FileUtils.close(r);
        }
    }
    
    public String toString() {
        return "HashvalueAlgorithm";
    }
}
