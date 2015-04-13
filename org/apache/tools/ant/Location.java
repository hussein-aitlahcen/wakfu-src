package org.apache.tools.ant;

import java.io.*;
import org.apache.tools.ant.util.*;
import org.xml.sax.*;

public class Location implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String fileName;
    private final int lineNumber;
    private final int columnNumber;
    public static final Location UNKNOWN_LOCATION;
    private static final FileUtils FILE_UTILS;
    
    private Location() {
        this(null, 0, 0);
    }
    
    public Location(final String fileName) {
        this(fileName, 0, 0);
    }
    
    public Location(final Locator loc) {
        this(loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber());
    }
    
    public Location(final String fileName, final int lineNumber, final int columnNumber) {
        super();
        if (fileName != null && fileName.startsWith("file:")) {
            this.fileName = Location.FILE_UTILS.fromURI(fileName);
        }
        else {
            this.fileName = fileName;
        }
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    public int getColumnNumber() {
        return this.columnNumber;
    }
    
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        if (this.fileName != null) {
            buf.append(this.fileName);
            if (this.lineNumber != 0) {
                buf.append(":");
                buf.append(this.lineNumber);
            }
            buf.append(": ");
        }
        return buf.toString();
    }
    
    public boolean equals(final Object other) {
        return this == other || (other != null && other.getClass() == this.getClass() && this.toString().equals(other.toString()));
    }
    
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    static {
        UNKNOWN_LOCATION = new Location();
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
