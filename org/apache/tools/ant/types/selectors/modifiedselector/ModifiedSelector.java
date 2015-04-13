package org.apache.tools.ant.types.selectors.modifiedselector;

import org.apache.tools.ant.types.selectors.*;
import org.apache.tools.ant.types.resources.selectors.*;
import java.io.*;
import java.util.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

public class ModifiedSelector extends BaseExtendSelector implements BuildListener, ResourceSelector
{
    private static final String CACHE_PREFIX = "cache.";
    private static final String ALGORITHM_PREFIX = "algorithm.";
    private static final String COMPARATOR_PREFIX = "comparator.";
    private CacheName cacheName;
    private String cacheClass;
    private AlgorithmName algoName;
    private String algorithmClass;
    private ComparatorName compName;
    private String comparatorClass;
    private boolean update;
    private boolean selectDirectories;
    private boolean selectResourcesWithoutInputStream;
    private boolean delayUpdate;
    private Comparator<? super String> comparator;
    private Algorithm algorithm;
    private Cache cache;
    private int modified;
    private boolean isConfigured;
    private Vector<Parameter> configParameter;
    private Vector<Parameter> specialParameter;
    private ClassLoader myClassLoader;
    private Path classpath;
    
    public ModifiedSelector() {
        super();
        this.cacheName = null;
        this.algoName = null;
        this.compName = null;
        this.update = true;
        this.selectDirectories = true;
        this.selectResourcesWithoutInputStream = true;
        this.delayUpdate = true;
        this.comparator = null;
        this.algorithm = null;
        this.cache = null;
        this.modified = 0;
        this.isConfigured = false;
        this.configParameter = new Vector<Parameter>();
        this.specialParameter = new Vector<Parameter>();
        this.myClassLoader = null;
        this.classpath = null;
    }
    
    public void verifySettings() {
        this.configure();
        if (this.cache == null) {
            this.setError("Cache must be set.");
        }
        else if (this.algorithm == null) {
            this.setError("Algorithm must be set.");
        }
        else if (!this.cache.isValid()) {
            this.setError("Cache must be proper configured.");
        }
        else if (!this.algorithm.isValid()) {
            this.setError("Algorithm must be proper configured.");
        }
    }
    
    public void configure() {
        if (this.isConfigured) {
            return;
        }
        this.isConfigured = true;
        final Project p = this.getProject();
        final String filename = "cache.properties";
        File cachefile = null;
        if (p != null) {
            cachefile = new File(p.getBaseDir(), filename);
            this.getProject().addBuildListener(this);
        }
        else {
            cachefile = new File(filename);
            this.setDelayUpdate(false);
        }
        final Cache defaultCache = new PropertiesfileCache(cachefile);
        final Algorithm defaultAlgorithm = new DigestAlgorithm();
        final Comparator<? super String> defaultComparator = new EqualComparator();
        for (final Parameter parameter : this.configParameter) {
            if (parameter.getName().indexOf(".") > 0) {
                this.specialParameter.add(parameter);
            }
            else {
                this.useParameter(parameter);
            }
        }
        this.configParameter = new Vector<Parameter>();
        if (this.algoName != null) {
            if ("hashvalue".equals(this.algoName.getValue())) {
                this.algorithm = new HashvalueAlgorithm();
            }
            else if ("digest".equals(this.algoName.getValue())) {
                this.algorithm = new DigestAlgorithm();
            }
            else if ("checksum".equals(this.algoName.getValue())) {
                this.algorithm = new ChecksumAlgorithm();
            }
        }
        else if (this.algorithmClass != null) {
            this.algorithm = this.loadClass(this.algorithmClass, "is not an Algorithm.", (Class<? extends Algorithm>)Algorithm.class);
        }
        else {
            this.algorithm = defaultAlgorithm;
        }
        if (this.cacheName != null) {
            if ("propertyfile".equals(this.cacheName.getValue())) {
                this.cache = new PropertiesfileCache();
            }
        }
        else if (this.cacheClass != null) {
            this.cache = this.loadClass(this.cacheClass, "is not a Cache.", (Class<? extends Cache>)Cache.class);
        }
        else {
            this.cache = defaultCache;
        }
        if (this.compName != null) {
            if ("equal".equals(this.compName.getValue())) {
                this.comparator = new EqualComparator();
            }
            else if ("rule".equals(this.compName.getValue())) {
                throw new BuildException("RuleBasedCollator not yet supported.");
            }
        }
        else if (this.comparatorClass != null) {
            final Comparator<? super String> localComparator = this.loadClass(this.comparatorClass, "is not a Comparator.", (Class<? extends Comparator<? super String>>)Comparator.class);
            this.comparator = localComparator;
        }
        else {
            this.comparator = defaultComparator;
        }
        for (final Parameter par : this.specialParameter) {
            this.useParameter(par);
        }
        this.specialParameter = new Vector<Parameter>();
    }
    
