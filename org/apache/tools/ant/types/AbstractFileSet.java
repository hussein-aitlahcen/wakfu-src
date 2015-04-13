package org.apache.tools.ant.types;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.selectors.modifiedselector.*;
import org.apache.tools.ant.types.selectors.*;
import java.util.*;

public abstract class AbstractFileSet extends DataType implements Cloneable, SelectorContainer
{
    private PatternSet defaultPatterns;
    private List<PatternSet> additionalPatterns;
    private List<FileSelector> selectors;
    private File dir;
    private boolean useDefaultExcludes;
    private boolean caseSensitive;
    private boolean followSymlinks;
    private boolean errorOnMissingDir;
    private int maxLevelsOfSymlinks;
    private DirectoryScanner directoryScanner;
    
    public AbstractFileSet() {
        super();
        this.defaultPatterns = new PatternSet();
        this.additionalPatterns = new ArrayList<PatternSet>();
        this.selectors = new ArrayList<FileSelector>();
        this.useDefaultExcludes = true;
        this.caseSensitive = true;
        this.followSymlinks = true;
        this.errorOnMissingDir = true;
        this.maxLevelsOfSymlinks = 5;
        this.directoryScanner = null;
    }
    
    protected AbstractFileSet(final AbstractFileSet fileset) {
        super();
        this.defaultPatterns = new PatternSet();
        this.additionalPatterns = new ArrayList<PatternSet>();
        this.selectors = new ArrayList<FileSelector>();
        this.useDefaultExcludes = true;
        this.caseSensitive = true;
        this.followSymlinks = true;
        this.errorOnMissingDir = true;
        this.maxLevelsOfSymlinks = 5;
        this.directoryScanner = null;
        this.dir = fileset.dir;
        this.defaultPatterns = fileset.defaultPatterns;
        this.additionalPatterns = fileset.additionalPatterns;
        this.selectors = fileset.selectors;
        this.useDefaultExcludes = fileset.useDefaultExcludes;
        this.caseSensitive = fileset.caseSensitive;
        this.followSymlinks = fileset.followSymlinks;
        this.errorOnMissingDir = fileset.errorOnMissingDir;
        this.maxLevelsOfSymlinks = fileset.maxLevelsOfSymlinks;
        this.setProject(fileset.getProject());
    }
    
    public void setRefid(final Reference r) throws BuildException {
        if (this.dir != null || this.defaultPatterns.hasPatterns(this.getProject())) {
            throw this.tooManyAttributes();
        }
        if (!this.additionalPatterns.isEmpty()) {
            throw this.noChildrenAllowed();
        }
        if (!this.selectors.isEmpty()) {
            throw this.noChildrenAllowed();
        }
        super.setRefid(r);
    }
    
    public synchronized void setDir(final File dir) throws BuildException {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.dir = dir;
        this.directoryScanner = null;
    }
    
    public File getDir() {
        return this.getDir(this.getProject());
    }
    
    public synchronized File getDir(final Project p) {
        if (this.isReference()) {
            return this.getRef(p).getDir(p);
        }
        this.dieOnCircularReference();
        return this.dir;
    }
    
