package com.ankamagames.framework.sound.helper;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.*;
import com.ankamagames.framework.sound.stream.*;
import java.io.*;

public class FileAudioResourceHelper implements AudioResourceHelper
{
    private static final Logger m_logger;
    private String m_basePath;
    private final String[] m_extension;
    
    public FileAudioResourceHelper(final String basePath, final String... extensions) {
        super();
        this.m_basePath = basePath;
        this.m_extension = extensions;
    }
    
    public void setBasePath(final String basePath) {
        this.m_basePath = basePath;
    }
    
    @Override
    public AudioStreamProvider fromId(final long id) throws IOException {
        final String base = this.m_basePath + id + ".";
        for (int i = 0, size = this.m_extension.length; i < size; ++i) {
            final String fileName = base + this.m_extension[i];
            FileLoadingLogger.logUsageOf(fileName);
            final File f = new File(fileName);
            if (f.exists()) {
                return new DirectFileAudioStreamProvider(f);
            }
        }
        FileAudioResourceHelper.m_logger.error((Object)("Impossible de trouver le fichier d'id " + id + " dans le path " + this.m_basePath));
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FileAudioResourceHelper.class);
    }
}