    protected <T> T loadClass(final String classname, final String msg, final Class<? extends T> type) {
        try {
            final ClassLoader cl = this.getClassLoader();
            Class<?> clazz = null;
            if (cl != null) {
                clazz = cl.loadClass(classname);
            }
            else {
                clazz = Class.forName(classname);
            }
            final T rv = (T)clazz.asSubclass(type).newInstance();
            if (!type.isInstance(rv)) {
                throw new BuildException("Specified class (" + classname + ") " + msg);
            }
            return rv;
        }
        catch (ClassNotFoundException e2) {
            throw new BuildException("Specified class (" + classname + ") not found.");
        }
        catch (Exception e) {
            throw new BuildException(e);
        }
    }
    
    public boolean isSelected(final Resource resource) {
        if (resource.isFilesystemOnly()) {
            final FileResource fileResource = (FileResource)resource;
            final File file = fileResource.getFile();
            final String filename = fileResource.getName();
            final File basedir = fileResource.getBaseDir();
            return this.isSelected(basedir, filename, file);
        }
        try {
            final FileUtils fu = FileUtils.getFileUtils();
            final File tmpFile = fu.createTempFile("modified-", ".tmp", null, true, false);
            final Resource tmpResource = new FileResource(tmpFile);
            ResourceUtils.copyResource(resource, tmpResource);
            final boolean isSelected = this.isSelected(tmpFile.getParentFile(), tmpFile.getName(), resource.toLongString());
            tmpFile.delete();
            return isSelected;
        }
        catch (UnsupportedOperationException uoe) {
            this.log("The resource '" + resource.getName() + "' does not provide an InputStream, so it is not checked. " + "Akkording to 'selres' attribute value it is " + (this.selectResourcesWithoutInputStream ? "" : " not") + "selected.", 2);
            return this.selectResourcesWithoutInputStream;
        }
        catch (Exception e) {
            throw new BuildException(e);
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        return this.isSelected(basedir, filename, file.getAbsolutePath());
    }
    
    private boolean isSelected(final File basedir, final String filename, final String cacheKey) {
        this.validate();
        final File f = new File(basedir, filename);
        if (f.isDirectory()) {
            return this.selectDirectories;
        }
        final String cachedValue = String.valueOf(this.cache.get(f.getAbsolutePath()));
        final String newValue = this.algorithm.getValue(f);
        final boolean rv = this.comparator.compare(cachedValue, newValue) != 0;
        if (this.update && rv) {
            this.cache.put(f.getAbsolutePath(), newValue);
            this.setModified(this.getModified() + 1);
            if (!this.getDelayUpdate()) {
                this.saveCache();
            }
        }
        return rv;
    }
    
    protected void saveCache() {
        if (this.getModified() > 0) {
            this.cache.save();
            this.setModified(0);
        }
    }
    
    public void setAlgorithmClass(final String classname) {
        this.algorithmClass = classname;
    }
    
    public void setComparatorClass(final String classname) {
        this.comparatorClass = classname;
    }
    
    public void setCacheClass(final String classname) {
        this.cacheClass = classname;
    }
    
    public void setUpdate(final boolean update) {
        this.update = update;
    }
    
    public void setSeldirs(final boolean seldirs) {
        this.selectDirectories = seldirs;
    }
    
    public void setSelres(final boolean newValue) {
        this.selectResourcesWithoutInputStream = newValue;
    }
    
    public int getModified() {
        return this.modified;
    }
    
    public void setModified(final int modified) {
        this.modified = modified;
    }
    
    public boolean getDelayUpdate() {
        return this.delayUpdate;
    }
    
    public void setDelayUpdate(final boolean delayUpdate) {
        this.delayUpdate = delayUpdate;
    }
    
    public void addClasspath(final Path path) {
        if (this.classpath != null) {
            throw new BuildException("<classpath> can be set only once.");
        }
        this.classpath = path;
    }
    
    public ClassLoader getClassLoader() {
        if (this.myClassLoader == null) {
            this.myClassLoader = ((this.classpath == null) ? this.getClass().getClassLoader() : this.getProject().createClassLoader(this.classpath));
        }
        return this.myClassLoader;
    }
    
    public void setClassLoader(final ClassLoader loader) {
        this.myClassLoader = loader;
    }
    
    public void addParam(final String key, final Object value) {
        final Parameter par = new Parameter();
        par.setName(key);
        par.setValue(String.valueOf(value));
        this.configParameter.add(par);
    }
    
    public void addParam(final Parameter parameter) {
        this.configParameter.add(parameter);
    }
    
    public void setParameters(final Parameter[] parameters) {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                this.configParameter.add(parameters[i]);
            }
        }
    }
    
    public void useParameter(final Parameter parameter) {
        final String key = parameter.getName();
        final String value = parameter.getValue();
        if ("cache".equals(key)) {
            final CacheName cn = new CacheName();
            cn.setValue(value);
            this.setCache(cn);
        }
        else if ("algorithm".equals(key)) {
            final AlgorithmName an = new AlgorithmName();
            an.setValue(value);
            this.setAlgorithm(an);
        }
        else if ("comparator".equals(key)) {
            final ComparatorName cn2 = new ComparatorName();
            cn2.setValue(value);
            this.setComparator(cn2);
        }
        else if ("update".equals(key)) {
            final boolean updateValue = "true".equalsIgnoreCase(value);
            this.setUpdate(updateValue);
        }
        else if ("delayupdate".equals(key)) {
            final boolean updateValue = "true".equalsIgnoreCase(value);
            this.setDelayUpdate(updateValue);
        }
        else if ("seldirs".equals(key)) {
            final boolean sdValue = "true".equalsIgnoreCase(value);
            this.setSeldirs(sdValue);
        }
        else if (key.startsWith("cache.")) {
            final String name = key.substring("cache.".length());
            this.tryToSetAParameter(this.cache, name, value);
        }
        else if (key.startsWith("algorithm.")) {
            final String name = key.substring("algorithm.".length());
            this.tryToSetAParameter(this.algorithm, name, value);
        }
        else if (key.startsWith("comparator.")) {
            final String name = key.substring("comparator.".length());
            this.tryToSetAParameter(this.comparator, name, value);
        }
        else {
            this.setError("Invalid parameter " + key);
        }
    }
    
    protected void tryToSetAParameter(final Object obj, final String name, final String value) {
        final Project prj = (this.getProject() != null) ? this.getProject() : new Project();
        final IntrospectionHelper iHelper = IntrospectionHelper.getHelper(prj, obj.getClass());
        try {
            iHelper.setAttribute(prj, obj, name, value);
        }
        catch (BuildException ex) {}
    }
    
    public String toString() {
        final StringBuffer buf = new StringBuffer("{modifiedselector");
        buf.append(" update=").append(this.update);
        buf.append(" seldirs=").append(this.selectDirectories);
        buf.append(" cache=").append(this.cache);
        buf.append(" algorithm=").append(this.algorithm);
        buf.append(" comparator=").append(this.comparator);
        buf.append("}");
        return buf.toString();
    }
    
    public void buildFinished(final BuildEvent event) {
        if (this.getDelayUpdate()) {
            this.saveCache();
        }
    }
    
    public void targetFinished(final BuildEvent event) {
        if (this.getDelayUpdate()) {
            this.saveCache();
        }
    }
    
    public void taskFinished(final BuildEvent event) {
        if (this.getDelayUpdate()) {
            this.saveCache();
        }
    }
    
    public void buildStarted(final BuildEvent event) {
    }
    
    public void targetStarted(final BuildEvent event) {
    }
    
    public void taskStarted(final BuildEvent event) {
    }
    
    public void messageLogged(final BuildEvent event) {
    }
    
    public Cache getCache() {
        return this.cache;
    }
    
    public void setCache(final CacheName name) {
        this.cacheName = name;
    }
    
    public Algorithm getAlgorithm() {
        return this.algorithm;
    }
    
    public void setAlgorithm(final AlgorithmName name) {
        this.algoName = name;
    }
    
    public Comparator<? super String> getComparator() {
        return this.comparator;
    }
    
    public void setComparator(final ComparatorName name) {
        this.compName = name;
    }
    
    public static class CacheName extends EnumeratedAttribute
    {
        public String[] getValues() {
            return new String[] { "propertyfile" };
        }
    }
    
    public static class AlgorithmName extends EnumeratedAttribute
    {
        public String[] getValues() {
            return new String[] { "hashvalue", "digest", "checksum" };
        }
    }
    
    public static class ComparatorName extends EnumeratedAttribute
    {
        public String[] getValues() {
            return new String[] { "equal", "rule" };
        }
    }
}
