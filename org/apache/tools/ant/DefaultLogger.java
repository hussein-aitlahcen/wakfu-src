package org.apache.tools.ant;

import java.io.*;
import org.apache.tools.ant.util.*;
import java.util.*;
import java.text.*;

public class DefaultLogger implements BuildLogger
{
    public static final int LEFT_COLUMN_SIZE = 12;
    protected PrintStream out;
    protected PrintStream err;
    protected int msgOutputLevel;
    private long startTime;
    protected static final String lSep;
    protected boolean emacsMode;
    
    public DefaultLogger() {
        super();
        this.msgOutputLevel = 0;
        this.startTime = System.currentTimeMillis();
        this.emacsMode = false;
    }
    
    public void setMessageOutputLevel(final int level) {
        this.msgOutputLevel = level;
    }
    
    public void setOutputPrintStream(final PrintStream output) {
        this.out = new PrintStream(output, true);
    }
    
    public void setErrorPrintStream(final PrintStream err) {
        this.err = new PrintStream(err, true);
    }
    
    public void setEmacsMode(final boolean emacsMode) {
        this.emacsMode = emacsMode;
    }
    
    public void buildStarted(final BuildEvent event) {
        this.startTime = System.currentTimeMillis();
    }
    
    static void throwableMessage(final StringBuffer m, Throwable error, final boolean verbose) {
        while (error instanceof BuildException) {
            final Throwable cause = error.getCause();
            if (cause == null) {
                break;
            }
            final String msg1 = error.toString();
            final String msg2 = cause.toString();
            if (!msg1.endsWith(msg2)) {
                break;
            }
            m.append(msg1.substring(0, msg1.length() - msg2.length()));
            error = cause;
        }
        if (verbose || !(error instanceof BuildException)) {
            m.append(StringUtils.getStackTrace(error));
        }
        else {
            m.append(error).append(DefaultLogger.lSep);
        }
    }
    
    public void buildFinished(final BuildEvent event) {
        final Throwable error = event.getException();
        final StringBuffer message = new StringBuffer();
        if (error == null) {
            message.append(StringUtils.LINE_SEP);
            message.append(this.getBuildSuccessfulMessage());
        }
        else {
            message.append(StringUtils.LINE_SEP);
            message.append(this.getBuildFailedMessage());
            message.append(StringUtils.LINE_SEP);
            throwableMessage(message, error, 3 <= this.msgOutputLevel);
        }
        message.append(StringUtils.LINE_SEP);
        message.append("Total time: ");
        message.append(formatTime(System.currentTimeMillis() - this.startTime));
        final String msg = message.toString();
        if (error == null) {
            this.printMessage(msg, this.out, 3);
        }
        else {
            this.printMessage(msg, this.err, 0);
        }
        this.log(msg);
    }
    
    protected String getBuildFailedMessage() {
        return "BUILD FAILED";
    }
    
    protected String getBuildSuccessfulMessage() {
        return "BUILD SUCCESSFUL";
    }
    
    public void targetStarted(final BuildEvent event) {
        if (2 <= this.msgOutputLevel && !event.getTarget().getName().equals("")) {
            final String msg = StringUtils.LINE_SEP + event.getTarget().getName() + ":";
            this.printMessage(msg, this.out, event.getPriority());
            this.log(msg);
        }
    }
    
    public void targetFinished(final BuildEvent event) {
    }
    
    public void taskStarted(final BuildEvent event) {
    }
    
    public void taskFinished(final BuildEvent event) {
    }
    
    public void messageLogged(final BuildEvent event) {
        final int priority = event.getPriority();
        if (priority <= this.msgOutputLevel) {
            final StringBuffer message = new StringBuffer();
            if (event.getTask() != null && !this.emacsMode) {
                final String name = event.getTask().getTaskName();
                String label = "[" + name + "] ";
                final int size = 12 - label.length();
                final StringBuffer tmp = new StringBuffer();
                for (int i = 0; i < size; ++i) {
                    tmp.append(" ");
                }
                tmp.append(label);
                label = tmp.toString();
                BufferedReader r = null;
                try {
                    r = new BufferedReader(new StringReader(event.getMessage()));
                    String line = r.readLine();
                    boolean first = true;
                    do {
                        if (first) {
                            if (line == null) {
                                message.append(label);
                                break;
                            }
                        }
                        else {
                            message.append(StringUtils.LINE_SEP);
                        }
                        first = false;
                        message.append(label).append(line);
                        line = r.readLine();
                    } while (line != null);
                }
                catch (IOException e) {
                    message.append(label).append(event.getMessage());
                }
                finally {
                    if (r != null) {
                        FileUtils.close(r);
                    }
                }
            }
            else {
                message.append(event.getMessage());
            }
            final Throwable ex = event.getException();
            if (4 <= this.msgOutputLevel && ex != null) {
                message.append(StringUtils.getStackTrace(ex));
            }
            final String msg = message.toString();
            if (priority != 0) {
                this.printMessage(msg, this.out, priority);
            }
            else {
                this.printMessage(msg, this.err, priority);
            }
            this.log(msg);
        }
    }
    
    protected static String formatTime(final long millis) {
        return DateUtils.formatElapsedTime(millis);
    }
    
    protected void printMessage(final String message, final PrintStream stream, final int priority) {
        stream.println(message);
    }
    
    protected void log(final String message) {
    }
    
    protected String getTimestamp() {
        final Date date = new Date(System.currentTimeMillis());
        final DateFormat formatter = DateFormat.getDateTimeInstance(3, 3);
        final String finishTime = formatter.format(date);
        return finishTime;
    }
    
    protected String extractProjectName(final BuildEvent event) {
        final Project project = event.getProject();
        return (project != null) ? project.getName() : null;
    }
    
    static {
        lSep = StringUtils.LINE_SEP;
    }
}
