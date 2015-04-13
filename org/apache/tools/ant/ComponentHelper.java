package org.apache.tools.ant;

import java.util.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.launch.*;
import java.io.*;
import java.lang.reflect.*;

public class ComponentHelper
{
    private Map<String, List<AntTypeDefinition>> restrictedDefinitions;
    private final Hashtable<String, AntTypeDefinition> antTypeTable;
    private final Hashtable<String, Class<?>> taskClassDefinitions;
    private boolean rebuildTaskClassDefinitions;
    private final Hashtable<String, Class<?>> typeClassDefinitions;
    private boolean rebuildTypeClassDefinitions;
    private final HashSet<String> checkedNamespaces;
    private Stack<String> antLibStack;
    private String antLibCurrentUri;
    private ComponentHelper next;
    private Project project;
    private static final String ERROR_NO_TASK_LIST_LOAD = "Can't load default task list";
    private static final String ERROR_NO_TYPE_LIST_LOAD = "Can't load default type list";
    public static final String COMPONENT_HELPER_REFERENCE = "ant.ComponentHelper";
    private static final String BUILD_SYSCLASSPATH_ONLY = "only";
    private static final String ANT_PROPERTY_TASK = "property";
    private static Properties[] defaultDefinitions;
    
    public Project getProject() {
        return this.project;
    }
    
    public static ComponentHelper getComponentHelper(final Project project) {
        if (project == null) {
            return null;
        }
        ComponentHelper ph = project.getReference("ant.ComponentHelper");
        if (ph != null) {
            return ph;
        }
        ph = new ComponentHelper();
        ph.setProject(project);
        project.addReference("ant.ComponentHelper", ph);
        return ph;
    }
    
    protected ComponentHelper() {
        super();
        this.restrictedDefinitions = new HashMap<String, List<AntTypeDefinition>>();
        this.antTypeTable = new Hashtable<String, AntTypeDefinition>();
        this.taskClassDefinitions = new Hashtable<String, Class<?>>();
        this.rebuildTaskClassDefinitions = true;
        this.typeClassDefinitions = new Hashtable<String, Class<?>>();
        this.rebuildTypeClassDefinitions = true;
        this.checkedNamespaces = new HashSet<String>();
        this.antLibStack = new Stack<String>();
        this.antLibCurrentUri = null;
    }
    
    public void setNext(final ComponentHelper next) {
        this.next = next;
    }
    
    public ComponentHelper getNext() {
        return this.next;
    }
    
    public void setProject(final Project project) {
        this.project = project;
    }
    
    private synchronized Set<String> getCheckedNamespace() {
        final Set<String> result = (Set<String>)this.checkedNamespaces.clone();
        return result;
    }
    
    private Map<String, List<AntTypeDefinition>> getRestrictedDefinition() {
        final Map<String, List<AntTypeDefinition>> result = new HashMap<String, List<AntTypeDefinition>>();
        synchronized (this.restrictedDefinitions) {
            for (final Map.Entry<String, List<AntTypeDefinition>> entry : this.restrictedDefinitions.entrySet()) {
                List<AntTypeDefinition> entryVal = entry.getValue();
                synchronized (entryVal) {
                    entryVal = new ArrayList<AntTypeDefinition>(entryVal);
                }
                result.put(entry.getKey(), entryVal);
            }
        }
        return result;
    }
    
    public void initSubProject(final ComponentHelper helper) {
        final Hashtable<String, AntTypeDefinition> typeTable = (Hashtable<String, AntTypeDefinition>)helper.antTypeTable.clone();
        synchronized (this.antTypeTable) {
            for (final AntTypeDefinition def : typeTable.values()) {
                this.antTypeTable.put(def.getName(), def);
            }
        }
        final Set<String> inheritedCheckedNamespace = helper.getCheckedNamespace();
        synchronized (this) {
            this.checkedNamespaces.addAll((Collection<?>)inheritedCheckedNamespace);
        }
        final Map<String, List<AntTypeDefinition>> inheritedRestrictedDef = helper.getRestrictedDefinition();
        synchronized (this.restrictedDefinitions) {
            this.restrictedDefinitions.putAll(inheritedRestrictedDef);
        }
    }
    
