package org.apache.tools.ant.types.selectors;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.*;

public class PresentSelector extends BaseSelector
{
    private File targetdir;
    private Mapper mapperElement;
    private FileNameMapper map;
    private boolean destmustexist;
    
    public PresentSelector() {
        super();
        this.targetdir = null;
        this.mapperElement = null;
        this.map = null;
        this.destmustexist = true;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{presentselector targetdir: ");
        if (this.targetdir == null) {
            buf.append("NOT YET SET");
        }
        else {
            buf.append(this.targetdir.getName());
        }
        buf.append(" present: ");
        if (this.destmustexist) {
            buf.append("both");
        }
        else {
            buf.append("srconly");
        }
        if (this.map != null) {
            buf.append(this.map.toString());
        }
        else if (this.mapperElement != null) {
            buf.append(this.mapperElement.toString());
        }
        buf.append("}");
        return buf.toString();
    }
    
    public void setTargetdir(final File targetdir) {
        this.targetdir = targetdir;
    }
    
    public Mapper createMapper() throws BuildException {
        if (this.map != null || this.mapperElement != null) {
            throw new BuildException("Cannot define more than one mapper");
        }
        return this.mapperElement = new Mapper(this.getProject());
    }
    
    public void addConfigured(final FileNameMapper fileNameMapper) {
        if (this.map != null || this.mapperElement != null) {
            throw new BuildException("Cannot define more than one mapper");
        }
        this.map = fileNameMapper;
    }
    
    public void setPresent(final FilePresence fp) {
        if (fp.getIndex() == 0) {
            this.destmustexist = false;
        }
    }
    
    public void verifySettings() {
        if (this.targetdir == null) {
            this.setError("The targetdir attribute is required.");
        }
        if (this.map == null) {
            if (this.mapperElement == null) {
                this.map = new IdentityMapper();
            }
            else {
                this.map = this.mapperElement.getImplementation();
                if (this.map == null) {
                    this.setError("Could not set <mapper> element.");
                }
            }
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        this.validate();
        final String[] destfiles = this.map.mapFileName(filename);
        if (destfiles == null) {
            return false;
        }
        if (destfiles.length != 1 || destfiles[0] == null) {
            throw new BuildException("Invalid destination file results for " + this.targetdir + " with filename " + filename);
        }
        final String destname = destfiles[0];
        final File destfile = FileUtils.getFileUtils().resolveFile(this.targetdir, destname);
        return destfile.exists() == this.destmustexist;
    }
    
    public static class FilePresence extends EnumeratedAttribute
    {
        public String[] getValues() {
            return new String[] { "srconly", "both" };
        }
    }
}
