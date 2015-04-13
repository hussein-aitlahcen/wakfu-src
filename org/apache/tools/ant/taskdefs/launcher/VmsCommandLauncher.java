package org.apache.tools.ant.taskdefs.launcher;

import org.apache.tools.ant.*;
import java.io.*;
import org.apache.tools.ant.util.*;

public class VmsCommandLauncher extends Java13CommandLauncher
{
    public Process exec(final Project project, final String[] cmd, final String[] env) throws IOException {
        final File cmdFile = this.createCommandFile(cmd, env);
        final Process p = super.exec(project, new String[] { cmdFile.getPath() }, env);
        this.deleteAfter(cmdFile, p);
        return p;
    }
    
    public Process exec(final Project project, final String[] cmd, final String[] env, final File workingDir) throws IOException {
        final File cmdFile = this.createCommandFile(cmd, env);
        final Process p = super.exec(project, new String[] { cmdFile.getPath() }, env, workingDir);
        this.deleteAfter(cmdFile, p);
        return p;
    }
    
    private File createCommandFile(final String[] cmd, final String[] env) throws IOException {
        final File script = VmsCommandLauncher.FILE_UTILS.createTempFile("ANT", ".COM", null, true, true);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(script));
            if (env != null) {
                for (int i = 0; i < env.length; ++i) {
                    final int eqIndex = env[i].indexOf(61);
                    if (eqIndex != -1) {
                        out.write("$ DEFINE/NOLOG ");
                        out.write(env[i].substring(0, eqIndex));
                        out.write(" \"");
                        out.write(env[i].substring(eqIndex + 1));
                        out.write(34);
                        out.newLine();
                    }
                }
            }
            out.write("$ " + cmd[0]);
            for (int j = 1; j < cmd.length; ++j) {
                out.write(" -");
                out.newLine();
                out.write(cmd[j]);
            }
        }
        finally {
            FileUtils.close(out);
        }
        return script;
    }
    
    private void deleteAfter(final File f, final Process p) {
        new Thread() {
            public void run() {
                try {
                    p.waitFor();
                }
                catch (InterruptedException ex) {}
                FileUtils.delete(f);
            }
        }.start();
    }
}
