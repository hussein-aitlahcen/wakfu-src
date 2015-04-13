package org.apache.tools.ant;

import java.net.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.*;
import org.apache.tools.ant.launch.*;
import java.lang.reflect.*;
import org.apache.tools.ant.util.*;
import java.io.*;
import java.util.*;

public final class Diagnostics
{
    private static final int JAVA_1_5_NUMBER = 15;
    private static final int BIG_DRIFT_LIMIT = 10000;
    private static final int TEST_FILE_SIZE = 32;
    private static final int KILOBYTE = 1024;
    private static final int SECONDS_PER_MILLISECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    protected static final String ERROR_PROPERTY_ACCESS_BLOCKED = "Access to this property blocked by a security manager";
    
    public static boolean isOptionalAvailable() {
        return true;
    }
    
    public static void validateVersion() throws BuildException {
    }
    
    public static File[] listLibraries() {
        final String home = System.getProperty("ant.home");
        if (home == null) {
            return null;
        }
        final File libDir = new File(home, "lib");
        return listJarFiles(libDir);
    }
    
    private static File[] listJarFiles(final File libDir) {
        final FilenameFilter filter = new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".jar");
            }
        };
        final File[] files = libDir.listFiles(filter);
        return files;
    }
    
    public static void main(final String[] args) {
        doReport(System.out);
    }
    
    private static String getImplementationVersion(final Class<?> clazz) {
        return clazz.getPackage().getImplementationVersion();
    }
    
    private static URL getClassLocation(final Class<?> clazz) {
        if (clazz.getProtectionDomain().getCodeSource() == null) {
            return null;
        }
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }
    
    private static String getXMLParserName() {
        final SAXParser saxParser = getSAXParser();
        if (saxParser == null) {
            return "Could not create an XML Parser";
        }
        final String saxParserName = saxParser.getClass().getName();
        return saxParserName;
    }
    
    private static String getXSLTProcessorName() {
        final Transformer transformer = getXSLTProcessor();
        if (transformer == null) {
            return "Could not create an XSLT Processor";
        }
        final String processorName = transformer.getClass().getName();
        return processorName;
    }
    
    private static SAXParser getSAXParser() {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        if (saxParserFactory == null) {
            return null;
        }
        SAXParser saxParser = null;
        try {
            saxParser = saxParserFactory.newSAXParser();
        }
        catch (Exception e) {
            ignoreThrowable(e);
        }
        return saxParser;
    }
    
    private static Transformer getXSLTProcessor() {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        if (transformerFactory == null) {
            return null;
        }
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        }
        catch (Exception e) {
            ignoreThrowable(e);
        }
        return transformer;
    }
    
    private static String getXMLParserLocation() {
        final SAXParser saxParser = getSAXParser();
        if (saxParser == null) {
            return null;
        }
        final URL location = getClassLocation(saxParser.getClass());
        return (location != null) ? location.toString() : null;
    }
    
    private static String getNamespaceParserName() {
        try {
            final XMLReader reader = JAXPUtils.getNamespaceXMLReader();
            return reader.getClass().getName();
        }
        catch (BuildException e) {
            ignoreThrowable(e);
            return null;
        }
    }
    
    private static String getNamespaceParserLocation() {
        try {
            final XMLReader reader = JAXPUtils.getNamespaceXMLReader();
            final URL location = getClassLocation(reader.getClass());
            return (location != null) ? location.toString() : null;
        }
        catch (BuildException e) {
            ignoreThrowable(e);
            return null;
        }
    }
    
    private static String getXSLTProcessorLocation() {
        final Transformer transformer = getXSLTProcessor();
        if (transformer == null) {
            return null;
        }
        final URL location = getClassLocation(transformer.getClass());
        return (location != null) ? location.toString() : null;
    }
    
    private static void ignoreThrowable(final Throwable thrown) {
    }
    
    public static void doReport(final PrintStream out) {
        doReport(out, 2);
    }
    
    public static void doReport(final PrintStream out, final int logLevel) {
        out.println("------- Ant diagnostics report -------");
        out.println(Main.getAntVersion());
        header(out, "Implementation Version");
        out.println("core tasks     : " + getImplementationVersion(Main.class) + " in " + getClassLocation(Main.class));
        header(out, "ANT PROPERTIES");
        doReportAntProperties(out);
        header(out, "ANT_HOME/lib jar listing");
        doReportAntHomeLibraries(out);
        header(out, "USER_HOME/.ant/lib jar listing");
        doReportUserHomeLibraries(out);
        header(out, "Tasks availability");
        doReportTasksAvailability(out);
        header(out, "org.apache.env.Which diagnostics");
        doReportWhich(out);
        header(out, "XML Parser information");
        doReportParserInfo(out);
        header(out, "XSLT Processor information");
        doReportXSLTProcessorInfo(out);
        header(out, "System properties");
        doReportSystemProperties(out);
        header(out, "Temp dir");
        doReportTempDir(out);
        header(out, "Locale information");
        doReportLocale(out);
        header(out, "Proxy information");
        doReportProxy(out);
        out.println();
    }
    
    private static void header(final PrintStream out, final String section) {
        out.println();
        out.println("-------------------------------------------");
        out.print(" ");
        out.println(section);
        out.println("-------------------------------------------");
    }
    
    private static void doReportSystemProperties(final PrintStream out) {
        Properties sysprops = null;
        try {
            sysprops = System.getProperties();
        }
        catch (SecurityException e) {
            ignoreThrowable(e);
            out.println("Access to System.getProperties() blocked by a security manager");
            return;
        }
        final Enumeration<?> keys = sysprops.propertyNames();
        while (keys.hasMoreElements()) {
            final String key = (String)keys.nextElement();
            final String value = getProperty(key);
            out.println(key + " : " + value);
        }
    }
    
    private static String getProperty(final String key) {
        String value;
        try {
            value = System.getProperty(key);
        }
        catch (SecurityException e) {
            value = "Access to this property blocked by a security manager";
        }
        return value;
    }
    
    private static void doReportAntProperties(final PrintStream out) {
        final Project p = new Project();
        p.initProperties();
        out.println("ant.version: " + p.getProperty("ant.version"));
        out.println("ant.java.version: " + p.getProperty("ant.java.version"));
        out.println("Is this the Apache Harmony VM? " + (JavaEnvUtils.isApacheHarmony() ? "yes" : "no"));
        out.println("Is this the Kaffe VM? " + (JavaEnvUtils.isKaffe() ? "yes" : "no"));
        out.println("Is this gij/gcj? " + (JavaEnvUtils.isGij() ? "yes" : "no"));
        out.println("ant.core.lib: " + p.getProperty("ant.core.lib"));
        out.println("ant.home: " + p.getProperty("ant.home"));
    }
    
    private static void doReportAntHomeLibraries(final PrintStream out) {
        out.println("ant.home: " + System.getProperty("ant.home"));
        final File[] libs = listLibraries();
        printLibraries(libs, out);
    }
    
    private static void doReportUserHomeLibraries(final PrintStream out) {
        final String home = System.getProperty("user.home");
        out.println("user.home: " + home);
        final File libDir = new File(home, Launcher.USER_LIBDIR);
        final File[] libs = listJarFiles(libDir);
        printLibraries(libs, out);
    }
    
    private static void printLibraries(final File[] libs, final PrintStream out) {
        if (libs == null) {
            out.println("No such directory.");
            return;
        }
        for (int i = 0; i < libs.length; ++i) {
            out.println(libs[i].getName() + " (" + libs[i].length() + " bytes)");
        }
    }
    
    private static void doReportWhich(final PrintStream out) {
        Throwable error = null;
        try {
            final Class<?> which = Class.forName("org.apache.env.Which");
            final Method method = which.getMethod("main", String[].class);
            method.invoke(null, new String[0]);
        }
        catch (ClassNotFoundException e3) {
            out.println("Not available.");
            out.println("Download it at http://xml.apache.org/commons/");
        }
        catch (InvocationTargetException e) {
            error = ((e.getTargetException() == null) ? e : e.getTargetException());
        }
        catch (Throwable e2) {
            error = e2;
        }
        if (error != null) {
            out.println("Error while running org.apache.env.Which");
            error.printStackTrace();
        }
    }
    
    private static void doReportTasksAvailability(final PrintStream out) {
        final InputStream is = Main.class.getResourceAsStream("/org/apache/tools/ant/taskdefs/defaults.properties");
        if (is == null) {
            out.println("None available");
        }
        else {
            final Properties props = new Properties();
            try {
                props.load(is);
                final Enumeration<?> keys = ((Hashtable<?, V>)props).keys();
                while (keys.hasMoreElements()) {
                    final String key = (String)keys.nextElement();
                    final String classname = props.getProperty(key);
                    try {
                        Class.forName(classname);
                        props.remove(key);
                    }
                    catch (ClassNotFoundException e3) {
                        out.println(key + " : Not Available " + "(the implementation class is not present)");
                    }
                    catch (NoClassDefFoundError e) {
                        final String pkg = e.getMessage().replace('/', '.');
                        out.println(key + " : Missing dependency " + pkg);
                    }
                    catch (LinkageError e4) {
                        out.println(key + " : Initialization error");
                    }
                }
                if (props.size() == 0) {
                    out.println("All defined tasks are available");
                }
                else {
                    out.println("A task being missing/unavailable should only matter if you are trying to use it");
                }
            }
            catch (IOException e2) {
                out.println(e2.getMessage());
            }
        }
    }
    
    private static void doReportParserInfo(final PrintStream out) {
        final String parserName = getXMLParserName();
        final String parserLocation = getXMLParserLocation();
        printParserInfo(out, "XML Parser", parserName, parserLocation);
        printParserInfo(out, "Namespace-aware parser", getNamespaceParserName(), getNamespaceParserLocation());
    }
    
    private static void doReportXSLTProcessorInfo(final PrintStream out) {
        final String processorName = getXSLTProcessorName();
        final String processorLocation = getXSLTProcessorLocation();
        printParserInfo(out, "XSLT Processor", processorName, processorLocation);
    }
    
    private static void printParserInfo(final PrintStream out, final String parserType, String parserName, String parserLocation) {
        if (parserName == null) {
            parserName = "unknown";
        }
        if (parserLocation == null) {
            parserLocation = "unknown";
        }
        out.println(parserType + " : " + parserName);
        out.println(parserType + " Location: " + parserLocation);
    }
    
    private static void doReportTempDir(final PrintStream out) {
        final String tempdir = System.getProperty("java.io.tmpdir");
        if (tempdir == null) {
            out.println("Warning: java.io.tmpdir is undefined");
            return;
        }
        out.println("Temp dir is " + tempdir);
        final File tempDirectory = new File(tempdir);
        if (!tempDirectory.exists()) {
            out.println("Warning, java.io.tmpdir directory does not exist: " + tempdir);
            return;
        }
        final long now = System.currentTimeMillis();
        File tempFile = null;
        FileOutputStream fileout = null;
        FileInputStream filein = null;
        try {
            tempFile = File.createTempFile("diag", "txt", tempDirectory);
            fileout = new FileOutputStream(tempFile);
            final byte[] buffer = new byte[1024];
            for (int i = 0; i < 32; ++i) {
                fileout.write(buffer);
            }
            fileout.close();
            fileout = null;
            Thread.sleep(1000L);
            filein = new FileInputStream(tempFile);
            int total = 0;
            int read = 0;
            while ((read = filein.read(buffer, 0, 1024)) > 0) {
                total += read;
            }
            filein.close();
            filein = null;
            final long filetime = tempFile.lastModified();
            final long drift = filetime - now;
            tempFile.delete();
            out.print("Temp dir is writeable");
            if (total != 32768) {
                out.println(", but seems to be full.  Wrote 32768but could only read " + total + " bytes.");
            }
            else {
                out.println();
            }
            out.println("Temp dir alignment with system clock is " + drift + " ms");
            if (Math.abs(drift) > 10000L) {
                out.println("Warning: big clock drift -maybe a network filesystem");
            }
        }
        catch (IOException e) {
            ignoreThrowable(e);
            out.println("Failed to create a temporary file in the temp dir " + tempdir);
            out.println("File  " + tempFile + " could not be created/written to");
        }
        catch (InterruptedException e2) {
            ignoreThrowable(e2);
            out.println("Failed to check whether tempdir is writable");
        }
        finally {
            FileUtils.close(fileout);
            FileUtils.close(filein);
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
    
    private static void doReportLocale(final PrintStream out) {
        final Calendar cal = Calendar.getInstance();
        final TimeZone tz = cal.getTimeZone();
        out.println("Timezone " + tz.getDisplayName() + " offset=" + tz.getOffset(cal.get(0), cal.get(1), cal.get(2), cal.get(5), cal.get(7), ((cal.get(11) * 60 + cal.get(12)) * 60 + cal.get(13)) * 1000 + cal.get(14)));
    }
    
    private static void printProperty(final PrintStream out, final String key) {
        final String value = getProperty(key);
        if (value != null) {
            out.print(key);
            out.print(" = ");
            out.print('\"');
            out.print(value);
            out.println('\"');
        }
    }
    
    private static void doReportProxy(final PrintStream out) {
        printProperty(out, "http.proxyHost");
        printProperty(out, "http.proxyPort");
        printProperty(out, "http.proxyUser");
        printProperty(out, "http.proxyPassword");
        printProperty(out, "http.nonProxyHosts");
        printProperty(out, "https.proxyHost");
        printProperty(out, "https.proxyPort");
        printProperty(out, "https.nonProxyHosts");
        printProperty(out, "ftp.proxyHost");
        printProperty(out, "ftp.proxyPort");
        printProperty(out, "ftp.nonProxyHosts");
        printProperty(out, "socksProxyHost");
        printProperty(out, "socksProxyPort");
        printProperty(out, "java.net.socks.username");
        printProperty(out, "java.net.socks.password");
        if (JavaEnvUtils.getJavaVersionNumber() < 15) {
            return;
        }
        printProperty(out, "java.net.useSystemProxies");
        final String proxyDiagClassname = "org.apache.tools.ant.util.java15.ProxyDiagnostics";
        try {
            final Class<?> proxyDiagClass = Class.forName("org.apache.tools.ant.util.java15.ProxyDiagnostics");
            final Object instance = proxyDiagClass.newInstance();
            out.println("Java1.5+ proxy settings:");
            out.println(instance.toString());
        }
        catch (ClassNotFoundException e) {}
        catch (IllegalAccessException e2) {}
        catch (InstantiationException e3) {}
        catch (NoClassDefFoundError noClassDefFoundError) {}
    }
}
