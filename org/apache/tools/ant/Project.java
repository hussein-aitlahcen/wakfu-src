package org.apache.tools.ant;

import org.apache.tools.ant.input.*;
import org.apache.tools.ant.launch.*;
import org.apache.tools.ant.helper.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.resources.*;

public class Project implements ResourceFactory
{
    public static final int MSG_ERR = 0;
    public static final int MSG_WARN = 1;
    public static final int MSG_INFO = 2;
    public static final int MSG_VERBOSE = 3;
    public static final int MSG_DEBUG = 4;
    private static final String VISITING = "VISITING";
    private static final String VISITED = "VISITED";
    public static final String JAVA_1_0 = "1.0";
    public static final String JAVA_1_1 = "1.1";
    public static final String JAVA_1_2 = "1.2";
    public static final String JAVA_1_3 = "1.3";
    public static final String JAVA_1_4 = "1.4";
    public static final String TOKEN_START = "@";
    public static final String TOKEN_END = "@";
    private static final FileUtils FILE_UTILS;
    private String name;
    private String description;
    private Hashtable<String, Object> references;
    private HashMap<String, Object> idReferences;
    private String defaultTarget;
    private Hashtable<String, Target> targets;
    private FilterSet globalFilterSet;
    private FilterSetCollection globalFilters;
    private File baseDir;
    private final Object listenersLock;
    private volatile BuildListener[] listeners;
    private final ThreadLocal<Boolean> isLoggingMessage;
    private ClassLoader coreLoader;
    private final Map<Thread, Task> threadTasks;
    private final Map<ThreadGroup, Task> threadGroupTasks;
    private InputHandler inputHandler;
    private InputStream defaultInputStream;
    private boolean keepGoingMode;
    
    public void setInputHandler(final InputHandler handler) {
        this.inputHandler = handler;
    }
    
    public void setDefaultInputStream(final InputStream defaultInputStream) {
        this.defaultInputStream = defaultInputStream;
    }
    
    public InputStream getDefaultInputStream() {
        return this.defaultInputStream;
    }
    
    public InputHandler getInputHandler() {
        return this.inputHandler;
    }
    
    public Project() {
        super();
        this.references = new AntRefTable();
        this.idReferences = new HashMap<String, Object>();
        this.targets = new Hashtable<String, Target>();
        (this.globalFilterSet = new FilterSet()).setProject(this);
        this.globalFilters = new FilterSetCollection(this.globalFilterSet);
        this.listenersLock = new Object();
        this.listeners = new BuildListener[0];
        this.isLoggingMessage = new ThreadLocal<Boolean>() {
            protected Boolean initialValue() {
                return Boolean.FALSE;
            }
        };
        this.coreLoader = null;
        this.threadTasks = Collections.synchronizedMap(new WeakHashMap<Thread, Task>());
        this.threadGroupTasks = Collections.synchronizedMap(new WeakHashMap<ThreadGroup, Task>());
        this.inputHandler = null;
        this.defaultInputStream = null;
        this.keepGoingMode = false;
        this.inputHandler = new DefaultInputHandler();
    }
    
    public Project createSubProject() {
        Project subProject = null;
        try {
            subProject = (Project)this.getClass().newInstance();
        }
        catch (Exception e) {
            subProject = new Project();
        }
        this.initSubProject(subProject);
        return subProject;
    }
    
    public void initSubProject(final Project subProject) {
        ComponentHelper.getComponentHelper(subProject).initSubProject(ComponentHelper.getComponentHelper(this));
        subProject.setDefaultInputStream(this.getDefaultInputStream());
        subProject.setKeepGoingMode(this.isKeepGoingMode());
        subProject.setExecutor(this.getExecutor().getSubProjectExecutor());
    }
    
    public void init() throws BuildException {
        this.initProperties();
        ComponentHelper.getComponentHelper(this).initDefaultDefinitions();
    }
    
    public void initProperties() throws BuildException {
        this.setJavaVersionProperty();
        this.setSystemProperties();
        this.setPropertyInternal("ant.version", Main.getAntVersion());
        this.setAntLib();
    }
    
