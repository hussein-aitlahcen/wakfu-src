package org.apache.tools.ant;

import org.apache.tools.ant.taskdefs.condition.*;
import java.io.*;
import java.util.*;

public class PathTokenizer
{
    private StringTokenizer tokenizer;
    private String lookahead;
    private boolean onNetWare;
    private boolean dosStyleFilesystem;
    
    public PathTokenizer(final String path) {
        super();
        this.lookahead = null;
        this.onNetWare = Os.isFamily("netware");
        if (this.onNetWare) {
            this.tokenizer = new StringTokenizer(path, ":;", true);
        }
        else {
            this.tokenizer = new StringTokenizer(path, ":;", false);
        }
        this.dosStyleFilesystem = (File.pathSeparatorChar == ';');
    }
    
    public boolean hasMoreTokens() {
        return this.lookahead != null || this.tokenizer.hasMoreTokens();
    }
    
    public String nextToken() throws NoSuchElementException {
        String token = null;
        if (this.lookahead != null) {
            token = this.lookahead;
            this.lookahead = null;
        }
        else {
            token = this.tokenizer.nextToken().trim();
        }
        if (!this.onNetWare) {
            if (token.length() == 1 && Character.isLetter(token.charAt(0)) && this.dosStyleFilesystem && this.tokenizer.hasMoreTokens()) {
                final String nextToken = this.tokenizer.nextToken().trim();
                if (nextToken.startsWith("\\") || nextToken.startsWith("/")) {
                    token = token + ":" + nextToken;
                }
                else {
                    this.lookahead = nextToken;
                }
            }
        }
        else {
            if (token.equals(File.pathSeparator) || token.equals(":")) {
                token = this.tokenizer.nextToken().trim();
            }
            if (this.tokenizer.hasMoreTokens()) {
                final String nextToken = this.tokenizer.nextToken().trim();
                if (!nextToken.equals(File.pathSeparator)) {
                    if (nextToken.equals(":")) {
                        if (!token.startsWith("/") && !token.startsWith("\\") && !token.startsWith(".") && !token.startsWith("..")) {
                            final String oneMore = this.tokenizer.nextToken().trim();
                            if (!oneMore.equals(File.pathSeparator)) {
                                token = token + ":" + oneMore;
                            }
                            else {
                                token += ":";
                                this.lookahead = oneMore;
                            }
                        }
                    }
                    else {
                        this.lookahead = nextToken;
                    }
                }
            }
        }
        return token;
    }
}
