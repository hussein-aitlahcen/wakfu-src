package org.apache.tools.ant.launch;

import java.io.*;
import java.net.*;
import java.util.*;

public class Launcher
{
    public static final String ANTHOME_PROPERTY = "ant.home";
    public static final String ANTLIBDIR_PROPERTY = "ant.library.dir";
    public static final String ANT_PRIVATEDIR = ".ant";
    public static final String ANT_PRIVATELIB = "lib";
    public static boolean launchDiag;
    public static final String USER_LIBDIR;
    public static final String MAIN_CLASS = "org.apache.tools.ant.Main";
    public static final String USER_HOMEDIR = "user.home";
    private static final String JAVA_CLASS_PATH = "java.class.path";
    protected static final int EXIT_CODE_ERROR = 2;
    
    public static void main(final String[] args) {
        int exitCode;
        try {
            final Launcher launcher = new Launcher();
            exitCode = launcher.run(args);
        }
        catch (LaunchException e) {
            exitCode = 2;
            System.err.println(e.getMessage());
        }
        catch (Throwable t) {
            exitCode = 2;
            t.printStackTrace(System.err);
        }
        if (exitCode != 0) {
            if (Launcher.launchDiag) {
                System.out.println("Exit code: " + exitCode);
            }
            System.exit(exitCode);
        }
    }
    
    private void addPath(final String path, final boolean getJars, final List libPathURLs) throws MalformedURLException {
        final StringTokenizer tokenizer = new StringTokenizer(path, File.pathSeparator);
        while (tokenizer.hasMoreElements()) {
            final String elementName = tokenizer.nextToken();
            final File element = new File(elementName);
            if (elementName.indexOf(37) != -1 && !element.exists()) {
                continue;
            }
            if (getJars && element.isDirectory()) {
                final URL[] dirURLs = Locator.getLocationURLs(element);
                for (int j = 0; j < dirURLs.length; ++j) {
                    if (Launcher.launchDiag) {
                        System.out.println("adding library JAR: " + dirURLs[j]);
                    }
                    libPathURLs.add(dirURLs[j]);
                }
            }
            final URL url = Locator.fileToURL(element);
            if (Launcher.launchDiag) {
                System.out.println("adding library URL: " + url);
            }
            libPathURLs.add(url);
        }
    }
    
