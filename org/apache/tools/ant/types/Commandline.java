package org.apache.tools.ant.types;

import org.apache.tools.ant.util.*;
import java.util.*;
import org.apache.tools.ant.taskdefs.condition.*;
import org.apache.tools.ant.*;
import java.io.*;

public class Commandline implements Cloneable
{
    private static final boolean IS_WIN_9X;
    private List<Argument> arguments;
    private String executable;
    protected static final String DISCLAIMER;
    
    public Commandline(final String toProcess) {
        super();
        this.arguments = new ArrayList<Argument>();
        this.executable = null;
        final String[] tmp = translateCommandline(toProcess);
        if (tmp != null && tmp.length > 0) {
            this.setExecutable(tmp[0]);
            for (int i = 1; i < tmp.length; ++i) {
                this.createArgument().setValue(tmp[i]);
            }
        }
    }
    
    public Commandline() {
        super();
        this.arguments = new ArrayList<Argument>();
        this.executable = null;
    }
    
    public Argument createArgument() {
        return this.createArgument(false);
    }
    
    public Argument createArgument(final boolean insertAtStart) {
        final Argument argument = new Argument();
        if (insertAtStart) {
            this.arguments.add(0, argument);
        }
        else {
            this.arguments.add(argument);
        }
        return argument;
    }
    
    public void setExecutable(final String executable) {
        if (executable == null || executable.length() == 0) {
            return;
        }
        this.executable = executable.replace('/', File.separatorChar).replace('\\', File.separatorChar);
    }
    
    public String getExecutable() {
        return this.executable;
    }
    
    public void addArguments(final String[] line) {
        for (int i = 0; i < line.length; ++i) {
            this.createArgument().setValue(line[i]);
        }
    }
    
    public String[] getCommandline() {
        final List<String> commands = new LinkedList<String>();
        this.addCommandToList(commands.listIterator());
        return commands.toArray(new String[commands.size()]);
    }
    
    public void addCommandToList(final ListIterator<String> list) {
        if (this.executable != null) {
            list.add(this.executable);
        }
        this.addArgumentsToList(list);
    }
    
    public String[] getArguments() {
        final List<String> result = new ArrayList<String>(this.arguments.size() * 2);
        this.addArgumentsToList(result.listIterator());
        return result.toArray(new String[result.size()]);
    }
    
    public void addArgumentsToList(final ListIterator<String> list) {
        for (int size = this.arguments.size(), i = 0; i < size; ++i) {
            final Argument arg = this.arguments.get(i);
            final String[] s = arg.getParts();
            if (s != null) {
                for (int j = 0; j < s.length; ++j) {
                    list.add(s[j]);
                }
            }
        }
    }
    
    public String toString() {
        return toString(this.getCommandline());
    }
    
    public static String quoteArgument(final String argument) {
        if (argument.indexOf("\"") > -1) {
            if (argument.indexOf("'") > -1) {
                throw new BuildException("Can't handle single and double quotes in same argument");
            }
            return '\'' + argument + '\'';
        }
        else {
            if (argument.indexOf("'") > -1 || argument.indexOf(" ") > -1 || (Commandline.IS_WIN_9X && argument.indexOf(59) != -1)) {
                return '\"' + argument + '\"';
            }
            return argument;
        }
    }
    
