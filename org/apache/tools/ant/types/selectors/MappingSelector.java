package org.apache.tools.ant.types.selectors;

import java.io.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;

public abstract class MappingSelector extends BaseSelector
{
    private static final FileUtils FILE_UTILS;
    protected File targetdir;
    protected Mapper mapperElement;
    protected FileNameMapper map;
    protected int granularity;
    
    public MappingSelector() {
        super();
        this.targetdir = null;
        this.mapperElement = null;
        this.map = null;
        this.granularity = 0;
        this.granularity = (int)MappingSelector.FILE_UTILS.getFileTimestampGranularity();
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
            throw new BuildException("Invalid destination file results for " + this.targetdir.getName() + " with filename " + filename);
        }
        final String destname = destfiles[0];
        final File destfile = MappingSelector.FILE_UTILS.resolveFile(this.targetdir, destname);
        final boolean selected = this.selectionTest(file, destfile);
        return selected;
    }
    
    protected abstract boolean selectionTest(final File p0, final File p1);
    
    public void setGranularity(final int granularity) {
        this.granularity = granularity;
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