    public Object createComponent(final UnknownElement ue, final String ns, final String componentType) throws BuildException {
        final Object component = this.createComponent(componentType);
        if (component instanceof Task) {
            final Task task = (Task)component;
            task.setLocation(ue.getLocation());
            task.setTaskType(componentType);
            task.setTaskName(ue.getTaskName());
            task.setOwningTarget(ue.getOwningTarget());
            task.init();
        }
        return component;
    }
    
    public Object createComponent(final String componentName) {
        final AntTypeDefinition def = this.getDefinition(componentName);
        return (def == null) ? null : def.create(this.project);
    }
    
    public Class<?> getComponentClass(final String componentName) {
        final AntTypeDefinition def = this.getDefinition(componentName);
        return (def == null) ? null : def.getExposedClass(this.project);
    }
    
    public AntTypeDefinition getDefinition(final String componentName) {
        this.checkNamespace(componentName);
        return this.antTypeTable.get(componentName);
    }
    
    public void initDefaultDefinitions() {
        this.initTasks();
        this.initTypes();
        new DefaultDefinitions(this).execute();
    }
    
    public void addTaskDefinition(final String taskName, final Class<?> taskClass) {
        this.checkTaskClass(taskClass);
        final AntTypeDefinition def = new AntTypeDefinition();
        def.setName(taskName);
        def.setClassLoader(taskClass.getClassLoader());
        def.setClass(taskClass);
        def.setAdapterClass(TaskAdapter.class);
        def.setClassName(taskClass.getName());
        def.setAdaptToClass(Task.class);
        this.updateDataTypeDefinition(def);
    }
    
    public void checkTaskClass(final Class<?> taskClass) throws BuildException {
        if (!Modifier.isPublic(taskClass.getModifiers())) {
            final String message = taskClass + " is not public";
            this.project.log(message, 0);
            throw new BuildException(message);
        }
        if (Modifier.isAbstract(taskClass.getModifiers())) {
            final String message = taskClass + " is abstract";
            this.project.log(message, 0);
            throw new BuildException(message);
        }
        try {
            taskClass.getConstructor((Class<?>[])null);
        }
        catch (NoSuchMethodException e) {
            final String message2 = "No public no-arg constructor in " + taskClass;
            this.project.log(message2, 0);
            throw new BuildException(message2);
        }
        if (!Task.class.isAssignableFrom(taskClass)) {
            TaskAdapter.checkTaskClass(taskClass, this.project);
        }
    }
    
    public Hashtable<String, Class<?>> getTaskDefinitions() {
        synchronized (this.taskClassDefinitions) {
            synchronized (this.antTypeTable) {
                if (this.rebuildTaskClassDefinitions) {
                    this.taskClassDefinitions.clear();
                    for (final Map.Entry<String, AntTypeDefinition> e : this.antTypeTable.entrySet()) {
                        final Class<?> clazz = e.getValue().getExposedClass(this.project);
                        if (clazz == null) {
                            continue;
                        }
                        if (!Task.class.isAssignableFrom(clazz)) {
                            continue;
                        }
                        this.taskClassDefinitions.put(e.getKey(), e.getValue().getTypeClass(this.project));
                    }
                    this.rebuildTaskClassDefinitions = false;
                }
            }
        }
        return this.taskClassDefinitions;
    }
    
    public Hashtable<String, Class<?>> getDataTypeDefinitions() {
        synchronized (this.typeClassDefinitions) {
            synchronized (this.antTypeTable) {
                if (this.rebuildTypeClassDefinitions) {
                    this.typeClassDefinitions.clear();
                    for (final Map.Entry<String, AntTypeDefinition> e : this.antTypeTable.entrySet()) {
                        final Class<?> clazz = e.getValue().getExposedClass(this.project);
                        if (clazz == null) {
                            continue;
                        }
                        if (Task.class.isAssignableFrom(clazz)) {
                            continue;
                        }
                        this.typeClassDefinitions.put(e.getKey(), e.getValue().getTypeClass(this.project));
                    }
                    this.rebuildTypeClassDefinitions = false;
                }
            }
        }
        return this.typeClassDefinitions;
    }
    
