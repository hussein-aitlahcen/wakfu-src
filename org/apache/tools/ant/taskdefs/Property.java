package org.apache.tools.ant.taskdefs;

import java.net.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;
import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.property.*;
import java.util.*;

public class Property extends Task
{
    protected String name;
    protected String value;
    protected File file;
    protected URL url;
    protected String resource;
    protected Path classpath;
    protected String env;
    protected Reference ref;
    protected String prefix;
    private Project fallback;
    private Object untypedValue;
    private boolean valueAttributeUsed;
    private boolean relative;
    private File basedir;
    private boolean prefixValues;
    protected boolean userProperty;
    
    public Property() {
        this(false);
    }
    
    protected Property(final boolean userProperty) {
        this(userProperty, null);
    }
    
    protected Property(final boolean userProperty, final Project fallback) {
        super();
        this.valueAttributeUsed = false;
        this.relative = false;
        this.prefixValues = false;
        this.userProperty = userProperty;
        this.fallback = fallback;
    }
    
    public void setRelative(final boolean relative) {
        this.relative = relative;
    }
    
    public void setBasedir(final File basedir) {
        this.basedir = basedir;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setLocation(final File location) {
        if (this.relative) {
            this.internalSetValue(location);
        }
        else {
            this.setValue(location.getAbsolutePath());
        }
    }
    
    public void setValue(final Object value) {
        this.valueAttributeUsed = true;
        this.internalSetValue(value);
    }
    
    private void internalSetValue(final Object value) {
        this.untypedValue = value;
        this.value = ((value == null) ? null : value.toString());
    }
    
    public void setValue(final String value) {
        this.setValue((Object)value);
    }
    
    public void addText(String msg) {
        if (!this.valueAttributeUsed) {
            msg = this.getProject().replaceProperties(msg);
            final String currentValue = this.getValue();
            if (currentValue != null) {
                msg = currentValue + msg;
            }
            this.internalSetValue(msg);
        }
        else if (msg.trim().length() > 0) {
            throw new BuildException("can't combine nested text with value attribute");
        }
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setFile(final File file) {
        this.file = file;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public void setUrl(final URL url) {
        this.url = url;
    }
    
    public URL getUrl() {
        return this.url;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
        if (prefix != null && !prefix.endsWith(".")) {
            this.prefix += ".";
        }
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefixValues(final boolean b) {
        this.prefixValues = b;
    }
    
    public boolean getPrefixValues() {
        return this.prefixValues;
    }
    
    public void setRefid(final Reference ref) {
        this.ref = ref;
    }
    
    public Reference getRefid() {
        return this.ref;
    }
    
    public void setResource(final String resource) {
        this.resource = resource;
    }
    
    public String getResource() {
        return this.resource;
    }
    
    public void setEnvironment(final String env) {
        this.env = env;
    }
    
    public String getEnvironment() {
        return this.env;
    }
    
    public void setClasspath(final Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        }
        else {
            this.classpath.append(classpath);
        }
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
    
    public Path getClasspath() {
        return this.classpath;
    }
    
    public void setUserProperty(final boolean userProperty) {
        this.log("DEPRECATED: Ignoring request to set user property in Property task.", 1);
    }
    
    public String toString() {
        return (this.value == null) ? "" : this.value;
    }
    
    public void execute() throws BuildException {
        if (this.getProject() == null) {
            throw new IllegalStateException("project has not been set");
        }
        if (this.name != null) {
            if (this.untypedValue == null && this.ref == null) {
                throw new BuildException("You must specify value, location or refid with the name attribute", this.getLocation());
            }
        }
        else if (this.url == null && this.file == null && this.resource == null && this.env == null) {
            throw new BuildException("You must specify url, file, resource or environment when not using the name attribute", this.getLocation());
        }
        if (this.url == null && this.file == null && this.resource == null && this.prefix != null) {
            throw new BuildException("Prefix is only valid when loading from a url, file or resource", this.getLocation());
        }
        Label_0268: {
            if (this.name != null && this.untypedValue != null) {
                if (this.relative) {
                    try {
                        final File from = (File)((this.untypedValue instanceof File) ? this.untypedValue : new File(this.untypedValue.toString()));
                        final File to = (this.basedir != null) ? this.basedir : this.getProject().getBaseDir();
                        String relPath = FileUtils.getRelativePath(to, from);
                        relPath = relPath.replace('/', File.separatorChar);
                        this.addProperty(this.name, relPath);
                        break Label_0268;
                    }
                    catch (Exception e) {
                        throw new BuildException(e, this.getLocation());
                    }
                }
                this.addProperty(this.name, this.untypedValue);
            }
        }
        if (this.file != null) {
            this.loadFile(this.file);
        }
        if (this.url != null) {
            this.loadUrl(this.url);
        }
        if (this.resource != null) {
            this.loadResource(this.resource);
        }
        if (this.env != null) {
            this.loadEnvironment(this.env);
        }
        if (this.name != null && this.ref != null) {
            try {
                this.addProperty(this.name, this.ref.getReferencedObject(this.getProject()).toString());
            }
            catch (BuildException be) {
                if (this.fallback == null) {
                    throw be;
                }
                this.addProperty(this.name, this.ref.getReferencedObject(this.fallback).toString());
            }
        }
    }
    
    protected void loadUrl(final URL url) throws BuildException {
        final Properties props = new Properties();
        this.log("Loading " + url, 3);
        try {
            final InputStream is = url.openStream();
            try {
                this.loadProperties(props, is, url.getFile().endsWith(".xml"));
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
            this.addProperties(props);
        }
        catch (IOException ex) {
            throw new BuildException(ex, this.getLocation());
        }
    }
    
    private void loadProperties(final Properties props, final InputStream is, final boolean isXml) throws IOException {
        if (isXml) {
            props.loadFromXML(is);
        }
        else {
            props.load(is);
        }
    }
    
    protected void loadFile(final File file) throws BuildException {
        final Properties props = new Properties();
        this.log("Loading " + file.getAbsolutePath(), 3);
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    this.loadProperties(props, fis, file.getName().endsWith(".xml"));
                }
                finally {
                    FileUtils.close(fis);
                }
                this.addProperties(props);
            }
            else {
                this.log("Unable to find property file: " + file.getAbsolutePath(), 3);
            }
        }
        catch (IOException ex) {
            throw new BuildException(ex, this.getLocation());
        }
    }
    
    protected void loadResource(final String name) {
        final Properties props = new Properties();
        this.log("Resource Loading " + name, 3);
        InputStream is = null;
        ClassLoader cL = null;
        boolean cleanup = false;
        try {
            if (this.classpath != null) {
                cleanup = true;
                cL = this.getProject().createClassLoader(this.classpath);
            }
            else {
                cL = this.getClass().getClassLoader();
            }
            if (cL == null) {
                is = ClassLoader.getSystemResourceAsStream(name);
            }
            else {
                is = cL.getResourceAsStream(name);
            }
            if (is != null) {
                this.loadProperties(props, is, name.endsWith(".xml"));
                this.addProperties(props);
            }
            else {
                this.log("Unable to find resource " + name, 1);
            }
        }
        catch (IOException ex) {
            throw new BuildException(ex, this.getLocation());
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException ex2) {}
            }
            if (cleanup && cL != null) {
                ((AntClassLoader)cL).cleanup();
            }
        }
    }
    
