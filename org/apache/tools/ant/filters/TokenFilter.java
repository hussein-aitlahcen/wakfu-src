package org.apache.tools.ant.filters;

import java.util.*;
import java.io.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.regexp.*;

public class TokenFilter extends BaseFilterReader implements ChainableReader
{
    private Vector<Filter> filters;
    private Tokenizer tokenizer;
    private String delimOutput;
    private String line;
    private int linePos;
    
    public TokenFilter() {
        super();
        this.filters = new Vector<Filter>();
        this.tokenizer = null;
        this.delimOutput = null;
        this.line = null;
        this.linePos = 0;
    }
    
    public TokenFilter(final Reader in) {
        super(in);
        this.filters = new Vector<Filter>();
        this.tokenizer = null;
        this.delimOutput = null;
        this.line = null;
        this.linePos = 0;
    }
    
    public int read() throws IOException {
        if (this.tokenizer == null) {
            this.tokenizer = new LineTokenizer();
        }
        while (this.line == null || this.line.length() == 0) {
            this.line = this.tokenizer.getToken(this.in);
            if (this.line == null) {
                return -1;
            }
            final Enumeration<Filter> e = this.filters.elements();
            while (e.hasMoreElements()) {
                final Filter filter = e.nextElement();
                this.line = filter.filter(this.line);
                if (this.line == null) {
                    break;
                }
            }
            this.linePos = 0;
            if (this.line == null || this.tokenizer.getPostToken().length() == 0) {
                continue;
            }
            if (this.delimOutput != null) {
                this.line += this.delimOutput;
            }
            else {
                this.line += this.tokenizer.getPostToken();
            }
        }
        final int ch = this.line.charAt(this.linePos);
        ++this.linePos;
        if (this.linePos == this.line.length()) {
            this.line = null;
        }
        return ch;
    }
    
    public final Reader chain(final Reader reader) {
        final TokenFilter newFilter = new TokenFilter(reader);
        newFilter.filters = this.filters;
        newFilter.tokenizer = this.tokenizer;
        newFilter.delimOutput = this.delimOutput;
        newFilter.setProject(this.getProject());
        return newFilter;
    }
    
    public void setDelimOutput(final String delimOutput) {
        this.delimOutput = resolveBackSlash(delimOutput);
    }
    
    public void addLineTokenizer(final LineTokenizer tokenizer) {
        this.add(tokenizer);
    }
    
    public void addStringTokenizer(final StringTokenizer tokenizer) {
        this.add(tokenizer);
    }
    
    public void addFileTokenizer(final FileTokenizer tokenizer) {
        this.add(tokenizer);
    }
    
    public void add(final Tokenizer tokenizer) {
        if (this.tokenizer != null) {
            throw new BuildException("Only one tokenizer allowed");
        }
        this.tokenizer = tokenizer;
    }
    
    public void addReplaceString(final ReplaceString filter) {
        this.filters.addElement(filter);
    }
    
    public void addContainsString(final ContainsString filter) {
        this.filters.addElement(filter);
    }
    
    public void addReplaceRegex(final ReplaceRegex filter) {
        this.filters.addElement(filter);
    }
    
    public void addContainsRegex(final ContainsRegex filter) {
        this.filters.addElement(filter);
    }
    
    public void addTrim(final Trim filter) {
        this.filters.addElement(filter);
    }
    
    public void addIgnoreBlank(final IgnoreBlank filter) {
        this.filters.addElement(filter);
    }
    
    public void addDeleteCharacters(final DeleteCharacters filter) {
        this.filters.addElement(filter);
    }
    
    public void add(final Filter filter) {
        this.filters.addElement(filter);
    }
    
    public static String resolveBackSlash(final String input) {
        return StringUtils.resolveBackSlash(input);
    }
    
    public static int convertRegexOptions(final String flags) {
        return RegexpUtil.asOptions(flags);
    }
    
    public static class FileTokenizer extends org.apache.tools.ant.util.FileTokenizer
    {
    }
    
    public static class StringTokenizer extends org.apache.tools.ant.util.StringTokenizer
    {
    }
    
    public abstract static class ChainableReaderFilter extends ProjectComponent implements ChainableReader, Filter
    {
        private boolean byLine;
        
        public ChainableReaderFilter() {
            super();
            this.byLine = true;
        }
        
        public void setByLine(final boolean byLine) {
            this.byLine = byLine;
        }
        
        public Reader chain(final Reader reader) {
            final TokenFilter tokenFilter = new TokenFilter(reader);
            if (!this.byLine) {
                tokenFilter.add(new FileTokenizer());
            }
            tokenFilter.add(this);
            return tokenFilter;
        }
    }
    
    public static class ReplaceString extends ChainableReaderFilter
    {
        private String from;
        private String to;
        
        public void setFrom(final String from) {
            this.from = from;
        }
        
        public void setTo(final String to) {
            this.to = to;
        }
        
