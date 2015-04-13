package com.ankamagames.framework.sound.helper;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.fileFormat.*;
import com.ankamagames.framework.sound.stream.*;
import java.io.*;

public class PakAudioResourceHelper implements AudioResourceHelper
{
    private static final Logger m_logger;
    private PakFile m_pakFile;
    private final String[] m_extension;
    
    public PakAudioResourceHelper(final String... extension) {
        super();
        this.m_extension = extension;
    }
    
    public void setPakFile(final PakFile pakFile) {
        this.m_pakFile = pakFile;
    }
    
    @Override
    public AudioStreamProvider fromId(final long id) throws IOException {
        if (this.m_pakFile == null) {
            PakAudioResourceHelper.m_logger.error((Object)"PakFile non d\u00e9finie !");
            return null;
        }
        final String base = this.m_pakFile.getDataPath() + "!/";
        for (int i = 0, size = this.m_extension.length; i < size; ++i) {
            final String fileName = id + "." + this.m_extension[i];
            FileLoadingLogger.logUsageOf(base + fileName);
            if (this.m_pakFile.containsFile(fileName)) {
                return new PakAudioStreamProvider(this.m_pakFile, fileName, base + fileName);
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PakAudioResourceHelper.class);
    }
}
