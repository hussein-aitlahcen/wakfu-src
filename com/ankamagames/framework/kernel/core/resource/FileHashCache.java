package com.ankamagames.framework.kernel.core.resource;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.io.*;
import java.util.jar.*;
import java.net.*;
import java.util.*;

public abstract class FileHashCache<T> implements FileLoader
{
    public static final Logger m_logger;
    protected final Reference EmptyReference;
    private String m_path;
    private final HashMap<String, Reference> m_cache;
    private long m_size;
    private long m_maxSize;
    private long m_notEnoughSpaceCount;
    private long m_missCacheCount;
    private boolean m_useCache;
    
    protected FileHashCache(final long maxSize, final boolean useCache) {
        super();
        this.EmptyReference = new Reference((Object)null) {
            @Override
            public long estimatedSize() {
                return 0L;
            }
        };
        this.m_cache = new HashMap<String, Reference>();
        this.m_maxSize = maxSize;
        this.m_useCache = useCache;
    }
    
    public abstract String getExtension();
    
    public abstract FilenameFilter getFilenameFilter();
    
    protected abstract Reference createReference(final byte[] p0) throws Exception;
    
    private Reference putData(final String fileName) throws Exception {
        assert this.m_path != null;
        assert fileName != null;
        final String name = this.m_path + fileName;
        final byte[] data = readFile(name);
        Reference ref;
        if (data.length == 0) {
            FileHashCache.m_logger.error((Object)("Impossible d'ouvrir un stream pour le fichier " + name));
            ref = this.EmptyReference;
        }
        else {
            ref = this.createReference(data);
            this.m_size += ref.estimatedSize();
            this.checkSpace();
            assert this.m_size >= 0L;
        }
        if (this.m_useCache) {
            this.m_cache.put(fileName, ref);
        }
        return ref;
    }
    
    private static byte[] readFile(final String name) {
        byte[] data;
        try {
            data = ContentFileHelper.readFile(name);
        }
        catch (IOException e) {
            FileHashCache.m_logger.error((Object)"", (Throwable)e);
            data = PrimitiveArrays.EMPTY_BYTE_ARRAY;
        }
        return data;
    }
    
    private void checkSpace() {
        final Iterator<Reference> iter = this.m_cache.values().iterator();
        while (iter.hasNext() && this.m_maxSize < this.m_size && !this.m_cache.isEmpty()) {
            final Reference ref = iter.next();
            this.m_size -= ref.estimatedSize();
            iter.remove();
            ++this.m_notEnoughSpaceCount;
            assert !this.m_cache.isEmpty() && this.m_size > 0L;
        }
    }
    
    public final T getData(final String fileName) throws Exception {
        assert fileName != null;
        Reference ref = this.m_useCache ? this.m_cache.get(fileName) : null;
        if (ref == null) {
            ref = this.putData(fileName);
            ++this.m_missCacheCount;
        }
        return ref.get();
    }
    
    public final void setMaxSize(final long maxSize) {
        this.m_maxSize = maxSize;
    }
    
    public boolean isUseCache() {
        return this.m_useCache;
    }
    
    public final void setUseCache(final long maxSize) {
        this.m_useCache = true;
        this.setMaxSize(maxSize);
    }
    
    public final String getPath() {
        return this.m_path;
    }
    
    public final void setPath(final String path) {
        assert path != null;
        this.m_path = path;
    }
    
    public void preloadAll() {
        this.preload(Long.MAX_VALUE, 1.0f);
        this.setMaxSize(this.m_size);
    }
    
    public void resetCache() {
        this.m_cache.clear();
    }
    
    public void preload(long maxSize, final float loadFactor) {
        this.setMaxSize(maxSize);
        assert this.m_path != null;
        maxSize = (int)(this.m_maxSize * loadFactor);
        try {
            final URL url = ContentFileHelper.getURL(this.m_path);
            if (url.getProtocol().equals("file")) {
                final File dir = new File(url.getPath() + File.separator);
                final String[] listNames = dir.list(this.getFilenameFilter());
                for (int i = 0; i < listNames.length && this.m_size < maxSize; ++i) {
                    this.putData(listNames[i]);
                }
            }
            else if (url.getProtocol().equals("jar")) {
                JarFile jarFile = null;
                try {
                    jarFile = new JarFile(url.getPath());
                    final Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements() && this.m_size < maxSize) {
                        final String fileName = entries.nextElement().getName();
                        if (fileName.endsWith(this.getExtension())) {
                            this.putData(fileName);
                        }
                    }
                }
                catch (Exception e) {
                    FileHashCache.m_logger.error((Object)"Exception", (Throwable)e);
                }
                if (jarFile != null) {
                    jarFile.close();
                }
            }
        }
        catch (Exception e3) {
            FileHashCache.m_logger.error((Object)("Impossible de pr\u00e9charger le contenu de " + this.m_path + " depuis une URL(tentative depuis un fichier"));
            try {
                final File dir2 = new File(this.m_path + File.separator);
                final String[] listNames2 = dir2.list(this.getFilenameFilter());
                for (int j = 0; j < listNames2.length && this.m_size < maxSize; ++j) {
                    this.putData(listNames2[j]);
                }
            }
            catch (Exception e2) {
                FileHashCache.m_logger.error((Object)"Exception", (Throwable)e2);
            }
        }
        FileHashCache.m_logger.info((Object)("PRELOAD " + this));
    }
    
    @Override
    public void addFileLoaderEventListener(final FileLoaderEventListener listener) {
    }
    
    @Override
    public void fireOnLoadCompleteEvent(final String fileName) {
    }
    
    @Override
    public void fireOnLoadErrorEvent(final String fileName, final String error) {
    }
    
    @Override
    public void fireOnLoadStartEvent(final String fileName) {
    }
    
    @Override
    public void removeFileLoaderEventLstener(final FileLoaderEventListener listener) {
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": cacheSize=" + this.m_cache.size() + "\t size=" + this.m_size + "octets/ " + this.m_maxSize + " missCache=" + this.m_missCacheCount + " notEnoughSize=" + this.m_notEnoughSpaceCount;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FileHashCache.class);
    }
    
    protected abstract class Reference
    {
        protected T m_data;
        
        protected Reference(final T data) {
            super();
            this.m_data = data;
        }
        
        public final T get() {
            return this.m_data;
        }
        
        public abstract long estimatedSize();
    }
}
