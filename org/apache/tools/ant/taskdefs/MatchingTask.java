package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.types.*;
import java.io.*;
import org.apache.tools.ant.*;
import java.util.*;
import org.apache.tools.ant.types.selectors.*;
import org.apache.tools.ant.types.selectors.modifiedselector.*;

public abstract class MatchingTask extends Task implements SelectorContainer
{
    protected FileSet fileset;
    
    public MatchingTask() {
        super();
        this.fileset = new FileSet();
    }
    
    public void setProject(final Project project) {
        super.setProject(project);
        this.fileset.setProject(project);
    }
    
    public PatternSet.NameEntry createInclude() {
        return this.fileset.createInclude();
    }
    
    public PatternSet.NameEntry createIncludesFile() {
        return this.fileset.createIncludesFile();
    }
    
    public PatternSet.NameEntry createExclude() {
        return this.fileset.createExclude();
    }
    
    public PatternSet.NameEntry createExcludesFile() {
        return this.fileset.createExcludesFile();
    }
    
    public PatternSet createPatternSet() {
        return this.fileset.createPatternSet();
    }
    
    public void setIncludes(final String includes) {
        this.fileset.setIncludes(includes);
    }
    
    public void XsetItems(final String itemString) {
        this.log("The items attribute is deprecated. Please use the includes attribute.", 1);
        if (itemString == null || itemString.equals("*") || itemString.equals(".")) {
            this.createInclude().setName("**");
        }
        else {
            final StringTokenizer tok = new StringTokenizer(itemString, ", ");
            while (tok.hasMoreTokens()) {
                final String pattern = tok.nextToken().trim();
                if (pattern.length() > 0) {
                    this.createInclude().setName(pattern + "/**");
                }
            }
        }
    }
    
    public void setExcludes(final String excludes) {
        this.fileset.setExcludes(excludes);
    }
    
    public void XsetIgnore(final String ignoreString) {
        this.log("The ignore attribute is deprecated.Please use the excludes attribute.", 1);
        if (ignoreString != null && ignoreString.length() > 0) {
            final StringTokenizer tok = new StringTokenizer(ignoreString, ", ", false);
            while (tok.hasMoreTokens()) {
                this.createExclude().setName("**/" + tok.nextToken().trim() + "/**");
            }
        }
    }
    
    public void setDefaultexcludes(final boolean useDefaultExcludes) {
        this.fileset.setDefaultexcludes(useDefaultExcludes);
    }
    
    protected DirectoryScanner getDirectoryScanner(final File baseDir) {
        this.fileset.setDir(baseDir);
        return this.fileset.getDirectoryScanner(this.getProject());
    }
    
    public void setIncludesfile(final File includesfile) {
        this.fileset.setIncludesfile(includesfile);
    }
    
    public void setExcludesfile(final File excludesfile) {
        this.fileset.setExcludesfile(excludesfile);
    }
    
    public void setCaseSensitive(final boolean isCaseSensitive) {
        this.fileset.setCaseSensitive(isCaseSensitive);
    }
    
    public void setFollowSymlinks(final boolean followSymlinks) {
        this.fileset.setFollowSymlinks(followSymlinks);
    }
    
    public boolean hasSelectors() {
        return this.fileset.hasSelectors();
    }
    
    public int selectorCount() {
        return this.fileset.selectorCount();
    }
    
    public FileSelector[] getSelectors(final Project p) {
        return this.fileset.getSelectors(p);
    }
    
    public Enumeration<FileSelector> selectorElements() {
        return this.fileset.selectorElements();
    }
    
    public void appendSelector(final FileSelector selector) {
        this.fileset.appendSelector(selector);
    }
    
    public void addSelector(final SelectSelector selector) {
        this.fileset.addSelector(selector);
    }
    
    public void addAnd(final AndSelector selector) {
        this.fileset.addAnd(selector);
    }
    
    public void addOr(final OrSelector selector) {
        this.fileset.addOr(selector);
    }
    
    public void addNot(final NotSelector selector) {
        this.fileset.addNot(selector);
    }
    
    public void addNone(final NoneSelector selector) {
        this.fileset.addNone(selector);
    }
    
    public void addMajority(final MajoritySelector selector) {
        this.fileset.addMajority(selector);
    }
    
    public void addDate(final DateSelector selector) {
        this.fileset.addDate(selector);
    }
    
    public void addSize(final SizeSelector selector) {
        this.fileset.addSize(selector);
    }
    
    public void addFilename(final FilenameSelector selector) {
        this.fileset.addFilename(selector);
    }
    
    public void addCustom(final ExtendSelector selector) {
        this.fileset.addCustom(selector);
    }
    
    public void addContains(final ContainsSelector selector) {
        this.fileset.addContains(selector);
    }
    
    public void addPresent(final PresentSelector selector) {
        this.fileset.addPresent(selector);
    }
    
    public void addDepth(final DepthSelector selector) {
        this.fileset.addDepth(selector);
    }
    
    public void addDepend(final DependSelector selector) {
        this.fileset.addDepend(selector);
    }
    
    public void addContainsRegexp(final ContainsRegexpSelector selector) {
        this.fileset.addContainsRegexp(selector);
    }
    
    public void addDifferent(final DifferentSelector selector) {
        this.fileset.addDifferent(selector);
    }
    
    public void addType(final TypeSelector selector) {
        this.fileset.addType(selector);
    }
    
    public void addModified(final ModifiedSelector selector) {
        this.fileset.addModified(selector);
    }
    
    public void add(final FileSelector selector) {
        this.fileset.add(selector);
    }
    
    protected final FileSet getImplicitFileSet() {
        return this.fileset;
    }
}
