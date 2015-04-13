package org.apache.tools.ant.launch;

import java.net.*;
import java.text.*;
import java.util.*;
import java.io.*;

public final class Locator
{
    private static final int NIBBLE = 4;
    private static final int NIBBLE_MASK = 15;
    private static final int ASCII_SIZE = 128;
    private static final int BYTE_SIZE = 256;
    private static final int WORD = 16;
    private static final int SPACE = 32;
    private static final int DEL = 127;
    public static final String URI_ENCODING = "UTF-8";
    private static boolean[] gNeedEscaping;
    private static char[] gAfterEscaping1;
    private static char[] gAfterEscaping2;
    private static char[] gHexChs;
    public static final String ERROR_NOT_FILE_URI = "Can only handle valid file: URIs, not ";
    static /* synthetic */ Class class$org$apache$tools$ant$launch$Locator;
    
    public static File getClassSource(final Class c) {
        final String classResource = c.getName().replace('.', '/') + ".class";
        return getResourceSource(c.getClassLoader(), classResource);
    }
    
    public static File getResourceSource(ClassLoader c, final String resource) {
        if (c == null) {
            c = ((Locator.class$org$apache$tools$ant$launch$Locator == null) ? (Locator.class$org$apache$tools$ant$launch$Locator = class$("org.apache.tools.ant.launch.Locator")) : Locator.class$org$apache$tools$ant$launch$Locator).getClassLoader();
        }
        URL url = null;
        if (c == null) {
            url = ClassLoader.getSystemResource(resource);
        }
        else {
            url = c.getResource(resource);
        }
        if (url != null) {
            final String u = url.toString();
            try {
                if (u.startsWith("jar:file:")) {
                    return new File(fromJarURI(u));
                }
                if (u.startsWith("file:")) {
                    final int tail = u.indexOf(resource);
                    final String dirName = u.substring(0, tail);
                    return new File(fromURI(dirName));
                }
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
    
    public static String fromURI(final String uri) {
        return fromURIJava13(uri);
    }
    
    private static String fromURIJava13(String uri) {
        URL url = null;
        try {
            url = new URL(uri);
        }
        catch (MalformedURLException ex) {}
        if (url == null || !"file".equals(url.getProtocol())) {
            throw new IllegalArgumentException("Can only handle valid file: URIs, not " + uri);
        }
        final StringBuffer buf = new StringBuffer(url.getHost());
        if (buf.length() > 0) {
            buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
        }
        final String file = url.getFile();
        final int queryPos = file.indexOf(63);
        buf.append((queryPos < 0) ? file : file.substring(0, queryPos));
        uri = buf.toString().replace('/', File.separatorChar);
        if (File.pathSeparatorChar == ';' && uri.startsWith("\\") && uri.length() > 2 && Character.isLetter(uri.charAt(1)) && uri.lastIndexOf(58) > -1) {
            uri = uri.substring(1);
        }
        String path = null;
        try {
            path = decodeUri(uri);
            final String cwd = System.getProperty("user.dir");
            final int posi = cwd.indexOf(58);
            final boolean pathStartsWithFileSeparator = path.startsWith(File.separator);
            final boolean pathStartsWithUNC = path.startsWith("" + File.separator + File.separator);
            if (posi > 0 && pathStartsWithFileSeparator && !pathStartsWithUNC) {
                path = cwd.substring(0, posi + 1) + path;
            }
        }
        catch (UnsupportedEncodingException exc) {
            throw new IllegalStateException("Could not convert URI " + uri + " to path: " + exc.getMessage());
        }
        return path;
    }
    
    public static String fromJarURI(final String uri) {
        final int pling = uri.indexOf(33);
        final String jarName = uri.substring("jar:".length(), pling);
        return fromURI(jarName);
    }
    
    public static String decodeUri(final String uri) throws UnsupportedEncodingException {
        if (uri.indexOf(37) == -1) {
            return uri;
        }
        final ByteArrayOutputStream sb = new ByteArrayOutputStream(uri.length());
        final CharacterIterator iter = new StringCharacterIterator(uri);
        for (char c = iter.first(); c != '\uffff'; c = iter.next()) {
            if (c == '%') {
                final char c2 = iter.next();
                if (c2 != '\uffff') {
                    final int i1 = Character.digit(c2, 16);
                    final char c3 = iter.next();
                    if (c3 != '\uffff') {
                        final int i2 = Character.digit(c3, 16);
                        sb.write((char)((i1 << 4) + i2));
                    }
                }
            }
            else {
                sb.write(c);
            }
        }
        return sb.toString("UTF-8");
    }
    
    public static String encodeURI(final String path) throws UnsupportedEncodingException {
        int i = 0;
        final int len = path.length();
        int ch = 0;
        StringBuffer sb = null;
        while (i < len) {
            ch = path.charAt(i);
            if (ch >= 128) {
                break;
            }
            if (Locator.gNeedEscaping[ch]) {
                if (sb == null) {
                    sb = new StringBuffer(path.substring(0, i));
                }
                sb.append('%');
                sb.append(Locator.gAfterEscaping1[ch]);
                sb.append(Locator.gAfterEscaping2[ch]);
            }
            else if (sb != null) {
                sb.append((char)ch);
            }
            ++i;
        }
        if (i < len) {
            if (sb == null) {
                sb = new StringBuffer(path.substring(0, i));
            }
            byte[] bytes = null;
            bytes = path.substring(i).getBytes("UTF-8");
            for (final byte b : bytes) {
                if (b < 0) {
                    ch = b + 256;
                    sb.append('%');
                    sb.append(Locator.gHexChs[ch >> 4]);
                    sb.append(Locator.gHexChs[ch & 0xF]);
                }
                else if (Locator.gNeedEscaping[b]) {
                    sb.append('%');
                    sb.append(Locator.gAfterEscaping1[b]);
                    sb.append(Locator.gAfterEscaping2[b]);
                }
                else {
                    sb.append((char)b);
                }
            }
        }
        return (sb == null) ? path : sb.toString();
    }
    
    public static URL fileToURL(final File file) throws MalformedURLException {
        try {
            return new URL(encodeURI(file.toURL().toString()));
        }
        catch (UnsupportedEncodingException ex) {
            throw new MalformedURLException(ex.toString());
        }
    }
    
    public static File getToolsJar() {
        boolean toolsJarAvailable = false;
        try {
            Class.forName("com.sun.tools.javac.Main");
            toolsJarAvailable = true;
        }
        catch (Exception e) {
            try {
                Class.forName("sun.tools.javac.Main");
                toolsJarAvailable = true;
            }
            catch (Exception ex) {}
        }
        if (toolsJarAvailable) {
            return null;
        }
        final String libToolsJar = File.separator + "lib" + File.separator + "tools.jar";
        String javaHome = System.getProperty("java.home");
        File toolsJar = new File(javaHome + libToolsJar);
        if (toolsJar.exists()) {
            return toolsJar;
        }
        if (javaHome.toLowerCase(Locale.US).endsWith(File.separator + "jre")) {
            javaHome = javaHome.substring(0, javaHome.length() - "/jre".length());
            toolsJar = new File(javaHome + libToolsJar);
        }
        if (!toolsJar.exists()) {
            System.out.println("Unable to locate tools.jar. Expected to find it in " + toolsJar.getPath());
            return null;
        }
        return toolsJar;
    }
    
    public static URL[] getLocationURLs(final File location) throws MalformedURLException {
        return getLocationURLs(location, new String[] { ".jar" });
    }
    
    public static URL[] getLocationURLs(final File location, final String[] extensions) throws MalformedURLException {
        URL[] urls = new URL[0];
        if (!location.exists()) {
            return urls;
        }
        if (!location.isDirectory()) {
            urls = new URL[] { null };
            final String path = location.getPath();
            final String littlePath = path.toLowerCase(Locale.US);
            for (int i = 0; i < extensions.length; ++i) {
                if (littlePath.endsWith(extensions[i])) {
                    urls[0] = fileToURL(location);
                    break;
                }
            }
            return urls;
        }
        final File[] matches = location.listFiles(new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                final String littleName = name.toLowerCase(Locale.US);
                for (int i = 0; i < extensions.length; ++i) {
                    if (littleName.endsWith(extensions[i])) {
                        return true;
                    }
                }
                return false;
            }
        });
        urls = new URL[matches.length];
        for (int j = 0; j < matches.length; ++j) {
            urls[j] = fileToURL(matches[j]);
        }
        return urls;
    }
    
    static /* synthetic */ Class class$(final String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x) {
            throw new NoClassDefFoundError(x.getMessage());
        }
    }
    
    static {
        Locator.gNeedEscaping = new boolean[128];
        Locator.gAfterEscaping1 = new char[128];
        Locator.gAfterEscaping2 = new char[128];
        Locator.gHexChs = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        for (int i = 0; i < 32; ++i) {
            Locator.gNeedEscaping[i] = true;
            Locator.gAfterEscaping1[i] = Locator.gHexChs[i >> 4];
            Locator.gAfterEscaping2[i] = Locator.gHexChs[i & 0xF];
        }
        Locator.gNeedEscaping[127] = true;
        Locator.gAfterEscaping1[127] = '7';
        Locator.gAfterEscaping2[127] = 'F';
        for (final char ch : new char[] { ' ', '<', '>', '#', '%', '\"', '{', '}', '|', '\\', '^', '~', '[', ']', '`' }) {
            Locator.gNeedEscaping[ch] = true;
            Locator.gAfterEscaping1[ch] = Locator.gHexChs[ch >> 4];
            Locator.gAfterEscaping2[ch] = Locator.gHexChs[ch & '\u000f'];
        }
    }
}
