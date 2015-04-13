package org.apache.tools.ant.filters;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import java.io.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.types.*;
import java.util.*;

public final class ReplaceTokens extends BaseParamFilterReader implements ChainableReader
{
    private static final char DEFAULT_BEGIN_TOKEN = '@';
    private static final char DEFAULT_END_TOKEN = '@';
    private String queuedData;
    private String replaceData;
    private int replaceIndex;
    private int queueIndex;
    private Hashtable<String, String> hash;
    private char beginToken;
    private char endToken;
    
    public ReplaceTokens() {
        super();
        this.queuedData = null;
        this.replaceData = null;
        this.replaceIndex = -1;
        this.queueIndex = -1;
        this.hash = new Hashtable<String, String>();
        this.beginToken = '@';
        this.endToken = '@';
    }
    
    public ReplaceTokens(final Reader in) {
        super(in);
        this.queuedData = null;
        this.replaceData = null;
        this.replaceIndex = -1;
        this.queueIndex = -1;
        this.hash = new Hashtable<String, String>();
        this.beginToken = '@';
        this.endToken = '@';
    }
    
    private int getNextChar() throws IOException {
        if (this.queueIndex != -1) {
            final int ch = this.queuedData.charAt(this.queueIndex++);
            if (this.queueIndex >= this.queuedData.length()) {
                this.queueIndex = -1;
            }
            return ch;
        }
        return this.in.read();
    }
    
    public int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        if (this.replaceIndex != -1) {
            final int ch = this.replaceData.charAt(this.replaceIndex++);
            if (this.replaceIndex >= this.replaceData.length()) {
                this.replaceIndex = -1;
            }
            return ch;
        }
        int ch = this.getNextChar();
        if (ch != this.beginToken) {
            return ch;
        }
        final StringBuffer key = new StringBuffer("");
        do {
            ch = this.getNextChar();
            if (ch == -1) {
                break;
            }
            key.append((char)ch);
        } while (ch != this.endToken);
        if (ch == -1) {
            if (this.queuedData == null || this.queueIndex == -1) {
                this.queuedData = key.toString();
            }
            else {
                this.queuedData = key.toString() + this.queuedData.substring(this.queueIndex);
            }
            if (this.queuedData.length() > 0) {
                this.queueIndex = 0;
            }
            else {
                this.queueIndex = -1;
            }
            return this.beginToken;
        }
        key.setLength(key.length() - 1);
        final String replaceWith = this.hash.get(key.toString());
        if (replaceWith != null) {
            if (replaceWith.length() > 0) {
                this.replaceData = replaceWith;
                this.replaceIndex = 0;
            }
            return this.read();
        }
        final String newData = key.toString() + this.endToken;
        if (this.queuedData == null || this.queueIndex == -1) {
            this.queuedData = newData;
        }
        else {
            this.queuedData = newData + this.queuedData.substring(this.queueIndex);
        }
        this.queueIndex = 0;
        return this.beginToken;
    }
    
    public void setBeginToken(final char beginToken) {
        this.beginToken = beginToken;
    }
    
    private char getBeginToken() {
        return this.beginToken;
    }
    
    public void setEndToken(final char endToken) {
        this.endToken = endToken;
    }
    
    private char getEndToken() {
        return this.endToken;
    }
    
    public void setPropertiesResource(final Resource r) {
        this.makeTokensFromProperties(r);
    }
    
    public void addConfiguredToken(final Token token) {
        this.hash.put(token.getKey(), token.getValue());
    }
    
    private Properties getProperties(final Resource r) {
        InputStream in = null;
        final Properties props = new Properties();
        try {
            in = r.getInputStream();
            props.load(in);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            FileUtils.close(in);
        }
        return props;
    }
    
    private void setTokens(final Hashtable<String, String> hash) {
        this.hash = hash;
    }
    
    private Hashtable<String, String> getTokens() {
        return this.hash;
    }
    
    public Reader chain(final Reader rdr) {
        final ReplaceTokens newFilter = new ReplaceTokens(rdr);
        newFilter.setBeginToken(this.getBeginToken());
        newFilter.setEndToken(this.getEndToken());
        newFilter.setTokens(this.getTokens());
        newFilter.setInitialized(true);
        return newFilter;
    }
    
    private void initialize() {
        final Parameter[] params = this.getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                if (params[i] != null) {
                    final String type = params[i].getType();
                    if ("tokenchar".equals(type)) {
                        final String name = params[i].getName();
                        final String value = params[i].getValue();
                        if ("begintoken".equals(name)) {
                            if (value.length() == 0) {
                                throw new BuildException("Begin token cannot be empty");
                            }
                            this.beginToken = params[i].getValue().charAt(0);
                        }
                        else if ("endtoken".equals(name)) {
                            if (value.length() == 0) {
                                throw new BuildException("End token cannot be empty");
                            }
                            this.endToken = params[i].getValue().charAt(0);
                        }
                    }
                    else if ("token".equals(type)) {
                        final String name = params[i].getName();
                        final String value = params[i].getValue();
                        this.hash.put(name, value);
                    }
                    else if ("propertiesfile".equals(type)) {
                        this.makeTokensFromProperties(new FileResource(new File(params[i].getValue())));
                    }
                }
            }
        }
    }
    
    private void makeTokensFromProperties(final Resource r) {
        final Properties props = this.getProperties(r);
        final Enumeration<?> e = ((Hashtable<?, V>)props).keys();
        while (e.hasMoreElements()) {
            final String key = (String)e.nextElement();
            final String value = props.getProperty(key);
            this.hash.put(key, value);
        }
    }
    
    public static class Token
    {
        private String key;
        private String value;
        
        public final void setKey(final String key) {
            this.key = key;
        }
        
        public final void setValue(final String value) {
            this.value = value;
        }
        
        public final String getKey() {
            return this.key;
        }
        
        public final String getValue() {
            return this.value;
        }
    }
}
