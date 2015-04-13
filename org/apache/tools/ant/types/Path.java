package org.apache.tools.ant.types;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.resources.*;
import java.util.*;
import java.lang.reflect.*;

public class Path extends DataType implements Cloneable, ResourceCollection
{
    public static Path systemClasspath;
    public static Path systemBootClasspath;
    private Boolean preserveBC;
    private Union union;
    private boolean cache;
    
    public Path(final Project p, final String path) {
        this(p);
        this.createPathElement().setPath(path);
    }
    
    public Path(final Project project) {
        super();
        this.union = null;
        this.cache = false;
        this.setProject(project);
    }
    
    public void setLocation(final File location) throws BuildException {
        this.checkAttributesAllowed();
        this.createPathElement().setLocation(location);
    }
    
    public void setPath(final String path) throws BuildException {
        this.checkAttributesAllowed();
        this.createPathElement().setPath(path);
    }
    
    public void setRefid(final Reference r) throws BuildException {
        if (this.union != null) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    public PathElement createPathElement() throws BuildException {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        final PathElement pe = new PathElement();
        this.add(pe);
        return pe;
    }
    
    public void addFileset(final FileSet fs) throws BuildException {
        if (fs.getProject() == null) {
            fs.setProject(this.getProject());
        }
        this.add(fs);
    }
    
    public void addFilelist(final FileList fl) throws BuildException {
        if (fl.getProject() == null) {
            fl.setProject(this.getProject());
        }
        this.add(fl);
    }
    
    public void addDirset(final DirSet dset) throws BuildException {
        if (dset.getProject() == null) {
            dset.setProject(this.getProject());
        }
        this.add(dset);
    }
    
    public void add(final Path path) throws BuildException {
        if (path == this) {
            throw this.circularReference();
        }
        if (path.getProject() == null) {
            path.setProject(this.getProject());
        }
        this.add((ResourceCollection)path);
    }
    
    public void add(final ResourceCollection c) {
        this.checkChildrenAllowed();
        if (c == null) {
            return;
        }
        if (this.union == null) {
            (this.union = new Union()).setProject(this.getProject());
            this.union.setCache(this.cache);
        }
        this.union.add(c);
        this.setChecked(false);
    }
    
    public Path createPath() throws BuildException {
        final Path p = new Path(this.getProject());
        this.add(p);
        return p;
    }
    
    public void append(final Path other) {
        if (other == null) {
            return;
        }
        this.add(other);
    }
    
    public void addExisting(final Path source) {
        this.addExisting(source, false);
    }
    
    public void addExisting(final Path source, final boolean tryUserDir) {
        final String[] list = source.list();
        final File userDir = tryUserDir ? new File(System.getProperty("user.dir")) : null;
        for (int i = 0; i < list.length; ++i) {
            File f = resolveFile(this.getProject(), list[i]);
            if (tryUserDir && !f.exists()) {
                f = new File(userDir, list[i]);
            }
            if (f.exists()) {
                this.setLocation(f);
            }
            else if (f.getParentFile() != null && f.getParentFile().exists() && containsWildcards(f.getName())) {
                this.setLocation(f);
                this.log("adding " + f + " which contains wildcards and may not" + " do what you intend it to do depending on your OS or" + " version of Java", 3);
            }
            else {
                this.log("dropping " + f + " from path as it doesn't exist", 3);
            }
        }
    }
    
    public void setCache(final boolean b) {
        this.checkAttributesAllowed();
        this.cache = b;
        if (this.union != null) {
            this.union.setCache(b);
        }
    }
    
    public String[] list() {
        if (this.isReference()) {
            return ((Path)this.getCheckedRef()).list();
        }
        return (this.assertFilesystemOnly(this.union) == null) ? new String[0] : this.union.list();
    }
    
    public String toString() {
        return this.isReference() ? this.getCheckedRef().toString() : ((this.union == null) ? "" : this.union.toString());
    }
    
    public static String[] translatePath(final Project project, final String source) {
        final Vector<String> result = new Vector<String>();
        if (source == null) {
            return new String[0];
        }
        final PathTokenizer tok = new PathTokenizer(source);
        StringBuffer element = new StringBuffer();
        while (tok.hasMoreTokens()) {
            final String pathElement = tok.nextToken();
            try {
                element.append(resolveFile(project, pathElement).getPath());
            }
            catch (BuildException e) {
                project.log("Dropping path element " + pathElement + " as it is not valid relative to the project", 3);
            }
            for (int i = 0; i < element.length(); ++i) {
                translateFileSep(element, i);
            }
            result.addElement(element.toString());
            element = new StringBuffer();
        }
        return result.toArray(new String[result.size()]);
    }
    
    public static String translateFile(final String source) {
        if (source == null) {
            return "";
        }
        final StringBuffer result = new StringBuffer(source);
        for (int i = 0; i < result.length(); ++i) {
            translateFileSep(result, i);
        }
        return result.toString();
    }
    
    protected static boolean translateFileSep(final StringBuffer buffer, final int pos) {
        if (buffer.charAt(pos) == '/' || buffer.charAt(pos) == '\\') {
            buffer.setCharAt(pos, File.separatorChar);
            return true;
        }
        return false;
    }
    
    public synchronized int size() {
        if (this.isReference()) {
            return ((Path)this.getCheckedRef()).size();
        }
        this.dieOnCircularReference();
        return (this.union == null) ? 0 : this.assertFilesystemOnly(this.union).size();
    }
    
    public Object clone() {
        try {
            final Path result = (Path)super.clone();
            result.union = (Union)((this.union == null) ? this.union : this.union.clone());
            return result;
        }
        catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            if (this.union != null) {
                DataType.pushAndInvokeCircularReferenceCheck(this.union, stk, p);
            }
            this.setChecked(true);
        }
    }
    
