package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.*;
import java.io.*;

public class ScriptCommandLauncher extends CommandLauncherProxy
{
    private final String myScript;
    
    public ScriptCommandLauncher(final String script, final CommandLauncher launcher) {
        super(launcher);
        this.myScript = script;
    }
    
    public Process exec(final Project project, final String[] cmd, final String[] env, final File workingDir) throws IOException {
        if (project == null) {
            if (workingDir == null) {
                return this.exec(project, cmd, env);
            }
            throw new IOException("Cannot locate antRun script: No project provided");
        }
        else {
            final String antHome = project.getProperty("ant.home");
            if (antHome == null) {
                throw new IOException("Cannot locate antRun script: Property 'ant.home' not found");
            }
            final String antRun = ScriptCommandLauncher.FILE_UTILS.resolveFile(project.getBaseDir(), antHome + File.separator + this.myScript).toString();
            File commandDir;
            if ((commandDir = workingDir) == null) {
                commandDir = project.getBaseDir();
            }
            final String[] newcmd = new String[cmd.length + 2];
            newcmd[0] = antRun;
            newcmd[1] = commandDir.getAbsolutePath();
            System.arraycopy(cmd, 0, newcmd, 2, cmd.length);
            return this.exec(project, newcmd, env);
        }
    }
}
