package com.ankamagames.framework.preferences;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.io.*;
import java.util.*;

public class PreferenceStore
{
    protected static final Logger m_logger;
    public static final boolean BOOLEAN_DEFAULT_DEFAULT = false;
    public static final double DOUBLE_DEFAULT_DEFAULT = 0.0;
    public static final float FLOAT_DEFAULT_DEFAULT = 0.0f;
    public static final int INT_DEFAULT_DEFAULT = 0;
    public static final long LONG_DEFAULT_DEFAULT = 0L;
    public static final String STRING_DEFAULT_DEFAULT = "";
    public static final String PREFERENCE_STORE_PATH = "preferenceStorePath";
    private final Properties m_properties;
    private final Properties m_defaultProperties;
    private boolean m_dirty;
    private String m_fileName;
    private boolean m_autoSave;
    private boolean m_ignoreNoneDefaultDefinedProperties;
    private final ArrayList<PreferencePropertyChangeListener> m_preferencePropertyChangeListeners;
    private final ArrayList<PreferenceStoreLoadListener> m_preferenceStoreLoadListeners;
    
    public PreferenceStore() {
        super();
        this.m_dirty = false;
        this.m_autoSave = false;
        this.m_ignoreNoneDefaultDefinedProperties = false;
        this.m_preferencePropertyChangeListeners = new ArrayList<PreferencePropertyChangeListener>();
        this.m_preferenceStoreLoadListeners = new ArrayList<PreferenceStoreLoadListener>();
        this.m_defaultProperties = new Properties();
        this.m_properties = new Properties(this.m_defaultProperties);
    }
    
    public PreferenceStore(final String fileName) {
        this();
        final String path = System.getProperty("preferenceStorePath");
        final File file = new File(StringUtils.isEmptyOrNull(path) ? "." : path, fileName);
        final File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        this.setFileName(file.getAbsolutePath());
    }
    
    public String getFileName() {
        return this.m_fileName;
    }
    
    public void setFileName(final String fileName) {
        this.m_fileName = fileName;
    }
    
    public boolean isAutoSave() {
        return this.m_autoSave;
    }
    
    public void setAutoSave(final boolean autoSave) {
        this.m_autoSave = autoSave;
    }
    
    public boolean isIgnoreNoneDefaultDefinedProperties() {
        return this.m_ignoreNoneDefaultDefinedProperties;
    }
    
    public void setIgnoreNoneDefaultDefinedProperties(final boolean ignoreNoneDefaultDefinedProperties) {
        this.m_ignoreNoneDefaultDefinedProperties = ignoreNoneDefaultDefinedProperties;
    }
    
    public void load() throws IOException {
        if (this.m_fileName == null) {
            return;
        }
        try {
            final FileInputStream inputStream = new FileInputStream(this.m_fileName);
            try {
                this.load(inputStream);
            }
            catch (Exception e) {
                PreferenceStore.m_logger.info((Object)("Erreur dans le fichier de pr\u00e9f\u00e9rences " + this.m_fileName), (Throwable)e);
            }
            finally {
                inputStream.close();
            }
        }
        catch (FileNotFoundException ex) {
            PreferenceStore.m_logger.info((Object)("Fichier de pr\u00e9f\u00e9rences " + this.m_fileName + " inexistant, on le cr\u00e9e"));
        }
    }
    
    private void load(final InputStream inputStrean) throws IOException {
        try {
            this.m_properties.load(inputStrean);
            this.m_dirty = false;
            this.notifyLoadListeners();
        }
        catch (IllegalArgumentException e) {
            PreferenceStore.m_logger.info((Object)("Fichier de pr\u00e9f\u00e9rences " + this.m_fileName + " corrompu"), (Throwable)e);
        }
    }
    
