package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import java.io.*;

public class DifferentSelector extends MappingSelector
{
    private static final FileUtils FILE_UTILS;
    private boolean ignoreFileTimes;
    private boolean ignoreContents;
    
    public DifferentSelector() {
        super();
        this.ignoreFileTimes = true;
        this.ignoreContents = false;
    }
    
    public void setIgnoreFileTimes(final boolean ignoreFileTimes) {
        this.ignoreFileTimes = ignoreFileTimes;
    }
    
    public void setIgnoreContents(final boolean ignoreContents) {
        this.ignoreContents = ignoreContents;
    }
    
    protected boolean selectionTest(final File srcfile, final File destfile) {
        if (srcfile.exists() != destfile.exists()) {
            return true;
        }
        if (srcfile.length() != destfile.length()) {
            return true;
        }
        if (!this.ignoreFileTimes) {
            final boolean sameDate = destfile.lastModified() >= srcfile.lastModified() - this.granularity && destfile.lastModified() <= srcfile.lastModified() + this.granularity;
            if (!sameDate) {
                return true;
            }
        }
        if (!this.ignoreContents) {
            try {
                return !DifferentSelector.FILE_UTILS.contentEquals(srcfile, destfile);
            }
            catch (IOException e) {
                throw new BuildException("while comparing " + srcfile + " and " + destfile, e);
            }
        }
        return false;
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
