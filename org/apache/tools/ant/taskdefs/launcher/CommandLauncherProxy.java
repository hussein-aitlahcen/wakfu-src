package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.*;
import java.io.*;

public class CommandLauncherProxy extends CommandLauncher
{
    private final CommandLauncher myLauncher;
    
    protected CommandLauncherProxy(final CommandLauncher launcher) {
        super();
        this.myLauncher = launcher;
    }
    
    public Process exec(final Project project, final String[] cmd, final String[] env) throws IOException {
        return this.myLauncher.exec(project, cmd, env);
    }
}
