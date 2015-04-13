package org.apache.tools.ant.types;

import org.apache.tools.ant.*;
import java.io.*;
import java.util.*;
import org.apache.tools.ant.util.*;

public class FilterSet extends DataType implements Cloneable
{
    public static final String DEFAULT_TOKEN_START = "@";
    public static final String DEFAULT_TOKEN_END = "@";
    private String startOfToken;
    private String endOfToken;
    private Vector<String> passedTokens;
    private boolean duplicateToken;
    private boolean recurse;
    private Hashtable<String, String> filterHash;
    private Vector<File> filtersFiles;
    private OnMissing onMissingFiltersFile;
    private boolean readingFiles;
    private int recurseDepth;
    private Vector<Filter> filters;
    
    public FilterSet() {
        super();
        this.startOfToken = "@";
        this.endOfToken = "@";
        this.duplicateToken = false;
        this.recurse = true;
        this.filterHash = null;
        this.filtersFiles = new Vector<File>();
        this.onMissingFiltersFile = OnMissing.FAIL;
        this.readingFiles = false;
        this.recurseDepth = 0;
        this.filters = new Vector<Filter>();
    }
    
    protected FilterSet(final FilterSet filterset) {
        super();
        this.startOfToken = "@";
        this.endOfToken = "@";
        this.duplicateToken = false;
        this.recurse = true;
        this.filterHash = null;
        this.filtersFiles = new Vector<File>();
        this.onMissingFiltersFile = OnMissing.FAIL;
        this.readingFiles = false;
        this.recurseDepth = 0;
        this.filters = new Vector<Filter>();
        final Vector<Filter> clone = (Vector<Filter>)filterset.getFilters().clone();
        this.filters = clone;
    }
    
    protected synchronized Vector<Filter> getFilters() {
        if (this.isReference()) {
            return this.getRef().getFilters();
        }
        this.dieOnCircularReference();
        if (!this.readingFiles) {
            this.readingFiles = true;
            for (int size = this.filtersFiles.size(), i = 0; i < size; ++i) {
                this.readFiltersFromFile(this.filtersFiles.get(i));
            }
            this.filtersFiles.clear();
            this.readingFiles = false;
        }
        return this.filters;
    }
    
    protected FilterSet getRef() {
        return this.getCheckedRef(FilterSet.class, "filterset");
    }
    
    public synchronized Hashtable<String, String> getFilterHash() {
        if (this.isReference()) {
            return this.getRef().getFilterHash();
        }
        this.dieOnCircularReference();
        if (this.filterHash == null) {
            this.filterHash = new Hashtable<String, String>(this.getFilters().size());
            final Enumeration<Filter> e = this.getFilters().elements();
            while (e.hasMoreElements()) {
                final Filter filter = e.nextElement();
                this.filterHash.put(filter.getToken(), filter.getValue());
            }
        }
        return this.filterHash;
    }
    
    public void setFiltersfile(final File filtersFile) throws BuildException {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.filtersFiles.add(filtersFile);
    }
    
