package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.*;
import java.io.*;

public class WinNTCommandLauncher extends CommandLauncherProxy
{
    public WinNTCommandLauncher(final CommandLauncher launcher) {
        super(launcher);
    }
    
    public Process exec(final Project project, final String[] cmd, final String[] env, final File workingDir) throws IOException {
        File commandDir = workingDir;
        if (workingDir == null) {
            if (project == null) {
                return this.exec(project, cmd, env);
            }
            commandDir = project.getBaseDir();
        }
        final int preCmdLength = 6;
        final String[] newcmd = new String[cmd.length + 6];
        newcmd[0] = "cmd";
        newcmd[1] = "/c";
        newcmd[2] = "cd";
        newcmd[3] = "/d";
        newcmd[4] = commandDir.getAbsolutePath();
        newcmd[5] = "&&";
        System.arraycopy(cmd, 0, newcmd, 6, cmd.length);
        return this.exec(project, newcmd, env);
    }
}
