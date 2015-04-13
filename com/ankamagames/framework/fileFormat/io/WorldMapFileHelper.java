package com.ankamagames.framework.fileFormat.io;

import org.apache.log4j.*;
import gnu.trove.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.java.util.*;
import java.io.*;
import java.net.*;

public class WorldMapFileHelper
{
    private static final Logger m_logger;
    private static final TIntObjectHashMap<String> m_worldsSuffix;
    
    public static boolean prepare(final String fileIndexersPath) {
        try {
            final InputStream istream = FileHelper.openFile(fileIndexersPath);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                final String[] pair = StringUtils.split(line, '=');
                if (pair.length != 2) {
                    WorldMapFileHelper.m_logger.warn((Object)("Erreur avec la ligne " + line + " (" + fileIndexersPath + ")"));
                }
                else {
                    WorldMapFileHelper.m_worldsSuffix.put(PrimitiveConverter.getInteger(pair[0]), pair[1].intern());
                }
            }
            reader.close();
            istream.close();
        }
        catch (IOException e) {
            WorldMapFileHelper.m_logger.error((Object)"", (Throwable)e);
            return false;
        }
        WorldMapFileHelper.m_worldsSuffix.setAutoCompactionFactor(0.0f);
        WorldMapFileHelper.m_worldsSuffix.compact();
        return true;
    }
    
    public static byte[] readFile(final String filename) throws IOException {
        return FileHelper.readFile(transformFileName(filename));
    }
    
    public static URL getURL(final String filename) throws MalformedURLException {
        return new URL(transformFileName(filename));
    }
    
    public static String getURLString(final String filename) {
        return transformFileName(filename);
    }
    
    private static String transformFileName(final String name) {
        try {
            final String filename = ContentFileHelper.transformFileName(name);
            final String path = StringUtils.splitByWholeSeparator(filename, "!/", 0)[0];
            final String worldName = FileHelper.getNameWithoutExt(path);
            final int worldId = PrimitiveConverter.getInteger(worldName);
            final String suffix = WorldMapFileHelper.m_worldsSuffix.get(worldId);
            if (suffix == null) {
                return filename;
            }
            final String fileExt = FileHelper.getFileExt(path);
            final String extension = fileExt.isEmpty() ? "" : ('.' + fileExt);
            return filename.replace(extension, suffix + extension);
        }
        catch (Exception e) {
            WorldMapFileHelper.m_logger.error((Object)"", (Throwable)e);
            return name;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldMapFileHelper.class);
        m_worldsSuffix = new TIntObjectHashMap<String>(20, 1.0f);
    }
}