    private void setAntLib() {
        final File antlib = Locator.getClassSource(Project.class);
        if (antlib != null) {
            this.setPropertyInternal("ant.core.lib", antlib.getAbsolutePath());
        }
    }
    
    public AntClassLoader createClassLoader(final Path path) {
        return AntClassLoader.newAntClassLoader(this.getClass().getClassLoader(), this, path, true);
    }
    
    public AntClassLoader createClassLoader(final ClassLoader parent, final Path path) {
        return AntClassLoader.newAntClassLoader(parent, this, path, true);
    }
    
    public void setCoreLoader(final ClassLoader coreLoader) {
        this.coreLoader = coreLoader;
    }
    
    public ClassLoader getCoreLoader() {
        return this.coreLoader;
    }
    
    public void addBuildListener(final BuildListener listener) {
        synchronized (this.listenersLock) {
            for (int i = 0; i < this.listeners.length; ++i) {
                if (this.listeners[i] == listener) {
                    return;
                }
            }
            final BuildListener[] newListeners = new BuildListener[this.listeners.length + 1];
            System.arraycopy(this.listeners, 0, newListeners, 0, this.listeners.length);
            newListeners[this.listeners.length] = listener;
            this.listeners = newListeners;
        }
    }
    
    public void removeBuildListener(final BuildListener listener) {
        synchronized (this.listenersLock) {
            for (int i = 0; i < this.listeners.length; ++i) {
                if (this.listeners[i] == listener) {
                    final BuildListener[] newListeners = new BuildListener[this.listeners.length - 1];
                    System.arraycopy(this.listeners, 0, newListeners, 0, i);
                    System.arraycopy(this.listeners, i + 1, newListeners, i, this.listeners.length - i - 1);
                    this.listeners = newListeners;
                    break;
                }
            }
        }
    }
    
    public Vector<BuildListener> getBuildListeners() {
        synchronized (this.listenersLock) {
            final Vector<BuildListener> r = new Vector<BuildListener>(this.listeners.length);
            for (int i = 0; i < this.listeners.length; ++i) {
                r.add(this.listeners[i]);
            }
            return r;
        }
    }
    
    public void log(final String message) {
        this.log(message, 2);
    }
    
    public void log(final String message, final int msgLevel) {
        this.log(message, null, msgLevel);
    }
    
    public void log(final String message, final Throwable throwable, final int msgLevel) {
        this.fireMessageLogged(this, message, throwable, msgLevel);
    }
    
    public void log(final Task task, final String message, final int msgLevel) {
        this.fireMessageLogged(task, message, null, msgLevel);
    }
    
    public void log(final Task task, final String message, final Throwable throwable, final int msgLevel) {
        this.fireMessageLogged(task, message, throwable, msgLevel);
    }
    
    public void log(final Target target, final String message, final int msgLevel) {
        this.log(target, message, null, msgLevel);
    }
    
    public void log(final Target target, final String message, final Throwable throwable, final int msgLevel) {
        this.fireMessageLogged(target, message, throwable, msgLevel);
    }
    
    public FilterSet getGlobalFilterSet() {
        return this.globalFilterSet;
    }
    
    public void setProperty(final String name, final String value) {
        PropertyHelper.getPropertyHelper(this).setProperty(name, value, true);
    }
    
    public void setNewProperty(final String name, final String value) {
        PropertyHelper.getPropertyHelper(this).setNewProperty(name, value);
    }
    
    public void setUserProperty(final String name, final String value) {
        PropertyHelper.getPropertyHelper(this).setUserProperty(name, value);
    }
    
    public void setInheritedProperty(final String name, final String value) {
        PropertyHelper.getPropertyHelper(this).setInheritedProperty(name, value);
    }
    
    private void setPropertyInternal(final String name, final String value) {
        PropertyHelper.getPropertyHelper(this).setProperty(name, value, false);
    }
    
    public String getProperty(final String propertyName) {
        final Object value = PropertyHelper.getPropertyHelper(this).getProperty(propertyName);
        return (value == null) ? null : String.valueOf(value);
    }
    