        public String filter(final String line) {
            if (this.from == null) {
                throw new BuildException("Missing from in stringreplace");
            }
            final StringBuffer ret = new StringBuffer();
            int start = 0;
            for (int found = line.indexOf(this.from); found >= 0; found = line.indexOf(this.from, start)) {
                if (found > start) {
                    ret.append(line.substring(start, found));
                }
                if (this.to != null) {
                    ret.append(this.to);
                }
                start = found + this.from.length();
            }
            if (line.length() > start) {
                ret.append(line.substring(start, line.length()));
            }
            return ret.toString();
        }
    }
    
    public static class ContainsString extends ProjectComponent implements Filter
    {
        private String contains;
        
        public void setContains(final String contains) {
            this.contains = contains;
        }
        
        public String filter(final String string) {
            if (this.contains == null) {
                throw new BuildException("Missing contains in containsstring");
            }
            if (string.indexOf(this.contains) > -1) {
                return string;
            }
            return null;
        }
    }
    
    public static class ReplaceRegex extends ChainableReaderFilter
    {
        private String from;
        private String to;
        private RegularExpression regularExpression;
        private Substitution substitution;
        private boolean initialized;
        private String flags;
        private int options;
        private Regexp regexp;
        
        public ReplaceRegex() {
            super();
            this.initialized = false;
            this.flags = "";
        }
        
        public void setPattern(final String from) {
            this.from = from;
        }
        
        public void setReplace(final String to) {
            this.to = to;
        }
        
        public void setFlags(final String flags) {
            this.flags = flags;
        }
        
        private void initialize() {
            if (this.initialized) {
                return;
            }
            this.options = TokenFilter.convertRegexOptions(this.flags);
            if (this.from == null) {
                throw new BuildException("Missing pattern in replaceregex");
            }
            (this.regularExpression = new RegularExpression()).setPattern(this.from);
            this.regexp = this.regularExpression.getRegexp(this.getProject());
            if (this.to == null) {
                this.to = "";
            }
            (this.substitution = new Substitution()).setExpression(this.to);
        }
        
        public String filter(final String line) {
            this.initialize();
            if (!this.regexp.matches(line, this.options)) {
                return line;
            }
            return this.regexp.substitute(line, this.substitution.getExpression(this.getProject()), this.options);
        }
    }
    
    public static class ContainsRegex extends ChainableReaderFilter
    {
        private String from;
        private String to;
        private RegularExpression regularExpression;
        private Substitution substitution;
        private boolean initialized;
        private String flags;
        private int options;
        private Regexp regexp;
        
        public ContainsRegex() {
            super();
            this.initialized = false;
            this.flags = "";
        }
        
        public void setPattern(final String from) {
            this.from = from;
        }
        
        public void setReplace(final String to) {
            this.to = to;
        }
        
        public void setFlags(final String flags) {
            this.flags = flags;
        }
        
        private void initialize() {
            if (this.initialized) {
                return;
            }
            this.options = TokenFilter.convertRegexOptions(this.flags);
            if (this.from == null) {
                throw new BuildException("Missing from in containsregex");
            }
            (this.regularExpression = new RegularExpression()).setPattern(this.from);
            this.regexp = this.regularExpression.getRegexp(this.getProject());
            if (this.to == null) {
                return;
            }
            (this.substitution = new Substitution()).setExpression(this.to);
        }
        
        public String filter(final String string) {
            this.initialize();
            if (!this.regexp.matches(string, this.options)) {
                return null;
            }
            if (this.substitution == null) {
                return string;
            }
            return this.regexp.substitute(string, this.substitution.getExpression(this.getProject()), this.options);
        }
    }
    
    public static class Trim extends ChainableReaderFilter
    {
        public String filter(final String line) {
            return line.trim();
        }
    }
    
    public static class IgnoreBlank extends ChainableReaderFilter
    {
        public String filter(final String line) {
            if (line.trim().length() == 0) {
                return null;
            }
            return line;
        }
    }
    
    public static class DeleteCharacters extends ProjectComponent implements Filter, ChainableReader
    {
        private String deleteChars;
        
        public DeleteCharacters() {
            super();
            this.deleteChars = "";
        }
        
        public void setChars(final String deleteChars) {
            this.deleteChars = TokenFilter.resolveBackSlash(deleteChars);
        }
        
        public String filter(final String string) {
            final StringBuffer output = new StringBuffer(string.length());
            for (int i = 0; i < string.length(); ++i) {
                final char ch = string.charAt(i);
                if (!this.isDeleteCharacter(ch)) {
                    output.append(ch);
                }
            }
            return output.toString();
        }
        
        public Reader chain(final Reader reader) {
            return new BaseFilterReader(reader) {
                public int read() throws IOException {
                    while (true) {
                        final int c = this.in.read();
                        if (c == -1) {
                            return c;
                        }
                        if (!DeleteCharacters.this.isDeleteCharacter((char)c)) {
                            return c;
                        }
                    }
                }
            };
        }
        
        private boolean isDeleteCharacter(final char c) {
            for (int d = 0; d < this.deleteChars.length(); ++d) {
                if (this.deleteChars.charAt(d) == c) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public interface Filter
    {
        String filter(String p0);
    }
}
