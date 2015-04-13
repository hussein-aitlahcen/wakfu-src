package org.apache.tools.ant;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.launch.*;
import java.lang.reflect.*;
import java.util.zip.*;
import java.util.jar.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.io.*;
import org.apache.tools.ant.util.*;
import java.util.*;

public class AntClassLoader extends ClassLoader implements SubBuildListener
{
    private static final FileUtils FILE_UTILS;
    private static final int BUFFER_SIZE = 8192;
    private static final int NUMBER_OF_STRINGS = 256;
    private Vector<File> pathComponents;
    private Project project;
    private boolean parentFirst;
    private Vector<String> systemPackages;
    private Vector<String> loaderPackages;
    private boolean ignoreBase;
    private ClassLoader parent;
    private Hashtable<File, JarFile> jarFiles;
    private static Map<String, String> pathMap;
    private ClassLoader savedContextLoader;
    private boolean isContextLoaderSaved;
    private static Class<?> subClassToLoad;
    private static final Class<?>[] CONSTRUCTOR_ARGS;
    
    public AntClassLoader(final ClassLoader parent, final Project project, final Path classpath) {
        super();
        this.pathComponents = new VectorSet<File>();
        this.parentFirst = true;
        this.systemPackages = new Vector<String>();
        this.loaderPackages = new Vector<String>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<File, JarFile>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        this.setParent(parent);
        this.setClassPath(classpath);
        this.setProject(project);
    }
    
    public AntClassLoader() {
        super();
        this.pathComponents = new VectorSet<File>();
        this.parentFirst = true;
        this.systemPackages = new Vector<String>();
        this.loaderPackages = new Vector<String>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<File, JarFile>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        this.setParent(null);
    }
    
    public AntClassLoader(final Project project, final Path classpath) {
        super();
        this.pathComponents = new VectorSet<File>();
        this.parentFirst = true;
        this.systemPackages = new Vector<String>();
        this.loaderPackages = new Vector<String>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<File, JarFile>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        this.setParent(null);
        this.setProject(project);
        this.setClassPath(classpath);
    }
    
    public AntClassLoader(final ClassLoader parent, final Project project, final Path classpath, final boolean parentFirst) {
        this(project, classpath);
        if (parent != null) {
            this.setParent(parent);
        }
        this.setParentFirst(parentFirst);
        this.addJavaLibraries();
    }
    
    public AntClassLoader(final Project project, final Path classpath, final boolean parentFirst) {
        this(null, project, classpath, parentFirst);
    }
    
    public AntClassLoader(final ClassLoader parent, final boolean parentFirst) {
        super();
        this.pathComponents = new VectorSet<File>();
        this.parentFirst = true;
        this.systemPackages = new Vector<String>();
        this.loaderPackages = new Vector<String>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<File, JarFile>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        this.setParent(parent);
        this.project = null;
        this.parentFirst = parentFirst;
    }
    
    public void setProject(final Project project) {
        this.project = project;
        if (project != null) {
            project.addBuildListener(this);
        }
    }
    
    public void setClassPath(final Path classpath) {
        this.pathComponents.removeAllElements();
        if (classpath != null) {
            final Path actualClasspath = classpath.concatSystemClasspath("ignore");
            final String[] pathElements = actualClasspath.list();
            for (int i = 0; i < pathElements.length; ++i) {
                try {
                    this.addPathElement(pathElements[i]);
                }
                catch (BuildException ex) {}
            }
        }
    }
    
    public void setParent(final ClassLoader parent) {
        this.parent = ((parent == null) ? AntClassLoader.class.getClassLoader() : parent);
    }
    
    public void setParentFirst(final boolean parentFirst) {
        this.parentFirst = parentFirst;
    }
    
    protected void log(final String message, final int priority) {
        if (this.project != null) {
            this.project.log(message, priority);
        }
    }
    
    public void setThreadContextLoader() {
        if (this.isContextLoaderSaved) {
            throw new BuildException("Context loader has not been reset");
        }
        if (LoaderUtils.isContextLoaderAvailable()) {
            this.savedContextLoader = LoaderUtils.getContextClassLoader();
            ClassLoader loader = this;
            if (this.project != null && "only".equals(this.project.getProperty("build.sysclasspath"))) {
                loader = this.getClass().getClassLoader();
            }
            LoaderUtils.setContextClassLoader(loader);
            this.isContextLoaderSaved = true;
        }
    }
    
