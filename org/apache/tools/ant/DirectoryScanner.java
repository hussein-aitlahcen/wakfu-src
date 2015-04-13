package org.apache.tools.ant;

import org.apache.tools.ant.types.selectors.*;
import java.io.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.util.*;
import java.util.*;
import org.apache.tools.ant.taskdefs.condition.*;

public class DirectoryScanner implements FileScanner, SelectorScanner, ResourceFactory
{
    private static final boolean ON_VMS;
    protected static final String[] DEFAULTEXCLUDES;
    public static final int MAX_LEVELS_OF_SYMLINKS = 5;
    public static final String DOES_NOT_EXIST_POSTFIX = " does not exist.";
    private static final FileUtils FILE_UTILS;
    private static final SymbolicLinkUtils SYMLINK_UTILS;
    private static final Set<String> defaultExcludes;
    protected File basedir;
    protected String[] includes;
    protected String[] excludes;
    protected FileSelector[] selectors;
    protected Vector<String> filesIncluded;
    protected Vector<String> filesNotIncluded;
    protected Vector<String> filesExcluded;
    protected Vector<String> dirsIncluded;
    protected Vector<String> dirsNotIncluded;
    protected Vector<String> dirsExcluded;
    protected Vector<String> filesDeselected;
    protected Vector<String> dirsDeselected;
    protected boolean haveSlowResults;
    protected boolean isCaseSensitive;
    protected boolean errorOnMissingDir;
    private boolean followSymlinks;
    protected boolean everythingIncluded;
    private Set<String> scannedDirs;
    private Map<String, TokenizedPath> includeNonPatterns;
    private Map<String, TokenizedPath> excludeNonPatterns;
    private TokenizedPattern[] includePatterns;
    private TokenizedPattern[] excludePatterns;
    private boolean areNonPatternSetsReady;
    private boolean scanning;
    private Object scanLock;
    private boolean slowScanning;
    private Object slowScanLock;
    private IllegalStateException illegal;
    private int maxLevelsOfSymlinks;
    private Set<String> notFollowedSymlinks;
    
    public DirectoryScanner() {
        super();
        this.selectors = null;
        this.haveSlowResults = false;
        this.isCaseSensitive = true;
        this.errorOnMissingDir = true;
        this.followSymlinks = true;
        this.everythingIncluded = true;
        this.scannedDirs = new HashSet<String>();
        this.includeNonPatterns = new HashMap<String, TokenizedPath>();
        this.excludeNonPatterns = new HashMap<String, TokenizedPath>();
        this.areNonPatternSetsReady = false;
        this.scanning = false;
        this.scanLock = new Object();
        this.slowScanning = false;
        this.slowScanLock = new Object();
        this.illegal = null;
        this.maxLevelsOfSymlinks = 5;
        this.notFollowedSymlinks = new HashSet<String>();
    }
    
    protected static boolean matchPatternStart(final String pattern, final String str) {
        return SelectorUtils.matchPatternStart(pattern, str);
    }
    
    protected static boolean matchPatternStart(final String pattern, final String str, final boolean isCaseSensitive) {
        return SelectorUtils.matchPatternStart(pattern, str, isCaseSensitive);
    }
    
    protected static boolean matchPath(final String pattern, final String str) {
        return SelectorUtils.matchPath(pattern, str);
    }
    
    protected static boolean matchPath(final String pattern, final String str, final boolean isCaseSensitive) {
        return SelectorUtils.matchPath(pattern, str, isCaseSensitive);
    }
    
    public static boolean match(final String pattern, final String str) {
        return SelectorUtils.match(pattern, str);
    }
    
    protected static boolean match(final String pattern, final String str, final boolean isCaseSensitive) {
        return SelectorUtils.match(pattern, str, isCaseSensitive);
    }
    
    public static String[] getDefaultExcludes() {
        synchronized (DirectoryScanner.defaultExcludes) {
            return DirectoryScanner.defaultExcludes.toArray(new String[DirectoryScanner.defaultExcludes.size()]);
        }
    }
    