    private int run(final String[] args) throws LaunchException, MalformedURLException {
        final String antHomeProperty = System.getProperty("ant.home");
        File antHome = null;
        final File sourceJar = Locator.getClassSource(this.getClass());
        final File jarDir = sourceJar.getParentFile();
        String mainClassname = "org.apache.tools.ant.Main";
        if (antHomeProperty != null) {
            antHome = new File(antHomeProperty);
        }
        if (antHome == null || !antHome.exists()) {
            antHome = jarDir.getParentFile();
            this.setProperty("ant.home", antHome.getAbsolutePath());
        }
        if (!antHome.exists()) {
            throw new LaunchException("Ant home is set incorrectly or ant could not be located (estimated value=" + antHome.getAbsolutePath() + ")");
        }
        final List libPaths = new ArrayList();
        String cpString = null;
        final List argList = new ArrayList();
        boolean noUserLib = false;
        boolean noClassPath = false;
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-lib")) {
                if (i == args.length - 1) {
                    throw new LaunchException("The -lib argument must be followed by a library location");
                }
                libPaths.add(args[++i]);
            }
            else if (args[i].equals("-cp")) {
                if (i == args.length - 1) {
                    throw new LaunchException("The -cp argument must be followed by a classpath expression");
                }
                if (cpString != null) {
                    throw new LaunchException("The -cp argument must not be repeated");
                }
                cpString = args[++i];
            }
            else if (args[i].equals("--nouserlib") || args[i].equals("-nouserlib")) {
                noUserLib = true;
            }
            else if (args[i].equals("--launchdiag")) {
                Launcher.launchDiag = true;
            }
            else if (args[i].equals("--noclasspath") || args[i].equals("-noclasspath")) {
                noClassPath = true;
            }
            else if (args[i].equals("-main")) {
                if (i == args.length - 1) {
                    throw new LaunchException("The -main argument must be followed by a library location");
                }
                mainClassname = args[++i];
            }
            else {
                argList.add(args[i]);
            }
        }
        this.logPath("Launcher JAR", sourceJar);
        this.logPath("Launcher JAR directory", sourceJar.getParentFile());
        this.logPath("java.home", new File(System.getProperty("java.home")));
        String[] newArgs;
        if (argList.size() == args.length) {
            newArgs = args;
        }
        else {
            newArgs = argList.toArray(new String[argList.size()]);
        }
        final URL[] libURLs = this.getLibPathURLs(noClassPath ? null : cpString, libPaths);
        final URL[] systemURLs = this.getSystemURLs(jarDir);
        final URL[] userURLs = noUserLib ? new URL[0] : this.getUserURLs();
        final File toolsJAR = Locator.getToolsJar();
        this.logPath("tools.jar", toolsJAR);
        final URL[] jars = this.getJarArray(libURLs, userURLs, systemURLs, toolsJAR);
        final StringBuffer baseClassPath = new StringBuffer(System.getProperty("java.class.path"));
        if (baseClassPath.charAt(baseClassPath.length() - 1) == File.pathSeparatorChar) {
            baseClassPath.setLength(baseClassPath.length() - 1);
        }
        for (int j = 0; j < jars.length; ++j) {
            baseClassPath.append(File.pathSeparatorChar);
            baseClassPath.append(Locator.fromURI(jars[j].toString()));
        }
        this.setProperty("java.class.path", baseClassPath.toString());
        final URLClassLoader loader = new URLClassLoader(jars);
        Thread.currentThread().setContextClassLoader(loader);
        Class mainClass = null;
        int exitCode = 0;
        Throwable thrown = null;
        try {
            mainClass = loader.loadClass(mainClassname);
            final AntMain main = mainClass.newInstance();
            main.startAnt(newArgs, null, null);
        }
        catch (InstantiationException ex) {
            System.err.println("Incompatible version of " + mainClassname + " detected");
            final File mainJar = Locator.getClassSource(mainClass);
            System.err.println("Location of this class " + mainJar);
            thrown = ex;
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("Failed to locate" + mainClassname);
            thrown = cnfe;
        }
        catch (Throwable t) {
            t.printStackTrace(System.err);
            thrown = t;
        }
        if (thrown != null) {
            System.err.println("ant.home: " + antHome.getAbsolutePath());
            System.err.println("Classpath: " + baseClassPath.toString());
            System.err.println("Launcher JAR: " + sourceJar.getAbsolutePath());
            System.err.println("Launcher Directory: " + jarDir.getAbsolutePath());
            exitCode = 2;
        }
        return exitCode;
    }
    
    private URL[] getLibPathURLs(final String cpString, final List libPaths) throws MalformedURLException {
        final List libPathURLs = new ArrayList();
        if (cpString != null) {
            this.addPath(cpString, false, libPathURLs);
        }
        for (final String libPath : libPaths) {
            this.addPath(libPath, true, libPathURLs);
        }
        return libPathURLs.toArray(new URL[libPathURLs.size()]);
    }
    
    private URL[] getSystemURLs(final File antLauncherDir) throws MalformedURLException {
        File antLibDir = null;
        final String antLibDirProperty = System.getProperty("ant.library.dir");
        if (antLibDirProperty != null) {
            antLibDir = new File(antLibDirProperty);
        }
        if (antLibDir == null || !antLibDir.exists()) {
            antLibDir = antLauncherDir;
            this.setProperty("ant.library.dir", antLibDir.getAbsolutePath());
        }
        return Locator.getLocationURLs(antLibDir);
    }
    
    private URL[] getUserURLs() throws MalformedURLException {
        final File userLibDir = new File(System.getProperty("user.home"), Launcher.USER_LIBDIR);
        return Locator.getLocationURLs(userLibDir);
    }
    
    private URL[] getJarArray(final URL[] libJars, final URL[] userJars, final URL[] systemJars, final File toolsJar) throws MalformedURLException {
        int numJars = libJars.length + userJars.length + systemJars.length;
        if (toolsJar != null) {
            ++numJars;
        }
        final URL[] jars = new URL[numJars];
        System.arraycopy(libJars, 0, jars, 0, libJars.length);
        System.arraycopy(userJars, 0, jars, libJars.length, userJars.length);
        System.arraycopy(systemJars, 0, jars, userJars.length + libJars.length, systemJars.length);
        if (toolsJar != null) {
            jars[jars.length - 1] = Locator.fileToURL(toolsJar);
        }
        return jars;
    }
    
    private void setProperty(final String name, final String value) {
        if (Launcher.launchDiag) {
            System.out.println("Setting \"" + name + "\" to \"" + value + "\"");
        }
        System.setProperty(name, value);
    }
    
    private void logPath(final String name, final File path) {
        if (Launcher.launchDiag) {
            System.out.println(name + "= \"" + path + "\"");
        }
    }
    
    static {
        Launcher.launchDiag = false;
        USER_LIBDIR = ".ant" + File.separatorChar + "lib";
    }
}