    public String replaceProperties(final String value) throws BuildException {
        return PropertyHelper.getPropertyHelper(this).replaceProperties(null, value, null);
    }
    
    public String getUserProperty(final String propertyName) {
        return (String)PropertyHelper.getPropertyHelper(this).getUserProperty(propertyName);
    }
    
    public Hashtable<String, Object> getProperties() {
        return PropertyHelper.getPropertyHelper(this).getProperties();
    }
    
    public Hashtable<String, Object> getUserProperties() {
        return PropertyHelper.getPropertyHelper(this).getUserProperties();
    }
    
    public Hashtable<String, Object> getInheritedProperties() {
        return PropertyHelper.getPropertyHelper(this).getInheritedProperties();
    }
    
    public void copyUserProperties(final Project other) {
        PropertyHelper.getPropertyHelper(this).copyUserProperties(other);
    }
    
    public void copyInheritedProperties(final Project other) {
        PropertyHelper.getPropertyHelper(this).copyInheritedProperties(other);
    }
    
    public void setDefaultTarget(final String defaultTarget) {
        this.setDefault(defaultTarget);
    }
    
    public String getDefaultTarget() {
        return this.defaultTarget;
    }
    
    public void setDefault(final String defaultTarget) {
        if (defaultTarget != null) {
            this.setUserProperty("ant.project.default-target", defaultTarget);
        }
        this.defaultTarget = defaultTarget;
    }
    