    private void notifyLoadListeners() {
        if (!this.m_preferenceStoreLoadListeners.isEmpty()) {
            for (int i = 0; i < this.m_preferenceStoreLoadListeners.size(); ++i) {
                this.m_preferenceStoreLoadListeners.get(i).onPreferenceStoreLoaded(this);
            }
        }
    }
    
    public void save() throws IOException {
        if (this.m_fileName == null) {
            throw new IOException("File name not specified");
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(this.m_fileName);
            this.save(out);
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    public void save(final OutputStream outputStream) throws IOException {
        this.m_properties.store(outputStream, null);
        this.m_dirty = false;
    }
    
    public int getPropertiesCount() {
        return this.m_properties.size();
    }
    
    public String[] getPropertyList() {
        final ArrayList propertyList = new ArrayList();
        Enumeration it = this.m_defaultProperties.propertyNames();
        while (it.hasMoreElements()) {
            propertyList.add(it.nextElement());
        }
        it = this.m_properties.propertyNames();
        while (it.hasMoreElements()) {
            final Object property = it.nextElement();
            if (!propertyList.contains(property)) {
                propertyList.add(property);
            }
        }
        return propertyList.toArray(new String[propertyList.size()]);
    }
    
    public void resetToDefault() {
        final Enumeration it = this.m_properties.propertyNames();
        this.m_properties.clear();
        while (it.hasMoreElements()) {
            final Object key = it.nextElement();
            this.m_properties.setProperty(String.valueOf(key), String.valueOf(this.m_defaultProperties.getProperty(String.valueOf(key))));
        }
        try {
            this.save();
        }
        catch (IOException e) {
            PreferenceStore.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    public boolean needsSaving() {
        return this.m_dirty;
    }
    
    public void addPreferencePropertyChangedListener(final PreferencePropertyChangeListener listener) {
        this.m_preferencePropertyChangeListeners.add(listener);
    }
    
    public void removePreferencePropertyChangedListener(final PreferencePropertyChangeListener listener) {
        this.m_preferencePropertyChangeListeners.remove(listener);
    }
    
    public void addPreferenceStoreLoadListener(final PreferenceStoreLoadListener listener) {
        this.m_preferenceStoreLoadListeners.add(listener);
    }
    
    public void removePreferenceStoreLoadListener(final PreferenceStoreLoadListener listener) {
        this.m_preferenceStoreLoadListeners.remove(listener);
    }
    
    public void removeAllPreferencePropertyChangedListeners() {
        this.m_preferencePropertyChangeListeners.clear();
    }
    
    public boolean contains(final String name) {
        return this.m_properties.containsKey(name) || this.m_defaultProperties.containsKey(name);
    }
    
    public Enumeration<Object> keyIterator() {
        return ((Hashtable<Object, V>)this.m_properties).keys();
    }
    
    public void removeKey(final String key) {
        final Object value = ((Hashtable<K, Object>)this.m_properties).remove(key);
        if (value != null) {
            this.firePropertyChangeEvent(key, value, null);
            if (this.m_autoSave) {
                try {
                    this.save();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public boolean getDefaultBoolean(final String name) {
        return this.getBoolean(this.m_defaultProperties, name);
    }
    
    public double getDefaultDouble(final String name) {
        return this.getDouble(this.m_defaultProperties, name);
    }
    
    public float getDefaultFloat(final String name) {
        return this.getFloat(this.m_defaultProperties, name);
    }
    
    public int getDefaultInt(final String name) {
        return this.getInt(this.m_defaultProperties, name);
    }
    
    public long getDefaultLong(final String name) {
        return this.getLong(this.m_defaultProperties, name);
    }
    
    public String getDefaultString(final String name) {
        return this.getString(this.m_defaultProperties, name);
    }
    
    public boolean isDefault(final String name) {
        return !this.m_properties.containsKey(name) && this.m_defaultProperties.containsKey(name);
    }
    
    public String getValue(final String name) {
        if (this.m_properties.containsKey(name)) {
            return this.m_properties.getProperty(name);
        }
        if (this.m_defaultProperties.containsKey(name)) {
            return this.m_defaultProperties.getProperty(name);
        }
        return null;
    }
    
    public boolean getBoolean(final String name) {
        return this.getBoolean(null, name);
    }
    
    public double getDouble(final String name) {
        return this.getDouble(null, name);
    }
    
    public float getFloat(final String name) {
        return this.getFloat(null, name);
    }
    
    public int getInt(final String name) {
        return this.getInt(null, name);
    }
    
    public long getLong(final String name) {
        return this.getLong(null, name);
    }
    
    public String getString(final String name) {
        return this.getString(null, name);
    }
    
    public void setDefault(final String name, final boolean value) {
        this.setValue(this.m_defaultProperties, name, value);
    }
    
    public void setDefault(final String name, final double value) {
        this.setValue(this.m_defaultProperties, name, value);
    }
    
    public void setDefault(final String name, final float value) {
        this.setValue(this.m_defaultProperties, name, value);
    }
    
    public void setDefault(final String name, final int value) {
        this.setValue(this.m_defaultProperties, name, value);
    }
    
    public void setDefault(final String name, final long value) {
        this.setValue(this.m_defaultProperties, name, value);
    }
    
    public void setDefault(final String name, final String value) {
        this.setValue(this.m_defaultProperties, name, value);
    }
    
    public void setValue(final String name, final boolean value) {
        final boolean oldValue = this.getBoolean(name);
        if (oldValue != value || !this.contains(name)) {
            if (this.m_ignoreNoneDefaultDefinedProperties && !this.m_defaultProperties.containsKey(name)) {
                return;
            }
            this.setValue(this.m_properties, name, value);
            this.m_dirty = true;
            this.firePropertyChangeEvent(name, oldValue, value);
            if (this.m_autoSave) {
                try {
                    this.save();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public void setValue(final String name, final double value) {
        final double oldValue = this.getDouble(name);
        if (oldValue != value || !this.contains(name)) {
            if (this.m_ignoreNoneDefaultDefinedProperties && !this.m_defaultProperties.containsKey(name)) {
                return;
            }
            this.setValue(this.m_properties, name, value);
            this.m_dirty = true;
            this.firePropertyChangeEvent(name, oldValue, value);
            if (this.m_autoSave) {
                try {
                    this.save();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public void setValue(final String name, final float value) {
        final float oldValue = this.getFloat(name);
        if (oldValue != value || !this.contains(name)) {
            if (this.m_ignoreNoneDefaultDefinedProperties && !this.m_defaultProperties.containsKey(name)) {
                return;
            }
            this.setValue(this.m_properties, name, value);
            this.m_dirty = true;
            this.firePropertyChangeEvent(name, oldValue, value);
            if (this.m_autoSave) {
                try {
                    this.save();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public void setValue(final String name, final int value) {
        final int oldValue = this.getInt(name);
        if (oldValue != value || !this.contains(name)) {
            if (this.m_ignoreNoneDefaultDefinedProperties && !this.m_defaultProperties.containsKey(name)) {
                return;
            }
            this.setValue(this.m_properties, name, value);
            this.m_dirty = true;
            this.firePropertyChangeEvent(name, oldValue, value);
            if (this.m_autoSave) {
                try {
                    this.save();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public void setValue(final String name, final long value) {
        final long oldValue = this.getLong(name);
        if (oldValue != value || !this.contains(name)) {
            if (this.m_ignoreNoneDefaultDefinedProperties && !this.m_defaultProperties.containsKey(name)) {
                return;
            }
            this.setValue(this.m_properties, name, value);
            this.m_dirty = true;
            this.firePropertyChangeEvent(name, oldValue, value);
            if (this.m_autoSave) {
                try {
                    this.save();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public void setValue(final String name, final String value) {
        final String oldValue = this.getString(name);
        if (oldValue == null || !oldValue.equals(value) || !this.contains(name)) {
            if (this.m_ignoreNoneDefaultDefinedProperties && !this.m_defaultProperties.containsKey(name)) {
                return;
            }
            this.setValue(this.m_properties, name, value);
            this.m_dirty = true;
            this.firePropertyChangeEvent(name, oldValue, value);
            if (this.m_autoSave) {
                try {
                    this.save();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public void firePropertyChangeEvent(final String name, final Object oldValue, final Object newValue) {
        if (this.m_preferencePropertyChangeListeners.size() > 0 && (oldValue == null || !oldValue.equals(newValue))) {
            final PreferencePropertyChangeEvent event = new PreferencePropertyChangeEvent(this, name, oldValue, newValue);
            for (int i = 0; i < this.m_preferencePropertyChangeListeners.size(); ++i) {
                final PreferencePropertyChangeListener listener = this.m_preferencePropertyChangeListeners.get(i);
                listener.propertyChange(event);
            }
        }
    }
    
    private boolean getBoolean(final Properties properties, final String name) {
        final String value = (properties != null) ? properties.getProperty(name) : this.getValue(name);
        return value != null && Boolean.valueOf(value);
    }
    
    private double getDouble(final Properties properties, final String name) {
        final String value = (properties != null) ? properties.getProperty(name) : this.getValue(name);
        if (value == null) {
            return 0.0;
        }
        double ival = 0.0;
        try {
            ival = Double.valueOf(value);
        }
        catch (NumberFormatException ex) {}
        return ival;
    }
    
    private float getFloat(final Properties properties, final String name) {
        final String value = (properties != null) ? properties.getProperty(name) : this.getValue(name);
        if (value == null) {
            return 0.0f;
        }
        float ival = 0.0f;
        try {
            ival = Float.valueOf(value);
        }
        catch (NumberFormatException ex) {}
        return ival;
    }
    
    private int getInt(final Properties properties, final String name) {
        final String value = (properties != null) ? properties.getProperty(name) : this.getValue(name);
        if (value == null) {
            return 0;
        }
        int ival = 0;
        try {
            ival = Integer.valueOf(value);
        }
        catch (NumberFormatException ex) {}
        return ival;
    }
    
    private long getLong(final Properties properties, final String name) {
        final String value = (properties != null) ? properties.getProperty(name) : this.getValue(name);
        if (value == null) {
            return 0L;
        }
        long ival = 0L;
        try {
            ival = Long.valueOf(value);
        }
        catch (NumberFormatException ex) {}
        return ival;
    }
    
    private String getString(final Properties properties, final String name) {
        final String value = (properties != null) ? properties.getProperty(name) : this.getValue(name);
        if (value == null) {
            return "";
        }
        return value;
    }
    
    private void setValue(final Properties properties, final String name, final boolean value) {
        if (properties != null) {
            ((Hashtable<String, String>)properties).put(name, Boolean.toString(value));
        }
    }
    
    private void setValue(final Properties properties, final String name, final double value) {
        if (properties != null) {
            ((Hashtable<String, String>)properties).put(name, Double.toString(value));
        }
    }
    
    private void setValue(final Properties properties, final String name, final float value) {
        if (properties != null) {
            ((Hashtable<String, String>)properties).put(name, Float.toString(value));
        }
    }
    
    private void setValue(final Properties properties, final String name, final int value) {
        if (properties != null) {
            ((Hashtable<String, String>)properties).put(name, Integer.toString(value));
        }
    }
    
    private void setValue(final Properties properties, final String name, final long value) {
        if (properties != null) {
            ((Hashtable<String, String>)properties).put(name, Long.toString(value));
        }
    }
    
    private void setValue(final Properties properties, final String name, final String value) {
        if (properties != null) {
            ((Hashtable<String, String>)properties).put(name, value);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PreferenceStore.class);
    }
}