    public void resetThreadContextLoader() {
        if (LoaderUtils.isContextLoaderAvailable() && this.isContextLoaderSaved) {
            LoaderUtils.setContextClassLoader(this.savedContextLoader);
            this.savedContextLoader = null;
            this.isContextLoaderSaved = false;
        }
    }
    
    public void addPathElement(final String pathElement) throws BuildException {
        final File pathComponent = (this.project != null) ? this.project.resolveFile(pathElement) : new File(pathElement);
        try {
            this.addPathFile(pathComponent);
        }
        catch (IOException e) {
            throw new BuildException(e);
        }
    }
    
    public void addPathComponent(final File file) {
        if (this.pathComponents.contains(file)) {
            return;
        }
        this.pathComponents.addElement(file);
    }
    
    protected void addPathFile(final File pathComponent) throws IOException {
        if (!this.pathComponents.contains(pathComponent)) {
            this.pathComponents.addElement(pathComponent);
        }
        if (pathComponent.isDirectory()) {
            return;
        }
        final String absPathPlusTimeAndLength = pathComponent.getAbsolutePath() + pathComponent.lastModified() + "-" + pathComponent.length();
        String classpath = AntClassLoader.pathMap.get(absPathPlusTimeAndLength);
        if (classpath == null) {
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(pathComponent);
                final Manifest manifest = jarFile.getManifest();
                if (manifest == null) {
                    return;
                }
                classpath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
            }
            finally {
                if (jarFile != null) {
                    jarFile.close();
                }
            }
            if (classpath == null) {
                classpath = "";
            }
            AntClassLoader.pathMap.put(absPathPlusTimeAndLength, classpath);
        }
        if (!"".equals(classpath)) {
            final URL baseURL = AntClassLoader.FILE_UTILS.getFileURL(pathComponent);
            final StringTokenizer st = new StringTokenizer(classpath);
            while (st.hasMoreTokens()) {
                final String classpathElement = st.nextToken();
                final URL libraryURL = new URL(baseURL, classpathElement);
                if (!libraryURL.getProtocol().equals("file")) {
                    this.log("Skipping jar library " + classpathElement + " since only relative URLs are supported by this" + " loader", 3);
                }
                else {
                    final String decodedPath = Locator.decodeUri(libraryURL.getFile());
                    final File libraryFile = new File(decodedPath);
                    if (!libraryFile.exists() || this.isInPath(libraryFile)) {
                        continue;
                    }
                    this.addPathFile(libraryFile);
                }
            }
        }
    }
    
    public String getClasspath() {
        final StringBuilder sb = new StringBuilder();
        boolean firstPass = true;
        final Enumeration<File> componentEnum = this.pathComponents.elements();
        while (componentEnum.hasMoreElements()) {
            if (!firstPass) {
                sb.append(System.getProperty("path.separator"));
            }
            else {
                firstPass = false;
            }
            sb.append(componentEnum.nextElement().getAbsolutePath());
        }
        return sb.toString();
    }
    
    public synchronized void setIsolated(final boolean isolated) {
        this.ignoreBase = isolated;
    }
    
    public static void initializeClass(final Class<?> theClass) {
        final Constructor<?>[] cons = theClass.getDeclaredConstructors();
        if (cons != null && cons.length > 0 && cons[0] != null) {
            final String[] strs = new String[256];
            try {
                cons[0].newInstance((Object[])strs);
            }
            catch (Exception ex) {}
        }
    }
    
    public void addSystemPackageRoot(final String packageRoot) {
        this.systemPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
    }
    
    public void addLoaderPackageRoot(final String packageRoot) {
        this.loaderPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
    }
    
    public Class<?> forceLoadClass(final String classname) throws ClassNotFoundException {
        this.log("force loading " + classname, 4);
        Class<?> theClass = this.findLoadedClass(classname);
        if (theClass == null) {
            theClass = this.findClass(classname);
        }
        return theClass;
    }
    
    public Class<?> forceLoadSystemClass(final String classname) throws ClassNotFoundException {
        this.log("force system loading " + classname, 4);
        Class<?> theClass = this.findLoadedClass(classname);
        if (theClass == null) {
            theClass = this.findBaseClass(classname);
        }
        return theClass;
    }
    
    public InputStream getResourceAsStream(final String name) {
        InputStream resourceStream = null;
        if (this.isParentFirst(name)) {
            resourceStream = this.loadBaseResource(name);
        }
        if (resourceStream != null) {
            this.log("ResourceStream for " + name + " loaded from parent loader", 4);
        }
        else {
            resourceStream = this.loadResource(name);
            if (resourceStream != null) {
                this.log("ResourceStream for " + name + " loaded from ant loader", 4);
            }
        }
        if (resourceStream == null && !this.isParentFirst(name)) {
            if (this.ignoreBase) {
                resourceStream = ((this.getRootLoader() == null) ? null : this.getRootLoader().getResourceAsStream(name));
            }
            else {
                resourceStream = this.loadBaseResource(name);
            }
            if (resourceStream != null) {
                this.log("ResourceStream for " + name + " loaded from parent loader", 4);
            }
        }
        if (resourceStream == null) {
            this.log("Couldn't load ResourceStream for " + name, 4);
        }
        return resourceStream;
    }
    
    private InputStream loadResource(final String name) {
        InputStream stream = null;
        File pathComponent;
        for (Enumeration<File> e = this.pathComponents.elements(); e.hasMoreElements() && stream == null; stream = this.getResourceStream(pathComponent, name)) {
            pathComponent = e.nextElement();
        }
        return stream;
    }
    
    private InputStream loadBaseResource(final String name) {
        return (this.parent == null) ? super.getResourceAsStream(name) : this.parent.getResourceAsStream(name);
    }
    
    private InputStream getResourceStream(final File file, final String resourceName) {
        try {
            JarFile jarFile = this.jarFiles.get(file);
            if (jarFile == null && file.isDirectory()) {
                final File resource = new File(file, resourceName);
                if (resource.exists()) {
                    return new FileInputStream(resource);
                }
            }
            else {
                if (jarFile == null) {
                    if (!file.exists()) {
                        return null;
                    }
                    jarFile = new JarFile(file);
                    this.jarFiles.put(file, jarFile);
                    jarFile = this.jarFiles.get(file);
                }
                final JarEntry entry = jarFile.getJarEntry(resourceName);
                if (entry != null) {
                    return jarFile.getInputStream(entry);
                }
            }
        }
        catch (Exception e) {
            this.log("Ignoring Exception " + e.getClass().getName() + ": " + e.getMessage() + " reading resource " + resourceName + " from " + file, 3);
        }
        return null;
    }
    
    private boolean isParentFirst(final String resourceName) {
        boolean useParentFirst = this.parentFirst;
        Enumeration<String> e = this.systemPackages.elements();
        while (e.hasMoreElements()) {
            final String packageName = e.nextElement();
            if (resourceName.startsWith(packageName)) {
                useParentFirst = true;
                break;
            }
        }
        e = this.loaderPackages.elements();
        while (e.hasMoreElements()) {
            final String packageName = e.nextElement();
            if (resourceName.startsWith(packageName)) {
                useParentFirst = false;
                break;
            }
        }
        return useParentFirst;
    }
    
    private ClassLoader getRootLoader() {
        ClassLoader ret;
        for (ret = this.getClass().getClassLoader(); ret != null && ret.getParent() != null; ret = ret.getParent()) {}
        return ret;
    }
    
    public URL getResource(final String name) {
        URL url = null;
        if (this.isParentFirst(name)) {
            url = ((this.parent == null) ? super.getResource(name) : this.parent.getResource(name));
        }
        if (url != null) {
            this.log("Resource " + name + " loaded from parent loader", 4);
        }
        else {
            final Enumeration<File> e = this.pathComponents.elements();
            while (e.hasMoreElements() && url == null) {
                final File pathComponent = e.nextElement();
                url = this.getResourceURL(pathComponent, name);
                if (url != null) {
                    this.log("Resource " + name + " loaded from ant loader", 4);
                }
            }
        }
        if (url == null && !this.isParentFirst(name)) {
            if (this.ignoreBase) {
                url = ((this.getRootLoader() == null) ? null : this.getRootLoader().getResource(name));
            }
            else {
                url = ((this.parent == null) ? super.getResource(name) : this.parent.getResource(name));
            }
            if (url != null) {
                this.log("Resource " + name + " loaded from parent loader", 4);
            }
        }
        if (url == null) {
            this.log("Couldn't load Resource " + name, 4);
        }
        return url;
    }
    
    public Enumeration<URL> getNamedResources(final String name) throws IOException {
        return this.findResources(name, false);
    }
    
    protected Enumeration<URL> findResources(final String name) throws IOException {
        return this.findResources(name, true);
    }
    
    protected Enumeration<URL> findResources(final String name, final boolean parentHasBeenSearched) throws IOException {
        final Enumeration<URL> mine = new ResourceEnumeration(name);
        Enumeration<URL> base;
        if (this.parent != null && (!parentHasBeenSearched || this.parent != this.getParent())) {
            base = this.parent.getResources(name);
        }
        else {
            base = new CollectionUtils.EmptyEnumeration<URL>();
        }
        if (this.isParentFirst(name)) {
            return CollectionUtils.append(base, mine);
        }
        if (this.ignoreBase) {
            return (this.getRootLoader() == null) ? mine : CollectionUtils.append(mine, this.getRootLoader().getResources(name));
        }
        return CollectionUtils.append(mine, base);
    }
    
    protected URL getResourceURL(final File file, final String resourceName) {
        try {
            JarFile jarFile = this.jarFiles.get(file);
            if (jarFile == null && file.isDirectory()) {
                final File resource = new File(file, resourceName);
                if (resource.exists()) {
                    try {
                        return AntClassLoader.FILE_UTILS.getFileURL(resource);
                    }
                    catch (MalformedURLException ex) {
                        return null;
                    }
                }
            }
            else {
                if (jarFile == null) {
                    if (!file.exists()) {
                        return null;
                    }
                    jarFile = new JarFile(file);
                    this.jarFiles.put(file, jarFile);
                    jarFile = this.jarFiles.get(file);
                }
                final JarEntry entry = jarFile.getJarEntry(resourceName);
                if (entry != null) {
                    try {
                        return new URL("jar:" + AntClassLoader.FILE_UTILS.getFileURL(file) + "!/" + entry);
                    }
                    catch (MalformedURLException ex) {
                        return null;
                    }
                }
            }
        }
        catch (Exception e) {
            final String msg = "Unable to obtain resource from " + file + ": ";
            this.log(msg + e, 1);
            System.err.println(msg);
            e.printStackTrace();
        }
        return null;
    }
    
    protected synchronized Class<?> loadClass(final String classname, final boolean resolve) throws ClassNotFoundException {
        Class<?> theClass = this.findLoadedClass(classname);
        if (theClass != null) {
            return theClass;
        }
        if (this.isParentFirst(classname)) {
            try {
                theClass = this.findBaseClass(classname);
                this.log("Class " + classname + " loaded from parent loader " + "(parentFirst)", 4);
            }
            catch (ClassNotFoundException cnfe) {
                theClass = this.findClass(classname);
                this.log("Class " + classname + " loaded from ant loader " + "(parentFirst)", 4);
            }
        }
        else {
            try {
                theClass = this.findClass(classname);
                this.log("Class " + classname + " loaded from ant loader", 4);
            }
            catch (ClassNotFoundException cnfe) {
                if (this.ignoreBase) {
                    throw cnfe;
                }
                theClass = this.findBaseClass(classname);
                this.log("Class " + classname + " loaded from parent loader", 4);
            }
        }
        if (resolve) {
            this.resolveClass(theClass);
        }
        return theClass;
    }
    
    private String getClassFilename(final String classname) {
        return classname.replace('.', '/') + ".class";
    }
    
    protected Class<?> defineClassFromData(final File container, final byte[] classData, final String classname) throws IOException {
        this.definePackage(container, classname);
        final ProtectionDomain currentPd = Project.class.getProtectionDomain();
        final String classResource = this.getClassFilename(classname);
        final CodeSource src = new CodeSource(AntClassLoader.FILE_UTILS.getFileURL(container), this.getCertificates(container, classResource));
        final ProtectionDomain classesPd = new ProtectionDomain(src, currentPd.getPermissions(), this, currentPd.getPrincipals());
        return this.defineClass(classname, classData, 0, classData.length, classesPd);
    }
    
    protected void definePackage(final File container, final String className) throws IOException {
        final int classIndex = className.lastIndexOf(46);
        if (classIndex == -1) {
            return;
        }
        final String packageName = className.substring(0, classIndex);
        if (this.getPackage(packageName) != null) {
            return;
        }
        final Manifest manifest = this.getJarManifest(container);
        if (manifest == null) {
            this.definePackage(packageName, null, null, null, null, null, null, null);
        }
        else {
            this.definePackage(container, packageName, manifest);
        }
    }
    
    private Manifest getJarManifest(final File container) throws IOException {
        if (container.isDirectory()) {
            return null;
        }
        final JarFile jarFile = this.jarFiles.get(container);
        if (jarFile == null) {
            return null;
        }
        return jarFile.getManifest();
    }
    
    private Certificate[] getCertificates(final File container, final String entry) throws IOException {
        if (container.isDirectory()) {
            return null;
        }
        final JarFile jarFile = this.jarFiles.get(container);
        if (jarFile == null) {
            return null;
        }
        final JarEntry ent = jarFile.getJarEntry(entry);
        return (Certificate[])((ent == null) ? null : ent.getCertificates());
    }
    
    protected void definePackage(final File container, final String packageName, final Manifest manifest) {
        final String sectionName = packageName.replace('.', '/') + "/";
        String specificationTitle = null;
        String specificationVendor = null;
        String specificationVersion = null;
        String implementationTitle = null;
        String implementationVendor = null;
        String implementationVersion = null;
        String sealedString = null;
        URL sealBase = null;
        final Attributes sectionAttributes = manifest.getAttributes(sectionName);
        if (sectionAttributes != null) {
            specificationTitle = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            specificationVendor = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            specificationVersion = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            implementationTitle = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            implementationVendor = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            implementationVersion = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            sealedString = sectionAttributes.getValue(Attributes.Name.SEALED);
        }
        final Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes != null) {
            if (specificationTitle == null) {
                specificationTitle = mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            }
            if (specificationVendor == null) {
                specificationVendor = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            }
            if (specificationVersion == null) {
                specificationVersion = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            }
            if (implementationTitle == null) {
                implementationTitle = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            }
            if (implementationVendor == null) {
                implementationVendor = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            }
            if (implementationVersion == null) {
                implementationVersion = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            }
            if (sealedString == null) {
                sealedString = mainAttributes.getValue(Attributes.Name.SEALED);
            }
        }
        if (sealedString != null && sealedString.equalsIgnoreCase("true")) {
            try {
                sealBase = new URL(FileUtils.getFileUtils().toURI(container.getAbsolutePath()));
            }
            catch (MalformedURLException ex) {}
        }
        this.definePackage(packageName, specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, sealBase);
    }
    
    private Class<?> getClassFromStream(final InputStream stream, final String classname, final File container) throws IOException, SecurityException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead = -1;
        final byte[] buffer = new byte[8192];
        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        final byte[] classData = baos.toByteArray();
        return this.defineClassFromData(container, classData, classname);
    }
    
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        this.log("Finding class " + name, 4);
        return this.findClassInComponents(name);
    }
    
    protected boolean isInPath(final File component) {
        return this.pathComponents.contains(component);
    }
    
    private Class<?> findClassInComponents(final String name) throws ClassNotFoundException {
        final String classFilename = this.getClassFilename(name);
        final Enumeration<File> e = this.pathComponents.elements();
        while (e.hasMoreElements()) {
            final File pathComponent = e.nextElement();
            InputStream stream = null;
            try {
                stream = this.getResourceStream(pathComponent, classFilename);
                if (stream != null) {
                    this.log("Loaded from " + pathComponent + " " + classFilename, 4);
                    return this.getClassFromStream(stream, name, pathComponent);
                }
                continue;
            }
            catch (SecurityException se) {
                throw se;
            }
            catch (IOException ioe) {
                this.log("Exception reading component " + pathComponent + " (reason: " + ioe.getMessage() + ")", 3);
            }
            finally {
                FileUtils.close(stream);
            }
        }
        throw new ClassNotFoundException(name);
    }
    
    private Class<?> findBaseClass(final String name) throws ClassNotFoundException {
        return (this.parent == null) ? this.findSystemClass(name) : this.parent.loadClass(name);
    }
    
    public synchronized void cleanup() {
        final Enumeration<JarFile> e = this.jarFiles.elements();
        while (e.hasMoreElements()) {
            final JarFile jarFile = e.nextElement();
            try {
                jarFile.close();
            }
            catch (IOException ex) {}
        }
        this.jarFiles = new Hashtable<File, JarFile>();
        if (this.project != null) {
            this.project.removeBuildListener(this);
        }
        this.project = null;
    }
    
    public ClassLoader getConfiguredParent() {
        return this.parent;
    }
    
    public void buildStarted(final BuildEvent event) {
    }
    
    public void buildFinished(final BuildEvent event) {
        this.cleanup();
    }
    
    public void subBuildFinished(final BuildEvent event) {
        if (event.getProject() == this.project) {
            this.cleanup();
        }
    }
    
    public void subBuildStarted(final BuildEvent event) {
    }
    
    public void targetStarted(final BuildEvent event) {
    }
    
    public void targetFinished(final BuildEvent event) {
    }
    
    public void taskStarted(final BuildEvent event) {
    }
    
    public void taskFinished(final BuildEvent event) {
    }
    
    public void messageLogged(final BuildEvent event) {
    }
    
    public void addJavaLibraries() {
        final Vector<String> packages = JavaEnvUtils.getJrePackages();
        final Enumeration<String> e = packages.elements();
        while (e.hasMoreElements()) {
            final String packageName = e.nextElement();
            this.addSystemPackageRoot(packageName);
        }
    }
    
    public String toString() {
        return "AntClassLoader[" + this.getClasspath() + "]";
    }
    
    public static AntClassLoader newAntClassLoader(final ClassLoader parent, final Project project, final Path path, final boolean parentFirst) {
        if (AntClassLoader.subClassToLoad != null) {
            return ReflectUtil.newInstance(AntClassLoader.subClassToLoad, AntClassLoader.CONSTRUCTOR_ARGS, new Object[] { parent, project, path, parentFirst });
        }
        return new AntClassLoader(parent, project, path, parentFirst);
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
        AntClassLoader.pathMap = Collections.synchronizedMap(new HashMap<String, String>());
        AntClassLoader.subClassToLoad = null;
        CONSTRUCTOR_ARGS = new Class[] { ClassLoader.class, Project.class, Path.class, Boolean.TYPE };
        if (JavaEnvUtils.isAtLeastJavaVersion("1.5")) {
            try {
                AntClassLoader.subClassToLoad = Class.forName("org.apache.tools.ant.loader.AntClassLoader5");
            }
            catch (ClassNotFoundException ex) {}
        }
    }
    
    private class ResourceEnumeration implements Enumeration<URL>
    {
        private String resourceName;
        private int pathElementsIndex;
        private URL nextResource;
        
        ResourceEnumeration(final String name) {
            super();
            this.resourceName = name;
            this.pathElementsIndex = 0;
            this.findNextResource();
        }
        
        public boolean hasMoreElements() {
            return this.nextResource != null;
        }
        
        public URL nextElement() {
            final URL ret = this.nextResource;
            if (ret == null) {
                throw new NoSuchElementException();
            }
            this.findNextResource();
            return ret;
        }
        
        private void findNextResource() {
            URL url = null;
            while (this.pathElementsIndex < AntClassLoader.this.pathComponents.size() && url == null) {
                try {
                    final File pathComponent = AntClassLoader.this.pathComponents.elementAt(this.pathElementsIndex);
                    url = AntClassLoader.this.getResourceURL(pathComponent, this.resourceName);
                    ++this.pathElementsIndex;
                }
                catch (BuildException e) {}
            }
            this.nextResource = url;
        }
    }
}