    public void setName(final String name) {
        this.setUserProperty("ant.project.name", name);
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDescription() {
        if (this.description == null) {
            this.description = Description.getDescription(this);
        }
        return this.description;
    }
    
    public void addFilter(final String token, final String value) {
        if (token == null) {
            return;
        }
        this.globalFilterSet.addFilter(new FilterSet.Filter(token, value));
    }
    
    public Hashtable<String, String> getFilters() {
        return this.globalFilterSet.getFilterHash();
    }
    
    public void setBasedir(final String baseD) throws BuildException {
        this.setBaseDir(new File(baseD));
    }
    
    public void setBaseDir(File baseDir) throws BuildException {
        baseDir = Project.FILE_UTILS.normalize(baseDir.getAbsolutePath());
        if (!baseDir.exists()) {
            throw new BuildException("Basedir " + baseDir.getAbsolutePath() + " does not exist");
        }
        if (!baseDir.isDirectory()) {
            throw new BuildException("Basedir " + baseDir.getAbsolutePath() + " is not a directory");
        }
        this.baseDir = baseDir;
        this.setPropertyInternal("basedir", this.baseDir.getPath());
        final String msg = "Project base dir set to: " + this.baseDir;
        this.log(msg, 3);
    }
    
    public File getBaseDir() {
        if (this.baseDir == null) {
            try {
                this.setBasedir(".");
            }
            catch (BuildException ex) {
                ex.printStackTrace();
            }
        }
        return this.baseDir;
    }
    
    public void setKeepGoingMode(final boolean keepGoingMode) {
        this.keepGoingMode = keepGoingMode;
    }
    
    public boolean isKeepGoingMode() {
        return this.keepGoingMode;
    }
    
    public static String getJavaVersion() {
        return JavaEnvUtils.getJavaVersion();
    }
    
    public void setJavaVersionProperty() throws BuildException {
        final String javaVersion = JavaEnvUtils.getJavaVersion();
        this.setPropertyInternal("ant.java.version", javaVersion);
        if (!JavaEnvUtils.isAtLeastJavaVersion("1.4")) {
            throw new BuildException("Ant cannot work on Java prior to 1.4");
        }
        this.log("Detected Java version: " + javaVersion + " in: " + System.getProperty("java.home"), 3);
        this.log("Detected OS: " + System.getProperty("os.name"), 3);
    }
    
    public void setSystemProperties() {
        final Properties systemP = System.getProperties();
        final Enumeration<?> e = systemP.propertyNames();
        while (e.hasMoreElements()) {
            final String propertyName = (String)e.nextElement();
            final String value = systemP.getProperty(propertyName);
            if (value != null) {
                this.setPropertyInternal(propertyName, value);
            }
        }
    }
    
    public void addTaskDefinition(final String taskName, final Class<?> taskClass) throws BuildException {
        ComponentHelper.getComponentHelper(this).addTaskDefinition(taskName, taskClass);
    }
    
    public void checkTaskClass(final Class<?> taskClass) throws BuildException {
        ComponentHelper.getComponentHelper(this).checkTaskClass(taskClass);
        if (!Modifier.isPublic(taskClass.getModifiers())) {
            final String message = taskClass + " is not public";
            this.log(message, 0);
            throw new BuildException(message);
        }
        if (Modifier.isAbstract(taskClass.getModifiers())) {
            final String message = taskClass + " is abstract";
            this.log(message, 0);
            throw new BuildException(message);
        }
        try {
            taskClass.getConstructor((Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException e2) {
            final String message2 = "No public no-arg constructor in " + taskClass;
            this.log(message2, 0);
            throw new BuildException(message2);
        }
        catch (LinkageError e) {
            final String message2 = "Could not load " + taskClass + ": " + e;
            this.log(message2, 0);
            throw new BuildException(message2, e);
        }
        if (!Task.class.isAssignableFrom(taskClass)) {
            TaskAdapter.checkTaskClass(taskClass, this);
        }
    }
    
    public Hashtable<String, Class<?>> getTaskDefinitions() {
        return ComponentHelper.getComponentHelper(this).getTaskDefinitions();
    }
    
    public Map<String, Class<?>> getCopyOfTaskDefinitions() {
        return new HashMap<String, Class<?>>(this.getTaskDefinitions());
    }
    
    public void addDataTypeDefinition(final String typeName, final Class<?> typeClass) {
        ComponentHelper.getComponentHelper(this).addDataTypeDefinition(typeName, typeClass);
    }
    
    public Hashtable<String, Class<?>> getDataTypeDefinitions() {
        return ComponentHelper.getComponentHelper(this).getDataTypeDefinitions();
    }
    
    public Map<String, Class<?>> getCopyOfDataTypeDefinitions() {
        return new HashMap<String, Class<?>>(this.getDataTypeDefinitions());
    }
    
    public void addTarget(final Target target) throws BuildException {
        this.addTarget(target.getName(), target);
    }
    
    public void addTarget(final String targetName, final Target target) throws BuildException {
        if (this.targets.get(targetName) != null) {
            throw new BuildException("Duplicate target: `" + targetName + "'");
        }
        this.addOrReplaceTarget(targetName, target);
    }
    
    public void addOrReplaceTarget(final Target target) {
        this.addOrReplaceTarget(target.getName(), target);
    }
    
    public void addOrReplaceTarget(final String targetName, final Target target) {
        final String msg = " +Target: " + targetName;
        this.log(msg, 4);
        target.setProject(this);
        this.targets.put(targetName, target);
    }
    
    public Hashtable<String, Target> getTargets() {
        return this.targets;
    }
    
    public Map<String, Target> getCopyOfTargets() {
        return new HashMap<String, Target>(this.targets);
    }
    
    public Task createTask(final String taskType) throws BuildException {
        return ComponentHelper.getComponentHelper(this).createTask(taskType);
    }
    
    public Object createDataType(final String typeName) throws BuildException {
        return ComponentHelper.getComponentHelper(this).createDataType(typeName);
    }
    
    public void setExecutor(final Executor e) {
        this.addReference("ant.executor", e);
    }
    
    public Executor getExecutor() {
        Object o = this.getReference("ant.executor");
        if (o == null) {
            String classname = this.getProperty("ant.executor.class");
            if (classname == null) {
                classname = DefaultExecutor.class.getName();
            }
            this.log("Attempting to create object of type " + classname, 4);
            try {
                o = Class.forName(classname, true, this.coreLoader).newInstance();
            }
            catch (ClassNotFoundException seaEnEfEx) {
                try {
                    o = Class.forName(classname).newInstance();
                }
                catch (Exception ex) {
                    this.log(ex.toString(), 0);
                }
            }
            catch (Exception ex2) {
                this.log(ex2.toString(), 0);
            }
            if (o == null) {
                throw new BuildException("Unable to obtain a Target Executor instance.");
            }
            this.setExecutor((Executor)o);
        }
        return (Executor)o;
    }
    
    public void executeTargets(final Vector<String> names) throws BuildException {
        this.setUserProperty("ant.project.invoked-targets", CollectionUtils.flattenToString(names));
        this.getExecutor().executeTargets(this, names.toArray(new String[names.size()]));
    }
    
    public void demuxOutput(final String output, final boolean isWarning) {
        final Task task = this.getThreadTask(Thread.currentThread());
        if (task == null) {
            this.log(output, isWarning ? 1 : 2);
        }
        else if (isWarning) {
            task.handleErrorOutput(output);
        }
        else {
            task.handleOutput(output);
        }
    }
    
    public int defaultInput(final byte[] buffer, final int offset, final int length) throws IOException {
        if (this.defaultInputStream != null) {
            System.out.flush();
            return this.defaultInputStream.read(buffer, offset, length);
        }
        throw new EOFException("No input provided for project");
    }
    
    public int demuxInput(final byte[] buffer, final int offset, final int length) throws IOException {
        final Task task = this.getThreadTask(Thread.currentThread());
        if (task == null) {
            return this.defaultInput(buffer, offset, length);
        }
        return task.handleInput(buffer, offset, length);
    }
    
    public void demuxFlush(final String output, final boolean isError) {
        final Task task = this.getThreadTask(Thread.currentThread());
        if (task == null) {
            this.fireMessageLogged(this, output, isError ? 0 : 2);
        }
        else if (isError) {
            task.handleErrorFlush(output);
        }
        else {
            task.handleFlush(output);
        }
    }
    
    public void executeTarget(final String targetName) throws BuildException {
        if (targetName == null) {
            final String msg = "No target specified";
            throw new BuildException(msg);
        }
        this.executeSortedTargets(this.topoSort(targetName, this.targets, false));
    }
    
    public void executeSortedTargets(final Vector<Target> sortedTargets) throws BuildException {
        final Set<String> succeededTargets = new HashSet<String>();
        BuildException buildException = null;
        for (final Target curtarget : sortedTargets) {
            boolean canExecute = true;
            final Enumeration<String> depIter = curtarget.getDependencies();
            while (depIter.hasMoreElements()) {
                final String dependencyName = depIter.nextElement();
                if (!succeededTargets.contains(dependencyName)) {
                    canExecute = false;
                    this.log(curtarget, "Cannot execute '" + curtarget.getName() + "' - '" + dependencyName + "' failed or was not executed.", 0);
                    break;
                }
            }
            if (canExecute) {
                Throwable thrownException = null;
                try {
                    curtarget.performTasks();
                    succeededTargets.add(curtarget.getName());
                }
                catch (RuntimeException ex) {
                    if (!this.keepGoingMode) {
                        throw ex;
                    }
                    thrownException = ex;
                }
                catch (Throwable ex2) {
                    if (!this.keepGoingMode) {
                        throw new BuildException(ex2);
                    }
                    thrownException = ex2;
                }
                if (thrownException == null) {
                    continue;
                }
                if (thrownException instanceof BuildException) {
                    this.log(curtarget, "Target '" + curtarget.getName() + "' failed with message '" + thrownException.getMessage() + "'.", 0);
                    if (buildException != null) {
                        continue;
                    }
                    buildException = (BuildException)thrownException;
                }
                else {
                    this.log(curtarget, "Target '" + curtarget.getName() + "' failed with message '" + thrownException.getMessage() + "'.", 0);
                    thrownException.printStackTrace(System.err);
                    if (buildException != null) {
                        continue;
                    }
                    buildException = new BuildException(thrownException);
                }
            }
        }
        if (buildException != null) {
            throw buildException;
        }
    }
    
    public File resolveFile(final String fileName, final File rootDir) {
        return Project.FILE_UTILS.resolveFile(rootDir, fileName);
    }
    
    public File resolveFile(final String fileName) {
        return Project.FILE_UTILS.resolveFile(this.baseDir, fileName);
    }
    
    public static String translatePath(final String toProcess) {
        return FileUtils.translatePath(toProcess);
    }
    
    public void copyFile(final String sourceFile, final String destFile) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final boolean filtering) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final boolean filtering, final boolean overwrite) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final boolean filtering, final boolean overwrite, final boolean preserveLastModified) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite, preserveLastModified);
    }
    
    public void copyFile(final File sourceFile, final File destFile) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final boolean filtering) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final boolean filtering, final boolean overwrite) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final boolean filtering, final boolean overwrite, final boolean preserveLastModified) throws IOException {
        Project.FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite, preserveLastModified);
    }
    
    public void setFileLastModified(final File file, final long time) throws BuildException {
        Project.FILE_UTILS.setFileLastModified(file, time);
        this.log("Setting modification time for " + file, 3);
    }
    
    public static boolean toBoolean(final String s) {
        return "on".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s);
    }
    
    public static Project getProject(final Object o) {
        if (o instanceof ProjectComponent) {
            return ((ProjectComponent)o).getProject();
        }
        try {
            final Method m = o.getClass().getMethod("getProject", (Class<?>[])null);
            if (Project.class == m.getReturnType()) {
                return (Project)m.invoke(o, (Object[])null);
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    public final Vector<Target> topoSort(final String root, final Hashtable<String, Target> targetTable) throws BuildException {
        return this.topoSort(new String[] { root }, targetTable, true);
    }
    
    public final Vector<Target> topoSort(final String root, final Hashtable<String, Target> targetTable, final boolean returnAll) throws BuildException {
        return this.topoSort(new String[] { root }, targetTable, returnAll);
    }
    
    public final Vector<Target> topoSort(final String[] root, final Hashtable<String, Target> targetTable, final boolean returnAll) throws BuildException {
        final Vector<Target> ret = new VectorSet<Target>();
        final Hashtable<String, String> state = new Hashtable<String, String>();
        final Stack<String> visiting = new Stack<String>();
        for (int i = 0; i < root.length; ++i) {
            final String st = state.get(root[i]);
            if (st == null) {
                this.tsort(root[i], targetTable, state, visiting, ret);
            }
            else if (st == "VISITING") {
                throw new RuntimeException("Unexpected node in visiting state: " + root[i]);
            }
        }
        final StringBuffer buf = new StringBuffer("Build sequence for target(s)");
        for (int j = 0; j < root.length; ++j) {
            buf.append((j == 0) ? " `" : ", `").append(root[j]).append('\'');
        }
        buf.append(" is " + ret);
        this.log(buf.toString(), 3);
        final Vector<Target> complete = returnAll ? ret : new Vector<Target>(ret);
        final Enumeration<String> en = targetTable.keys();
        while (en.hasMoreElements()) {
            final String curTarget = en.nextElement();
            final String st2 = state.get(curTarget);
            if (st2 == null) {
                this.tsort(curTarget, targetTable, state, visiting, complete);
            }
            else {
                if (st2 == "VISITING") {
                    throw new RuntimeException("Unexpected node in visiting state: " + curTarget);
                }
                continue;
            }
        }
        this.log("Complete build sequence is " + complete, 3);
        return ret;
    }
    
    private void tsort(final String root, final Hashtable<String, Target> targetTable, final Hashtable<String, String> state, final Stack<String> visiting, final Vector<Target> ret) throws BuildException {
        state.put(root, "VISITING");
        visiting.push(root);
        final Target target = targetTable.get(root);
        if (target == null) {
            final StringBuilder sb = new StringBuilder("Target \"");
            sb.append(root);
            sb.append("\" does not exist in the project \"");
            sb.append(this.name);
            sb.append("\". ");
            visiting.pop();
            if (!visiting.empty()) {
                final String parent = visiting.peek();
                sb.append("It is used from target \"");
                sb.append(parent);
                sb.append("\".");
            }
            throw new BuildException(new String(sb));
        }
        final Enumeration<String> en = target.getDependencies();
        while (en.hasMoreElements()) {
            final String cur = en.nextElement();
            final String m = state.get(cur);
            if (m == null) {
                this.tsort(cur, targetTable, state, visiting, ret);
            }
            else {
                if (m == "VISITING") {
                    throw makeCircularException(cur, visiting);
                }
                continue;
            }
        }
        final String p = visiting.pop();
        if (root != p) {
            throw new RuntimeException("Unexpected internal error: expected to pop " + root + " but got " + p);
        }
        state.put(root, "VISITED");
        ret.addElement(target);
    }
    
    private static BuildException makeCircularException(final String end, final Stack<String> stk) {
        final StringBuilder sb = new StringBuilder("Circular dependency: ");
        sb.append(end);
        String c;
        do {
            c = stk.pop();
            sb.append(" <- ");
            sb.append(c);
        } while (!c.equals(end));
        return new BuildException(sb.toString());
    }
    
    public void inheritIDReferences(final Project parent) {
    }
    
    public void addIdReference(final String id, final Object value) {
        this.idReferences.put(id, value);
    }
    
    public void addReference(final String referenceName, final Object value) {
        final Object old = ((AntRefTable)this.references).getReal(referenceName);
        if (old == value) {
            return;
        }
        if (old != null && !(old instanceof UnknownElement)) {
            this.log("Overriding previous definition of reference to " + referenceName, 3);
        }
        this.log("Adding reference: " + referenceName, 4);
        this.references.put(referenceName, value);
    }
    
    public Hashtable<String, Object> getReferences() {
        return this.references;
    }
    
    public boolean hasReference(final String key) {
        return this.references.containsKey(key);
    }
    
    public Map<String, Object> getCopyOfReferences() {
        return new HashMap<String, Object>(this.references);
    }
    
    public <T> T getReference(final String key) {
        final T ret = (T)this.references.get(key);
        if (ret != null) {
            return ret;
        }
        if (!key.equals("ant.PropertyHelper")) {
            try {
                if (PropertyHelper.getPropertyHelper(this).containsProperties(key)) {
                    this.log("Unresolvable reference " + key + " might be a misuse of property expansion syntax.", 1);
                }
            }
            catch (Exception ex) {}
        }
        return null;
    }
    
    public String getElementName(final Object element) {
        return ComponentHelper.getComponentHelper(this).getElementName(element);
    }
    
    public void fireBuildStarted() {
        final BuildEvent event = new BuildEvent(this);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            currListeners[i].buildStarted(event);
        }
    }
    
    public void fireBuildFinished(final Throwable exception) {
        final BuildEvent event = new BuildEvent(this);
        event.setException(exception);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            currListeners[i].buildFinished(event);
        }
        IntrospectionHelper.clearCache();
    }
    
    public void fireSubBuildStarted() {
        final BuildEvent event = new BuildEvent(this);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            if (currListeners[i] instanceof SubBuildListener) {
                ((SubBuildListener)currListeners[i]).subBuildStarted(event);
            }
        }
    }
    
    public void fireSubBuildFinished(final Throwable exception) {
        final BuildEvent event = new BuildEvent(this);
        event.setException(exception);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            if (currListeners[i] instanceof SubBuildListener) {
                ((SubBuildListener)currListeners[i]).subBuildFinished(event);
            }
        }
    }
    
    protected void fireTargetStarted(final Target target) {
        final BuildEvent event = new BuildEvent(target);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            currListeners[i].targetStarted(event);
        }
    }
    
    protected void fireTargetFinished(final Target target, final Throwable exception) {
        final BuildEvent event = new BuildEvent(target);
        event.setException(exception);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            currListeners[i].targetFinished(event);
        }
    }
    
    protected void fireTaskStarted(final Task task) {
        this.registerThreadTask(Thread.currentThread(), task);
        final BuildEvent event = new BuildEvent(task);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            currListeners[i].taskStarted(event);
        }
    }
    
    protected void fireTaskFinished(final Task task, final Throwable exception) {
        this.registerThreadTask(Thread.currentThread(), null);
        System.out.flush();
        System.err.flush();
        final BuildEvent event = new BuildEvent(task);
        event.setException(exception);
        final BuildListener[] currListeners = this.listeners;
        for (int i = 0; i < currListeners.length; ++i) {
            currListeners[i].taskFinished(event);
        }
    }
    
    private void fireMessageLoggedEvent(final BuildEvent event, String message, final int priority) {
        if (message == null) {
            message = String.valueOf(message);
        }
        if (message.endsWith(StringUtils.LINE_SEP)) {
            final int endIndex = message.length() - StringUtils.LINE_SEP.length();
            event.setMessage(message.substring(0, endIndex), priority);
        }
        else {
            event.setMessage(message, priority);
        }
        if (this.isLoggingMessage.get() != Boolean.FALSE) {
            return;
        }
        try {
            this.isLoggingMessage.set(Boolean.TRUE);
            final BuildListener[] currListeners = this.listeners;
            for (int i = 0; i < currListeners.length; ++i) {
                currListeners[i].messageLogged(event);
            }
        }
        finally {
            this.isLoggingMessage.set(Boolean.FALSE);
        }
    }
    
    protected void fireMessageLogged(final Project project, final String message, final int priority) {
        this.fireMessageLogged(project, message, null, priority);
    }
    
    protected void fireMessageLogged(final Project project, final String message, final Throwable throwable, final int priority) {
        final BuildEvent event = new BuildEvent(project);
        event.setException(throwable);
        this.fireMessageLoggedEvent(event, message, priority);
    }
    
    protected void fireMessageLogged(final Target target, final String message, final int priority) {
        this.fireMessageLogged(target, message, null, priority);
    }
    
    protected void fireMessageLogged(final Target target, final String message, final Throwable throwable, final int priority) {
        final BuildEvent event = new BuildEvent(target);
        event.setException(throwable);
        this.fireMessageLoggedEvent(event, message, priority);
    }
    
    protected void fireMessageLogged(final Task task, final String message, final int priority) {
        this.fireMessageLogged(task, message, null, priority);
    }
    
    protected void fireMessageLogged(final Task task, final String message, final Throwable throwable, final int priority) {
        final BuildEvent event = new BuildEvent(task);
        event.setException(throwable);
        this.fireMessageLoggedEvent(event, message, priority);
    }
    
    public void registerThreadTask(final Thread thread, final Task task) {
        synchronized (this.threadTasks) {
            if (task != null) {
                this.threadTasks.put(thread, task);
                this.threadGroupTasks.put(thread.getThreadGroup(), task);
            }
            else {
                this.threadTasks.remove(thread);
                this.threadGroupTasks.remove(thread.getThreadGroup());
            }
        }
    }
    
    public Task getThreadTask(final Thread thread) {
        synchronized (this.threadTasks) {
            Task task = this.threadTasks.get(thread);
            if (task == null) {
                for (ThreadGroup group = thread.getThreadGroup(); task == null && group != null; task = this.threadGroupTasks.get(group), group = group.getParent()) {}
            }
            return task;
        }
    }
    
    public final void setProjectReference(final Object obj) {
        if (obj instanceof ProjectComponent) {
            ((ProjectComponent)obj).setProject(this);
            return;
        }
        try {
            final Method method = obj.getClass().getMethod("setProject", Project.class);
            if (method != null) {
                method.invoke(obj, this);
            }
        }
        catch (Throwable t) {}
    }
    
    public Resource getResource(final String name) {
        return new FileResource(this.getBaseDir(), name);
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
    
    private static class AntRefTable extends Hashtable<String, Object>
    {
        private static final long serialVersionUID = 1L;
        
        private Object getReal(final Object key) {
            return super.get(key);
        }
        
        public Object get(final Object key) {
            Object o = this.getReal(key);
            if (o instanceof UnknownElement) {
                final UnknownElement ue = (UnknownElement)o;
                ue.maybeConfigure();
                o = ue.getRealThing();
            }
            return o;
        }
    }
}
