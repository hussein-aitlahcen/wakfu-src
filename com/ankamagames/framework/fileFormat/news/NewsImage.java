package com.ankamagames.framework.fileFormat.news;

import java.io.*;
import java.net.*;
import org.jetbrains.annotations.*;

public class NewsImage
{
    private final File m_localFile;
    private final URL m_remoteURL;
    
    NewsImage(@NotNull final URL remoteURL, @NotNull final File localFile) {
        super();
        this.m_localFile = localFile;
        this.m_remoteURL = remoteURL;
    }
    
    public File getFile() {
        return this.m_localFile;
    }
    
    public boolean isAvailable() {
        return this.m_localFile != null && this.m_localFile.isFile();
    }
    
    URL getRemoteURL() {
        return this.m_remoteURL;
    }
}
