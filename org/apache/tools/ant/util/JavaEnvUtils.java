package org.apache.tools.ant.util;

import java.util.*;
import java.io.*;
import org.apache.tools.ant.taskdefs.condition.*;

public final class JavaEnvUtils
{
    private static final boolean IS_DOS;
    private static final boolean IS_NETWARE;
    private static final boolean IS_AIX;
    private static final String JAVA_HOME;
    private static final FileUtils FILE_UTILS;
    private static String javaVersion;
    private static int javaVersionNumber;
    public static final String JAVA_1_0 = "1.0";
    public static final int VERSION_1_0 = 10;
    public static final String JAVA_1_1 = "1.1";
    public static final int VERSION_1_1 = 11;
    public static final String JAVA_1_2 = "1.2";
    public static final int VERSION_1_2 = 12;
    public static final String JAVA_1_3 = "1.3";
    public static final int VERSION_1_3 = 13;
    public static final String JAVA_1_4 = "1.4";
    public static final int VERSION_1_4 = 14;
    public static final String JAVA_1_5 = "1.5";
    public static final int VERSION_1_5 = 15;
    public static final String JAVA_1_6 = "1.6";
    public static final int VERSION_1_6 = 16;
    public static final String JAVA_1_7 = "1.7";
    public static final int VERSION_1_7 = 17;
    public static final String JAVA_1_8 = "1.8";
    public static final int VERSION_1_8 = 18;
    private static boolean kaffeDetected;
    private static boolean classpathDetected;
    private static boolean gijDetected;
    private static boolean harmonyDetected;
    private static Vector<String> jrePackages;
    
    public static String getJavaVersion() {
        return JavaEnvUtils.javaVersion;
    }
    
    public static int getJavaVersionNumber() {
        return JavaEnvUtils.javaVersionNumber;
    }
    
    public static boolean isJavaVersion(final String version) {
        return JavaEnvUtils.javaVersion.equals(version);
    }
    
    public static boolean isAtLeastJavaVersion(final String version) {
        return JavaEnvUtils.javaVersion.compareTo(version) >= 0;
    }
    
    public static boolean isKaffe() {
        return JavaEnvUtils.kaffeDetected;
    }
    
    public static boolean isClasspathBased() {
        return JavaEnvUtils.classpathDetected;
    }
    
    public static boolean isGij() {
        return JavaEnvUtils.gijDetected;
    }
    
    public static boolean isApacheHarmony() {
        return JavaEnvUtils.harmonyDetected;
    }
    
    public static String getJreExecutable(final String command) {
        if (JavaEnvUtils.IS_NETWARE) {
            return command;
        }
        File jExecutable = null;
        if (JavaEnvUtils.IS_AIX) {
            jExecutable = findInDir(JavaEnvUtils.JAVA_HOME + "/sh", command);
        }
        if (jExecutable == null) {
            jExecutable = findInDir(JavaEnvUtils.JAVA_HOME + "/bin", command);
        }
        if (jExecutable != null) {
            return jExecutable.getAbsolutePath();
        }
        return addExtension(command);
    }
    
    public static String getJdkExecutable(final String command) {
        if (JavaEnvUtils.IS_NETWARE) {
            return command;
        }
        File jExecutable = null;
        if (JavaEnvUtils.IS_AIX) {
            jExecutable = findInDir(JavaEnvUtils.JAVA_HOME + "/../sh", command);
        }
        if (jExecutable == null) {
            jExecutable = findInDir(JavaEnvUtils.JAVA_HOME + "/../bin", command);
        }
        if (jExecutable != null) {
            return jExecutable.getAbsolutePath();
        }
        return getJreExecutable(command);
    }
    
    private static String addExtension(final String command) {
        return command + (JavaEnvUtils.IS_DOS ? ".exe" : "");
    }
    
    private static File findInDir(final String dirName, final String commandName) {
        final File dir = JavaEnvUtils.FILE_UTILS.normalize(dirName);
        File executable = null;
        if (dir.exists()) {
            executable = new File(dir, addExtension(commandName));
            if (!executable.exists()) {
                executable = null;
            }
        }
        return executable;
    }
    
    private static void buildJrePackages() {
        JavaEnvUtils.jrePackages = new Vector<String>();
        switch (JavaEnvUtils.javaVersionNumber) {
            case 15:
            case 16:
            case 17:
            case 18: {
                JavaEnvUtils.jrePackages.addElement("com.sun.org.apache");
            }
            case 14: {
                if (JavaEnvUtils.javaVersionNumber == 14) {
                    JavaEnvUtils.jrePackages.addElement("org.apache.crimson");
                    JavaEnvUtils.jrePackages.addElement("org.apache.xalan");
                    JavaEnvUtils.jrePackages.addElement("org.apache.xml");
                    JavaEnvUtils.jrePackages.addElement("org.apache.xpath");
                }
                JavaEnvUtils.jrePackages.addElement("org.ietf.jgss");
                JavaEnvUtils.jrePackages.addElement("org.w3c.dom");
                JavaEnvUtils.jrePackages.addElement("org.xml.sax");
            }
            case 13: {
                JavaEnvUtils.jrePackages.addElement("org.omg");
                JavaEnvUtils.jrePackages.addElement("com.sun.corba");
                JavaEnvUtils.jrePackages.addElement("com.sun.jndi");
                JavaEnvUtils.jrePackages.addElement("com.sun.media");
                JavaEnvUtils.jrePackages.addElement("com.sun.naming");
                JavaEnvUtils.jrePackages.addElement("com.sun.org.omg");
                JavaEnvUtils.jrePackages.addElement("com.sun.rmi");
                JavaEnvUtils.jrePackages.addElement("sunw.io");
                JavaEnvUtils.jrePackages.addElement("sunw.util");
            }
            case 12: {
                JavaEnvUtils.jrePackages.addElement("com.sun.java");
                JavaEnvUtils.jrePackages.addElement("com.sun.image");
                break;
            }
        }
        JavaEnvUtils.jrePackages.addElement("sun");
        JavaEnvUtils.jrePackages.addElement("java");
        JavaEnvUtils.jrePackages.addElement("javax");
    }
    
