package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.util.*;
import java.io.*;
import org.apache.tools.ant.*;

public class TokenizedPath
{
    public static final TokenizedPath EMPTY_PATH;
    private static final FileUtils FILE_UTILS;
    private static final SymbolicLinkUtils SYMLINK_UTILS;
    private static final boolean[] CS_SCAN_ONLY;
    private static final boolean[] CS_THEN_NON_CS;
    private final String path;
    private final String[] tokenizedPath;
    
    public TokenizedPath(final String path) {
        this(path, SelectorUtils.tokenizePathAsArray(path));
    }
    
    public TokenizedPath(final TokenizedPath parent, final String child) {
        super();
        if (parent.path.length() > 0 && parent.path.charAt(parent.path.length() - 1) != File.separatorChar) {
            this.path = parent.path + File.separatorChar + child;
        }
        else {
            this.path = parent.path + child;
        }
        this.tokenizedPath = new String[parent.tokenizedPath.length + 1];
        System.arraycopy(parent.tokenizedPath, 0, this.tokenizedPath, 0, parent.tokenizedPath.length);
        this.tokenizedPath[parent.tokenizedPath.length] = child;
    }
    
    TokenizedPath(final String path, final String[] tokens) {
        super();
        this.path = path;
        this.tokenizedPath = tokens;
    }
    
    public String toString() {
        return this.path;
    }
    
    public int depth() {
        return this.tokenizedPath.length;
    }
    
    String[] getTokens() {
        return this.tokenizedPath;
    }
    
    public File findFile(File base, final boolean cs) {
        String[] tokens = this.tokenizedPath;
        if (FileUtils.isAbsolutePath(this.path)) {
            if (base == null) {
                final String[] s = TokenizedPath.FILE_UTILS.dissect(this.path);
                base = new File(s[0]);
                tokens = SelectorUtils.tokenizePathAsArray(s[1]);
            }
            else {
                final File f = TokenizedPath.FILE_UTILS.normalize(this.path);
                final String s2 = TokenizedPath.FILE_UTILS.removeLeadingPath(base, f);
                if (s2.equals(f.getAbsolutePath())) {
                    return null;
                }
                tokens = SelectorUtils.tokenizePathAsArray(s2);
            }
        }
        return findFile(base, tokens, cs);
    }
    
    public boolean isSymlink(File base) {
        for (int i = 0; i < this.tokenizedPath.length; ++i) {
            try {
                if ((base != null && TokenizedPath.SYMLINK_UTILS.isSymbolicLink(base, this.tokenizedPath[i])) || (base == null && TokenizedPath.SYMLINK_UTILS.isSymbolicLink(this.tokenizedPath[i]))) {
                    return true;
                }
                base = new File(base, this.tokenizedPath[i]);
            }
            catch (IOException ioe) {
                final String msg = "IOException caught while checking for links, couldn't get canonical path!";
                System.err.println(msg);
            }
        }
        return false;
    }
    
    public boolean equals(final Object o) {
        return o instanceof TokenizedPath && this.path.equals(((TokenizedPath)o).path);
    }
    
    public int hashCode() {
        return this.path.hashCode();
    }
    
    private static File findFile(File base, final String[] pathElements, final boolean cs) {
        for (int current = 0; current < pathElements.length; ++current) {
            if (!base.isDirectory()) {
                return null;
            }
            final String[] files = base.list();
            if (files == null) {
                throw new BuildException("IO error scanning directory " + base.getAbsolutePath());
            }
            boolean found = false;
            final boolean[] matchCase = cs ? TokenizedPath.CS_SCAN_ONLY : TokenizedPath.CS_THEN_NON_CS;
            for (int i = 0; !found && i < matchCase.length; ++i) {
                for (int j = 0; !found && j < files.length; ++j) {
                    if (matchCase[i]) {
                        if (!files[j].equals(pathElements[current])) {
                            continue;
                        }
                    }
                    else if (!files[j].equalsIgnoreCase(pathElements[current])) {
                        continue;
                    }
                    base = new File(base, files[j]);
                    found = true;
                }
            }
            if (!found) {
                return null;
            }
        }
        return (pathElements.length == 0 && !base.isDirectory()) ? null : base;
    }
    
    public TokenizedPattern toPattern() {
        return new TokenizedPattern(this.path, this.tokenizedPath);
    }
    
    static {
        EMPTY_PATH = new TokenizedPath("", new String[0]);
        FILE_UTILS = FileUtils.getFileUtils();
        SYMLINK_UTILS = SymbolicLinkUtils.getSymbolicLinkUtils();
        CS_SCAN_ONLY = new boolean[] { true };
        CS_THEN_NON_CS = new boolean[] { true, false };
    }
}
