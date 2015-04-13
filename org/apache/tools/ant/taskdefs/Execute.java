package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.taskdefs.condition.*;
import java.io.*;
import org.apache.tools.ant.taskdefs.launcher.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;
import java.util.*;

public class Execute
{
    private static final int ONE_SECOND = 1000;
    public static final int INVALID = Integer.MAX_VALUE;
    private String[] cmdl;
    private String[] env;
    private int exitValue;
    private ExecuteStreamHandler streamHandler;
    private final ExecuteWatchdog watchdog;
    private File workingDirectory;
    private Project project;
    private boolean newEnvironment;
    private boolean useVMLauncher;
    private static String antWorkingDirectory;
    private static Map<String, String> procEnvironment;
    private static ProcessDestroyer processDestroyer;
    private static boolean environmentCaseInSensitive;
    
    @Deprecated
    public void setSpawn(final boolean spawn) {
    }
    
    public static synchronized Map<String, String> getEnvironmentVariables() {
        if (Execute.procEnvironment != null) {
            return Execute.procEnvironment;
        }
        if (!Os.isFamily("openvms")) {
            try {
                return Execute.procEnvironment = System.getenv();
            }
            catch (Exception x) {
                x.printStackTrace();
            }
        }
        Execute.procEnvironment = new LinkedHashMap<String, String>();
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final Execute exe = new Execute(new PumpStreamHandler(out));
            exe.setCommandline(getProcEnvCommand());
            exe.setNewenvironment(true);
            final int retval = exe.execute();
            if (retval == 0) {}
            final BufferedReader in = new BufferedReader(new StringReader(toString(out)));
            if (Os.isFamily("openvms")) {
                return Execute.procEnvironment = getVMSLogicals(in);
            }
            String var = null;
            final String lineSep = StringUtils.LINE_SEP;
            String line;
            while ((line = in.readLine()) != null) {
                if (line.indexOf(61) == -1) {
                    if (var == null) {
                        var = lineSep + line;
                    }
                    else {
                        var = var + lineSep + line;
                    }
                }
                else {
                    if (var != null) {
                        final int eq = var.indexOf("=");
                        Execute.procEnvironment.put(var.substring(0, eq), var.substring(eq + 1));
                    }
                    var = line;
                }
            }
            if (var != null) {
                final int eq = var.indexOf("=");
                Execute.procEnvironment.put(var.substring(0, eq), var.substring(eq + 1));
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
        return Execute.procEnvironment;
    }
    
    @Deprecated
    public static synchronized Vector<String> getProcEnvironment() {
        final Vector<String> v = new Vector<String>();
        for (final Map.Entry<String, String> entry : getEnvironmentVariables().entrySet()) {
            v.add(entry.getKey() + "=" + entry.getValue());
        }
        return v;
    }
    
    private static String[] getProcEnvCommand() {
        if (Os.isFamily("os/2")) {
            return new String[] { "cmd", "/c", "set" };
        }
        if (Os.isFamily("windows")) {
            if (Os.isFamily("win9x")) {
                return new String[] { "command.com", "/c", "set" };
            }
            return new String[] { "cmd", "/c", "set" };
        }
        else {
            if (Os.isFamily("z/os") || Os.isFamily("unix")) {
                final String[] cmd = { null };
                if (new File("/bin/env").canRead()) {
                    cmd[0] = "/bin/env";
                }
                else if (new File("/usr/bin/env").canRead()) {
                    cmd[0] = "/usr/bin/env";
                }
                else {
                    cmd[0] = "env";
                }
                return cmd;
            }
            if (Os.isFamily("netware") || Os.isFamily("os/400")) {
                return new String[] { "env" };
            }
            if (Os.isFamily("openvms")) {
                return new String[] { "show", "logical" };
            }
            return null;
        }
    }
    
    public static String toString(final ByteArrayOutputStream bos) {
        if (Os.isFamily("z/os")) {
            try {
                return bos.toString("Cp1047");
            }
            catch (UnsupportedEncodingException e) {
                return bos.toString();
            }
        }
        if (Os.isFamily("os/400")) {
            try {
                return bos.toString("Cp500");
            }
            catch (UnsupportedEncodingException ex) {}
        }
        return bos.toString();
    }
    
    public Execute() {
        this(new PumpStreamHandler(), null);
    }
    
    public Execute(final ExecuteStreamHandler streamHandler) {
        this(streamHandler, null);
    }
    
    public Execute(final ExecuteStreamHandler streamHandler, final ExecuteWatchdog watchdog) {
        super();
        this.cmdl = null;
        this.env = null;
        this.exitValue = Integer.MAX_VALUE;
        this.workingDirectory = null;
        this.project = null;
        this.newEnvironment = false;
        this.useVMLauncher = true;
        this.setStreamHandler(streamHandler);
        this.watchdog = watchdog;
        if (Os.isFamily("openvms")) {
            this.useVMLauncher = false;
        }
    }
    
    public void setStreamHandler(final ExecuteStreamHandler streamHandler) {
        this.streamHandler = streamHandler;
    }
    
    public String[] getCommandline() {
        return this.cmdl;
    }
    
    public void setCommandline(final String[] commandline) {
        this.cmdl = commandline;
    }
    
    public void setNewenvironment(final boolean newenv) {
        this.newEnvironment = newenv;
    }
    
    public String[] getEnvironment() {
        return (this.env == null || this.newEnvironment) ? this.env : this.patchEnvironment();
    }
    
    public void setEnvironment(final String[] env) {
        this.env = env;
    }
    
    public void setWorkingDirectory(final File wd) {
        this.workingDirectory = ((wd == null || wd.getAbsolutePath().equals(Execute.antWorkingDirectory)) ? null : wd);
    }
    
    public File getWorkingDirectory() {
        return (this.workingDirectory == null) ? new File(Execute.antWorkingDirectory) : this.workingDirectory;
    }
    
    public void setAntRun(final Project project) throws BuildException {
        this.project = project;
    }
    
    public void setVMLauncher(final boolean useVMLauncher) {
        this.useVMLauncher = useVMLauncher;
    }
    
    public static Process launch(final Project project, final String[] command, final String[] env, final File dir, final boolean useVM) throws IOException {
        if (dir != null && !dir.exists()) {
            throw new BuildException(dir + " doesn't exist.");
        }
        final CommandLauncher vmLauncher = CommandLauncher.getVMLauncher(project);
        final CommandLauncher launcher = (useVM && vmLauncher != null) ? vmLauncher : CommandLauncher.getShellLauncher(project);
        return launcher.exec(project, command, env, dir);
    }
    
    public int execute() throws IOException {
        if (this.workingDirectory != null && !this.workingDirectory.exists()) {
            throw new BuildException(this.workingDirectory + " doesn't exist.");
        }
        final Process process = launch(this.project, this.getCommandline(), this.getEnvironment(), this.workingDirectory, this.useVMLauncher);
        try {
            this.streamHandler.setProcessInputStream(process.getOutputStream());
            this.streamHandler.setProcessOutputStream(process.getInputStream());
            this.streamHandler.setProcessErrorStream(process.getErrorStream());
        }
        catch (IOException e) {
            process.destroy();
            throw e;
        }
        this.streamHandler.start();
        try {
            Execute.processDestroyer.add(process);
            if (this.watchdog != null) {
                this.watchdog.start(process);
            }
            this.waitFor(process);
            if (this.watchdog != null) {
                this.watchdog.stop();
            }
            this.streamHandler.stop();
            closeStreams(process);
            if (this.watchdog != null) {
                this.watchdog.checkException();
            }
            return this.getExitValue();
        }
        catch (ThreadDeath t) {
            process.destroy();
            throw t;
        }
        finally {
            Execute.processDestroyer.remove(process);
        }
    }
    
    public void spawn() throws IOException {
        if (this.workingDirectory != null && !this.workingDirectory.exists()) {
            throw new BuildException(this.workingDirectory + " doesn't exist.");
        }
        final Process process = launch(this.project, this.getCommandline(), this.getEnvironment(), this.workingDirectory, this.useVMLauncher);
        if (Os.isFamily("windows")) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e) {
                this.project.log("interruption in the sleep after having spawned a process", 3);
            }
        }
        final OutputStream dummyOut = new OutputStream() {
            public void write(final int b) throws IOException {
            }
        };
        final ExecuteStreamHandler handler = new PumpStreamHandler(dummyOut);
        handler.setProcessErrorStream(process.getErrorStream());
        handler.setProcessOutputStream(process.getInputStream());
        handler.start();
        process.getOutputStream().close();
        this.project.log("spawned process " + process.toString(), 3);
    }
    
    protected void waitFor(final Process process) {
        try {
            process.waitFor();
            this.setExitValue(process.exitValue());
        }
        catch (InterruptedException e) {
            process.destroy();
        }
    }
    
    protected void setExitValue(final int value) {
        this.exitValue = value;
    }
    
    public int getExitValue() {
        return this.exitValue;
    }
    
    public static boolean isFailure(final int exitValue) {
        return Os.isFamily("openvms") ? (exitValue % 2 == 0) : (exitValue != 0);
    }
    
    public boolean isFailure() {
        return isFailure(this.getExitValue());
    }
    
    public boolean killedProcess() {
        return this.watchdog != null && this.watchdog.killedProcess();
    }
    
    private String[] patchEnvironment() {
        if (Os.isFamily("openvms")) {
            return this.env;
        }
        final Map<String, String> osEnv = new LinkedHashMap<String, String>(getEnvironmentVariables());
        for (int i = 0; i < this.env.length; ++i) {
            final String keyValue = this.env[i];
            String key = keyValue.substring(0, keyValue.indexOf(61));
            if (osEnv.remove(key) == null && Execute.environmentCaseInSensitive) {
                for (final String osEnvItem : osEnv.keySet()) {
                    if (osEnvItem.toLowerCase().equals(key.toLowerCase())) {
                        key = osEnvItem;
                        break;
                    }
                }
            }
            osEnv.put(key, keyValue.substring(key.length() + 1));
        }
        final ArrayList<String> l = new ArrayList<String>();
        for (final Map.Entry<String, String> entry : osEnv.entrySet()) {
            l.add(entry.getKey() + "=" + entry.getValue());
        }
        return l.toArray(new String[osEnv.size()]);
    }
    
    public static void runCommand(final Task task, final String[] cmdline) throws BuildException {
        try {
            task.log(Commandline.describeCommand(cmdline), 3);
            final Execute exe = new Execute(new LogStreamHandler(task, 2, 0));
            exe.setAntRun(task.getProject());
            exe.setCommandline(cmdline);
            final int retval = exe.execute();
            if (isFailure(retval)) {
                throw new BuildException(cmdline[0] + " failed with return code " + retval, task.getLocation());
            }
        }
        catch (IOException exc) {
            throw new BuildException("Could not launch " + cmdline[0] + ": " + exc, task.getLocation());
        }
    }
    
    public static void closeStreams(final Process process) {
        FileUtils.close(process.getInputStream());
        FileUtils.close(process.getOutputStream());
        FileUtils.close(process.getErrorStream());
    }
    
    private static Map<String, String> getVMSLogicals(final BufferedReader in) throws IOException {
        final HashMap<String, String> logicals = new HashMap<String, String>();
        String logName = null;
        String logValue = null;
        String line = null;
        while ((line = in.readLine()) != null) {
            if (line.startsWith("\t=")) {
                if (logName == null) {
                    continue;
                }
                logValue = logValue + "," + line.substring(4, line.length() - 1);
            }
            else {
                if (!line.startsWith("  \"")) {
                    continue;
                }
                if (logName != null) {
                    logicals.put(logName, logValue);
                }
                final int eqIndex = line.indexOf(61);
                final String newLogName = line.substring(3, eqIndex - 2);
                if (logicals.containsKey(newLogName)) {
                    logName = null;
                }
                else {
                    logName = newLogName;
                    logValue = line.substring(eqIndex + 3, line.length() - 1);
                }
            }
        }
        if (logName != null) {
            logicals.put(logName, logValue);
        }
        return logicals;
    }
    
    static {
        Execute.antWorkingDirectory = System.getProperty("user.dir");
        Execute.procEnvironment = null;
        Execute.processDestroyer = new ProcessDestroyer();
        Execute.environmentCaseInSensitive = false;
        if (Os.isFamily("windows")) {
            Execute.environmentCaseInSensitive = true;
        }
    }
}