    public static boolean addDefaultExclude(final String s) {
        synchronized (DirectoryScanner.defaultExcludes) {
            return DirectoryScanner.defaultExcludes.add(s);
        }
    }
    
    public static boolean removeDefaultExclude(final String s) {
        synchronized (DirectoryScanner.defaultExcludes) {
            return DirectoryScanner.defaultExcludes.remove(s);
        }
    }
    
    public static void resetDefaultExcludes() {
        synchronized (DirectoryScanner.defaultExcludes) {
            DirectoryScanner.defaultExcludes.clear();
            for (int i = 0; i < DirectoryScanner.DEFAULTEXCLUDES.length; ++i) {
                DirectoryScanner.defaultExcludes.add(DirectoryScanner.DEFAULTEXCLUDES[i]);
            }
        }
    }
    
    public void setBasedir(final String basedir) {
        this.setBasedir((basedir == null) ? ((File)null) : new File(basedir.replace('/', File.separatorChar).replace('\\', File.separatorChar)));
    }
    
    public synchronized void setBasedir(final File basedir) {
        this.basedir = basedir;
    }
    
    public synchronized File getBasedir() {
        return this.basedir;
    }
    
    public synchronized boolean isCaseSensitive() {
        return this.isCaseSensitive;
    }
    
    public synchronized void setCaseSensitive(final boolean isCaseSensitive) {
        this.isCaseSensitive = isCaseSensitive;
    }
    
    public void setErrorOnMissingDir(final boolean errorOnMissingDir) {
        this.errorOnMissingDir = errorOnMissingDir;
    }
    
    public synchronized boolean isFollowSymlinks() {
        return this.followSymlinks;
    }
    
    public synchronized void setFollowSymlinks(final boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }
    
    public void setMaxLevelsOfSymlinks(final int max) {
        this.maxLevelsOfSymlinks = max;
    }
    
    public synchronized void setIncludes(final String[] includes) {
        if (includes == null) {
            this.includes = null;
        }
        else {
            this.includes = new String[includes.length];
            for (int i = 0; i < includes.length; ++i) {
                this.includes[i] = normalizePattern(includes[i]);
            }
        }
    }
    
    public synchronized void setExcludes(final String[] excludes) {
        if (excludes == null) {
            this.excludes = null;
        }
        else {
            this.excludes = new String[excludes.length];
            for (int i = 0; i < excludes.length; ++i) {
                this.excludes[i] = normalizePattern(excludes[i]);
            }
        }
    }
    
    public synchronized void addExcludes(final String[] excludes) {
        if (excludes != null && excludes.length > 0) {
            if (this.excludes != null && this.excludes.length > 0) {
                final String[] tmp = new String[excludes.length + this.excludes.length];
                System.arraycopy(this.excludes, 0, tmp, 0, this.excludes.length);
                for (int i = 0; i < excludes.length; ++i) {
                    tmp[this.excludes.length + i] = normalizePattern(excludes[i]);
                }
                this.excludes = tmp;
            }
            else {
                this.setExcludes(excludes);
            }
        }
    }
    
    private static String normalizePattern(final String p) {
        String pattern = p.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        if (pattern.endsWith(File.separator)) {
            pattern += "**";
        }
        return pattern;
    }
    
    public synchronized void setSelectors(final FileSelector[] selectors) {
        this.selectors = selectors;
    }
    
    public synchronized boolean isEverythingIncluded() {
        return this.everythingIncluded;
    }
    
