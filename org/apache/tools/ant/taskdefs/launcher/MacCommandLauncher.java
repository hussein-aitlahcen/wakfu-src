package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.*;
import java.io.*;
import java.util.*;

public class MacCommandLauncher extends CommandLauncherProxy
{
    public MacCommandLauncher(final CommandLauncher launcher) {
        super(launcher);
    }
    
    public Process exec(final Project project, final String[] cmd, final String[] env, final File workingDir) throws IOException {
        if (workingDir == null) {
            return this.exec(project, cmd, env);
        }
        ((Hashtable<String, String>)System.getProperties()).put("user.dir", workingDir.getAbsolutePath());
        try {
            return this.exec(project, cmd, env);
        }
        finally {
            ((Hashtable<String, String>)System.getProperties()).put("user.dir", System.getProperty("user.dir"));
        }
    }
}