    public synchronized PatternSet createPatternSet() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        final PatternSet patterns = new PatternSet();
        this.additionalPatterns.add(patterns);
        this.directoryScanner = null;
        return patterns;
    }
    
    public synchronized PatternSet.NameEntry createInclude() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createInclude();
    }
    
    public synchronized PatternSet.NameEntry createIncludesFile() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createIncludesFile();
    }
    
    public synchronized PatternSet.NameEntry createExclude() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createExclude();
    }
    
    public synchronized PatternSet.NameEntry createExcludesFile() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createExcludesFile();
    }
    
    public synchronized void setFile(final File file) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.setDir(file.getParentFile());
        this.createInclude().setName(file.getName());
    }
    
    public synchronized void setIncludes(final String includes) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.defaultPatterns.setIncludes(includes);
        this.directoryScanner = null;
    }
    
    public synchronized void appendIncludes(final String[] includes) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (includes != null) {
            for (int i = 0; i < includes.length; ++i) {
                this.defaultPatterns.createInclude().setName(includes[i]);
            }
            this.directoryScanner = null;
        }
    }
    
    public synchronized void setExcludes(final String excludes) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.defaultPatterns.setExcludes(excludes);
        this.directoryScanner = null;
    }
    
    public synchronized void appendExcludes(final String[] excludes) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (excludes != null) {
            for (int i = 0; i < excludes.length; ++i) {
                this.defaultPatterns.createExclude().setName(excludes[i]);
            }
            this.directoryScanner = null;
        }
    }
    
    public synchronized void setIncludesfile(final File incl) throws BuildException {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.defaultPatterns.setIncludesfile(incl);
        this.directoryScanner = null;
    }
    
    public synchronized void setExcludesfile(final File excl) throws BuildException {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.defaultPatterns.setExcludesfile(excl);
        this.directoryScanner = null;
    }
    
    public synchronized void setDefaultexcludes(final boolean useDefaultExcludes) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.useDefaultExcludes = useDefaultExcludes;
        this.directoryScanner = null;
    }
    
    public synchronized boolean getDefaultexcludes() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).getDefaultexcludes();
        }
        this.dieOnCircularReference();
        return this.useDefaultExcludes;
    }
    
    public synchronized void setCaseSensitive(final boolean caseSensitive) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.caseSensitive = caseSensitive;
        this.directoryScanner = null;
    }
    
    public synchronized boolean isCaseSensitive() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).isCaseSensitive();
        }
        this.dieOnCircularReference();
        return this.caseSensitive;
    }
    
    public synchronized void setFollowSymlinks(final boolean followSymlinks) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.followSymlinks = followSymlinks;
        this.directoryScanner = null;
    }
    
    public synchronized boolean isFollowSymlinks() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).isCaseSensitive();
        }
        this.dieOnCircularReference();
        return this.followSymlinks;
    }
    
    public void setMaxLevelsOfSymlinks(final int max) {
        this.maxLevelsOfSymlinks = max;
    }
    
    public int getMaxLevelsOfSymlinks() {
        return this.maxLevelsOfSymlinks;
    }
    
    public void setErrorOnMissingDir(final boolean errorOnMissingDir) {
        this.errorOnMissingDir = errorOnMissingDir;
    }
    
    public boolean getErrorOnMissingDir() {
        return this.errorOnMissingDir;
    }
    
    public DirectoryScanner getDirectoryScanner() {
        return this.getDirectoryScanner(this.getProject());
    }
    
    public DirectoryScanner getDirectoryScanner(final Project p) {
        if (this.isReference()) {
            return this.getRef(p).getDirectoryScanner(p);
        }
        this.dieOnCircularReference();
        DirectoryScanner ds = null;
        synchronized (this) {
            if (this.directoryScanner != null && p == this.getProject()) {
                ds = this.directoryScanner;
            }
            else {
                if (this.dir == null) {
                    throw new BuildException("No directory specified for " + this.getDataTypeName() + ".");
                }
                if (!this.dir.exists() && this.errorOnMissingDir) {
                    throw new BuildException(this.dir.getAbsolutePath() + " does not exist.");
                }
                if (!this.dir.isDirectory() && this.dir.exists()) {
                    throw new BuildException(this.dir.getAbsolutePath() + " is not a directory.");
                }
                ds = new DirectoryScanner();
                this.setupDirectoryScanner(ds, p);
                ds.setFollowSymlinks(this.followSymlinks);
                ds.setErrorOnMissingDir(this.errorOnMissingDir);
                ds.setMaxLevelsOfSymlinks(this.maxLevelsOfSymlinks);
                this.directoryScanner = ((p == this.getProject()) ? ds : this.directoryScanner);
            }
        }
        ds.scan();
        return ds;
    }
    
    public void setupDirectoryScanner(final FileScanner ds) {
        this.setupDirectoryScanner(ds, this.getProject());
    }
    
    public synchronized void setupDirectoryScanner(final FileScanner ds, final Project p) {
        if (this.isReference()) {
            this.getRef(p).setupDirectoryScanner(ds, p);
            return;
        }
        this.dieOnCircularReference(p);
        if (ds == null) {
            throw new IllegalArgumentException("ds cannot be null");
        }
        ds.setBasedir(this.dir);
        final PatternSet ps = this.mergePatterns(p);
        p.log(this.getDataTypeName() + ": Setup scanner in dir " + this.dir + " with " + ps, 4);
        ds.setIncludes(ps.getIncludePatterns(p));
        ds.setExcludes(ps.getExcludePatterns(p));
        if (ds instanceof SelectorScanner) {
            final SelectorScanner ss = (SelectorScanner)ds;
            ss.setSelectors(this.getSelectors(p));
        }
        if (this.useDefaultExcludes) {
            ds.addDefaultExcludes();
        }
        ds.setCaseSensitive(this.caseSensitive);
    }
    
    protected AbstractFileSet getRef(final Project p) {
        return (AbstractFileSet)this.getCheckedRef(p);
    }
    
    public synchronized boolean hasSelectors() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).hasSelectors();
        }
        this.dieOnCircularReference();
        return !this.selectors.isEmpty();
    }
    
    public synchronized boolean hasPatterns() {
        if (this.isReference() && this.getProject() != null) {
            return this.getRef(this.getProject()).hasPatterns();
        }
        this.dieOnCircularReference();
        if (this.defaultPatterns.hasPatterns(this.getProject())) {
            return true;
        }
        for (final PatternSet ps : this.additionalPatterns) {
            if (ps.hasPatterns(this.getProject())) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized int selectorCount() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).selectorCount();
        }
        this.dieOnCircularReference();
        return this.selectors.size();
    }
    
    public synchronized FileSelector[] getSelectors(final Project p) {
        if (this.isReference()) {
            return this.getRef(this.getProject()).getSelectors(p);
        }
        this.dieOnCircularReference(p);
        return this.selectors.toArray(new FileSelector[this.selectors.size()]);
    }
    
    public synchronized Enumeration<FileSelector> selectorElements() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).selectorElements();
        }
        this.dieOnCircularReference();
        return Collections.enumeration(this.selectors);
    }
    
    public synchronized void appendSelector(final FileSelector selector) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.selectors.add(selector);
        this.directoryScanner = null;
        this.setChecked(false);
    }
    
    public void addSelector(final SelectSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addAnd(final AndSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addOr(final OrSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addNot(final NotSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addNone(final NoneSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addMajority(final MajoritySelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDate(final DateSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addSize(final SizeSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDifferent(final DifferentSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addFilename(final FilenameSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addType(final TypeSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addCustom(final ExtendSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addContains(final ContainsSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addPresent(final PresentSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDepth(final DepthSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDepend(final DependSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addContainsRegexp(final ContainsRegexpSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addModified(final ModifiedSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addReadable(final ReadableSelector r) {
        this.appendSelector(r);
    }
    
    public void addWritable(final WritableSelector w) {
        this.appendSelector(w);
    }
    
    public void add(final FileSelector selector) {
        this.appendSelector(selector);
    }
    
    public String toString() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).toString();
        }
        this.dieOnCircularReference();
        final DirectoryScanner ds = this.getDirectoryScanner(this.getProject());
        final String[] files = ds.getIncludedFiles();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < files.length; ++i) {
            if (i > 0) {
                sb.append(';');
            }
            sb.append(files[i]);
        }
        return sb.toString();
    }
    
    public synchronized Object clone() {
        if (this.isReference()) {
            return this.getRef(this.getProject()).clone();
        }
        try {
            final AbstractFileSet fs = (AbstractFileSet)super.clone();
            fs.defaultPatterns = (PatternSet)this.defaultPatterns.clone();
            fs.additionalPatterns = new ArrayList<PatternSet>(this.additionalPatterns.size());
            for (final PatternSet ps : this.additionalPatterns) {
                fs.additionalPatterns.add((PatternSet)ps.clone());
            }
            fs.selectors = new ArrayList<FileSelector>(this.selectors);
            return fs;
        }
        catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }
    
    public String[] mergeIncludes(final Project p) {
        return this.mergePatterns(p).getIncludePatterns(p);
    }
    
    public String[] mergeExcludes(final Project p) {
        return this.mergePatterns(p).getExcludePatterns(p);
    }
    
    public synchronized PatternSet mergePatterns(final Project p) {
        if (this.isReference()) {
            return this.getRef(p).mergePatterns(p);
        }
        this.dieOnCircularReference();
        final PatternSet ps = (PatternSet)this.defaultPatterns.clone();
        for (int count = this.additionalPatterns.size(), i = 0; i < count; ++i) {
            ps.append(this.additionalPatterns.get(i), p);
        }
        return ps;
    }
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            for (final FileSelector fileSelector : this.selectors) {
                if (fileSelector instanceof DataType) {
                    DataType.pushAndInvokeCircularReferenceCheck((DataType)fileSelector, stk, p);
                }
            }
            for (final PatternSet ps : this.additionalPatterns) {
                DataType.pushAndInvokeCircularReferenceCheck(ps, stk, p);
            }
            this.setChecked(true);
        }
    }
}