    private static File resolveFile(final Project project, final String relativeName) {
        return FileUtils.getFileUtils().resolveFile((project == null) ? null : project.getBaseDir(), relativeName);
    }
    
    public Path concatSystemClasspath() {
        return this.concatSystemClasspath("last");
    }
    
    public Path concatSystemClasspath(final String defValue) {
        return this.concatSpecialPath(defValue, Path.systemClasspath);
    }
    
    public Path concatSystemBootClasspath(final String defValue) {
        return this.concatSpecialPath(defValue, Path.systemBootClasspath);
    }
    
    private Path concatSpecialPath(final String defValue, final Path p) {
        final Path result = new Path(this.getProject());
        String order = defValue;
        final String o = (this.getProject() != null) ? this.getProject().getProperty("build.sysclasspath") : System.getProperty("build.sysclasspath");
        if (o != null) {
            order = o;
        }
        if (order.equals("only")) {
            result.addExisting(p, true);
        }
        else if (order.equals("first")) {
            result.addExisting(p, true);
            result.addExisting(this);
        }
        else if (order.equals("ignore")) {
            result.addExisting(this);
        }
        else {
            if (!order.equals("last")) {
                this.log("invalid value for build.sysclasspath: " + order, 1);
            }
            result.addExisting(this);
            result.addExisting(p, true);
        }
        return result;
    }
    