    public static String toString(final String[] line) {
        if (line == null || line.length == 0) {
            return "";
        }
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < line.length; ++i) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(quoteArgument(line[i]));
        }
        return result.toString();
    }
    
    public static String[] translateCommandline(final String toProcess) {
        if (toProcess == null || toProcess.length() == 0) {
            return new String[0];
        }
        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = 0;
        final StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
        final ArrayList<String> result = new ArrayList<String>();
        final StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;
        while (tok.hasMoreTokens()) {
            final String nextTok = tok.nextToken();
            switch (state) {
                case 1: {
                    if ("'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = 0;
                        continue;
                    }
                    current.append(nextTok);
                    continue;
                }
                case 2: {
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = 0;
                        continue;
                    }
                    current.append(nextTok);
                    continue;
                }
                default: {
                    if ("'".equals(nextTok)) {
                        state = 1;
                    }
                    else if ("\"".equals(nextTok)) {
                        state = 2;
                    }
                    else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() != 0) {
                            result.add(current.toString());
                            current.setLength(0);
                        }
                    }
                    else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    continue;
                }
            }
        }
        if (lastTokenHasBeenQuoted || current.length() != 0) {
            result.add(current.toString());
        }
        if (state == 1 || state == 2) {
            throw new BuildException("unbalanced quotes in " + toProcess);
        }
        return result.toArray(new String[result.size()]);
    }
    
    public int size() {
        return this.getCommandline().length;
    }
    
    public Object clone() {
        try {
            final Commandline c = (Commandline)super.clone();
            c.arguments = new ArrayList<Argument>(this.arguments);
            return c;
        }
        catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }
    
    public void clear() {
        this.executable = null;
        this.arguments.clear();
    }
    
    public void clearArgs() {
        this.arguments.clear();
    }
    
    public Marker createMarker() {
        return new Marker(this.arguments.size());
    }
    
    public String describeCommand() {
        return describeCommand(this);
    }
    
    public String describeArguments() {
        return describeArguments(this);
    }
    
    public static String describeCommand(final Commandline line) {
        return describeCommand(line.getCommandline());
    }
    
    public static String describeArguments(final Commandline line) {
        return describeArguments(line.getArguments());
    }
    
    public static String describeCommand(final String[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        final StringBuffer buf = new StringBuffer("Executing '");
        buf.append(args[0]);
        buf.append("'");
        if (args.length > 1) {
            buf.append(" with ");
            buf.append(describeArguments(args, 1));
        }
        else {
            buf.append(Commandline.DISCLAIMER);
        }
        return buf.toString();
    }
    
    public static String describeArguments(final String[] args) {
        return describeArguments(args, 0);
    }
    
    protected static String describeArguments(final String[] args, final int offset) {
        if (args == null || args.length <= offset) {
            return "";
        }
        final StringBuffer buf = new StringBuffer("argument");
        if (args.length > offset) {
            buf.append("s");
        }
        buf.append(":").append(StringUtils.LINE_SEP);
        for (int i = offset; i < args.length; ++i) {
            buf.append("'").append(args[i]).append("'").append(StringUtils.LINE_SEP);
        }
        buf.append(Commandline.DISCLAIMER);
        return buf.toString();
    }
    
    public Iterator<Argument> iterator() {
        return this.arguments.iterator();
    }
    
    static {
        IS_WIN_9X = Os.isFamily("win9x");
        DISCLAIMER = StringUtils.LINE_SEP + "The ' characters around the executable and arguments are" + StringUtils.LINE_SEP + "not part of the command." + StringUtils.LINE_SEP;
    }
    
    public static class Argument extends ProjectComponent
    {
        private String[] parts;
        private String prefix;
        private String suffix;
        
        public Argument() {
            super();
            this.prefix = "";
            this.suffix = "";
        }
        
        public void setValue(final String value) {
            this.parts = new String[] { value };
        }
        
        public void setLine(final String line) {
            if (line == null) {
                return;
            }
            this.parts = Commandline.translateCommandline(line);
        }
        
        public void setPath(final Path value) {
            this.parts = new String[] { value.toString() };
        }
        
        public void setPathref(final Reference value) {
            final Path p = new Path(this.getProject());
            p.setRefid(value);
            this.parts = new String[] { p.toString() };
        }
        
        public void setFile(final File value) {
            this.parts = new String[] { value.getAbsolutePath() };
        }
        
        public void setPrefix(final String prefix) {
            this.prefix = ((prefix != null) ? prefix : "");
        }
        
        public void setSuffix(final String suffix) {
            this.suffix = ((suffix != null) ? suffix : "");
        }
        
        public String[] getParts() {
            if (this.parts == null || this.parts.length == 0 || (this.prefix.length() == 0 && this.suffix.length() == 0)) {
                return this.parts;
            }
            final String[] fullParts = new String[this.parts.length];
            for (int i = 0; i < fullParts.length; ++i) {
                fullParts[i] = this.prefix + this.parts[i] + this.suffix;
            }
            return fullParts;
        }
    }
    
    public class Marker
    {
        private int position;
        private int realPos;
        private String prefix;
        private String suffix;
        
        Marker(final int position) {
            super();
            this.realPos = -1;
            this.prefix = "";
            this.suffix = "";
            this.position = position;
        }
        
        public int getPosition() {
            if (this.realPos == -1) {
                this.realPos = ((Commandline.this.executable != null) ? 1 : 0);
                for (int i = 0; i < this.position; ++i) {
                    final Argument arg = Commandline.this.arguments.get(i);
                    this.realPos += arg.getParts().length;
                }
            }
            return this.realPos;
        }
        
        public void setPrefix(final String prefix) {
            this.prefix = ((prefix != null) ? prefix : "");
        }
        
        public String getPrefix() {
            return this.prefix;
        }
        
        public void setSuffix(final String suffix) {
            this.suffix = ((suffix != null) ? suffix : "");
        }
        
        public String getSuffix() {
            return this.suffix;
        }
    }
}
