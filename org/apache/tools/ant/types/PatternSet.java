package org.apache.tools.ant.types;

import java.io.*;
import org.apache.tools.ant.util.*;
import java.util.*;
import org.apache.tools.ant.*;

public class PatternSet extends DataType implements Cloneable
{
    private List<NameEntry> includeList;
    private List<NameEntry> excludeList;
    private List<NameEntry> includesFileList;
    private List<NameEntry> excludesFileList;
    
    public PatternSet() {
        super();
        this.includeList = new ArrayList<NameEntry>();
        this.excludeList = new ArrayList<NameEntry>();
        this.includesFileList = new ArrayList<NameEntry>();
        this.excludesFileList = new ArrayList<NameEntry>();
    }
    
    public void setRefid(final Reference r) throws BuildException {
        if (!this.includeList.isEmpty() || !this.excludeList.isEmpty()) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    public void addConfiguredPatternset(final PatternSet p) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        final String[] nestedIncludes = p.getIncludePatterns(this.getProject());
        final String[] nestedExcludes = p.getExcludePatterns(this.getProject());
        if (nestedIncludes != null) {
            for (int i = 0; i < nestedIncludes.length; ++i) {
                this.createInclude().setName(nestedIncludes[i]);
            }
        }
        if (nestedExcludes != null) {
            for (int i = 0; i < nestedExcludes.length; ++i) {
                this.createExclude().setName(nestedExcludes[i]);
            }
        }
    }
    