    protected void loadEnvironment(String prefix) {
        final Properties props = new Properties();
        if (!prefix.endsWith(".")) {
            prefix += ".";
        }
        this.log("Loading Environment " + prefix, 3);
        final Map osEnv = Execute.getEnvironmentVariables();
        for (final Map.Entry entry : osEnv.entrySet()) {
            ((Hashtable<String, Object>)props).put(prefix + entry.getKey(), entry.getValue());
        }
        this.addProperties(props);
    }
    
    protected void addProperties(final Properties props) {
        final HashMap m = new HashMap((Map<? extends K, ? extends V>)props);
        this.resolveAllProperties(m);
        for (final Object k : m.keySet()) {
            if (k instanceof String) {
                String propertyName = (String)k;
                if (this.prefix != null) {
                    propertyName = this.prefix + propertyName;
                }
                this.addProperty(propertyName, m.get(k));
            }
        }
    }
    
    protected void addProperty(final String n, final String v) {
        this.addProperty(n, (Object)v);
    }
    
    protected void addProperty(final String n, final Object v) {
        final PropertyHelper ph = PropertyHelper.getPropertyHelper(this.getProject());
        if (this.userProperty) {
            if (ph.getUserProperty(n) == null) {
                ph.setInheritedProperty(n, v);
            }
            else {
                this.log("Override ignored for " + n, 3);
            }
        }
        else {
            ph.setNewProperty(n, v);
        }
    }
    
    private void resolveAllProperties(final Map props) throws BuildException {
        final PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(this.getProject());
        new ResolvePropertyMap(this.getProject(), propertyHelper, propertyHelper.getExpanders()).resolveAllProperties(props, this.getPrefix(), this.getPrefixValues());
    }
}