    public void setBeginToken(final String startOfToken) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (startOfToken == null || "".equals(startOfToken)) {
            throw new BuildException("beginToken must not be empty");
        }
        this.startOfToken = startOfToken;
    }
    
    public String getBeginToken() {
        if (this.isReference()) {
            return this.getRef().getBeginToken();
        }
        return this.startOfToken;
    }
    
    public void setEndToken(final String endOfToken) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (endOfToken == null || "".equals(endOfToken)) {
            throw new BuildException("endToken must not be empty");
        }
        this.endOfToken = endOfToken;
    }
    
    public String getEndToken() {
        if (this.isReference()) {
            return this.getRef().getEndToken();
        }
        return this.endOfToken;
    }
    
    public void setRecurse(final boolean recurse) {
        this.recurse = recurse;
    }
    
    public boolean isRecurse() {
        return this.recurse;
    }
    
    public synchronized void readFiltersFromFile(final File filtersFile) throws BuildException {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (!filtersFile.exists()) {
            this.handleMissingFile("Could not read filters from file " + filtersFile + " as it doesn't exist.");
        }
        if (filtersFile.isFile()) {
            this.log("Reading filters from " + filtersFile, 3);
            FileInputStream in = null;
            try {
                final Properties props = new Properties();
                in = new FileInputStream(filtersFile);
                props.load(in);
                final Enumeration<?> e = props.propertyNames();
                final Vector<Filter> filts = this.getFilters();
                while (e.hasMoreElements()) {
                    final String strPropName = (String)e.nextElement();
                    final String strValue = props.getProperty(strPropName);
                    filts.addElement(new Filter(strPropName, strValue));
                }
            }
            catch (Exception ex) {
                throw new BuildException("Could not read filters from file: " + filtersFile, ex);
            }
            finally {
                FileUtils.close(in);
            }
        }
        else {
            this.handleMissingFile("Must specify a file rather than a directory in the filtersfile attribute:" + filtersFile);
        }
        this.filterHash = null;
    }
    
    public synchronized String replaceTokens(final String line) {
        return this.iReplaceTokens(line);
    }
    
    public synchronized void addFilter(final Filter filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.filters.addElement(filter);
        this.filterHash = null;
    }
    
    public FiltersFile createFiltersfile() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        return new FiltersFile();
    }
    
    public synchronized void addFilter(final String token, final String value) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.addFilter(new Filter(token, value));
    }
    
    public synchronized void addConfiguredFilterSet(final FilterSet filterSet) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        for (final Filter filter : filterSet.getFilters()) {
            this.addFilter(filter);
        }
    }
    
    public synchronized boolean hasFilters() {
        return this.getFilters().size() > 0;
    }
    
    public synchronized Object clone() throws BuildException {
        if (this.isReference()) {
            return this.getRef().clone();
        }
        try {
            final FilterSet fs = (FilterSet)super.clone();
            final Vector<Filter> clonedFilters = (Vector<Filter>)this.getFilters().clone();
            fs.filters = clonedFilters;
            fs.setProject(this.getProject());
            return fs;
        }
        catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }
    
    public void setOnMissingFiltersFile(final OnMissing onMissingFiltersFile) {
        this.onMissingFiltersFile = onMissingFiltersFile;
    }
    
    public OnMissing getOnMissingFiltersFile() {
        return this.onMissingFiltersFile;
    }
    
    private synchronized String iReplaceTokens(final String line) {
        final String beginToken = this.getBeginToken();
        final String endToken = this.getEndToken();
        int index = line.indexOf(beginToken);
        if (index > -1) {
            final Hashtable<String, String> tokens = this.getFilterHash();
            try {
                final StringBuilder b = new StringBuilder();
                int i = 0;
                String token = null;
                String value = null;
                while (index > -1) {
                    final int endIndex = line.indexOf(endToken, index + beginToken.length() + 1);
                    if (endIndex == -1) {
                        break;
                    }
                    token = line.substring(index + beginToken.length(), endIndex);
                    b.append(line.substring(i, index));
                    if (tokens.containsKey(token)) {
                        value = tokens.get(token);
                        if (this.recurse && !value.equals(token)) {
                            value = this.replaceTokens(value, token);
                        }
                        this.log("Replacing: " + beginToken + token + endToken + " -> " + value, 3);
                        b.append(value);
                        i = index + beginToken.length() + token.length() + endToken.length();
                    }
                    else {
                        b.append(beginToken.charAt(0));
                        i = index + 1;
                    }
                    index = line.indexOf(beginToken, i);
                }
                b.append(line.substring(i));
                return b.toString();
            }
            catch (StringIndexOutOfBoundsException e) {
                return line;
            }
        }
        return line;
    }
    
    private synchronized String replaceTokens(final String line, final String parent) throws BuildException {
        final String beginToken = this.getBeginToken();
        final String endToken = this.getEndToken();
        if (this.recurseDepth == 0) {
            this.passedTokens = new VectorSet<String>();
        }
        ++this.recurseDepth;
        if (this.passedTokens.contains(parent) && !this.duplicateToken) {
            this.duplicateToken = true;
            System.out.println("Infinite loop in tokens. Currently known tokens : " + this.passedTokens.toString() + "\nProblem token : " + beginToken + parent + endToken + " called from " + beginToken + this.passedTokens.lastElement().toString() + endToken);
            --this.recurseDepth;
            return parent;
        }
        this.passedTokens.addElement(parent);
        String value = this.iReplaceTokens(line);
        if (value.indexOf(beginToken) == -1 && !this.duplicateToken && this.recurseDepth == 1) {
            this.passedTokens = null;
        }
        else if (this.duplicateToken) {
            if (this.passedTokens.size() > 0) {
                value = this.passedTokens.remove(this.passedTokens.size() - 1);
                if (this.passedTokens.size() == 0) {
                    value = beginToken + value + endToken;
                    this.duplicateToken = false;
                }
            }
        }
        else if (this.passedTokens.size() > 0) {
            this.passedTokens.remove(this.passedTokens.size() - 1);
        }
        --this.recurseDepth;
        return value;
    }
    
    private void handleMissingFile(final String message) {
        switch (this.onMissingFiltersFile.getIndex()) {
            case 2: {}
            case 0: {
                throw new BuildException(message);
            }
            case 1: {
                this.log(message, 1);
            }
            default: {
                throw new BuildException("Invalid value for onMissingFiltersFile");
            }
        }
    }
    
    public static class Filter
    {
        String token;
        String value;
        
        public Filter(final String token, final String value) {
            super();
            this.setToken(token);
            this.setValue(value);
        }
        
        public Filter() {
            super();
        }
        
        public void setToken(final String token) {
            this.token = token;
        }
        
        public void setValue(final String value) {
            this.value = value;
        }
        
        public String getToken() {
            return this.token;
        }
        
        public String getValue() {
            return this.value;
        }
    }
    
    public class FiltersFile
    {
        public void setFile(final File file) {
            FilterSet.this.filtersFiles.add(file);
        }
    }
    
    public static class OnMissing extends EnumeratedAttribute
    {
        private static final String[] VALUES;
        public static final OnMissing FAIL;
        public static final OnMissing WARN;
        public static final OnMissing IGNORE;
        private static final int FAIL_INDEX = 0;
        private static final int WARN_INDEX = 1;
        private static final int IGNORE_INDEX = 2;
        
        public OnMissing() {
            super();
        }
        
        public OnMissing(final String value) {
            super();
            this.setValue(value);
        }
        
        public String[] getValues() {
            return OnMissing.VALUES;
        }
        
        static {
            VALUES = new String[] { "fail", "warn", "ignore" };
            FAIL = new OnMissing("fail");
            WARN = new OnMissing("warn");
            IGNORE = new OnMissing("ignore");
        }
    }
}