    public List<AntTypeDefinition> getRestrictedDefinitions(final String componentName) {
        synchronized (this.restrictedDefinitions) {
            return this.restrictedDefinitions.get(componentName);
        }
    }
    
    public void addDataTypeDefinition(final String typeName, final Class<?> typeClass) {
        final AntTypeDefinition def = new AntTypeDefinition();
        def.setName(typeName);
        def.setClass(typeClass);
        this.updateDataTypeDefinition(def);
        this.project.log(" +User datatype: " + typeName + "     " + typeClass.getName(), 4);
    }
    
    public void addDataTypeDefinition(final AntTypeDefinition def) {
        if (!def.isRestrict()) {
            this.updateDataTypeDefinition(def);
        }
        else {
            this.updateRestrictedDefinition(def);
        }
    }
    
    public Hashtable<String, AntTypeDefinition> getAntTypeTable() {
        return this.antTypeTable;
    }
    
    public Task createTask(final String taskType) throws BuildException {
        Task task = this.createNewTask(taskType);
        if (task == null && taskType.equals("property")) {
            this.addTaskDefinition("property", Property.class);
            task = this.createNewTask(taskType);
        }
        return task;
    }
    
    private Task createNewTask(final String taskType) throws BuildException {
        final Class<?> c = this.getComponentClass(taskType);
        if (c == null || !Task.class.isAssignableFrom(c)) {
            return null;
        }
        final Object obj = this.createComponent(taskType);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Task)) {
            throw new BuildException("Expected a Task from '" + taskType + "' but got an instance of " + obj.getClass().getName() + " instead");
        }
        final Task task = (Task)obj;
        task.setTaskType(taskType);
        task.setTaskName(taskType);
        this.project.log("   +Task: " + taskType, 4);
        return task;
    }
    
    public Object createDataType(final String typeName) throws BuildException {
        return this.createComponent(typeName);
    }
    
    public String getElementName(final Object element) {
        return this.getElementName(element, false);
    }
    
    public String getElementName(final Object o, final boolean brief) {
        final Class<?> elementClass = o.getClass();
        final String elementClassname = elementClass.getName();
        synchronized (this.antTypeTable) {
            for (final AntTypeDefinition def : this.antTypeTable.values()) {
                if (elementClassname.equals(def.getClassName()) && elementClass == def.getExposedClass(this.project)) {
                    final String name = def.getName();
                    return brief ? name : ("The <" + name + "> type");
                }
            }
        }
        return getUnmappedElementName(o.getClass(), brief);
    }
    
    public static String getElementName(Project p, final Object o, final boolean brief) {
        if (p == null) {
            p = Project.getProject(o);
        }
        return (p == null) ? getUnmappedElementName(o.getClass(), brief) : getComponentHelper(p).getElementName(o, brief);
    }
    
    private static String getUnmappedElementName(final Class<?> c, final boolean brief) {
        if (brief) {
            final String name = c.getName();
            return name.substring(name.lastIndexOf(46) + 1);
        }
        return c.toString();
    }
    
    private boolean validDefinition(final AntTypeDefinition def) {
        return def.getTypeClass(this.project) != null && def.getExposedClass(this.project) != null;
    }
    
    private boolean sameDefinition(final AntTypeDefinition def, final AntTypeDefinition old) {
        final boolean defValid = this.validDefinition(def);
        final boolean sameValidity = defValid == this.validDefinition(old);
        return sameValidity && (!defValid || def.sameDefinition(old, this.project));
    }
    
    private void updateRestrictedDefinition(final AntTypeDefinition def) {
        final String name = def.getName();
        List<AntTypeDefinition> list = null;
        synchronized (this.restrictedDefinitions) {
            list = this.restrictedDefinitions.get(name);
            if (list == null) {
                list = new ArrayList<AntTypeDefinition>();
                this.restrictedDefinitions.put(name, list);
            }
        }
        synchronized (list) {
            final Iterator<AntTypeDefinition> i = list.iterator();
            while (i.hasNext()) {
                final AntTypeDefinition current = i.next();
                if (current.getClassName().equals(def.getClassName())) {
                    i.remove();
                    break;
                }
            }
            list.add(def);
        }
    }
    
    private void updateDataTypeDefinition(final AntTypeDefinition def) {
        final String name = def.getName();
        synchronized (this.antTypeTable) {
            this.rebuildTaskClassDefinitions = true;
            this.rebuildTypeClassDefinitions = true;
            final AntTypeDefinition old = this.antTypeTable.get(name);
            if (old != null) {
                if (this.sameDefinition(def, old)) {
                    return;
                }
                final Class<?> oldClass = old.getExposedClass(this.project);
                final boolean isTask = oldClass != null && Task.class.isAssignableFrom(oldClass);
                this.project.log("Trying to override old definition of " + (isTask ? "task " : "datatype ") + name, def.similarDefinition(old, this.project) ? 3 : 1);
            }
            this.project.log(" +Datatype " + name + " " + def.getClassName(), 4);
            this.antTypeTable.put(name, def);
        }
    }
    
    public void enterAntLib(final String uri) {
        this.antLibCurrentUri = uri;
        this.antLibStack.push(uri);
    }
    
    public String getCurrentAntlibUri() {
        return this.antLibCurrentUri;
    }
    
    public void exitAntLib() {
        this.antLibStack.pop();
        this.antLibCurrentUri = ((this.antLibStack.size() == 0) ? null : this.antLibStack.peek());
    }
    
    private void initTasks() {
        final ClassLoader classLoader = this.getClassLoader(null);
        final Properties props = getDefaultDefinitions(false);
        final Enumeration<?> e = props.propertyNames();
        while (e.hasMoreElements()) {
            final String name = (String)e.nextElement();
            final String className = props.getProperty(name);
            final AntTypeDefinition def = new AntTypeDefinition();
            def.setName(name);
            def.setClassName(className);
            def.setClassLoader(classLoader);
            def.setAdaptToClass(Task.class);
            def.setAdapterClass(TaskAdapter.class);
            this.antTypeTable.put(name, def);
        }
    }
    
    private ClassLoader getClassLoader(ClassLoader classLoader) {
        final String buildSysclasspath = this.project.getProperty("build.sysclasspath");
        if (this.project.getCoreLoader() != null && !"only".equals(buildSysclasspath)) {
            classLoader = this.project.getCoreLoader();
        }
        return classLoader;
    }
    
    private static synchronized Properties getDefaultDefinitions(final boolean type) throws BuildException {
        final int idx = type ? 1 : 0;
        if (ComponentHelper.defaultDefinitions[idx] == null) {
            final String resource = type ? "/org/apache/tools/ant/types/defaults.properties" : "/org/apache/tools/ant/taskdefs/defaults.properties";
            final String errorString = type ? "Can't load default type list" : "Can't load default task list";
            InputStream in = null;
            try {
                in = ComponentHelper.class.getResourceAsStream(resource);
                if (in == null) {
                    throw new BuildException(errorString);
                }
                final Properties p = new Properties();
                p.load(in);
                ComponentHelper.defaultDefinitions[idx] = p;
            }
            catch (IOException e) {
                throw new BuildException(errorString, e);
            }
            finally {
                FileUtils.close(in);
            }
        }
        return ComponentHelper.defaultDefinitions[idx];
    }
    
    private void initTypes() {
        final ClassLoader classLoader = this.getClassLoader(null);
        final Properties props = getDefaultDefinitions(true);
        final Enumeration<?> e = props.propertyNames();
        while (e.hasMoreElements()) {
            final String name = (String)e.nextElement();
            final String className = props.getProperty(name);
            final AntTypeDefinition def = new AntTypeDefinition();
            def.setName(name);
            def.setClassName(className);
            def.setClassLoader(classLoader);
            this.antTypeTable.put(name, def);
        }
    }
    
    private synchronized void checkNamespace(final String componentName) {
        String uri = ProjectHelper.extractUriFromComponentName(componentName);
        if ("".equals(uri)) {
            uri = "antlib:org.apache.tools.ant";
        }
        if (!uri.startsWith("antlib:")) {
            return;
        }
        if (this.checkedNamespaces.contains(uri)) {
            return;
        }
        this.checkedNamespaces.add(uri);
        if (this.antTypeTable.size() == 0) {
            this.initDefaultDefinitions();
        }
        final Typedef definer = new Typedef();
        definer.setProject(this.project);
        definer.init();
        definer.setURI(uri);
        definer.setTaskName(uri);
        definer.setResource(Definer.makeResourceFromURI(uri));
        definer.setOnError(new Definer.OnError("ignore"));
        definer.execute();
    }
    
    public String diagnoseCreationFailure(final String componentName, final String type) {
        final StringWriter errorText = new StringWriter();
        final PrintWriter out = new PrintWriter(errorText);
        out.println("Problem: failed to create " + type + " " + componentName);
        boolean lowlevel = false;
        boolean jars = false;
        boolean definitions = false;
        final String home = System.getProperty("user.home");
        final File libDir = new File(home, Launcher.USER_LIBDIR);
        boolean probablyIDE = false;
        final String anthome = System.getProperty("ant.home");
        String antHomeLib;
        if (anthome != null) {
            final File antHomeLibDir = new File(anthome, "lib");
            antHomeLib = antHomeLibDir.getAbsolutePath();
        }
        else {
            probablyIDE = true;
            antHomeLib = "ANT_HOME" + File.separatorChar + "lib";
        }
        final StringBuffer dirListingText = new StringBuffer();
        final String tab = "        -";
        dirListingText.append("        -");
        dirListingText.append(antHomeLib);
        dirListingText.append('\n');
        if (probablyIDE) {
            dirListingText.append("        -");
            dirListingText.append("the IDE Ant configuration dialogs");
        }
        else {
            dirListingText.append("        -");
            dirListingText.append(libDir);
            dirListingText.append('\n');
            dirListingText.append("        -");
            dirListingText.append("a directory added on the command line with the -lib argument");
        }
        final String dirListing = dirListingText.toString();
        final AntTypeDefinition def = this.getDefinition(componentName);
        if (def == null) {
            this.printUnknownDefinition(out, componentName, dirListing);
            definitions = true;
        }
        else {
            final String classname = def.getClassName();
            final boolean antTask = classname.startsWith("org.apache.tools.ant.");
            boolean optional = classname.startsWith("org.apache.tools.ant.taskdefs.optional");
            optional |= classname.startsWith("org.apache.tools.ant.types.optional");
            Class<?> clazz = null;
            try {
                clazz = def.innerGetTypeClass();
            }
            catch (ClassNotFoundException e) {
                jars = true;
                if (!optional) {
                    definitions = true;
                }
                this.printClassNotFound(out, classname, optional, dirListing);
            }
            catch (NoClassDefFoundError ncdfe) {
                jars = true;
                this.printNotLoadDependentClass(out, optional, ncdfe, dirListing);
            }
            if (clazz != null) {
                try {
                    def.innerCreateAndSet(clazz, this.project);
                    out.println("The component could be instantiated.");
                }
                catch (NoSuchMethodException e2) {
                    lowlevel = true;
                    out.println("Cause: The class " + classname + " has no compatible constructor.");
                }
                catch (InstantiationException e3) {
                    lowlevel = true;
                    out.println("Cause: The class " + classname + " is abstract and cannot be instantiated.");
                }
                catch (IllegalAccessException e4) {
                    lowlevel = true;
                    out.println("Cause: The constructor for " + classname + " is private and cannot be invoked.");
                }
                catch (InvocationTargetException ex) {
                    lowlevel = true;
                    final Throwable t = ex.getTargetException();
                    out.println("Cause: The constructor threw the exception");
                    out.println(t.toString());
                    t.printStackTrace(out);
                }
                catch (NoClassDefFoundError ncdfe) {
                    jars = true;
                    out.println("Cause:  A class needed by class " + classname + " cannot be found: ");
                    out.println("       " + ncdfe.getMessage());
                    out.println("Action: Determine what extra JAR files are needed, and place them in:");
                    out.println(dirListing);
                }
            }
            out.println();
            out.println("Do not panic, this is a common problem.");
            if (definitions) {
                out.println("It may just be a typographical error in the build file or the task/type declaration.");
            }
            if (jars) {
                out.println("The commonest cause is a missing JAR.");
            }
            if (lowlevel) {
                out.println("This is quite a low level problem, which may need consultation with the author of the task.");
                if (antTask) {
                    out.println("This may be the Ant team. Please file a defect or contact the developer team.");
                }
                else {
                    out.println("This does not appear to be a task bundled with Ant.");
                    out.println("Please take it up with the supplier of the third-party " + type + ".");
                    out.println("If you have written it yourself, you probably have a bug to fix.");
                }
            }
            else {
                out.println();
                out.println("This is not a bug; it is a configuration problem");
            }
        }
        out.flush();
        out.close();
        return errorText.toString();
    }
    
    private void printUnknownDefinition(final PrintWriter out, final String componentName, final String dirListing) {
        final boolean isAntlib = componentName.indexOf("antlib:") == 0;
        final String uri = ProjectHelper.extractUriFromComponentName(componentName);
        out.println("Cause: The name is undefined.");
        out.println("Action: Check the spelling.");
        out.println("Action: Check that any custom tasks/types have been declared.");
        out.println("Action: Check that any <presetdef>/<macrodef> declarations have taken place.");
        if (uri.length() > 0) {
            final List<AntTypeDefinition> matches = this.findTypeMatches(uri);
            if (matches.size() > 0) {
                out.println();
                out.println("The definitions in the namespace " + uri + " are:");
                for (final AntTypeDefinition def : matches) {
                    final String local = ProjectHelper.extractNameFromComponentName(def.getName());
                    out.println("    " + local);
                }
            }
            else {
                out.println("No types or tasks have been defined in this namespace yet");
                if (isAntlib) {
                    out.println();
                    out.println("This appears to be an antlib declaration. ");
                    out.println("Action: Check that the implementing library exists in one of:");
                    out.println(dirListing);
                }
            }
        }
    }
    
    private void printClassNotFound(final PrintWriter out, final String classname, final boolean optional, final String dirListing) {
        out.println("Cause: the class " + classname + " was not found.");
        if (optional) {
            out.println("        This looks like one of Ant's optional components.");
            out.println("Action: Check that the appropriate optional JAR exists in");
            out.println(dirListing);
        }
        else {
            out.println("Action: Check that the component has been correctly declared");
            out.println("        and that the implementing JAR is in one of:");
            out.println(dirListing);
        }
    }
    
    private void printNotLoadDependentClass(final PrintWriter out, final boolean optional, final NoClassDefFoundError ncdfe, final String dirListing) {
        out.println("Cause: Could not load a dependent class " + ncdfe.getMessage());
        if (optional) {
            out.println("       It is not enough to have Ant's optional JARs");
            out.println("       you need the JAR files that the optional tasks depend upon.");
            out.println("       Ant's optional task dependencies are listed in the manual.");
        }
        else {
            out.println("       This class may be in a separate JAR that is not installed.");
        }
        out.println("Action: Determine what extra JAR files are needed, and place them in one of:");
        out.println(dirListing);
    }
    
    private List<AntTypeDefinition> findTypeMatches(final String prefix) {
        final List<AntTypeDefinition> result = new ArrayList<AntTypeDefinition>();
        synchronized (this.antTypeTable) {
            for (final AntTypeDefinition def : this.antTypeTable.values()) {
                if (def.getName().startsWith(prefix)) {
                    result.add(def);
                }
            }
        }
        return result;
    }
    
    static {
        ComponentHelper.defaultDefinitions = new Properties[2];
    }
}
