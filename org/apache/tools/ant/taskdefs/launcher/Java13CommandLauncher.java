package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.types.*;
import java.io.*;
import org.apache.tools.ant.*;

public class Java13CommandLauncher extends CommandLauncher
{
    public Process exec(final Project project, final String[] cmd, final String[] env, final File workingDir) throws IOException {
        try {
            if (project != null) {
                project.log("Execute:Java13CommandLauncher: " + Commandline.describeCommand(cmd), 4);
            }
            return Runtime.getRuntime().exec(cmd, env, workingDir);
        }
        catch (IOException ioex) {
            throw ioex;
        }
        catch (Exception exc) {
            throw new BuildException("Unable to execute command", exc);
        }
    }
}
