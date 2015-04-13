package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.taskdefs.condition.*;
import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.*;

public class Available extends Task implements Condition
{
    private static final FileUtils FILE_UTILS;
    private String property;
    private String classname;
    private String filename;
    private File file;
    private Path filepath;
    private String resource;
    private FileDir type;
    private Path classpath;
    private AntClassLoader loader;
    private Object value;
    private boolean isTask;
    private boolean ignoreSystemclasses;
    private boolean searchParents;
    
    public Available() {
        super();
        this.value = "true";
        this.isTask = false;
        this.ignoreSystemclasses = false;
        this.searchParents = false;
    }
    
    public void setSearchParents(final boolean searchParents) {
        this.searchParents = searchParents;
    }
    
    public void setClasspath(final Path classpath) {
        this.createClasspath().append(classpath);
    }
    
    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(this.getProject());
        }
        return this.classpath.createPath();
    }
    
    public void setClasspathRef(final Reference r) {
        this.createClasspath().setRefid(r);
    }
    
    public void setFilepath(final Path filepath) {
        this.createFilepath().append(filepath);
    }
    
    public Path createFilepath() {
        if (this.filepath == null) {
            this.filepath = new Path(this.getProject());
        }
        return this.filepath.createPath();
    }
    
    public void setProperty(final String property) {
        this.property = property;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
    
    public void setValue(final String value) {
        this.setValue((Object)value);
    }
    
    public void setClassname(final String classname) {
        if (!"".equals(classname)) {
            this.classname = classname;
        }
    }
    
    public void setFile(final File file) {
        this.file = file;
        this.filename = Available.FILE_UTILS.removeLeadingPath(this.getProject().getBaseDir(), file);
    }
    
    public void setResource(final String resource) {
        this.resource = resource;
    }
    
    public void setType(final String type) {
        this.log("DEPRECATED - The setType(String) method has been deprecated. Use setType(Available.FileDir) instead.", 1);
        (this.type = new FileDir()).setValue(type);
    }
    
    public void setType(final FileDir type) {
        this.type = type;
    }
    
    public void setIgnoresystemclasses(final boolean ignore) {
        this.ignoreSystemclasses = ignore;
    }
    
    public void execute() throws BuildException {
        if (this.property == null) {
            throw new BuildException("property attribute is required", this.getLocation());
        }
        this.isTask = true;
        try {
            if (this.eval()) {
                final PropertyHelper ph = PropertyHelper.getPropertyHelper(this.getProject());
                final Object oldvalue = ph.getProperty(this.property);
                if (null != oldvalue && !oldvalue.equals(this.value)) {
                    this.log("DEPRECATED - <available> used to override an existing property." + StringUtils.LINE_SEP + "  Build file should not reuse the same property" + " name for different values.", 1);
                }
                ph.setProperty(this.property, this.value, true);
            }
        }
        finally {
            this.isTask = false;
        }
    }
    
    public boolean eval() throws BuildException {
        try {
            if (this.classname == null && this.file == null && this.resource == null) {
                throw new BuildException("At least one of (classname|file|resource) is required", this.getLocation());
            }
            if (this.type != null && this.file == null) {
                throw new BuildException("The type attribute is only valid when specifying the file attribute.", this.getLocation());
            }
            if (this.classpath != null) {
                this.classpath.setProject(this.getProject());
                this.loader = this.getProject().createClassLoader(this.classpath);
            }
            String appendix = "";
            if (this.isTask) {
                appendix = " to set property " + this.property;
            }
            else {
                this.setTaskName("available");
            }
            if (this.classname != null && !this.checkClass(this.classname)) {
                this.log("Unable to load class " + this.classname + appendix, 3);
                return false;
            }
            if (this.file != null && !this.checkFile()) {
                final StringBuffer buf = new StringBuffer("Unable to find ");
                if (this.type != null) {
                    buf.append(this.type).append(' ');
                }
                buf.append(this.filename).append(appendix);
                this.log(buf.toString(), 3);
                return false;
            }
            if (this.resource != null && !this.checkResource(this.resource)) {
                this.log("Unable to load resource " + this.resource + appendix, 3);
                return false;
            }
        }
        finally {
            if (this.loader != null) {
                this.loader.cleanup();
                this.loader = null;
            }
            if (!this.isTask) {
                this.setTaskName(null);
            }
        }
        return true;
    }
    
    private boolean checkFile() {
        if (this.filepath == null) {
            return this.checkFile(this.file, this.filename);
        }
        final String[] paths = this.filepath.list();
        int i = 0;
        while (i < paths.length) {
            this.log("Searching " + paths[i], 3);
            final File path = new File(paths[i]);
            if (path.exists() && (this.filename.equals(paths[i]) || this.filename.equals(path.getName()))) {
                if (this.type == null) {
                    this.log("Found: " + path, 3);
                    return true;
                }
                if (this.type.isDir() && path.isDirectory()) {
                    this.log("Found directory: " + path, 3);
                    return true;
                }
                if (this.type.isFile() && path.isFile()) {
                    this.log("Found file: " + path, 3);
                    return true;
                }
                return false;
            }
            else {
                File parent = path.getParentFile();
                if (parent != null && parent.exists() && this.filename.equals(parent.getAbsolutePath())) {
                    if (this.type == null) {
                        this.log("Found: " + parent, 3);
                        return true;
                    }
                    if (this.type.isDir()) {
                        this.log("Found directory: " + parent, 3);
                        return true;
                    }
                    return false;
                }
                else {
                    if (path.exists() && path.isDirectory() && this.checkFile(new File(path, this.filename), this.filename + " in " + path)) {
                        return true;
                    }
                    while (this.searchParents && parent != null && parent.exists()) {
                        if (this.checkFile(new File(parent, this.filename), this.filename + " in " + parent)) {
                            return true;
                        }
                        parent = parent.getParentFile();
                    }
                    ++i;
                }
            }
        }
        return false;
    }
    
    private boolean checkFile(final File f, final String text) {
        if (this.type != null) {
            if (this.type.isDir()) {
                if (f.isDirectory()) {
                    this.log("Found directory: " + text, 3);
                }
                return f.isDirectory();
            }
            if (this.type.isFile()) {
                if (f.isFile()) {
                    this.log("Found file: " + text, 3);
                }
                return f.isFile();
            }
        }
        if (f.exists()) {
            this.log("Found: " + text, 3);
        }
        return f.exists();
    }
    
    private boolean checkResource(final String resource) {
        if (this.loader != null) {
            return this.loader.getResourceAsStream(resource) != null;
        }
        final ClassLoader cL = this.getClass().getClassLoader();
        if (cL != null) {
            return cL.getResourceAsStream(resource) != null;
        }
        return ClassLoader.getSystemResourceAsStream(resource) != null;
    }
    
    private boolean checkClass(final String classname) {
        try {
            if (this.ignoreSystemclasses) {
                (this.loader = this.getProject().createClassLoader(this.classpath)).setParentFirst(false);
                this.loader.addJavaLibraries();
                try {
                    this.loader.findClass(classname);
                    return true;
                }
                catch (SecurityException se) {
                    return true;
                }
            }
            if (this.loader != null) {
                this.loader.loadClass(classname);
            }
            else {
                final ClassLoader l = this.getClass().getClassLoader();
                if (l != null) {
                    Class.forName(classname, true, l);
                }
                else {
                    Class.forName(classname);
                }
            }
            return true;
        }
        catch (ClassNotFoundException e2) {
            this.log("class \"" + classname + "\" was not found", 4);
            return false;
        }
        catch (NoClassDefFoundError e) {
            this.log("Could not load dependent class \"" + e.getMessage() + "\" for class \"" + classname + "\"", 4);
            return false;
        }
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
    
    public static class FileDir extends EnumeratedAttribute
    {
        private static final String[] VALUES;
        
        public String[] getValues() {
            return FileDir.VALUES;
        }
        
        public boolean isDir() {
            return "dir".equalsIgnoreCase(this.getValue());
        }
        
        public boolean isFile() {
            return "file".equalsIgnoreCase(this.getValue());
        }
        
        static {
            VALUES = new String[] { "file", "dir" };
        }
    }
}