    public static Vector getJrePackageTestCases() {
        final Vector tests = new Vector();
        tests.addElement("java.lang.Object");
        switch (JavaEnvUtils.javaVersionNumber) {
            case 15:
            case 16:
            case 17:
            case 18: {
                tests.addElement("com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl ");
            }
            case 14: {
                tests.addElement("sun.audio.AudioPlayer");
                if (JavaEnvUtils.javaVersionNumber == 14) {
                    tests.addElement("org.apache.crimson.parser.ContentModel");
                    tests.addElement("org.apache.xalan.processor.ProcessorImport");
                    tests.addElement("org.apache.xml.utils.URI");
                    tests.addElement("org.apache.xpath.XPathFactory");
                }
                tests.addElement("org.ietf.jgss.Oid");
                tests.addElement("org.w3c.dom.Attr");
                tests.addElement("org.xml.sax.XMLReader");
            }
            case 13: {
                tests.addElement("org.omg.CORBA.Any");
                tests.addElement("com.sun.corba.se.internal.corba.AnyImpl");
                tests.addElement("com.sun.jndi.ldap.LdapURL");
                tests.addElement("com.sun.media.sound.Printer");
                tests.addElement("com.sun.naming.internal.VersionHelper");
                tests.addElement("com.sun.org.omg.CORBA.Initializer");
                tests.addElement("sunw.io.Serializable");
                tests.addElement("sunw.util.EventListener");
            }
            case 12: {
                tests.addElement("javax.accessibility.Accessible");
                tests.addElement("sun.misc.BASE64Encoder");
                tests.addElement("com.sun.image.codec.jpeg.JPEGCodec");
                break;
            }
        }
        tests.addElement("sun.reflect.SerializationConstructorAccessorImpl");
        tests.addElement("sun.net.www.http.HttpClient");
        tests.addElement("sun.audio.AudioPlayer");
        return tests;
    }
    
    public static Vector<String> getJrePackages() {
        if (JavaEnvUtils.jrePackages == null) {
            buildJrePackages();
        }
        return JavaEnvUtils.jrePackages;
    }
    
    public static File createVmsJavaOptionFile(final String[] cmd) throws IOException {
        final File script = JavaEnvUtils.FILE_UTILS.createTempFile("ANT", ".JAVA_OPTS", null, false, true);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(script));
            for (int i = 0; i < cmd.length; ++i) {
                out.write(cmd[i]);
                out.newLine();
            }
            FileUtils.close(out);
        }
        finally {
            FileUtils.close(out);
        }
        return script;
    }
    
    public static String getJavaHome() {
        return JavaEnvUtils.JAVA_HOME;
    }
    
    static {
        IS_DOS = Os.isFamily("dos");
        IS_NETWARE = Os.isName("netware");
        IS_AIX = Os.isName("aix");
        JAVA_HOME = System.getProperty("java.home");
        FILE_UTILS = FileUtils.getFileUtils();
        try {
            JavaEnvUtils.javaVersion = "1.0";
            JavaEnvUtils.javaVersionNumber = 10;
            Class.forName("java.lang.Void");
            JavaEnvUtils.javaVersion = "1.1";
            ++JavaEnvUtils.javaVersionNumber;
            Class.forName("java.lang.ThreadLocal");
            JavaEnvUtils.javaVersion = "1.2";
            ++JavaEnvUtils.javaVersionNumber;
            Class.forName("java.lang.StrictMath");
            JavaEnvUtils.javaVersion = "1.3";
            ++JavaEnvUtils.javaVersionNumber;
            Class.forName("java.lang.CharSequence");
            JavaEnvUtils.javaVersion = "1.4";
            ++JavaEnvUtils.javaVersionNumber;
            Class.forName("java.net.Proxy");
            JavaEnvUtils.javaVersion = "1.5";
            ++JavaEnvUtils.javaVersionNumber;
            Class.forName("java.net.CookieStore");
            JavaEnvUtils.javaVersion = "1.6";
            ++JavaEnvUtils.javaVersionNumber;
            Class.forName("java.nio.file.FileSystem");
            JavaEnvUtils.javaVersion = "1.7";
            ++JavaEnvUtils.javaVersionNumber;
            Class.forName("java.lang.reflect.Executable");
            JavaEnvUtils.javaVersion = "1.8";
            ++JavaEnvUtils.javaVersionNumber;
        }
        catch (Throwable t) {}
        JavaEnvUtils.kaffeDetected = false;
        try {
            Class.forName("kaffe.util.NotImplemented");
            JavaEnvUtils.kaffeDetected = true;
        }
        catch (Throwable t2) {}
        JavaEnvUtils.classpathDetected = false;
        try {
            Class.forName("gnu.classpath.Configuration");
            JavaEnvUtils.classpathDetected = true;
        }
        catch (Throwable t3) {}
        JavaEnvUtils.gijDetected = false;
        try {
            Class.forName("gnu.gcj.Core");
            JavaEnvUtils.gijDetected = true;
        }
        catch (Throwable t4) {}
        JavaEnvUtils.harmonyDetected = false;
        try {
            Class.forName("org.apache.harmony.luni.util.Base64");
            JavaEnvUtils.harmonyDetected = true;
        }
        catch (Throwable t5) {}
    }
}
