package org.apache.tools.ant.taskdefs;

import java.net.*;
import org.apache.tools.ant.util.*;
import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.util.*;

public abstract class Definer extends DefBase
{
    private static final String ANTLIB_XML = "/antlib.xml";
    private static final ThreadLocal<Map<URL, Location>> RESOURCE_STACK;
    private String name;
    private String classname;
    private File file;
    private String resource;
    private boolean restrict;
    private int format;
    private boolean definerSet;
    private int onError;
    private String adapter;
    private String adaptTo;
    private Class<?> adapterClass;
    private Class<?> adaptToClass;
    
    public Definer() {
        super();
        this.restrict = false;
        this.format = 0;
        this.definerSet = false;
        this.onError = 0;
    }
    
    protected void setRestrict(final boolean restrict) {
        this.restrict = restrict;
    }
    
    public void setOnError(final OnError onError) {
        this.onError = onError.getIndex();
    }
    
    public void setFormat(final Format format) {
        this.format = format.getIndex();
    }
    
    public String getName() {
        return this.name;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getResource() {
        return this.resource;
    }
    
    public void execute() throws BuildException {
        final ClassLoader al = this.createLoader();
        if (!this.definerSet) {
            if (this.getURI() == null) {
                throw new BuildException("name, file or resource attribute of " + this.getTaskName() + " is undefined", this.getLocation());
            }
            if (!this.getURI().startsWith("antlib:")) {
                throw new BuildException("Only antlib URIs can be located from the URI alone, not the URI '" + this.getURI() + "'");
            }
            final String uri1 = this.getURI();
            this.setResource(makeResourceFromURI(uri1));
        }
        if (this.name != null) {
            if (this.classname == null) {
                throw new BuildException("classname attribute of " + this.getTaskName() + " element " + "is undefined", this.getLocation());
            }
            this.addDefinition(al, this.name, this.classname);
        }
        else {
            if (this.classname != null) {
                final String msg = "You must not specify classname together with file or resource.";
                throw new BuildException(msg, this.getLocation());
            }
            Enumeration<URL> urls;
            if (this.file == null) {
                urls = this.resourceToURLs(al);
            }
            else {
                final URL url = this.fileToURL();
                if (url == null) {
                    return;
                }
                urls = Collections.enumeration(Collections.singleton(url));
            }
            while (urls.hasMoreElements()) {
                final URL url = urls.nextElement();
                int fmt = this.format;
                if (url.toString().toLowerCase(Locale.ENGLISH).endsWith(".xml")) {
                    fmt = 1;
                }
                if (fmt == 0) {
                    this.loadProperties(al, url);
                    break;
                }
                if (Definer.RESOURCE_STACK.get().get(url) != null) {
                    this.log("Warning: Recursive loading of " + url + " ignored" + " at " + this.getLocation() + " originally loaded at " + Definer.RESOURCE_STACK.get().get(url), 1);
                }
                else {
                    try {
                        Definer.RESOURCE_STACK.get().put(url, this.getLocation());
                        this.loadAntlib(al, url);
                    }
                    finally {
                        Definer.RESOURCE_STACK.get().remove(url);
                    }
                }
            }
        }
    }
    
    public static String makeResourceFromURI(final String uri) {
        final String path = uri.substring("antlib:".length());
        String resource;
        if (path.startsWith("//")) {
            resource = path.substring("//".length());
            if (!resource.endsWith(".xml")) {
                resource += "/antlib.xml";
            }
        }
        else {
            resource = path.replace('.', '/') + "/antlib.xml";
        }
        return resource;
    }
    
    private URL fileToURL() {
        String message = null;
        if (!this.file.exists()) {
            message = "File " + this.file + " does not exist";
        }
        if (message == null && !this.file.isFile()) {
            message = "File " + this.file + " is not a file";
        }
        if (message == null) {
            try {
                return FileUtils.getFileUtils().getFileURL(this.file);
            }
            catch (Exception ex) {
                message = "File " + this.file + " cannot use as URL: " + ex.toString();
            }
        }
        switch (this.onError) {
            case 3: {
                throw new BuildException(message);
            }
            case 0:
            case 1: {
                this.log(message, 1);
                break;
            }
            case 2: {
                this.log(message, 3);
                break;
            }
        }
        return null;
    }
    
    private Enumeration<URL> resourceToURLs(final ClassLoader classLoader) {
        Enumeration<URL> ret;
        try {
            ret = classLoader.getResources(this.resource);
        }
        catch (IOException e) {
            throw new BuildException("Could not fetch resources named " + this.resource, e, this.getLocation());
        }
        if (!ret.hasMoreElements()) {
            final String message = "Could not load definitions from resource " + this.resource + ". It could not be found.";
            switch (this.onError) {
                case 3: {
                    throw new BuildException(message);
                }
                case 0:
                case 1: {
                    this.log(message, 1);
                    break;
                }
                case 2: {
                    this.log(message, 3);
                    break;
                }
            }
        }
        return ret;
    }
    
    protected void loadProperties(final ClassLoader al, final URL url) {
        InputStream is = null;
        try {
            is = url.openStream();
            if (is == null) {
                this.log("Could not load definitions from " + url, 1);
                return;
            }
            final Properties props = new Properties();
            props.load(is);
            final Enumeration<?> keys = ((Hashtable<?, V>)props).keys();
            while (keys.hasMoreElements()) {
                this.name = (String)keys.nextElement();
                this.classname = props.getProperty(this.name);
                this.addDefinition(al, this.name, this.classname);
            }
        }
        catch (IOException ex) {
            throw new BuildException(ex, this.getLocation());
        }
        finally {
            FileUtils.close(is);
        }
    }
    
    private void loadAntlib(final ClassLoader classLoader, final URL url) {
        try {
            final Antlib antlib = Antlib.createAntlib(this.getProject(), url, this.getURI());
            antlib.setClassLoader(classLoader);
            antlib.setURI(this.getURI());
            antlib.execute();
        }
        catch (BuildException ex) {
            throw ProjectHelper.addLocationToBuildException(ex, this.getLocation());
        }
    }
    
    public void setFile(final File file) {
        if (this.definerSet) {
            this.tooManyDefinitions();
        }
        this.definerSet = true;
        this.file = file;
    }
    
    public void setResource(final String res) {
        if (this.definerSet) {
            this.tooManyDefinitions();
        }
        this.definerSet = true;
        this.resource = res;
    }
    
    public void setAntlib(final String antlib) {
        if (this.definerSet) {
            this.tooManyDefinitions();
        }
        if (!antlib.startsWith("antlib:")) {
            throw new BuildException("Invalid antlib attribute - it must start with antlib:");
        }
        this.setURI(antlib);
        this.resource = antlib.substring("antlib:".length()).replace('.', '/') + "/antlib.xml";
        this.definerSet = true;
    }
    
    public void setName(final String name) {
        if (this.definerSet) {
            this.tooManyDefinitions();
        }
        this.definerSet = true;
        this.name = name;
    }
    
    public String getClassname() {
        return this.classname;
    }
    
    public void setClassname(final String classname) {
        this.classname = classname;
    }
    
    public void setAdapter(final String adapter) {
        this.adapter = adapter;
    }
    
    protected void setAdapterClass(final Class<?> adapterClass) {
        this.adapterClass = adapterClass;
    }
    
    public void setAdaptTo(final String adaptTo) {
        this.adaptTo = adaptTo;
    }
    
    protected void setAdaptToClass(final Class<?> adaptToClass) {
        this.adaptToClass = adaptToClass;
    }
    
    protected void addDefinition(final ClassLoader al, String name, final String classname) throws BuildException {
        Class<?> cl = null;
        try {
            try {
                name = ProjectHelper.genComponentName(this.getURI(), name);
                if (this.onError != 2) {
                    cl = Class.forName(classname, true, al);
                }
                if (this.adapter != null) {
                    this.adapterClass = Class.forName(this.adapter, true, al);
                }
                if (this.adaptTo != null) {
                    this.adaptToClass = Class.forName(this.adaptTo, true, al);
                }
                final AntTypeDefinition def = new AntTypeDefinition();
                def.setName(name);
                def.setClassName(classname);
                def.setClass(cl);
                def.setAdapterClass(this.adapterClass);
                def.setAdaptToClass(this.adaptToClass);
                def.setRestrict(this.restrict);
                def.setClassLoader(al);
                if (cl != null) {
                    def.checkClass(this.getProject());
                }
                ComponentHelper.getComponentHelper(this.getProject()).addDataTypeDefinition(def);
            }
            catch (ClassNotFoundException cnfe) {
                final String msg = this.getTaskName() + " class " + classname + " cannot be found" + "\n using the classloader " + al;
                throw new BuildException(msg, cnfe, this.getLocation());
            }
            catch (NoClassDefFoundError ncdfe) {
                final String msg = this.getTaskName() + " A class needed by class " + classname + " cannot be found: " + ncdfe.getMessage() + "\n using the classloader " + al;
                throw new BuildException(msg, ncdfe, this.getLocation());
            }
        }
        catch (BuildException ex) {
            switch (this.onError) {
                case 0:
                case 3: {
                    throw ex;
                }
                case 1: {
                    this.log(ex.getLocation() + "Warning: " + ex.getMessage(), 1);
                    break;
                }
                default: {
                    this.log(ex.getLocation() + ex.getMessage(), 4);
                    break;
                }
            }
        }
    }
    
    private void tooManyDefinitions() {
        throw new BuildException("Only one of the attributes name, file and resource can be set", this.getLocation());
    }
    
    static {
        RESOURCE_STACK = new ThreadLocal<Map<URL, Location>>() {
            protected Map<URL, Location> initialValue() {
                return new HashMap<URL, Location>();
            }
        };
    }
    
    public static class OnError extends EnumeratedAttribute
    {
        public static final int FAIL = 0;
        public static final int REPORT = 1;
        public static final int IGNORE = 2;
        public static final int FAIL_ALL = 3;
        public static final String POLICY_FAIL = "fail";
        public static final String POLICY_REPORT = "report";
        public static final String POLICY_IGNORE = "ignore";
        public static final String POLICY_FAILALL = "failall";
        
        public OnError() {
            super();
        }
        
        public OnError(final String value) {
            super();
            this.setValue(value);
        }
        
        public String[] getValues() {
            return new String[] { "fail", "report", "ignore", "failall" };
        }
    }
    
    public static class Format extends EnumeratedAttribute
    {
        public static final int PROPERTIES = 0;
        public static final int XML = 1;
        
        public String[] getValues() {
            return new String[] { "properties", "xml" };
        }
    }
}
