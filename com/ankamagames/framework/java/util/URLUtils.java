package com.ankamagames.framework.java.util;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.net.*;
import com.ankamagames.framework.kernel.utils.*;
import java.io.*;

public final class URLUtils
{
    private static final Logger m_logger;
    
    public static boolean urlExists(final URL url) {
        try {
            final InputStream is = url.openStream();
            is.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public static boolean urlExists(final String url) {
        URL myUrl;
        try {
            myUrl = ContentFileHelper.getURL(url);
        }
        catch (MalformedURLException e) {
            return false;
        }
        return urlExists(myUrl);
    }
    
    public static URL urlCompound(final URL context, String path) throws MalformedURLException {
        int previousDirs;
        for (previousDirs = 0; path.startsWith("../"); path = path.substring(3), ++previousDirs) {}
        final String externalForm = context.toExternalForm();
        final String[] parts = StringUtils.split(externalForm, '/');
        if (previousDirs > parts.length - 1) {
            URLUtils.m_logger.error((Object)("Impossible de rajouter " + previousDirs + " ../ au chemin " + context.toExternalForm()));
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        final int offset = isDirectory(context) ? 0 : 1;
        final int repeat = parts.length - offset - previousDirs;
        if (repeat > 0) {
            for (int i = 0; i < repeat; ++i) {
                sb.append(parts[i]).append('/');
            }
        }
        else {
            sb.append(context.getProtocol()).append(':');
        }
        sb.append(path);
        return ContentFileHelper.getURL(sb.toString());
    }
    
    private static boolean isDirectory(final URL context) {
        final String fileComponent = context.getFile();
        if (fileComponent == null) {
            return false;
        }
        if (context.getProtocol().equals("file")) {
            final File f = new File(fileComponent);
            return f.exists() && f.isDirectory();
        }
        final int length = fileComponent.length();
        final int lastIndex = fileComponent.lastIndexOf(47);
        if (lastIndex != -1 && lastIndex + 1 == length) {
            return true;
        }
        for (int i = lastIndex + 1; i < length; ++i) {
            if (fileComponent.charAt(i) == '.') {
                return false;
            }
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)URLUtils.class);
    }
}
