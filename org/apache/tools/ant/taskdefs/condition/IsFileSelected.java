package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.util.*;
import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.selectors.*;

public class IsFileSelected extends AbstractSelectorContainer implements Condition
{
    private static final FileUtils FILE_UTILS;
    private File file;
    private File baseDir;
    
    public void setFile(final File file) {
        this.file = file;
    }
    
    public void setBaseDir(final File baseDir) {
        this.baseDir = baseDir;
    }
    
    public void validate() {
        if (this.selectorCount() != 1) {
            throw new BuildException("Only one selector allowed");
        }
        super.validate();
    }
    
    public boolean eval() {
        if (this.file == null) {
            throw new BuildException("file attribute not set");
        }
        this.validate();
        File myBaseDir = this.baseDir;
        if (myBaseDir == null) {
            myBaseDir = this.getProject().getBaseDir();
        }
        final FileSelector f = this.getSelectors(this.getProject())[0];
        return f.isSelected(myBaseDir, IsFileSelected.FILE_UTILS.removeLeadingPath(myBaseDir, this.file), this.file);
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