    public void scan() throws IllegalStateException {
        synchronized (this.scanLock) {
            if (this.scanning) {
                while (this.scanning) {
                    try {
                        this.scanLock.wait();
                    }
                    catch (InterruptedException e) {}
                }
                if (this.illegal != null) {
                    throw this.illegal;
                }
                return;
            }
            else {
                this.scanning = true;
            }
        }
        final File savedBase = this.basedir;
        try {
            synchronized (this) {
                this.illegal = null;
                this.clearResults();
                final boolean nullIncludes = this.includes == null;
                final String[] includes;
                if (nullIncludes) {
                    includes = new String[] { "**" };
                }
                else {
                    final String[] includes2 = this.includes;
                }
                this.includes = includes;
                final boolean nullExcludes = this.excludes == null;
                this.excludes = (nullExcludes ? new String[0] : this.excludes);
                if (this.basedir != null && !this.followSymlinks && DirectoryScanner.SYMLINK_UTILS.isSymbolicLink(this.basedir)) {
                    this.notFollowedSymlinks.add(this.basedir.getAbsolutePath());
                    this.basedir = null;
                }
                if (this.basedir == null) {
                    if (nullIncludes) {
                        return;
                    }
                }
                else {
                    if (!this.basedir.exists()) {
                        if (!this.errorOnMissingDir) {
                            return;
                        }
                        this.illegal = new IllegalStateException("basedir " + this.basedir + " does not exist.");
                    }
                    else if (!this.basedir.isDirectory()) {
                        this.illegal = new IllegalStateException("basedir " + this.basedir + " is not a" + " directory.");
                    }
                    if (this.illegal != null) {
                        throw this.illegal;
                    }
                }
                if (this.isIncluded(TokenizedPath.EMPTY_PATH)) {
                    if (!this.isExcluded(TokenizedPath.EMPTY_PATH)) {
                        if (this.isSelected("", this.basedir)) {
                            this.dirsIncluded.addElement("");
                        }
                        else {
                            this.dirsDeselected.addElement("");
                        }
                    }
                    else {
                        this.dirsExcluded.addElement("");
                    }
                }
                else {
                    this.dirsNotIncluded.addElement("");
                }
                this.checkIncludePatterns();
                this.clearCaches();
                this.includes = (String[])(nullIncludes ? null : this.includes);
                this.excludes = (String[])(nullExcludes ? null : this.excludes);
            }
        }
        catch (IOException ex) {
            throw new BuildException(ex);
        }
        finally {
            this.basedir = savedBase;
            synchronized (this.scanLock) {
                this.scanning = false;
                this.scanLock.notifyAll();
            }
        }
    }
    
    private void checkIncludePatterns() {
        this.ensureNonPatternSetsReady();
        final Map<TokenizedPath, String> newroots = new HashMap<TokenizedPath, String>();
        for (int i = 0; i < this.includePatterns.length; ++i) {
            final String pattern = this.includePatterns[i].toString();
            if (!this.shouldSkipPattern(pattern)) {
                newroots.put(this.includePatterns[i].rtrimWildcardTokens(), pattern);
            }
        }
        for (final Map.Entry<String, TokenizedPath> entry : this.includeNonPatterns.entrySet()) {
            final String pattern2 = entry.getKey();
            if (!this.shouldSkipPattern(pattern2)) {
                newroots.put(entry.getValue(), pattern2);
            }
        }
        if (newroots.containsKey(TokenizedPath.EMPTY_PATH) && this.basedir != null) {
            this.scandir(this.basedir, "", true);
        }
        else {
            File canonBase = null;
            if (this.basedir != null) {
                try {
                    canonBase = this.basedir.getCanonicalFile();
                }
                catch (IOException ex) {
                    throw new BuildException(ex);
                }
            }
            for (final Map.Entry<TokenizedPath, String> entry2 : newroots.entrySet()) {
                TokenizedPath currentPath = entry2.getKey();
                String currentelement = currentPath.toString();
                if (this.basedir == null && !FileUtils.isAbsolutePath(currentelement)) {
                    continue;
                }
                File myfile = new File(this.basedir, currentelement);
                if (myfile.exists()) {
                    try {
                        final String path = (this.basedir == null) ? myfile.getCanonicalPath() : DirectoryScanner.FILE_UTILS.removeLeadingPath(canonBase, myfile.getCanonicalFile());
                        if (!path.equals(currentelement) || DirectoryScanner.ON_VMS) {
                            myfile = currentPath.findFile(this.basedir, true);
                            if (myfile != null && this.basedir != null) {
                                currentelement = DirectoryScanner.FILE_UTILS.removeLeadingPath(this.basedir, myfile);
                                if (!currentPath.toString().equals(currentelement)) {
                                    currentPath = new TokenizedPath(currentelement);
                                }
                            }
                        }
                    }
                    catch (IOException ex2) {
                        throw new BuildException(ex2);
                    }
                }
                if ((myfile == null || !myfile.exists()) && !this.isCaseSensitive()) {
                    final File f = currentPath.findFile(this.basedir, false);
                    if (f != null && f.exists()) {
                        currentelement = ((this.basedir == null) ? f.getAbsolutePath() : DirectoryScanner.FILE_UTILS.removeLeadingPath(this.basedir, f));
                        myfile = f;
                        currentPath = new TokenizedPath(currentelement);
                    }
                }
                if (myfile == null || !myfile.exists()) {
                    continue;
                }
                if (!this.followSymlinks && currentPath.isSymlink(this.basedir)) {
                    if (this.isExcluded(currentPath)) {
                        continue;
                    }
                    this.notFollowedSymlinks.add(myfile.getAbsolutePath());
                }
                else if (myfile.isDirectory()) {
                    if (this.isIncluded(currentPath) && currentelement.length() > 0) {
                        this.accountForIncludedDir(currentPath, myfile, true);
                    }
                    else {
                        this.scandir(myfile, currentPath, true);
                    }
                }
                else {
                    final String originalpattern = entry2.getValue();
                    final boolean included = this.isCaseSensitive() ? originalpattern.equals(currentelement) : originalpattern.equalsIgnoreCase(currentelement);
                    if (!included) {
                        continue;
                    }
                    this.accountForIncludedFile(currentPath, myfile);
                }
            }
        }
    }
    