    public void addJavaRuntime() {
        if (JavaEnvUtils.isKaffe()) {
            final File kaffeShare = new File(System.getProperty("java.home") + File.separator + "share" + File.separator + "kaffe");
            if (kaffeShare.isDirectory()) {
                final FileSet kaffeJarFiles = new FileSet();
                kaffeJarFiles.setDir(kaffeShare);
                kaffeJarFiles.setIncludes("*.jar");
                this.addFileset(kaffeJarFiles);
            }
        }
        else if ("GNU libgcj".equals(System.getProperty("java.vm.name"))) {
            this.addExisting(Path.systemBootClasspath);
        }
        if (System.getProperty("java.vendor").toLowerCase(Locale.ENGLISH).indexOf("microsoft") >= 0) {
            final FileSet msZipFiles = new FileSet();
            msZipFiles.setDir(new File(System.getProperty("java.home") + File.separator + "Packages"));
            msZipFiles.setIncludes("*.ZIP");
            this.addFileset(msZipFiles);
        }
        else {
            this.addExisting(new Path(null, System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar"));
            this.addExisting(new Path(null, System.getProperty("java.home") + File.separator + "jre" + File.separator + "lib" + File.separator + "rt.jar"));
            final String[] secJars = { "jce", "jsse" };
            for (int i = 0; i < secJars.length; ++i) {
                this.addExisting(new Path(null, System.getProperty("java.home") + File.separator + "lib" + File.separator + secJars[i] + ".jar"));
                this.addExisting(new Path(null, System.getProperty("java.home") + File.separator + ".." + File.separator + "Classes" + File.separator + secJars[i] + ".jar"));
            }
            final String[] ibmJars = { "core", "graphics", "security", "server", "xml" };
            for (int j = 0; j < ibmJars.length; ++j) {
                this.addExisting(new Path(null, System.getProperty("java.home") + File.separator + "lib" + File.separator + ibmJars[j] + ".jar"));
            }
            this.addExisting(new Path(null, System.getProperty("java.home") + File.separator + ".." + File.separator + "Classes" + File.separator + "classes.jar"));
            this.addExisting(new Path(null, System.getProperty("java.home") + File.separator + ".." + File.separator + "Classes" + File.separator + "ui.jar"));
        }
    }
    
    public void addExtdirs(Path extdirs) {
        if (extdirs == null) {
            final String extProp = System.getProperty("java.ext.dirs");
            if (extProp == null) {
                return;
            }
            extdirs = new Path(this.getProject(), extProp);
        }
        final String[] dirs = extdirs.list();
        for (int i = 0; i < dirs.length; ++i) {
            final File dir = resolveFile(this.getProject(), dirs[i]);
            if (dir.exists() && dir.isDirectory()) {
                final FileSet fs = new FileSet();
                fs.setDir(dir);
                fs.setIncludes("*");
                this.addFileset(fs);
            }
        }
    }
    
    public final synchronized Iterator<Resource> iterator() {
        if (this.isReference()) {
            return ((Path)this.getCheckedRef()).iterator();
        }
        this.dieOnCircularReference();
        if (this.getPreserveBC()) {
            return new FileResourceIterator(this.getProject(), null, this.list());
        }
        return (this.union == null) ? Collections.emptySet().iterator() : this.assertFilesystemOnly(this.union).iterator();
    }
    
    public synchronized boolean isFilesystemOnly() {
        if (this.isReference()) {
            return ((Path)this.getCheckedRef()).isFilesystemOnly();
        }
        this.dieOnCircularReference();
        this.assertFilesystemOnly(this.union);
        return true;
    }
    
    protected ResourceCollection assertFilesystemOnly(final ResourceCollection rc) {
        if (rc != null && !rc.isFilesystemOnly()) {
            throw new BuildException(this.getDataTypeName() + " allows only filesystem resources.");
        }
        return rc;
    }
    
    protected boolean delegateIteratorToList() {
        if (this.getClass().equals(Path.class)) {
            return false;
        }
        try {
            final Method listMethod = this.getClass().getMethod("list", (Class<?>[])null);
            return !listMethod.getDeclaringClass().equals(Path.class);
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private synchronized boolean getPreserveBC() {
        if (this.preserveBC == null) {
            this.preserveBC = (this.delegateIteratorToList() ? Boolean.TRUE : Boolean.FALSE);
        }
        return this.preserveBC;
    }
    
    private static boolean containsWildcards(final String path) {
        return path != null && (path.indexOf("*") > -1 || path.indexOf("?") > -1);
    }
    
    static {
        Path.systemClasspath = new Path(null, System.getProperty("java.class.path"));
        Path.systemBootClasspath = new Path(null, System.getProperty("sun.boot.class.path"));
    }
    
    public class PathElement implements ResourceCollection
    {
        private String[] parts;
        
        public void setLocation(final File loc) {
            this.parts = new String[] { Path.translateFile(loc.getAbsolutePath()) };
        }
        
        public void setPath(final String path) {
            this.parts = Path.translatePath(Path.this.getProject(), path);
        }
        
        public String[] getParts() {
            return this.parts;
        }
        
        public Iterator<Resource> iterator() {
            return new FileResourceIterator(Path.this.getProject(), null, this.parts);
        }
        
        public boolean isFilesystemOnly() {
            return true;
        }
        
        public int size() {
            return (this.parts == null) ? 0 : this.parts.length;
        }
    }
}
