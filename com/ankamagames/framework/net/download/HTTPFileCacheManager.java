package com.ankamagames.framework.net.download;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;

public class HTTPFileCacheManager
{
    private static final Logger m_logger;
    private static final int VERSION = 0;
    public static final HTTPFileCacheManager INSTANCE;
    private final HashMap<String, String> m_ETagMap;
    private String m_fileName;
    private final Object m_mutex;
    
    public HTTPFileCacheManager() {
        super();
        this.m_ETagMap = new HashMap<String, String>();
        this.m_mutex = new Object();
    }
    
    public void setFileName(final String fileName) {
        this.m_fileName = fileName;
    }
    
    public void put(final String fileName, final String etag) {
        synchronized (this.m_mutex) {
            this.m_ETagMap.put(fileName, etag);
            this.save();
        }
    }
    
    public String getEtag(final String filename) {
        synchronized (this.m_mutex) {
            return this.m_ETagMap.get(filename);
        }
    }
    
    public void load() {
        synchronized (this.m_mutex) {
            this.m_ETagMap.clear();
            try {
                final ExtendedDataInputStream istream = ExtendedDataInputStream.wrap(FileHelper.readFile(this.m_fileName));
                final int version = istream.readInt();
                for (int numEntries = istream.readInt(), i = 0; i < numEntries; ++i) {
                    this.m_ETagMap.put(istream.readString(), istream.readString());
                }
                istream.close();
            }
            catch (FileNotFoundException e2) {
                HTTPFileCacheManager.m_logger.info((Object)"No cache data file");
            }
            catch (IOException e) {
                HTTPFileCacheManager.m_logger.warn((Object)("Probl\u00e8me \u00e0 l'ouverture des donn\u00e9es de cache : " + e.getMessage()), (Throwable)e);
            }
        }
    }
    
    public void save() {
        synchronized (this.m_mutex) {
            try {
                final FileOutputStream fileOutputStream = FileHelper.createFileOutputStream(this.m_fileName);
                final OutputBitStream ostream = new OutputBitStream(fileOutputStream);
                ostream.writeInt(0);
                ostream.writeInt(this.m_ETagMap.size());
                for (final Map.Entry<String, String> entry : this.m_ETagMap.entrySet()) {
                    ostream.writeString(entry.getKey());
                    ostream.writeString(entry.getValue());
                }
                ostream.close();
                fileOutputStream.close();
            }
            catch (IOException e) {
                HTTPFileCacheManager.m_logger.warn((Object)("Probl\u00e8me \u00e0 la sauvegarde des donn\u00e9es de cache :" + e.getMessage()));
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)HTTPFileCacheManager.class);
        INSTANCE = new HTTPFileCacheManager();
    }
}