    private boolean shouldSkipPattern(final String pattern) {
        if (FileUtils.isAbsolutePath(pattern)) {
            if (this.basedir != null && !SelectorUtils.matchPatternStart(pattern, this.basedir.getAbsolutePath(), this.isCaseSensitive())) {
                return true;
            }
        }
        else if (this.basedir == null) {
            return true;
        }
        return false;
    }
    
    protected synchronized void clearResults() {
        this.filesIncluded = new VectorSet<String>();
        this.filesNotIncluded = new VectorSet<String>();
        this.filesExcluded = new VectorSet<String>();
        this.filesDeselected = new VectorSet<String>();
        this.dirsIncluded = new VectorSet<String>();
        this.dirsNotIncluded = new VectorSet<String>();
        this.dirsExcluded = new VectorSet<String>();
        this.dirsDeselected = new VectorSet<String>();
        this.everythingIncluded = (this.basedir != null);
        this.scannedDirs.clear();
        this.notFollowedSymlinks.clear();
    }
    
    protected void slowScan() {
        synchronized (this.slowScanLock) {
            if (this.haveSlowResults) {
                return;
            }
            if (this.slowScanning) {
                while (this.slowScanning) {
                    try {
                        this.slowScanLock.wait();
                    }
                    catch (InterruptedException e) {}
                }
                return;
            }
            this.slowScanning = true;
        }
        try {
            synchronized (this) {
                final boolean nullIncludes = this.includes == null;
                final String[] includes;
                if (nullIncludes) {
                    includes = new String[] { "**" };
                }
                else {
                    final String[] includes2 = this.includes;
                }
                this.includes = includes;
                final boolean nullExcludes = this.excludes == null;
                this.excludes = (nullExcludes ? new String[0] : this.excludes);
                final String[] excl = new String[this.dirsExcluded.size()];
                this.dirsExcluded.copyInto(excl);
                final String[] notIncl = new String[this.dirsNotIncluded.size()];
                this.dirsNotIncluded.copyInto(notIncl);
                this.ensureNonPatternSetsReady();
                this.processSlowScan(excl);
                this.processSlowScan(notIncl);
                this.clearCaches();
                this.includes = (String[])(nullIncludes ? null : this.includes);
                this.excludes = (String[])(nullExcludes ? null : this.excludes);
            }
        }
        finally {
            synchronized (this.slowScanLock) {
                this.haveSlowResults = true;
                this.slowScanning = false;
                this.slowScanLock.notifyAll();
            }
        }
    }
    
