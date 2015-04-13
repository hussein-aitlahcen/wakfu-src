package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import java.net.*;
import org.apache.tools.ant.types.*;
import java.io.*;

public class URLResource extends Resource implements URLProvider
{
    private static final FileUtils FILE_UTILS;
    private static final int NULL_URL;
    private URL url;
    private URLConnection conn;
    private URL baseURL;
    private String relPath;
    
    public URLResource() {
        super();
    }
    
    public URLResource(final URL u) {
        super();
        this.setURL(u);
    }
    
    public URLResource(final URLProvider u) {
        super();
        this.setURL(u.getURL());
    }
    
    public URLResource(final File f) {
        super();
        this.setFile(f);
    }
    
    public URLResource(final String u) {
        this(newURL(u));
    }
    
    public synchronized void setURL(final URL u) {
        this.checkAttributesAllowed();
        this.url = u;
    }
    
    public synchronized void setFile(final File f) {
        try {
            this.setURL(URLResource.FILE_UTILS.getFileURL(f));
        }
        catch (MalformedURLException e) {
            throw new BuildException(e);
        }
    }
    
    public synchronized void setBaseURL(final URL base) {
        this.checkAttributesAllowed();
        if (this.url != null) {
            throw new BuildException("can't define URL and baseURL attribute");
        }
        this.baseURL = base;
    }
    
    public synchronized void setRelativePath(final String r) {
        this.checkAttributesAllowed();
        if (this.url != null) {
            throw new BuildException("can't define URL and relativePath attribute");
        }
        this.relPath = r;
    }
    
    public synchronized URL getURL() {
        if (this.isReference()) {
            return ((URLResource)this.getCheckedRef()).getURL();
        }
        if (this.url == null && this.baseURL != null) {
            if (this.relPath == null) {
                throw new BuildException("must provide relativePath attribute when using baseURL.");
            }
            try {
                this.url = new URL(this.baseURL, this.relPath);
            }
            catch (MalformedURLException e) {
                throw new BuildException(e);
            }
        }
        return this.url;
    }
    
    public synchronized void setRefid(final Reference r) {
        if (this.url != null || this.baseURL != null || this.relPath != null) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    public synchronized String getName() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getName();
        }
        final String name = this.getURL().getFile();
        return "".equals(name) ? name : name.substring(1);
    }
    
    public synchronized String toString() {
        return this.isReference() ? this.getCheckedRef().toString() : String.valueOf(this.getURL());
    }
    
    public synchronized boolean isExists() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).isExists();
        }
        return this.isExists(false);
    }
    
    private synchronized boolean isExists(final boolean closeConnection) {
        if (this.getURL() == null) {
            return false;
        }
        try {
            this.connect(3);
            final boolean b = true;
            if (closeConnection) {
                this.close();
            }
            return b;
        }
        catch (IOException e) {
            final boolean b2 = false;
            if (closeConnection) {
                this.close();
            }
            return b2;
        }
        finally {
            if (closeConnection) {
                this.close();
            }
        }
    }
    
    public synchronized long getLastModified() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getLastModified();
        }
        if (!this.isExists(false)) {
            return 0L;
        }
        return this.conn.getLastModified();
    }
    
    public synchronized boolean isDirectory() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).isDirectory() : this.getName().endsWith("/");
    }
    
    public synchronized long getSize() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getSize();
        }
        if (!this.isExists(false)) {
            return 0L;
        }
        try {
            this.connect();
            final long contentlength = this.conn.getContentLength();
            this.close();
            return contentlength;
        }
        catch (IOException e) {
            return -1L;
        }
    }
    
    public synchronized boolean equals(final Object another) {
        if (this == another) {
            return true;
        }
        if (this.isReference()) {
            return this.getCheckedRef().equals(another);
        }
        if (!another.getClass().equals(this.getClass())) {
            return false;
        }
        final URLResource otheru = (URLResource)another;
        return (this.getURL() == null) ? (otheru.getURL() == null) : this.getURL().equals(otheru.getURL());
    }
    
    public synchronized int hashCode() {
        if (this.isReference()) {
            return this.getCheckedRef().hashCode();
        }
        return URLResource.MAGIC * ((this.getURL() == null) ? URLResource.NULL_URL : this.getURL().hashCode());
    }
    
    public synchronized InputStream getInputStream() throws IOException {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getInputStream();
        }
        this.connect();
        try {
            final InputStream inputStream = this.conn.getInputStream();
            this.conn = null;
            return inputStream;
        }
        finally {
            this.conn = null;
        }
    }
    
    public synchronized OutputStream getOutputStream() throws IOException {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getOutputStream();
        }
        this.connect();
        try {
            final OutputStream outputStream = this.conn.getOutputStream();
            this.conn = null;
            return outputStream;
        }
        finally {
            this.conn = null;
        }
    }
    
    protected void connect() throws IOException {
        this.connect(0);
    }
    
    protected synchronized void connect(final int logLevel) throws IOException {
        final URL u = this.getURL();
        if (u == null) {
            throw new BuildException("URL not set");
        }
        if (this.conn == null) {
            try {
                (this.conn = u.openConnection()).connect();
            }
            catch (IOException e) {
                this.log(e.toString(), logLevel);
                this.conn = null;
                throw e;
            }
        }
    }
    
    private synchronized void close() {
        try {
            FileUtils.close(this.conn);
            this.conn = null;
        }
        finally {
            this.conn = null;
        }
    }
    
    private static URL newURL(final String u) {
        try {
            return new URL(u);
        }
        catch (MalformedURLException e) {
            throw new BuildException(e);
        }
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
        NULL_URL = Resource.getMagicNumber("null URL".getBytes());
    }
}
