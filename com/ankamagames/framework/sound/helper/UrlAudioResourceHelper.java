package com.ankamagames.framework.sound.helper;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.sound.stream.*;
import java.net.*;
import java.io.*;

public class UrlAudioResourceHelper implements AudioResourceHelper
{
    private static final Logger m_logger;
    private final String m_baseUrl;
    private final String[] m_extension;
    
    public UrlAudioResourceHelper(final String baseUrl, final String... extensions) {
        super();
        this.m_baseUrl = baseUrl;
        this.m_extension = extensions;
    }
    
    @Override
    public AudioStreamProvider fromId(final long id) throws IOException {
        final String base = this.m_baseUrl + id + ".";
        for (int i = 0, size = this.m_extension.length; i < size; ++i) {
            final String fileName = base + this.m_extension[i];
            FileLoadingLogger.logUsageOf(fileName);
            try {
                final URL url = ContentFileHelper.getURL(fileName);
                if (URLUtils.urlExists(url)) {
                    return new SimpleAudioStreamProvider(url);
                }
            }
            catch (MalformedURLException e) {
                UrlAudioResourceHelper.m_logger.error((Object)("URL malform\u00e9e \u00e0 partie de l'ID sp\u00e9cifi\u00e9: result=" + fileName));
            }
        }
        UrlAudioResourceHelper.m_logger.error((Object)("Impossible de trouver le fichier d'id " + id + " dans le path " + this.m_baseUrl));
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UrlAudioResourceHelper.class);
    }
}
