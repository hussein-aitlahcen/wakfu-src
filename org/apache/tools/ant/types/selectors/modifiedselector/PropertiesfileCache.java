package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.*;
import java.util.*;

public class PropertiesfileCache implements Cache
{
    private File cachefile;
    private Properties cache;
    private boolean cacheLoaded;
    private boolean cacheDirty;
    
    public PropertiesfileCache() {
        super();
        this.cachefile = null;
        this.cache = new Properties();
        this.cacheLoaded = false;
        this.cacheDirty = true;
    }
    
    public PropertiesfileCache(final File cachefile) {
        super();
        this.cachefile = null;
        this.cache = new Properties();
        this.cacheLoaded = false;
        this.cacheDirty = true;
        this.cachefile = cachefile;
    }
    
    public void setCachefile(final File file) {
        this.cachefile = file;
    }
    
    public File getCachefile() {
        return this.cachefile;
    }
    
    public boolean isValid() {
        return this.cachefile != null;
    }
    
    public void load() {
        if (this.cachefile != null && this.cachefile.isFile() && this.cachefile.canRead()) {
            try {
                final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this.cachefile));
                this.cache.load(bis);
                bis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.cacheLoaded = true;
        this.cacheDirty = false;
    }
    
    public void save() {
        if (!this.cacheDirty) {
            return;
        }
        if (this.cachefile != null && this.cache.propertyNames().hasMoreElements()) {
            try {
                final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(this.cachefile));
                this.cache.store(bos, null);
                bos.flush();
                bos.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.cacheDirty = false;
    }
    
    public void delete() {
        this.cache = new Properties();
        this.cachefile.delete();
        this.cacheLoaded = true;
        this.cacheDirty = false;
    }
    
    public Object get(final Object key) {
        if (!this.cacheLoaded) {
            this.load();
        }
        try {
            return this.cache.getProperty(String.valueOf(key));
        }
        catch (ClassCastException e) {
            return null;
        }
    }
    
    public void put(final Object key, final Object value) {
        ((Hashtable<String, String>)this.cache).put(String.valueOf(key), String.valueOf(value));
        this.cacheDirty = true;
    }
    
    public Iterator<String> iterator() {
        final Vector<String> v = new Vector<String>();
        final Enumeration<?> en = this.cache.propertyNames();
        while (en.hasMoreElements()) {
            v.add(en.nextElement().toString());
        }
        return v.iterator();
    }
    
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("<PropertiesfileCache:");
        buf.append("cachefile=").append(this.cachefile);
        buf.append(";noOfEntries=").append(this.cache.size());
        buf.append(">");
        return buf.toString();
    }
}
