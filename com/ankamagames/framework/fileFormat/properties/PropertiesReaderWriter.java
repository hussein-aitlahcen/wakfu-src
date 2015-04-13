package com.ankamagames.framework.fileFormat.properties;

import org.apache.log4j.*;
import com.ankamagames.framework.java.util.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class PropertiesReaderWriter
{
    private static Logger m_logger;
    private static final String STRING_ARRAY_DELIM = ",";
    private static final String MULTI_KEY_SIMPLE_FORMAT = "%s_";
    private static final String MULTI_KEY_FORMAT = "%s_%d";
    private final Properties m_properties;
    
    public PropertiesReaderWriter() {
        super();
        this.m_properties = new Properties();
    }
    
    public String getString(final String key) throws PropertyException {
        final String s = this.m_properties.getProperty(key);
        if (s == null) {
            throw new PropertyException("Il n'existe pas de propri\u00e9t\u00e9: " + key);
        }
        return s;
    }
    
    public String getString(final String key, final String defaultValue) {
        final String s = this.m_properties.getProperty(key);
        return (s == null) ? defaultValue : s;
    }
    
    public ArrayList<String> getMultiString(final String key) throws PropertyException {
        int index = 1;
        final ArrayList<String> strings = new ArrayList<String>();
        for (String multiKey = String.format("%s_%d", key, index); this.m_properties.containsKey(multiKey); multiKey = String.format("%s_%d", key, ++index)) {
            strings.add(this.getString(multiKey));
        }
        return strings;
    }
    
    public String[] getStringArray(final String key) throws PropertyException {
        final String s = this.getString(key);
        return this.getSplitedString(s);
    }
    
    public ArrayList<String[]> getMultiStringArray(final String key) throws PropertyException {
        final ArrayList<String> values = this.getMultiString(key);
        final ArrayList<String[]> strings = new ArrayList<String[]>();
        for (final String value : values) {
            strings.add(this.getSplitedString(value));
        }
        return strings;
    }
    
    public int getInteger(final String key) throws PropertyException {
        final String s = this.getString(key);
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            throw new PropertyException("La propri\u00e9t\u00e9 " + key + " n'est pas un int.");
        }
    }
    
    public int getInteger(final String key, final int defaultValue) {
        return PrimitiveConverter.getInteger(this.m_properties.getProperty(key), defaultValue);
    }
    
    public float getFloat(final String key) throws PropertyException {
        final String s = this.getString(key);
        try {
            return Float.valueOf(s);
        }
        catch (NumberFormatException e) {
            throw new PropertyException("La propri\u00e9t\u00e9 " + key + " n'est pas un float.");
        }
    }
    
    public float getFloat(final String key, final float defaultValue) {
        return PrimitiveConverter.getFloat(this.m_properties.getProperty(key), defaultValue);
    }
    
    public double getDouble(final String key) throws PropertyException {
        final String s = this.getString(key);
        try {
            return Double.valueOf(s);
        }
        catch (NumberFormatException e) {
            throw new PropertyException("La propri\u00e9t\u00e9 " + key + " n'est pas un double.");
        }
    }
    
    public double getDouble(final String key, final double defaultValue) {
        return PrimitiveConverter.getDouble(this.m_properties.getProperty(key), defaultValue);
    }
    
    public boolean getBoolean(final String key) throws PropertyException {
        final String s = this.getString(key);
        return Boolean.valueOf(s);
    }
    
    public boolean getBoolean(final String key, final boolean defaultValue) {
        return PrimitiveConverter.getBoolean(this.m_properties.getProperty(key), defaultValue);
    }
    
    public void setString(final String key, final String value) {
        if (this.m_properties != null) {
            this.m_properties.setProperty(key, value);
        }
    }
    
    public void setMultiString(final String key, final ArrayList<String> values) {
        int index = 1;
        for (final String value : values) {
            final String multiKey = String.format("%s_%d", key, index++);
            this.setString(multiKey, value);
        }
    }
    
    public void setMultiStringArray(final String key, final ArrayList<String[]> values) {
        int index = 1;
        for (final String[] value : values) {
            final String multiKey = String.format("%s_%d", key, index++);
            this.setStringArray(multiKey, value);
        }
    }
    
    public void setStringArray(final String key, final String[] value) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; ++i) {
            sb.append(value[i]);
            if (i < value.length - 1) {
                sb.append(",");
            }
        }
        this.setString(key, sb.toString());
    }
    
    public void setInteger(final String key, final int value) {
        this.setString(key, Integer.toString(value));
    }
    
    public void setFloat(final String key, final float value) {
        this.setString(key, Float.toString(value));
    }
    
    public void setDouble(final String key, final double value) {
        this.setString(key, Double.toString(value));
    }
    
    public void setBoolean(final String key, final boolean value) {
        this.setString(key, Boolean.toString(value));
    }
    
    public boolean load(final InputStream inputStream) {
        try {
            this.m_properties.load(inputStream);
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public boolean load(final URL url) {
        try {
            if (url != null) {
                return this.load(url.openStream());
            }
            PropertiesReaderWriter.m_logger.error((Object)"url nulle au load.");
            return false;
        }
        catch (IOException e) {
            PropertiesReaderWriter.m_logger.error((Object)"Exception", (Throwable)e);
            return false;
        }
    }
    
    public boolean load(final String fileName) {
        try {
            final File file = new File(fileName);
            if (file.exists()) {
                return this.load(new FileInputStream(file));
            }
            final URL url = this.getClass().getClassLoader().getResource(fileName);
            if (url != null) {
                return this.load(url);
            }
            PropertiesReaderWriter.m_logger.error((Object)("Impossible de trouver le fichier de propri\u00e9t\u00e9 " + fileName));
            return false;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }
    
    public boolean save(final String file) {
        try {
            final OutputStream propertiesFile = new FileOutputStream(file);
            this.m_properties.store(propertiesFile, null);
            propertiesFile.close();
        }
        catch (FileNotFoundException e) {
            return false;
        }
        catch (IOException e2) {
            return false;
        }
        return true;
    }
    
    public void clearMultiKey(final String key) {
        final String multiKey = String.format("%s_", key);
        final Enumeration<Object> keys = ((Hashtable<Object, V>)this.m_properties).keys();
        while (keys.hasMoreElements()) {
            final Object obj = keys.nextElement();
            if (obj instanceof String) {
                final String currentKey = (String)obj;
                if (!currentKey.startsWith(multiKey)) {
                    continue;
                }
                this.m_properties.remove(currentKey);
            }
        }
    }
    
    private String[] getSplitedString(final String s) {
        final StringTokenizer st = new StringTokenizer(s, ",");
        final Vector<String> v = new Vector<String>();
        while (st.hasMoreTokens()) {
            v.addElement(st.nextToken());
        }
        final String[] strings = new String[v.size()];
        v.copyInto(strings);
        return strings;
    }
    
    static {
        PropertiesReaderWriter.m_logger = Logger.getLogger((Class)PropertiesReaderWriter.class);
    }
}