    public NameEntry createInclude() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        return this.addPatternToList(this.includeList);
    }
    
    public NameEntry createIncludesFile() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        return this.addPatternToList(this.includesFileList);
    }
    
    public NameEntry createExclude() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        return this.addPatternToList(this.excludeList);
    }
    
    public NameEntry createExcludesFile() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        return this.addPatternToList(this.excludesFileList);
    }
    
    public void setIncludes(final String includes) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (includes != null && includes.length() > 0) {
            final StringTokenizer tok = new StringTokenizer(includes, ", ", false);
            while (tok.hasMoreTokens()) {
                this.createInclude().setName(tok.nextToken());
            }
        }
    }
    
    public void setExcludes(final String excludes) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (excludes != null && excludes.length() > 0) {
            final StringTokenizer tok = new StringTokenizer(excludes, ", ", false);
            while (tok.hasMoreTokens()) {
                this.createExclude().setName(tok.nextToken());
            }
        }
    }
    
    private NameEntry addPatternToList(final List<NameEntry> list) {
        final NameEntry result = new NameEntry();
        list.add(result);
        return result;
    }
    
    public void setIncludesfile(final File includesFile) throws BuildException {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.createIncludesFile().setName(includesFile.getAbsolutePath());
    }
    
    public void setExcludesfile(final File excludesFile) throws BuildException {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.createExcludesFile().setName(excludesFile.getAbsolutePath());
    }
    
    private void readPatterns(final File patternfile, final List<NameEntry> patternlist, final Project p) throws BuildException {
        BufferedReader patternReader = null;
        try {
            patternReader = new BufferedReader(new FileReader(patternfile));
            for (String line = patternReader.readLine(); line != null; line = patternReader.readLine()) {
                if (line.length() > 0) {
                    line = p.replaceProperties(line);
                    this.addPatternToList(patternlist).setName(line);
                }
            }
        }
        catch (IOException ioe) {
            throw new BuildException("An error occurred while reading from pattern file: " + patternfile, ioe);
        }
        finally {
            FileUtils.close(patternReader);
        }
    }
    
    public void append(final PatternSet other, final Project p) {
        if (this.isReference()) {
            throw new BuildException("Cannot append to a reference");
        }
        this.dieOnCircularReference(p);
        final String[] incl = other.getIncludePatterns(p);
        if (incl != null) {
            for (int i = 0; i < incl.length; ++i) {
                this.createInclude().setName(incl[i]);
            }
        }
        final String[] excl = other.getExcludePatterns(p);
        if (excl != null) {
            for (int j = 0; j < excl.length; ++j) {
                this.createExclude().setName(excl[j]);
            }
        }
    }
    
    public String[] getIncludePatterns(final Project p) {
        if (this.isReference()) {
            return this.getRef(p).getIncludePatterns(p);
        }
        this.dieOnCircularReference(p);
        this.readFiles(p);
        return this.makeArray(this.includeList, p);
    }
    
    public String[] getExcludePatterns(final Project p) {
        if (this.isReference()) {
            return this.getRef(p).getExcludePatterns(p);
        }
        this.dieOnCircularReference(p);
        this.readFiles(p);
        return this.makeArray(this.excludeList, p);
    }
    
    public boolean hasPatterns(final Project p) {
        if (this.isReference()) {
            return this.getRef(p).hasPatterns(p);
        }
        this.dieOnCircularReference(p);
        return this.includesFileList.size() > 0 || this.excludesFileList.size() > 0 || this.includeList.size() > 0 || this.excludeList.size() > 0;
    }
    
    private PatternSet getRef(final Project p) {
        return (PatternSet)this.getCheckedRef(p);
    }
    
    private String[] makeArray(final List<NameEntry> list, final Project p) {
        if (list.size() == 0) {
            return null;
        }
        final ArrayList<String> tmpNames = new ArrayList<String>();
        for (final NameEntry ne : list) {
            final String pattern = ne.evalName(p);
            if (pattern != null && pattern.length() > 0) {
                tmpNames.add(pattern);
            }
        }
        return tmpNames.toArray(new String[tmpNames.size()]);
    }
    
    private void readFiles(final Project p) {
        if (this.includesFileList.size() > 0) {
            for (final NameEntry ne : this.includesFileList) {
                final String fileName = ne.evalName(p);
                if (fileName != null) {
                    final File inclFile = p.resolveFile(fileName);
                    if (!inclFile.exists()) {
                        throw new BuildException("Includesfile " + inclFile.getAbsolutePath() + " not found.");
                    }
                    this.readPatterns(inclFile, this.includeList, p);
                }
            }
            this.includesFileList.clear();
        }
        if (this.excludesFileList.size() > 0) {
            for (final NameEntry ne : this.excludesFileList) {
                final String fileName = ne.evalName(p);
                if (fileName != null) {
                    final File exclFile = p.resolveFile(fileName);
                    if (!exclFile.exists()) {
                        throw new BuildException("Excludesfile " + exclFile.getAbsolutePath() + " not found.");
                    }
                    this.readPatterns(exclFile, this.excludeList, p);
                }
            }
            this.excludesFileList.clear();
        }
    }
    
    public String toString() {
        return "patternSet{ includes: " + this.includeList + " excludes: " + this.excludeList + " }";
    }
    
    public Object clone() {
        try {
            final PatternSet ps = (PatternSet)super.clone();
            ps.includeList = new ArrayList<NameEntry>(this.includeList);
            ps.excludeList = new ArrayList<NameEntry>(this.excludeList);
            ps.includesFileList = new ArrayList<NameEntry>(this.includesFileList);
            ps.excludesFileList = new ArrayList<NameEntry>(this.excludesFileList);
            return ps;
        }
        catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }
    
    public void addConfiguredInvert(final PatternSet p) {
        this.addConfiguredPatternset(new InvertedPatternSet(p));
    }
    
    public class NameEntry
    {
        private String name;
        private Object ifCond;
        private Object unlessCond;
        
        public void setName(final String name) {
            this.name = name;
        }
        
        public void setIf(final Object cond) {
            this.ifCond = cond;
        }
        
        public void setIf(final String cond) {
            this.setIf((Object)cond);
        }
        
        public void setUnless(final Object cond) {
            this.unlessCond = cond;
        }
        
        public void setUnless(final String cond) {
            this.setUnless((Object)cond);
        }
        
        public String getName() {
            return this.name;
        }
        
        public String evalName(final Project p) {
            return this.valid(p) ? this.name : null;
        }
        
        private boolean valid(final Project p) {
            final PropertyHelper ph = PropertyHelper.getPropertyHelper(p);
            return ph.testIfCondition(this.ifCond) && ph.testUnlessCondition(this.unlessCond);
        }
        
        public String toString() {
            final StringBuffer buf = new StringBuffer();
            if (this.name == null) {
                buf.append("noname");
            }
            else {
                buf.append(this.name);
            }
            if (this.ifCond != null || this.unlessCond != null) {
                buf.append(":");
                String connector = "";
                if (this.ifCond != null) {
                    buf.append("if->");
                    buf.append(this.ifCond);
                    connector = ";";
                }
                if (this.unlessCond != null) {
                    buf.append(connector);
                    buf.append("unless->");
                    buf.append(this.unlessCond);
                }
            }
            return buf.toString();
        }
    }
    
    private static final class InvertedPatternSet extends PatternSet
    {
        private InvertedPatternSet(final PatternSet p) {
            super();
            this.setProject(p.getProject());
            this.addConfiguredPatternset(p);
        }
        
        public String[] getIncludePatterns(final Project p) {
            return super.getExcludePatterns(p);
        }
        
        public String[] getExcludePatterns(final Project p) {
            return super.getIncludePatterns(p);
        }
    }
}
