package org.apache.tools.ant;

import org.apache.tools.ant.launch.*;
import java.io.*;
import org.apache.tools.ant.property.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.input.*;
import org.apache.tools.ant.listener.*;
import java.util.*;

public class Main implements AntMain
{
    private static final Set<String> LAUNCH_COMMANDS;
    public static final String DEFAULT_BUILD_FILENAME = "build.xml";
    private int msgOutputLevel;
    private File buildFile;
    private static PrintStream out;
    private static PrintStream err;
    private Vector<String> targets;
    private Properties definedProps;
    private Vector<String> listeners;
    private Vector<String> propertyFiles;
    private boolean allowInput;
    private boolean keepGoingMode;
    private String loggerClassname;
    private String inputHandlerClassname;
    private boolean emacsMode;
    private boolean silent;
    private boolean readyToRun;
    private boolean projectHelp;
    private static boolean isLogFileUsed;
    private Integer threadPriority;
    private boolean proxy;
    private Map<Class<?>, List<String>> extraArguments;
    private static final GetProperty NOPROPERTIES;
    private static String antVersion;
    
    private static void printMessage(final Throwable t) {
        final String message = t.getMessage();
        if (message != null) {
            System.err.println(message);
        }
    }
    
    public static void start(final String[] args, final Properties additionalUserProperties, final ClassLoader coreLoader) {
        final Main m = new Main();
        m.startAnt(args, additionalUserProperties, coreLoader);
    }
    
    public void startAnt(final String[] args, final Properties additionalUserProperties, final ClassLoader coreLoader) {
        try {
            this.processArgs(args);
        }
        catch (Throwable exc) {
            handleLogfile();
            printMessage(exc);
            this.exit(1);
            return;
        }
        if (additionalUserProperties != null) {
            final Enumeration<?> e = ((Hashtable<?, V>)additionalUserProperties).keys();
            while (e.hasMoreElements()) {
                final String key = (String)e.nextElement();
                final String property = additionalUserProperties.getProperty(key);
                ((Hashtable<String, String>)this.definedProps).put(key, property);
            }
        }
        int exitCode = 1;
        try {
            try {
                this.runBuild(coreLoader);
                exitCode = 0;
            }
            catch (ExitStatusException ese) {
                exitCode = ese.getStatus();
                if (exitCode != 0) {
                    throw ese;
                }
            }
        }
        catch (BuildException be) {
            if (Main.err != System.err) {
                printMessage(be);
            }
        }
        catch (Throwable exc2) {
            exc2.printStackTrace();
            printMessage(exc2);
        }
        finally {
            handleLogfile();
        }
        this.exit(exitCode);
    }
    
    protected void exit(final int exitCode) {
        System.exit(exitCode);
    }
    
    private static void handleLogfile() {
        if (Main.isLogFileUsed) {
            FileUtils.close(Main.out);
            FileUtils.close(Main.err);
        }
    }
    
    public static void main(final String[] args) {
        start(args, null, null);
    }
    
    public Main() {
        super();
        this.msgOutputLevel = 2;
        this.targets = new Vector<String>();
        this.definedProps = new Properties();
        this.listeners = new Vector<String>(1);
        this.propertyFiles = new Vector<String>(1);
        this.allowInput = true;
        this.keepGoingMode = false;
        this.loggerClassname = null;
        this.inputHandlerClassname = null;
        this.emacsMode = false;
        this.silent = false;
        this.readyToRun = false;
        this.projectHelp = false;
        this.threadPriority = null;
        this.proxy = false;
        this.extraArguments = new HashMap<Class<?>, List<String>>();
    }
    
    protected Main(final String[] args) throws BuildException {
        super();
        this.msgOutputLevel = 2;
        this.targets = new Vector<String>();
        this.definedProps = new Properties();
        this.listeners = new Vector<String>(1);
        this.propertyFiles = new Vector<String>(1);
        this.allowInput = true;
        this.keepGoingMode = false;
        this.loggerClassname = null;
        this.inputHandlerClassname = null;
        this.emacsMode = false;
        this.silent = false;
        this.readyToRun = false;
        this.projectHelp = false;
        this.threadPriority = null;
        this.proxy = false;
        this.extraArguments = new HashMap<Class<?>, List<String>>();
        this.processArgs(args);
    }
    
