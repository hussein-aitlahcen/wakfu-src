package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import java.io.*;

public class FilesMatch implements Condition
{
    private static final FileUtils FILE_UTILS;
    private File file1;
    private File file2;
    private boolean textfile;
    
    public FilesMatch() {
        super();
        this.textfile = false;
    }
    
    public void setFile1(final File file1) {
        this.file1 = file1;
    }
    
    public void setFile2(final File file2) {
        this.file2 = file2;
    }
    
    public void setTextfile(final boolean textfile) {
        this.textfile = textfile;
    }
    
    public boolean eval() throws BuildException {
        if (this.file1 == null || this.file2 == null) {
            throw new BuildException("both file1 and file2 are required in filesmatch");
        }
        boolean matches = false;
        try {
            matches = FilesMatch.FILE_UTILS.contentEquals(this.file1, this.file2, this.textfile);
        }
        catch (IOException ioe) {
            throw new BuildException("when comparing files: " + ioe.getMessage(), ioe);
        }
        return matches;
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