    private void processSlowScan(final String[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            final TokenizedPath path = new TokenizedPath(arr[i]);
            if (!this.couldHoldIncluded(path) || this.contentsExcluded(path)) {
                this.scandir(new File(this.basedir, arr[i]), path, false);
            }
        }
    }
    
    protected void scandir(final File dir, final String vpath, final boolean fast) {
        this.scandir(dir, new TokenizedPath(vpath), fast);
    }
    
    private void scandir(final File dir, final TokenizedPath path, final boolean fast) {
        if (dir == null) {
            throw new BuildException("dir must not be null.");
        }
        final String[] newfiles = dir.list();
        if (newfiles != null) {
            this.scandir(dir, path, fast, newfiles, new LinkedList<String>());
            return;
        }
        if (!dir.exists()) {
            throw new BuildException(dir + " does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new BuildException(dir + " is not a directory.");
        }
        throw new BuildException("IO error scanning directory '" + dir.getAbsolutePath() + "'");
    }
    
    private void scandir(final File dir, final TokenizedPath path, final boolean fast, String[] newfiles, final LinkedList<String> directoryNamesFollowed) {
        String vpath = path.toString();
        if (vpath.length() > 0 && !vpath.endsWith(File.separator)) {
            vpath += File.separator;
        }
        if (fast && this.hasBeenScanned(vpath)) {
            return;
        }
        if (!this.followSymlinks) {
            final ArrayList<String> noLinks = new ArrayList<String>();
            for (int i = 0; i < newfiles.length; ++i) {
                try {
                    if (DirectoryScanner.SYMLINK_UTILS.isSymbolicLink(dir, newfiles[i])) {
                        final String name = vpath + newfiles[i];
                        final File file = new File(dir, newfiles[i]);
                        (file.isDirectory() ? this.dirsExcluded : this.filesExcluded).addElement(name);
                        if (!this.isExcluded(name)) {
                            this.notFollowedSymlinks.add(file.getAbsolutePath());
                        }
                    }
                    else {
                        noLinks.add(newfiles[i]);
                    }
                }
                catch (IOException ioe) {
                    final String msg = "IOException caught while checking for links, couldn't get canonical path!";
                    System.err.println(msg);
                    noLinks.add(newfiles[i]);
                }
            }
            newfiles = noLinks.toArray(new String[noLinks.size()]);
        }
        else {
            directoryNamesFollowed.addFirst(dir.getName());
        }
        for (int j = 0; j < newfiles.length; ++j) {
            final String name2 = vpath + newfiles[j];
            final TokenizedPath newPath = new TokenizedPath(path, newfiles[j]);
            final File file = new File(dir, newfiles[j]);
            final String[] children = file.list();
            if (children == null || (children.length == 0 && file.isFile())) {
                if (this.isIncluded(newPath)) {
                    this.accountForIncludedFile(newPath, file);
                }
                else {
                    this.everythingIncluded = false;
                    this.filesNotIncluded.addElement(name2);
                }
            }
            else if (this.followSymlinks && this.causesIllegalSymlinkLoop(newfiles[j], dir, directoryNamesFollowed)) {
                System.err.println("skipping symbolic link " + file.getAbsolutePath() + " -- too many levels of symbolic" + " links.");
                this.notFollowedSymlinks.add(file.getAbsolutePath());
            }
            else {
                if (this.isIncluded(newPath)) {
                    this.accountForIncludedDir(newPath, file, fast, children, directoryNamesFollowed);
                }
                else {
                    this.everythingIncluded = false;
                    this.dirsNotIncluded.addElement(name2);
                    if (fast && this.couldHoldIncluded(newPath) && !this.contentsExcluded(newPath)) {
                        this.scandir(file, newPath, fast, children, directoryNamesFollowed);
                    }
                }
                if (!fast) {
                    this.scandir(file, newPath, fast, children, directoryNamesFollowed);
                }
            }
        }
        if (this.followSymlinks) {
            directoryNamesFollowed.removeFirst();
        }
    }
    
    private void accountForIncludedFile(final TokenizedPath name, final File file) {
        this.processIncluded(name, file, this.filesIncluded, this.filesExcluded, this.filesDeselected);
    }
    
    private void accountForIncludedDir(final TokenizedPath name, final File file, final boolean fast) {
        this.processIncluded(name, file, this.dirsIncluded, this.dirsExcluded, this.dirsDeselected);
        if (fast && this.couldHoldIncluded(name) && !this.contentsExcluded(name)) {
            this.scandir(file, name, fast);
        }
    }
    
    private void accountForIncludedDir(final TokenizedPath name, final File file, final boolean fast, final String[] children, final LinkedList<String> directoryNamesFollowed) {
        this.processIncluded(name, file, this.dirsIncluded, this.dirsExcluded, this.dirsDeselected);
        if (fast && this.couldHoldIncluded(name) && !this.contentsExcluded(name)) {
            this.scandir(file, name, fast, children, directoryNamesFollowed);
        }
    }
    
    private void processIncluded(final TokenizedPath path, final File file, final Vector<String> inc, final Vector<String> exc, final Vector<String> des) {
        final String name = path.toString();
        if (inc.contains(name) || exc.contains(name) || des.contains(name)) {
            return;
        }
        boolean included = false;
        if (this.isExcluded(path)) {
            exc.add(name);
        }
        else if (this.isSelected(name, file)) {
            included = true;
            inc.add(name);
        }
        else {
            des.add(name);
        }
        this.everythingIncluded &= included;
    }
    
    protected boolean isIncluded(final String name) {
        return this.isIncluded(new TokenizedPath(name));
    }
    
    private boolean isIncluded(final TokenizedPath path) {
        this.ensureNonPatternSetsReady();
        Label_0051: {
            if (this.isCaseSensitive()) {
                if (!this.includeNonPatterns.containsKey(path.toString())) {
                    break Label_0051;
                }
            }
            else if (!this.includeNonPatterns.containsKey(path.toString().toUpperCase())) {
                break Label_0051;
            }
            return true;
        }
        for (int i = 0; i < this.includePatterns.length; ++i) {
            if (this.includePatterns[i].matchPath(path, this.isCaseSensitive())) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean couldHoldIncluded(final String name) {
        return this.couldHoldIncluded(new TokenizedPath(name));
    }
    
    private boolean couldHoldIncluded(final TokenizedPath tokenizedName) {
        for (int i = 0; i < this.includePatterns.length; ++i) {
            if (this.couldHoldIncluded(tokenizedName, this.includePatterns[i])) {
                return true;
            }
        }
        final Iterator<TokenizedPath> iter = this.includeNonPatterns.values().iterator();
        while (iter.hasNext()) {
            if (this.couldHoldIncluded(tokenizedName, iter.next().toPattern())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean couldHoldIncluded(final TokenizedPath tokenizedName, final TokenizedPattern tokenizedInclude) {
        return tokenizedInclude.matchStartOf(tokenizedName, this.isCaseSensitive()) && this.isMorePowerfulThanExcludes(tokenizedName.toString()) && this.isDeeper(tokenizedInclude, tokenizedName);
    }
    
    private boolean isDeeper(final TokenizedPattern pattern, final TokenizedPath name) {
        return pattern.containsPattern("**") || pattern.depth() > name.depth();
    }
    
    private boolean isMorePowerfulThanExcludes(final String name) {
        final String soughtexclude = name + File.separatorChar + "**";
        for (int counter = 0; counter < this.excludePatterns.length; ++counter) {
            if (this.excludePatterns[counter].toString().equals(soughtexclude)) {
                return false;
            }
        }
        return true;
    }
    
    boolean contentsExcluded(final TokenizedPath path) {
        for (int i = 0; i < this.excludePatterns.length; ++i) {
            if (this.excludePatterns[i].endsWith("**") && this.excludePatterns[i].withoutLastToken().matchPath(path, this.isCaseSensitive())) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isExcluded(final String name) {
        return this.isExcluded(new TokenizedPath(name));
    }
    
    private boolean isExcluded(final TokenizedPath name) {
        this.ensureNonPatternSetsReady();
        Label_0051: {
            if (this.isCaseSensitive()) {
                if (!this.excludeNonPatterns.containsKey(name.toString())) {
                    break Label_0051;
                }
            }
            else if (!this.excludeNonPatterns.containsKey(name.toString().toUpperCase())) {
                break Label_0051;
            }
            return true;
        }
        for (int i = 0; i < this.excludePatterns.length; ++i) {
            if (this.excludePatterns[i].matchPath(name, this.isCaseSensitive())) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isSelected(final String name, final File file) {
        if (this.selectors != null) {
            for (int i = 0; i < this.selectors.length; ++i) {
                if (!this.selectors[i].isSelected(this.basedir, name, file)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public String[] getIncludedFiles() {
        final String[] files;
        synchronized (this) {
            if (this.filesIncluded == null) {
                throw new IllegalStateException("Must call scan() first");
            }
            files = new String[this.filesIncluded.size()];
            this.filesIncluded.copyInto(files);
        }
        Arrays.sort(files);
        return files;
    }
    
    public synchronized int getIncludedFilesCount() {
        if (this.filesIncluded == null) {
            throw new IllegalStateException("Must call scan() first");
        }
        return this.filesIncluded.size();
    }
    
    public synchronized String[] getNotIncludedFiles() {
        this.slowScan();
        final String[] files = new String[this.filesNotIncluded.size()];
        this.filesNotIncluded.copyInto(files);
        return files;
    }
    
    public synchronized String[] getExcludedFiles() {
        this.slowScan();
        final String[] files = new String[this.filesExcluded.size()];
        this.filesExcluded.copyInto(files);
        return files;
    }
    
    public synchronized String[] getDeselectedFiles() {
        this.slowScan();
        final String[] files = new String[this.filesDeselected.size()];
        this.filesDeselected.copyInto(files);
        return files;
    }
    
    public String[] getIncludedDirectories() {
        final String[] directories;
        synchronized (this) {
            if (this.dirsIncluded == null) {
                throw new IllegalStateException("Must call scan() first");
            }
            directories = new String[this.dirsIncluded.size()];
            this.dirsIncluded.copyInto(directories);
        }
        Arrays.sort(directories);
        return directories;
    }
    
    public synchronized int getIncludedDirsCount() {
        if (this.dirsIncluded == null) {
            throw new IllegalStateException("Must call scan() first");
        }
        return this.dirsIncluded.size();
    }
    
    public synchronized String[] getNotIncludedDirectories() {
        this.slowScan();
        final String[] directories = new String[this.dirsNotIncluded.size()];
        this.dirsNotIncluded.copyInto(directories);
        return directories;
    }
    
    public synchronized String[] getExcludedDirectories() {
        this.slowScan();
        final String[] directories = new String[this.dirsExcluded.size()];
        this.dirsExcluded.copyInto(directories);
        return directories;
    }
    
    public synchronized String[] getDeselectedDirectories() {
        this.slowScan();
        final String[] directories = new String[this.dirsDeselected.size()];
        this.dirsDeselected.copyInto(directories);
        return directories;
    }
    
    public synchronized String[] getNotFollowedSymlinks() {
        final String[] links;
        synchronized (this) {
            links = this.notFollowedSymlinks.toArray(new String[this.notFollowedSymlinks.size()]);
        }
        Arrays.sort(links);
        return links;
    }
    
    public synchronized void addDefaultExcludes() {
        final int excludesLength = (this.excludes == null) ? 0 : this.excludes.length;
        final String[] defaultExcludesTemp = getDefaultExcludes();
        final String[] newExcludes = new String[excludesLength + defaultExcludesTemp.length];
        if (excludesLength > 0) {
            System.arraycopy(this.excludes, 0, newExcludes, 0, excludesLength);
        }
        for (int i = 0; i < defaultExcludesTemp.length; ++i) {
            newExcludes[i + excludesLength] = defaultExcludesTemp[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
        }
        this.excludes = newExcludes;
    }
    
    public synchronized Resource getResource(final String name) {
        return new FileResource(this.basedir, name);
    }
    
    private boolean hasBeenScanned(final String vpath) {
        return !this.scannedDirs.add(vpath);
    }
    
    Set<String> getScannedDirs() {
        return this.scannedDirs;
    }
    
    private synchronized void clearCaches() {
        this.includeNonPatterns.clear();
        this.excludeNonPatterns.clear();
        this.includePatterns = null;
        this.excludePatterns = null;
        this.areNonPatternSetsReady = false;
    }
    
    synchronized void ensureNonPatternSetsReady() {
        if (!this.areNonPatternSetsReady) {
            this.includePatterns = this.fillNonPatternSet(this.includeNonPatterns, this.includes);
            this.excludePatterns = this.fillNonPatternSet(this.excludeNonPatterns, this.excludes);
            this.areNonPatternSetsReady = true;
        }
    }
    
    private TokenizedPattern[] fillNonPatternSet(final Map<String, TokenizedPath> map, final String[] patterns) {
        final ArrayList<TokenizedPattern> al = new ArrayList<TokenizedPattern>(patterns.length);
        for (int i = 0; i < patterns.length; ++i) {
            if (!SelectorUtils.hasWildcards(patterns[i])) {
                final String s = this.isCaseSensitive() ? patterns[i] : patterns[i].toUpperCase();
                map.put(s, new TokenizedPath(s));
            }
            else {
                al.add(new TokenizedPattern(patterns[i]));
            }
        }
        return al.toArray(new TokenizedPattern[al.size()]);
    }
    
    private boolean causesIllegalSymlinkLoop(final String dirName, final File parent, final LinkedList<String> directoryNamesFollowed) {
        try {
            if (directoryNamesFollowed.size() >= this.maxLevelsOfSymlinks && CollectionUtils.frequency(directoryNamesFollowed, dirName) >= this.maxLevelsOfSymlinks && DirectoryScanner.SYMLINK_UTILS.isSymbolicLink(parent, dirName)) {
                final ArrayList<String> files = new ArrayList<String>();
                File f = DirectoryScanner.FILE_UTILS.resolveFile(parent, dirName);
                final String target = f.getCanonicalPath();
                files.add(target);
                String relPath = "";
                for (final String dir : directoryNamesFollowed) {
                    relPath += "../";
                    if (dirName.equals(dir)) {
                        f = DirectoryScanner.FILE_UTILS.resolveFile(parent, relPath + dir);
                        files.add(f.getCanonicalPath());
                        if (files.size() > this.maxLevelsOfSymlinks && CollectionUtils.frequency(files, target) > this.maxLevelsOfSymlinks) {
                            return true;
                        }
                        continue;
                    }
                }
            }
            return false;
        }
        catch (IOException ex) {
            throw new BuildException("Caught error while checking for symbolic links", ex);
        }
    }
    
    static {
        ON_VMS = Os.isFamily("openvms");
        DEFAULTEXCLUDES = new String[] { "**/*~", "**/#*#", "**/.#*", "**/%*%", "**/._*", "**/CVS", "**/CVS/**", "**/.cvsignore", "**/SCCS", "**/SCCS/**", "**/vssver.scc", "**/.svn", "**/.svn/**", "**/.git", "**/.git/**", "**/.gitattributes", "**/.gitignore", "**/.gitmodules", "**/.hg", "**/.hg/**", "**/.hgignore", "**/.hgsub", "**/.hgsubstate", "**/.hgtags", "**/.bzr", "**/.bzr/**", "**/.bzrignore", "**/.DS_Store" };
        FILE_UTILS = FileUtils.getFileUtils();
        SYMLINK_UTILS = SymbolicLinkUtils.getSymbolicLinkUtils();
        defaultExcludes = new HashSet<String>();
        resetDefaultExcludes();
    }
}