    private void processArgs(final String[] args) {
        String searchForThis = null;
        boolean searchForFile = false;
        PrintStream logTo = null;
        boolean justPrintUsage = false;
        boolean justPrintVersion = false;
        boolean justPrintDiagnostics = false;
        final ArgumentProcessorRegistry processorRegistry = ArgumentProcessorRegistry.getInstance();
        for (int i = 0; i < args.length; ++i) {
            final String arg = args[i];
            if (arg.equals("-help") || arg.equals("-h")) {
                justPrintUsage = true;
            }
            else if (arg.equals("-version")) {
                justPrintVersion = true;
            }
            else if (arg.equals("-diagnostics")) {
                justPrintDiagnostics = true;
            }
            else if (arg.equals("-quiet") || arg.equals("-q")) {
                this.msgOutputLevel = 1;
            }
            else if (arg.equals("-verbose") || arg.equals("-v")) {
                this.msgOutputLevel = 3;
            }
            else if (arg.equals("-debug") || arg.equals("-d")) {
                this.msgOutputLevel = 4;
            }
            else if (arg.equals("-silent") || arg.equals("-S")) {
                this.silent = true;
            }
            else if (arg.equals("-noinput")) {
                this.allowInput = false;
            }
            else {
                Label_0320: {
                    if (!arg.equals("-logfile")) {
                        if (!arg.equals("-l")) {
                            break Label_0320;
                        }
                    }
                    try {
                        final File logFile = new File(args[i + 1]);
                        ++i;
                        logTo = new PrintStream(new FileOutputStream(logFile));
                        Main.isLogFileUsed = true;
                        continue;
                    }
                    catch (IOException ioe) {
                        final String msg = "Cannot write on the specified log file. Make sure the path exists and you have write permissions.";
                        throw new BuildException(msg);
                    }
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        final String msg = "You must specify a log file when using the -log argument";
                        throw new BuildException(msg);
                    }
                }
                if (arg.equals("-buildfile") || arg.equals("-file") || arg.equals("-f")) {
                    i = this.handleArgBuildFile(args, i);
                }
                else if (arg.equals("-listener")) {
                    i = this.handleArgListener(args, i);
                }
                else if (arg.startsWith("-D")) {
                    i = this.handleArgDefine(args, i);
                }
                else if (arg.equals("-logger")) {
                    i = this.handleArgLogger(args, i);
                }
                else if (arg.equals("-inputhandler")) {
                    i = this.handleArgInputHandler(args, i);
                }
                else if (arg.equals("-emacs") || arg.equals("-e")) {
                    this.emacsMode = true;
                }
                else if (arg.equals("-projecthelp") || arg.equals("-p")) {
                    this.projectHelp = true;
                }
                else if (arg.equals("-find") || arg.equals("-s")) {
                    searchForFile = true;
                    if (i < args.length - 1) {
                        searchForThis = args[++i];
                    }
                }
                else if (arg.startsWith("-propertyfile")) {
                    i = this.handleArgPropertyFile(args, i);
                }
                else if (arg.equals("-k") || arg.equals("-keep-going")) {
                    this.keepGoingMode = true;
                }
                else if (arg.equals("-nice")) {
                    i = this.handleArgNice(args, i);
                }
                else {
                    if (Main.LAUNCH_COMMANDS.contains(arg)) {
                        final String msg2 = "Ant's Main method is being handed an option " + arg + " that is only for the launcher class." + "\nThis can be caused by a version mismatch between " + "the ant script/.bat file and Ant itself.";
                        throw new BuildException(msg2);
                    }
                    if (arg.equals("-autoproxy")) {
                        this.proxy = true;
                    }
                    else if (arg.startsWith("-")) {
                        boolean processed = false;
                        for (final ArgumentProcessor processor : processorRegistry.getProcessors()) {
                            final int newI = processor.readArguments(args, i);
                            if (newI != -1) {
                                List<String> extraArgs = this.extraArguments.get(processor.getClass());
                                if (extraArgs == null) {
                                    extraArgs = new ArrayList<String>();
                                    this.extraArguments.put(processor.getClass(), extraArgs);
                                }
                                while (i < newI && i < args.length) {
                                    extraArgs.add(args[i]);
                                    ++i;
                                }
                                processed = true;
                                break;
                            }
                        }
                        if (!processed) {
                            final String msg = "Unknown argument: " + arg;
                            System.err.println(msg);
                            printUsage();
                            throw new BuildException("");
                        }
                    }
                    else {
                        this.targets.addElement(arg);
                    }
                }
            }
        }
        if (this.msgOutputLevel >= 3 || justPrintVersion) {
            printVersion(this.msgOutputLevel);
        }
        if (justPrintUsage || justPrintVersion || justPrintDiagnostics) {
            if (justPrintUsage) {
                printUsage();
            }
            if (justPrintDiagnostics) {
                Diagnostics.doReport(System.out, this.msgOutputLevel);
            }
            return;
        }
        if (this.buildFile == null) {
            if (searchForFile) {
                if (searchForThis != null) {
                    this.buildFile = this.findBuildFile(System.getProperty("user.dir"), searchForThis);
                    if (this.buildFile == null) {
                        throw new BuildException("Could not locate a build file!");
                    }
                }
                else {
                    final Iterator<ProjectHelper> it = ProjectHelperRepository.getInstance().getHelpers();
                    do {
                        final ProjectHelper helper = it.next();
                        searchForThis = helper.getDefaultBuildFile();
                        if (this.msgOutputLevel >= 3) {
                            System.out.println("Searching the default build file: " + searchForThis);
                        }
                        this.buildFile = this.findBuildFile(System.getProperty("user.dir"), searchForThis);
                    } while (this.buildFile == null && it.hasNext());
                    if (this.buildFile == null) {
                        throw new BuildException("Could not locate a build file!");
                    }
                }
            }
            else {
                final Iterator<ProjectHelper> it = ProjectHelperRepository.getInstance().getHelpers();
                do {
                    final ProjectHelper helper = it.next();
                    this.buildFile = new File(helper.getDefaultBuildFile());
                    if (this.msgOutputLevel >= 3) {
                        System.out.println("Trying the default build file: " + this.buildFile);
                    }
                } while (!this.buildFile.exists() && it.hasNext());
            }
        }
        if (!this.buildFile.exists()) {
            System.out.println("Buildfile: " + this.buildFile + " does not exist!");
            throw new BuildException("Build failed");
        }
        if (this.buildFile.isDirectory()) {
            final File whatYouMeant = new File(this.buildFile, "build.xml");
            if (!whatYouMeant.isFile()) {
                System.out.println("What? Buildfile: " + this.buildFile + " is a dir!");
                throw new BuildException("Build failed");
            }
            this.buildFile = whatYouMeant;
        }
        this.buildFile = FileUtils.getFileUtils().normalize(this.buildFile.getAbsolutePath());
        this.loadPropertyFiles();
        if (this.msgOutputLevel >= 2) {
            System.out.println("Buildfile: " + this.buildFile);
        }
        if (logTo != null) {
            Main.out = logTo;
            Main.err = logTo;
            System.setOut(Main.out);
            System.setErr(Main.err);
        }
        this.readyToRun = true;
    }
    
    private int handleArgBuildFile(final String[] args, int pos) {
        try {
            this.buildFile = new File(args[++pos].replace('/', File.separatorChar));
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new BuildException("You must specify a buildfile when using the -buildfile argument");
        }
        return pos;
    }
    
    private int handleArgListener(final String[] args, int pos) {
        try {
            this.listeners.addElement(args[pos + 1]);
            ++pos;
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            final String msg = "You must specify a classname when using the -listener argument";
            throw new BuildException(msg);
        }
        return pos;
    }
    
    private int handleArgDefine(final String[] args, int argPos) {
        final String arg = args[argPos];
        String name = arg.substring(2, arg.length());
        String value = null;
        final int posEq = name.indexOf("=");
        if (posEq > 0) {
            value = name.substring(posEq + 1);
            name = name.substring(0, posEq);
        }
        else {
            if (argPos >= args.length - 1) {
                throw new BuildException("Missing value for property " + name);
            }
            value = args[++argPos];
        }
        ((Hashtable<String, String>)this.definedProps).put(name, value);
        return argPos;
    }
    
    private int handleArgLogger(final String[] args, int pos) {
        if (this.loggerClassname != null) {
            throw new BuildException("Only one logger class may be specified.");
        }
        try {
            this.loggerClassname = args[++pos];
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new BuildException("You must specify a classname when using the -logger argument");
        }
        return pos;
    }
    
    private int handleArgInputHandler(final String[] args, int pos) {
        if (this.inputHandlerClassname != null) {
            throw new BuildException("Only one input handler class may be specified.");
        }
        try {
            this.inputHandlerClassname = args[++pos];
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new BuildException("You must specify a classname when using the -inputhandler argument");
        }
        return pos;
    }
    
    private int handleArgPropertyFile(final String[] args, int pos) {
        try {
            this.propertyFiles.addElement(args[++pos]);
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            final String msg = "You must specify a property filename when using the -propertyfile argument";
            throw new BuildException(msg);
        }
        return pos;
    }
    
    private int handleArgNice(final String[] args, int pos) {
        try {
            this.threadPriority = Integer.decode(args[++pos]);
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new BuildException("You must supply a niceness value (1-10) after the -nice option");
        }
        catch (NumberFormatException e) {
            throw new BuildException("Unrecognized niceness value: " + args[pos]);
        }
        if (this.threadPriority < 1 || this.threadPriority > 10) {
            throw new BuildException("Niceness value is out of the range 1-10");
        }
        return pos;
    }
    
    private void loadPropertyFiles() {
        for (final String filename : this.propertyFiles) {
            final Properties props = new Properties();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filename);
                props.load(fis);
            }
            catch (IOException e) {
                System.out.println("Could not load property file " + filename + ": " + e.getMessage());
            }
            finally {
                FileUtils.close(fis);
            }
            final Enumeration<?> propertyNames = props.propertyNames();
            while (propertyNames.hasMoreElements()) {
                final String name = (String)propertyNames.nextElement();
                if (this.definedProps.getProperty(name) == null) {
                    ((Hashtable<String, String>)this.definedProps).put(name, props.getProperty(name));
                }
            }
        }
    }
    
    private File getParentFile(final File file) {
        final File parent = file.getParentFile();
        if (parent != null && this.msgOutputLevel >= 3) {
            System.out.println("Searching in " + parent.getAbsolutePath());
        }
        return parent;
    }
    
    private File findBuildFile(final String start, final String suffix) {
        if (this.msgOutputLevel >= 2) {
            System.out.println("Searching for " + suffix + " ...");
        }
        File parent;
        File file;
        for (parent = new File(new File(start).getAbsolutePath()), file = new File(parent, suffix); !file.exists(); file = new File(parent, suffix)) {
            parent = this.getParentFile(parent);
            if (parent == null) {
                return null;
            }
        }
        return file;
    }
    
    private void runBuild(final ClassLoader coreLoader) throws BuildException {
        if (!this.readyToRun) {
            return;
        }
        final ArgumentProcessorRegistry processorRegistry = ArgumentProcessorRegistry.getInstance();
        for (final ArgumentProcessor processor : processorRegistry.getProcessors()) {
            final List<String> extraArgs = this.extraArguments.get(processor.getClass());
            if (extraArgs != null && processor.handleArg(extraArgs)) {
                return;
            }
        }
        final Project project = new Project();
        project.setCoreLoader(coreLoader);
        Throwable error = null;
        try {
            this.addBuildListeners(project);
            this.addInputHandler(project);
            final PrintStream savedErr = System.err;
            final PrintStream savedOut = System.out;
            final InputStream savedIn = System.in;
            SecurityManager oldsm = null;
            oldsm = System.getSecurityManager();
            try {
                if (this.allowInput) {
                    project.setDefaultInputStream(System.in);
                }
                System.setIn(new DemuxInputStream(project));
                System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
                System.setErr(new PrintStream(new DemuxOutputStream(project, true)));
                if (!this.projectHelp) {
                    project.fireBuildStarted();
                }
                if (this.threadPriority != null) {
                    try {
                        project.log("Setting Ant's thread priority to " + this.threadPriority, 3);
                        Thread.currentThread().setPriority(this.threadPriority);
                    }
                    catch (SecurityException swallowed) {
                        project.log("A security manager refused to set the -nice value");
                    }
                }
                this.setProperties(project);
                project.setKeepGoingMode(this.keepGoingMode);
                if (this.proxy) {
                    final ProxySetup proxySetup = new ProxySetup(project);
                    proxySetup.enableProxies();
                }
                for (final ArgumentProcessor processor2 : processorRegistry.getProcessors()) {
                    final List<String> extraArgs2 = this.extraArguments.get(processor2.getClass());
                    if (extraArgs2 != null) {
                        processor2.prepareConfigure(project, extraArgs2);
                    }
                }
                ProjectHelper.configureProject(project, this.buildFile);
                for (final ArgumentProcessor processor2 : processorRegistry.getProcessors()) {
                    final List<String> extraArgs2 = this.extraArguments.get(processor2.getClass());
                    if (extraArgs2 != null && processor2.handleArg(project, extraArgs2)) {
                        return;
                    }
                }
                if (this.projectHelp) {
                    printDescription(project);
                    printTargets(project, this.msgOutputLevel > 2, this.msgOutputLevel > 3);
                    return;
                }
                if (this.targets.size() == 0 && project.getDefaultTarget() != null) {
                    this.targets.addElement(project.getDefaultTarget());
                }
                project.executeTargets(this.targets);
            }
            finally {
                if (oldsm != null) {
                    System.setSecurityManager(oldsm);
                }
                System.setOut(savedOut);
                System.setErr(savedErr);
                System.setIn(savedIn);
            }
        }
        catch (RuntimeException exc) {
            error = exc;
            throw exc;
        }
        catch (Error e) {
            error = e;
            throw e;
        }
        finally {
            if (!this.projectHelp) {
                try {
                    project.fireBuildFinished(error);
                    return;
                }
                catch (Throwable t) {
                    System.err.println("Caught an exception while logging the end of the build.  Exception was:");
                    t.printStackTrace();
                    if (error != null) {
                        System.err.println("There has been an error prior to that:");
                        error.printStackTrace();
                    }
                    throw new BuildException(t);
                }
            }
            if (error != null) {
                project.log(error.toString(), 0);
            }
        }
    }
    
    private void setProperties(final Project project) {
        project.init();
        final PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(project);
        final Map<String, Object> props;
        final Map raw = props = new HashMap<String, Object>((Map<? extends String, ?>)this.definedProps);
        final ResolvePropertyMap resolver = new ResolvePropertyMap(project, Main.NOPROPERTIES, propertyHelper.getExpanders());
        resolver.resolveAllProperties(props, null, false);
        for (final Map.Entry<String, Object> ent : props.entrySet()) {
            final String arg = ent.getKey();
            final Object value = ent.getValue();
            project.setUserProperty(arg, String.valueOf(value));
        }
        project.setUserProperty("ant.file", this.buildFile.getAbsolutePath());
        project.setUserProperty("ant.file.type", "file");
    }
    
    protected void addBuildListeners(final Project project) {
        project.addBuildListener(this.createLogger());
        for (int count = this.listeners.size(), i = 0; i < count; ++i) {
            final String className = this.listeners.elementAt(i);
            final BuildListener listener = (BuildListener)ClasspathUtils.newInstance(className, Main.class.getClassLoader(), BuildListener.class);
            project.setProjectReference(listener);
            project.addBuildListener(listener);
        }
    }
    
    private void addInputHandler(final Project project) throws BuildException {
        InputHandler handler = null;
        if (this.inputHandlerClassname == null) {
            handler = new DefaultInputHandler();
        }
        else {
            handler = (InputHandler)ClasspathUtils.newInstance(this.inputHandlerClassname, Main.class.getClassLoader(), InputHandler.class);
            project.setProjectReference(handler);
        }
        project.setInputHandler(handler);
    }
    
    private BuildLogger createLogger() {
        BuildLogger logger = null;
        Label_0119: {
            if (this.silent) {
                logger = new SilentLogger();
                this.msgOutputLevel = 1;
                this.emacsMode = true;
            }
            else {
                if (this.loggerClassname != null) {
                    try {
                        logger = (BuildLogger)ClasspathUtils.newInstance(this.loggerClassname, Main.class.getClassLoader(), BuildLogger.class);
                        break Label_0119;
                    }
                    catch (BuildException e) {
                        System.err.println("The specified logger class " + this.loggerClassname + " could not be used because " + e.getMessage());
                        throw new RuntimeException();
                    }
                }
                logger = new DefaultLogger();
            }
        }
        logger.setMessageOutputLevel(this.msgOutputLevel);
        logger.setOutputPrintStream(Main.out);
        logger.setErrorPrintStream(Main.err);
        logger.setEmacsMode(this.emacsMode);
        return logger;
    }
    
    private static void printUsage() {
        System.out.println("ant [options] [target [target2 [target3] ...]]");
        System.out.println("Options: ");
        System.out.println("  -help, -h              print this message");
        System.out.println("  -projecthelp, -p       print project help information");
        System.out.println("  -version               print the version information and exit");
        System.out.println("  -diagnostics           print information that might be helpful to");
        System.out.println("                         diagnose or report problems.");
        System.out.println("  -quiet, -q             be extra quiet");
        System.out.println("  -silent, -S            print nothing but task outputs and build failures");
        System.out.println("  -verbose, -v           be extra verbose");
        System.out.println("  -debug, -d             print debugging information");
        System.out.println("  -emacs, -e             produce logging information without adornments");
        System.out.println("  -lib <path>            specifies a path to search for jars and classes");
        System.out.println("  -logfile <file>        use given file for log");
        System.out.println("    -l     <file>                ''");
        System.out.println("  -logger <classname>    the class which is to perform logging");
        System.out.println("  -listener <classname>  add an instance of class as a project listener");
        System.out.println("  -noinput               do not allow interactive input");
        System.out.println("  -buildfile <file>      use given buildfile");
        System.out.println("    -file    <file>              ''");
        System.out.println("    -f       <file>              ''");
        System.out.println("  -D<property>=<value>   use value for given property");
        System.out.println("  -keep-going, -k        execute all targets that do not depend");
        System.out.println("                         on failed target(s)");
        System.out.println("  -propertyfile <name>   load all properties from file with -D");
        System.out.println("                         properties taking precedence");
        System.out.println("  -inputhandler <class>  the class which will handle input requests");
        System.out.println("  -find <file>           (s)earch for buildfile towards the root of");
        System.out.println("    -s  <file>           the filesystem and use it");
        System.out.println("  -nice  number          A niceness value for the main thread:                         1 (lowest) to 10 (highest); 5 is the default");
        System.out.println("  -nouserlib             Run ant without using the jar files from                         ${user.home}/.ant/lib");
        System.out.println("  -noclasspath           Run ant without using CLASSPATH");
        System.out.println("  -autoproxy             Java1.5+: use the OS proxy settings");
        System.out.println("  -main <class>          override Ant's normal entry point");
        for (final ArgumentProcessor processor : ArgumentProcessorRegistry.getInstance().getProcessors()) {
            processor.printUsage(System.out);
        }
    }
    
    private static void printVersion(final int logLevel) throws BuildException {
        System.out.println(getAntVersion());
    }
    
    public static synchronized String getAntVersion() throws BuildException {
        if (Main.antVersion == null) {
            try {
                final Properties props = new Properties();
                final InputStream in = Main.class.getResourceAsStream("/org/apache/tools/ant/version.txt");
                props.load(in);
                in.close();
                final StringBuffer msg = new StringBuffer();
                msg.append("Apache Ant(TM) version ");
                msg.append(props.getProperty("VERSION"));
                msg.append(" compiled on ");
                msg.append(props.getProperty("DATE"));
                Main.antVersion = msg.toString();
            }
            catch (IOException ioe) {
                throw new BuildException("Could not load the version information:" + ioe.getMessage());
            }
            catch (NullPointerException npe) {
                throw new BuildException("Could not load the version information.");
            }
        }
        return Main.antVersion;
    }
    
    private static void printDescription(final Project project) {
        if (project.getDescription() != null) {
            project.log(project.getDescription());
        }
    }
    
    private static Map<String, Target> removeDuplicateTargets(final Map<String, Target> targets) {
        final Map<Location, Target> locationMap = new HashMap<Location, Target>();
        for (final Map.Entry<String, Target> entry : targets.entrySet()) {
            final String name = entry.getKey();
            final Target target = entry.getValue();
            final Target otherTarget = locationMap.get(target.getLocation());
            if (otherTarget == null || otherTarget.getName().length() > name.length()) {
                locationMap.put(target.getLocation(), target);
            }
        }
        final Map<String, Target> ret = new HashMap<String, Target>();
        for (final Target target2 : locationMap.values()) {
            ret.put(target2.getName(), target2);
        }
        return ret;
    }
    
    private static void printTargets(final Project project, boolean printSubTargets, final boolean printDependencies) {
        int maxLength = 0;
        final Map<String, Target> ptargets = removeDuplicateTargets(project.getTargets());
        final Vector<String> topNames = new Vector<String>();
        final Vector<String> topDescriptions = new Vector<String>();
        final Vector<Enumeration<String>> topDependencies = new Vector<Enumeration<String>>();
        final Vector<String> subNames = new Vector<String>();
        final Vector<Enumeration<String>> subDependencies = new Vector<Enumeration<String>>();
        for (final Target currentTarget : ptargets.values()) {
            final String targetName = currentTarget.getName();
            if (targetName.equals("")) {
                continue;
            }
            final String targetDescription = currentTarget.getDescription();
            if (targetDescription == null) {
                final int pos = findTargetPosition(subNames, targetName);
                subNames.insertElementAt(targetName, pos);
                if (!printDependencies) {
                    continue;
                }
                subDependencies.insertElementAt(currentTarget.getDependencies(), pos);
            }
            else {
                final int pos = findTargetPosition(topNames, targetName);
                topNames.insertElementAt(targetName, pos);
                topDescriptions.insertElementAt(targetDescription, pos);
                if (targetName.length() > maxLength) {
                    maxLength = targetName.length();
                }
                if (!printDependencies) {
                    continue;
                }
                topDependencies.insertElementAt(currentTarget.getDependencies(), pos);
            }
        }
        printTargets(project, topNames, topDescriptions, topDependencies, "Main targets:", maxLength);
        if (topNames.size() == 0) {
            printSubTargets = true;
        }
        if (printSubTargets) {
            printTargets(project, subNames, null, subDependencies, "Other targets:", 0);
        }
        final String defaultTarget = project.getDefaultTarget();
        if (defaultTarget != null && !"".equals(defaultTarget)) {
            project.log("Default target: " + defaultTarget);
        }
    }
    
    private static int findTargetPosition(final Vector<String> names, final String name) {
        int res;
        for (int size = res = names.size(), i = 0; i < size && res == size; ++i) {
            if (name.compareTo((String)names.elementAt(i)) < 0) {
                res = i;
            }
        }
        return res;
    }
    
    private static void printTargets(final Project project, final Vector<String> names, final Vector<String> descriptions, final Vector<Enumeration<String>> dependencies, final String heading, final int maxlen) {
        final String lSep = System.getProperty("line.separator");
        String spaces;
        for (spaces = "    "; spaces.length() <= maxlen; spaces += spaces) {}
        final StringBuilder msg = new StringBuilder();
        msg.append(heading + lSep + lSep);
        for (int size = names.size(), i = 0; i < size; ++i) {
            msg.append(" ");
            msg.append(names.elementAt(i));
            if (descriptions != null) {
                msg.append(spaces.substring(0, maxlen - names.elementAt(i).length() + 2));
                msg.append(descriptions.elementAt(i));
            }
            msg.append(lSep);
            if (!dependencies.isEmpty()) {
                final Enumeration<String> deps = dependencies.elementAt(i);
                if (deps.hasMoreElements()) {
                    msg.append("   depends on: ");
                    while (deps.hasMoreElements()) {
                        msg.append(deps.nextElement());
                        if (deps.hasMoreElements()) {
                            msg.append(", ");
                        }
                    }
                    msg.append(lSep);
                }
            }
        }
        project.log(msg.toString(), 1);
    }
    
    static {
        LAUNCH_COMMANDS = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("-lib", "-cp", "-noclasspath", "--noclasspath", "-nouserlib", "-main")));
        Main.out = System.out;
        Main.err = System.err;
        Main.isLogFileUsed = false;
        NOPROPERTIES = new GetProperty() {
            public Object getProperty(final String aName) {
                return null;
            }
        };
        Main.antVersion = null;
    }
}
