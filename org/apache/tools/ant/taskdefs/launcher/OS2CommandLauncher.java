package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.*;
import java.io.*;

public class OS2CommandLauncher extends CommandLauncherProxy
{
    public OS2CommandLauncher(final CommandLauncher launcher) {
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
        final int preCmdLength = 7;
        final String cmdDir = commandDir.getAbsolutePath();
        final String[] newcmd = new String[cmd.length + 7];
        newcmd[0] = "cmd";
        newcmd[1] = "/c";
        newcmd[2] = cmdDir.substring(0, 2);
        newcmd[3] = "&&";
        newcmd[4] = "cd";
        newcmd[5] = cmdDir.substring(2);
        newcmd[6] = "&&";
        System.arraycopy(cmd, 0, newcmd, 7, cmd.length);
        return this.exec(project, newcmd, env);
    }
}
