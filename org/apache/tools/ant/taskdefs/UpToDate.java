package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.taskdefs.condition.*;
import java.io.*;
import org.apache.tools.ant.types.resources.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

public class UpToDate extends Task implements Condition
{
    private String property;
    private String value;
    private File sourceFile;
    private File targetFile;
    private Vector sourceFileSets;
    private Union sourceResources;
    protected Mapper mapperElement;
    
    public UpToDate() {
        super();
        this.sourceFileSets = new Vector();
        this.sourceResources = new Union();
        this.mapperElement = null;
    }
    
    public void setProperty(final String property) {
        this.property = property;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    private String getValue() {
        return (this.value != null) ? this.value : "true";
    }
    
    public void setTargetFile(final File file) {
        this.targetFile = file;
    }
    
    public void setSrcfile(final File file) {
        this.sourceFile = file;
    }
    
    public void addSrcfiles(final FileSet fs) {
        this.sourceFileSets.addElement(fs);
    }
    
    public Union createSrcResources() {
        return this.sourceResources;
    }
    
    public Mapper createMapper() throws BuildException {
        if (this.mapperElement != null) {
            throw new BuildException("Cannot define more than one mapper", this.getLocation());
        }
        return this.mapperElement = new Mapper(this.getProject());
    }
    
    public void add(final FileNameMapper fileNameMapper) {
        this.createMapper().add(fileNameMapper);
    }
    
    public boolean eval() {
        if (this.sourceFileSets.size() == 0 && this.sourceResources.size() == 0 && this.sourceFile == null) {
            throw new BuildException("At least one srcfile or a nested <srcfiles> or <srcresources> element must be set.");
        }
        if ((this.sourceFileSets.size() > 0 || this.sourceResources.size() > 0) && this.sourceFile != null) {
            throw new BuildException("Cannot specify both the srcfile attribute and a nested <srcfiles> or <srcresources> element.");
        }
        if (this.targetFile == null && this.mapperElement == null) {
            throw new BuildException("The targetfile attribute or a nested mapper element must be set.");
        }
        if (this.targetFile != null && !this.targetFile.exists()) {
            this.log("The targetfile \"" + this.targetFile.getAbsolutePath() + "\" does not exist.", 3);
            return false;
        }
        if (this.sourceFile != null && !this.sourceFile.exists()) {
            throw new BuildException(this.sourceFile.getAbsolutePath() + " not found.");
        }
        boolean upToDate = true;
        if (this.sourceFile != null) {
            if (this.mapperElement == null) {
                upToDate = (this.targetFile.lastModified() >= this.sourceFile.lastModified());
            }
            else {
                final SourceFileScanner sfs = new SourceFileScanner(this);
                upToDate = (sfs.restrict(new String[] { this.sourceFile.getAbsolutePath() }, null, null, this.mapperElement.getImplementation()).length == 0);
            }
            if (!upToDate) {
                this.log(this.sourceFile.getAbsolutePath() + " is newer than (one of) its target(s).", 3);
            }
        }
        FileSet fs;
        DirectoryScanner ds;
        for (Enumeration e = this.sourceFileSets.elements(); upToDate && e.hasMoreElements(); upToDate = this.scanDir(fs.getDir(this.getProject()), ds.getIncludedFiles())) {
            fs = e.nextElement();
            ds = fs.getDirectoryScanner(this.getProject());
        }
        if (upToDate) {
            final Resource[] r = this.sourceResources.listResources();
            if (r.length > 0) {
                upToDate = (ResourceUtils.selectOutOfDateSources(this, r, this.getMapper(), this.getProject()).length == 0);
            }
        }
        return upToDate;
    }
    
    public void execute() throws BuildException {
        if (this.property == null) {
            throw new BuildException("property attribute is required.", this.getLocation());
        }
        final boolean upToDate = this.eval();
        if (upToDate) {
            this.getProject().setNewProperty(this.property, this.getValue());
            if (this.mapperElement == null) {
                this.log("File \"" + this.targetFile.getAbsolutePath() + "\" is up-to-date.", 3);
            }
            else {
                this.log("All target files are up-to-date.", 3);
            }
        }
    }
    
    protected boolean scanDir(final File srcDir, final String[] files) {
        final SourceFileScanner sfs = new SourceFileScanner(this);
        final FileNameMapper mapper = this.getMapper();
        File dir = srcDir;
        if (this.mapperElement == null) {
            dir = null;
        }
        return sfs.restrict(files, srcDir, dir, mapper).length == 0;
    }
    
    private FileNameMapper getMapper() {
        FileNameMapper mapper = null;
        if (this.mapperElement == null) {
            final MergingMapper mm = new MergingMapper();
            mm.setTo(this.targetFile.getAbsolutePath());
            mapper = mm;
        }
        else {
            mapper = this.mapperElement.getImplementation();
        }
        return mapper;
    }
}
