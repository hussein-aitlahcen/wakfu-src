package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.io.*;
import org.apache.tools.ant.taskdefs.condition.*;

public class CommandLauncher
{
    protected static final FileUtils FILE_UTILS;
    private static CommandLauncher vmLauncher;
    private static CommandLauncher shellLauncher;
    
    public Process exec(final Project project, final String[] cmd, final String[] env) throws IOException {
        if (project != null) {
            project.log("Execute:CommandLauncher: " + Commandline.describeCommand(cmd), 4);
        }
        return Runtime.getRuntime().exec(cmd, env);
    }
    
    public Process exec(final Project project, final String[] cmd, final String[] env, final File workingDir) throws IOException {
        if (workingDir == null) {
            return this.exec(project, cmd, env);
        }
        throw new IOException("Cannot execute a process in different directory under this JVM");
    }
    
    public static CommandLauncher getShellLauncher(final Project project) {
        CommandLauncher launcher = extractLauncher("ant.shellLauncher", project);
        if (launcher == null) {
            launcher = CommandLauncher.shellLauncher;
        }
        return launcher;
    }
    
    public static CommandLauncher getVMLauncher(final Project project) {
        CommandLauncher launcher = extractLauncher("ant.vmLauncher", project);
        if (launcher == null) {
            launcher = CommandLauncher.vmLauncher;
        }
        return launcher;
    }
    
    private static CommandLauncher extractLauncher(final String referenceName, final Project project) {
        CommandLauncher launcher = null;
        if (project != null) {
            launcher = project.getReference(referenceName);
        }
        if (launcher == null) {
            launcher = getSystemLauncher(referenceName);
        }
        return launcher;
    }
    
    private static CommandLauncher getSystemLauncher(final String launcherRefId) {
        CommandLauncher launcher = null;
        final String launcherClass = System.getProperty(launcherRefId);
        if (launcherClass != null) {
            try {
                launcher = (CommandLauncher)Class.forName(launcherClass).newInstance();
            }
            catch (InstantiationException e) {
                System.err.println("Could not instantiate launcher class " + launcherClass + ": " + e.getMessage());
            }
            catch (IllegalAccessException e2) {
                System.err.println("Could not instantiate launcher class " + launcherClass + ": " + e2.getMessage());
            }
            catch (ClassNotFoundException e3) {
                System.err.println("Could not instantiate launcher class " + launcherClass + ": " + e3.getMessage());
            }
        }
        return launcher;
    }
    
    public static void setVMLauncher(final Project project, final CommandLauncher launcher) {
        if (project != null) {
            project.addReference("ant.vmLauncher", launcher);
        }
    }
    
    public static void setShellLauncher(final Project project, final CommandLauncher launcher) {
        if (project != null) {
            project.addReference("ant.shellLauncher", launcher);
        }
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
        CommandLauncher.vmLauncher = null;
        CommandLauncher.shellLauncher = null;
        if (!Os.isFamily("os/2")) {
            CommandLauncher.vmLauncher = new Java13CommandLauncher();
        }
        if (Os.isFamily("mac") && !Os.isFamily("unix")) {
            CommandLauncher.shellLauncher = new MacCommandLauncher(new CommandLauncher());
        }
        else if (Os.isFamily("os/2")) {
            CommandLauncher.shellLauncher = new OS2CommandLauncher(new CommandLauncher());
        }
        else if (Os.isFamily("windows")) {
            final CommandLauncher baseLauncher = new CommandLauncher();
            if (!Os.isFamily("win9x")) {
                CommandLauncher.shellLauncher = new WinNTCommandLauncher(baseLauncher);
            }
            else {
                CommandLauncher.shellLauncher = new ScriptCommandLauncher("bin/antRun.bat", baseLauncher);
            }
        }
        else if (Os.isFamily("netware")) {
            final CommandLauncher baseLauncher = new CommandLauncher();
            CommandLauncher.shellLauncher = new PerlScriptCommandLauncher("bin/antRun.pl", baseLauncher);
        }
        else if (Os.isFamily("openvms")) {
            CommandLauncher.shellLauncher = new VmsCommandLauncher();
        }
        else {
            CommandLauncher.shellLauncher = new ScriptCommandLauncher("bin/antRun", new CommandLauncher());
        }
    }
}
