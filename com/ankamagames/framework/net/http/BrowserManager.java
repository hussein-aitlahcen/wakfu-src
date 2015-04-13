package com.ankamagames.framework.net.http;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.net.*;
import java.text.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;

public final class BrowserManager
{
    private static final Logger m_logger;
    private static final BrowserManager m_instance;
    public static String[] exec;
    
    private static BrowserManager getInstance() {
        return BrowserManager.m_instance;
    }
    
    public static final boolean openUrlInBrowser(final String url) {
        try {
            if (BrowserManager.exec == null || BrowserManager.exec.length == 0) {
                if (OS.getCurrentOS() != OS.MAC) {
                    throw new IOException();
                }
                boolean success = false;
                try {
                    Class nSWorkspace;
                    if (new File("/System/Library/Java/com/apple/cocoa/application/NSWorkspace.class").exists()) {
                        final ClassLoader classLoader = new URLClassLoader(new URL[] { new File("/System/Library/Java").toURL() });
                        nSWorkspace = Class.forName("com.apple.cocoa.application.NSWorkspace", true, classLoader);
                    }
                    else {
                        nSWorkspace = Class.forName("com.apple.cocoa.application.NSWorkspace");
                    }
                    final Method sharedWorkspace = nSWorkspace.getMethod("sharedWorkspace", (Class[])new Class[0]);
                    final Object workspace = sharedWorkspace.invoke(null, new Object[0]);
                    final Method openURL = nSWorkspace.getMethod("openURL", Class.forName("java.net.URL"));
                    success = (boolean)openURL.invoke(workspace, new URL(url));
                }
                catch (Exception ex) {}
                if (!success) {
                    try {
                        final Class mrjFileUtils = Class.forName("com.apple.mrj.MRJFileUtils");
                        final Method openURL2 = mrjFileUtils.getMethod("openURL", Class.forName("java.lang.String"));
                        openURL2.invoke(null, url);
                    }
                    catch (Exception x) {
                        throw new IOException();
                    }
                }
            }
            else {
                new URL(url);
                final String[] messageArray = { encode(url) };
                String command = null;
                boolean found = false;
                try {
                    for (int i = 0; i < BrowserManager.exec.length && !found; ++i) {
                        try {
                            command = MessageFormat.format(BrowserManager.exec[i], (Object[])messageArray);
                            final Vector argsVector = new Vector();
                            final BrowserCommandLexer lex = new BrowserCommandLexer(new StringReader(command));
                            String t;
                            while ((t = lex.getNextToken()) != null) {
                                argsVector.add(t);
                            }
                            String[] args = new String[argsVector.size()];
                            args = argsVector.toArray(args);
                            boolean useShortCut = false;
                            if (args[0].equals("rundll32") && args[1].equals("url.dll,FileProtocolHandler")) {
                                if (args[2].startsWith("file:/")) {
                                    if (args[2].charAt(6) != '/') {
                                        args[2] = "file://" + args[2].substring(6);
                                    }
                                    if (args[2].charAt(7) != '/') {
                                        args[2] = "file:///" + args[2].substring(7);
                                    }
                                    useShortCut = true;
                                }
                                else if (args[2].toLowerCase().endsWith("html") || args[2].toLowerCase().endsWith("htm")) {
                                    useShortCut = true;
                                }
                            }
                            if (useShortCut) {
                                File shortcut = File.createTempFile("OpenInBrowser", ".url");
                                shortcut = shortcut.getCanonicalFile();
                                shortcut.deleteOnExit();
                                final PrintWriter out = new PrintWriter(new FileWriter(shortcut));
                                out.println("[InternetShortcut]");
                                out.println("URL=" + args[2]);
                                out.close();
                                args[2] = shortcut.getCanonicalPath();
                            }
                            final Process p = Runtime.getRuntime().exec(args);
                            for (int j = 0; j < 2; ++j) {
                                try {
                                    Thread.currentThread();
                                    Thread.sleep(1000L);
                                }
                                catch (InterruptedException ex2) {}
                            }
                            if (p.exitValue() == 0) {
                                found = true;
                            }
                        }
                        catch (IOException ex3) {}
                    }
                    if (!found) {
                        throw new IOException();
                    }
                }
                catch (IllegalThreadStateException ex4) {}
            }
        }
        catch (Exception e) {
            BrowserManager.m_logger.error((Object)("Impossible d'ouvrir l'url " + url + " dans un browser"), (Throwable)e);
            return false;
        }
        return true;
    }
    
