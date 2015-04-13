package com.ankamagames.wakfu.client.core.game.almanach;

import org.apache.log4j.*;
import java.io.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.wakfu.client.core.*;

public class AlmanachFileHelper
{
    private static final Logger m_logger;
    
    public static String fileToURL(final File f) {
        try {
            return f.toURI().toURL().toString();
        }
        catch (MalformedURLException e) {
            AlmanachFileHelper.m_logger.warn((Object)e.getMessage());
            return null;
        }
    }
    
    public static File getCachedFilePathFromRemoteUrl(final String url) {
        final String parentDir = FileHelper.getParentPath(url);
        final String grandParentDir = FileHelper.getParentPath(parentDir);
        final String category = FileHelper.getDirectory(grandParentDir);
        final String id = FileHelper.getDirectory(parentDir);
        final String name = FileHelper.getNameWithoutExt(url);
        final String ext = FileHelper.getFileExt(url);
        final String fileName = category + id + name + '.' + ext;
        final String almanachDirectory = WakfuConfiguration.getInstance().getCachePath("almanach");
        return new File(almanachDirectory + File.separator + fileName);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AlmanachFileHelper.class);
    }
}
