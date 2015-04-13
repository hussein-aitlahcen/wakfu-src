package com.ankamagames.framework.fileFormat.io;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.io.*;
import java.io.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.*;

public class ContentFileHelper
{
    public static boolean FORCE_TUTORIAL;
    private static final Logger m_logger;
    private static final MultiPakIndexer m_multiJarIndex;
    
    public static String getBaseContentPath() {
        return ContentFileHelper.m_multiJarIndex.getContentPrefix();
    }
    
    public static boolean prepare(final String fileIndexersPath, final String contentPrefix) {
        try {
            ContentFileHelper.m_multiJarIndex.readFrom(fileIndexersPath);
            ContentFileHelper.m_multiJarIndex.setContentPrefix(contentPrefix);
            return true;
        }
        catch (IOException e) {
            ContentFileHelper.m_logger.error((Object)"", (Throwable)e);
            return false;
        }
    }
    
    public static AsyncURL loadAsyncURL(final String filename) throws MalformedURLException {
        final String path = transformFileName(filename);
        return AsyncLoader.getInstance().load(getURL(path));
    }
    
    public static byte[] readFile(final String filename) throws IOException {
        final String path = transformFileName(filename);
        return FileHelper.readFile(path);
    }
    
    public static byte[] readFile(final String filename, final int size) throws IOException {
        final String path = transformFileName(filename);
        return FileHelper.readFile(path, size);
    }
    
    public static InputStream openFile(final String filename) throws IOException {
        final String path = transformFileName(filename);
        return FileHelper.openFile(path);
    }
    
    public static URL getURL(final String filename) throws MalformedURLException {
        final String path = transformFileName(filename);
        return new URL(path);
    }
    
    public static String getPath(final String path, final Object... args) {
        final String format = String.format(path, args);
        return transformFileName(format);
    }
    
    public static String transformFileName(final String filename) {
        FileLoadingLogger.logUsageOf(filename);
        return ContentFileHelper.m_multiJarIndex.get(filename);
    }
    
    public static String transformFileNameForVlc(final String filename) {
        FileLoadingLogger.logUsageOf(filename);
        return ContentFileHelper.m_multiJarIndex.getAbsolute(filename, "zip:///");
    }
    
    static {
        ContentFileHelper.FORCE_TUTORIAL = false;
        m_logger = Logger.getLogger((Class)ContentFileHelper.class);
        m_multiJarIndex = new MultiPakIndexer();
    }
}