    public static String encode(final String url) {
        final StringBuffer sb = new StringBuffer(url.length());
        for (int i = 0; i < url.length(); ++i) {
            char c = url.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '.' || c == ':' || c == '&' || c == '@' || c == '/' || c == '?' || c == '%' || c == '+' || c == '=' || c == '#' || c == '-' || c == '\\') {
                sb.append(c);
            }
            else {
                c &= '\u00ff';
                if (c < '\u0010') {
                    sb.append("%0" + Integer.toHexString(c));
                }
                else {
                    sb.append("%" + Integer.toHexString(c));
                }
            }
        }
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)BrowserManager.class);
        m_instance = new BrowserManager();
        BrowserManager.exec = null;
        switch (OS.getCurrentOS()) {
            case WINDOWS: {
                BrowserManager.exec = new String[] { "rundll32 url.dll,FileProtocolHandler {0}" };
                break;
            }
            case MAC: {
                final Vector browsers = new Vector();
                try {
                    final Process p = Runtime.getRuntime().exec("which open");
                    if (p.waitFor() == 0) {
                        browsers.add("open {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex) {}
                if (browsers.size() == 0) {
                    BrowserManager.exec = null;
                }
                else {
                    BrowserManager.exec = browsers.toArray(new String[browsers.size()]);
                }
                break;
            }
            case SUNOS: {
                BrowserManager.exec = new String[] { "/usr/dt/bin/sdtwebclient {0}" };
                break;
            }
            case LINUX: {
                final ArrayList<String> list = new ArrayList<String>();
                try {
                    final Process p = Runtime.getRuntime().exec("which x-www-browser");
                    final BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;
                    String goodLine = null;
                    while ((line = is.readLine()) != null) {
                        goodLine = line;
                    }
                    boolean ok = p.waitFor() == 0;
                    if (!ok) {
                        final Process p2 = Runtime.getRuntime().exec("which www-browser");
                        final BufferedReader is2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                        while ((line = is2.readLine()) != null) {
                            goodLine = line;
                        }
                        ok = (p2.waitFor() == 0);
                    }
                    if (ok) {
                        list.add(goodLine + " {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex2) {}
                if (list.isEmpty()) {
                    BrowserManager.exec = null;
                }
                else {
                    BrowserManager.exec = list.toArray(new String[list.size()]);
                }
                break;
            }
            default: {
                final Vector browsers = new Vector();
                try {
                    final Process p = Runtime.getRuntime().exec("which firebird");
                    if (p.waitFor() == 0) {
                        browsers.add("firebird -remote openURL({0})");
                        browsers.add("firebird {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex3) {}
                try {
                    final Process p = Runtime.getRuntime().exec("which mozilla");
                    if (p.waitFor() == 0) {
                        browsers.add("mozilla -remote openURL({0})");
                        browsers.add("mozilla {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex4) {}
                try {
                    final Process p = Runtime.getRuntime().exec("which opera");
                    if (p.waitFor() == 0) {
                        browsers.add("opera -remote openURL({0})");
                        browsers.add("opera {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex5) {}
                try {
                    final Process p = Runtime.getRuntime().exec("which galeon");
                    if (p.waitFor() == 0) {
                        browsers.add("galeon {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex6) {}
                try {
                    final Process p = Runtime.getRuntime().exec("which konqueror");
                    if (p.waitFor() == 0) {
                        browsers.add("konqueror {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex7) {}
                try {
                    final Process p = Runtime.getRuntime().exec("which netscape");
                    if (p.waitFor() == 0) {
                        browsers.add("netscape -remote openURL({0})");
                        browsers.add("netscape {0}");
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex8) {}
                try {
                    Process p = Runtime.getRuntime().exec("which xterm");
                    if (p.waitFor() == 0) {
                        p = Runtime.getRuntime().exec("which lynx");
                        if (p.waitFor() == 0) {
                            browsers.add("xterm -e lynx {0}");
                        }
                    }
                }
                catch (IOException e) {}
                catch (InterruptedException ex9) {}
                if (browsers.size() == 0) {
                    BrowserManager.exec = null;
                    break;
                }
                BrowserManager.exec = browsers.toArray(new String[browsers.size()]);
                break;
            }
        }
    }
}
