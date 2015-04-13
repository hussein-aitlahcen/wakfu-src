package com.ankamagames.wakfu.client.core;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import java.io.*;
import com.ankamagames.framework.java.util.*;

public class WakfuClientConfigurationManager
{
    private static final Logger m_logger;
    private static WakfuClientConfigurationManager m_instance;
    public static final String DIRECTORY_NAME = "clientConfig";
    private String m_accountDirectoryPattern;
    private String m_worldDirectoryPattern;
    private String m_characterDirectoryPattern;
    private String m_instanceDirectoryPattern;
    private String m_accountFilePattern;
    private String m_worldFilePattern;
    private String m_characterFilePattern;
    private String m_instanceFilePattern;
    public static final byte ACCOUNT_PROPERTY = 1;
    public static final byte WORLD_PROPERTY = 2;
    public static final byte CHARACTER_PROPERTY = 3;
    public static final byte INSTANCE_PROPERTY = 4;
    private String m_currentAccountName;
    private String m_currentWorldName;
    private String m_currentCharacterName;
    private short m_currentInstanceId;
    private Properties m_accountProperties;
    private Properties m_worldProperties;
    private Properties m_characterProperties;
    private Properties m_instanceProperties;
    
    private WakfuClientConfigurationManager() {
        super();
        this.m_accountDirectoryPattern = "clientConfig/%s";
        this.m_worldDirectoryPattern = "clientConfig/%s/%s";
        this.m_characterDirectoryPattern = "clientConfig/%s/%s/%s";
        this.m_instanceDirectoryPattern = "clientConfig/%s/%s/%s/%d";
        this.m_accountFilePattern = "clientConfig/%s/account.xml";
        this.m_worldFilePattern = "clientConfig/%s/%s/world.xml";
        this.m_characterFilePattern = "clientConfig/%s/%s/%s/character.xml";
        this.m_instanceFilePattern = "clientConfig/%s/%s/%s/%d/instance.xml";
        this.m_currentAccountName = null;
        this.m_currentWorldName = null;
        this.m_currentCharacterName = null;
        this.m_currentInstanceId = -1;
        this.m_accountProperties = new Properties();
        this.m_worldProperties = new Properties();
        this.m_characterProperties = new Properties();
        this.m_instanceProperties = new Properties();
        String path = System.getProperty("preferenceStorePath");
        path = (StringUtils.isEmptyOrNull(path) ? "" : (path + "/"));
        final File clientConfigDirectory = new File(path + "clientConfig");
        if (!clientConfigDirectory.exists()) {
            clientConfigDirectory.mkdir();
        }
        this.m_accountDirectoryPattern = path + "clientConfig" + "/%s";
        this.m_worldDirectoryPattern = this.m_accountDirectoryPattern + "/%s";
        this.m_characterDirectoryPattern = this.m_worldDirectoryPattern + "/%s";
        this.m_instanceDirectoryPattern = this.m_characterDirectoryPattern + "/%d";
        this.m_accountFilePattern = this.m_accountDirectoryPattern + "/account.xml";
        this.m_worldFilePattern = this.m_worldDirectoryPattern + "/world.xml";
        this.m_characterFilePattern = this.m_characterDirectoryPattern + "/character.xml";
        this.m_instanceFilePattern = this.m_instanceDirectoryPattern + "/instance.xml";
    }
    
    public static WakfuClientConfigurationManager getInstance() {
        return WakfuClientConfigurationManager.m_instance;
    }
    
    public void setAccountName(final String name) {
        if (this.m_currentAccountName == name || (name != null && name.equals(this.m_currentAccountName)) || (this.m_currentAccountName != null && this.m_currentAccountName.equals(name))) {
            return;
        }
        if (this.m_currentAccountName != null) {
            this.saveAccount();
        }
        this.m_currentAccountName = name;
        if (this.m_currentAccountName != null) {
            this.loadAccount();
        }
        else {
            this.m_accountProperties.clear();
        }
    }
    
    public void setWorldName(final String name) {
        if (this.m_currentWorldName == name || (name != null && name.equals(this.m_currentWorldName)) || (this.m_currentWorldName != null && this.m_currentWorldName.equals(name))) {
            return;
        }
        if (this.m_currentWorldName != null) {
            this.saveWorld();
        }
        this.m_currentWorldName = name;
        if (this.m_currentWorldName != null) {
            this.loadWorld();
        }
        else {
            this.m_worldProperties.clear();
        }
    }
    
    public void setCharacterName(final String name) {
        if (this.m_currentCharacterName == name || (name != null && name.equals(this.m_currentCharacterName)) || (this.m_currentCharacterName != null && this.m_currentCharacterName.equals(name))) {
            return;
        }
        if (this.m_currentCharacterName != null) {
            this.saveCharacter();
        }
        this.m_currentCharacterName = name;
        if (this.m_currentCharacterName != null) {
            this.loadCharacter();
        }
        else {
            this.m_characterProperties.clear();
        }
    }
    
    public void setInstanceId(final short id) {
        if (this.m_currentInstanceId == id) {
            return;
        }
        if (this.m_currentInstanceId != -1) {
            this.saveInstance();
        }
        this.m_currentInstanceId = id;
        if (this.m_currentInstanceId != -1) {
            this.loadInstance();
        }
        else {
            this.m_instanceProperties.clear();
        }
    }
    
    private void load(final Properties p, final String directory, final String file) {
        File f = new File(directory);
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(file);
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (IOException e) {
                WakfuClientConfigurationManager.m_logger.warn((Object)("impossible de cr\u00e9er le fichier de compte " + file + " : " + e.getMessage()));
            }
        }
        try {
            final InputStream is = new FileInputStream(file);
            p.load(is);
            is.close();
        }
        catch (IOException e) {
            WakfuClientConfigurationManager.m_logger.warn((Object)("Impossible de charger le fichier de propri\u00e9t\u00e9s de compte " + file));
        }
    }
    
    private void save(final Properties p, final String file) {
        if (file == null) {
            WakfuClientConfigurationManager.m_logger.warn((Object)"Impossible de sauver les propri\u00e9t\u00e9s d'un fichier inconnu");
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            p.store(out, null);
        }
        catch (IOException e) {
            WakfuClientConfigurationManager.m_logger.warn((Object)("Probl\u00e8me \u00e0 l'enregistrement du fichier " + file));
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                    WakfuClientConfigurationManager.m_logger.warn((Object)("Probl\u00e8me \u00e0 la fermeture du fichier de propri\u00e9t\u00e9s " + file));
                }
            }
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e2) {
                    WakfuClientConfigurationManager.m_logger.warn((Object)("Probl\u00e8me \u00e0 la fermeture du fichier de propri\u00e9t\u00e9s " + file));
                }
            }
        }
    }
    
    private void saveAll() {
        if (this.m_currentAccountName == null) {
            return;
        }
        this.saveAccount();
        if (this.m_currentWorldName == null) {
            return;
        }
        this.saveWorld();
        if (this.m_currentCharacterName == null) {
            return;
        }
        this.saveCharacter();
        if (this.m_currentInstanceId == -1) {
            return;
        }
        this.saveInstance();
    }
    
    public void loadAccount() {
        if (this.m_currentAccountName == null) {
            return;
        }
        this.load(this.m_accountProperties, this.getAccountDirectory(), String.format(this.m_accountFilePattern, this.m_currentAccountName));
        this.loadWorld();
    }
    
    public void saveAccount() {
        if (this.m_currentAccountName != null) {
            this.save(this.m_accountProperties, String.format(this.m_accountFilePattern, this.m_currentAccountName));
        }
    }
    
    public void loadWorld() {
        if (this.m_currentAccountName != null && this.m_currentWorldName != null) {
            this.load(this.m_worldProperties, this.getWorldDirectory(), String.format(this.m_worldFilePattern, this.m_currentAccountName, this.m_currentWorldName));
            this.loadCharacter();
        }
    }
    
    public void saveWorld() {
        if (this.m_currentAccountName != null && this.m_currentWorldName != null) {
            this.save(this.m_worldProperties, String.format(this.m_worldFilePattern, this.m_currentAccountName, this.m_currentWorldName));
        }
    }
    
    public void loadCharacter() {
        if (this.m_currentAccountName != null && this.m_currentWorldName != null && this.m_currentCharacterName != null) {
            this.load(this.m_characterProperties, this.getCharacterDirectory(), String.format(this.m_characterFilePattern, this.m_currentAccountName, this.m_currentWorldName, this.m_currentCharacterName));
            this.loadInstance();
        }
    }
    
    public void saveCharacter() {
        if (this.m_currentAccountName != null && this.m_currentWorldName != null && this.m_currentCharacterName != null) {
            this.save(this.m_characterProperties, String.format(this.m_characterFilePattern, this.m_currentAccountName, this.m_currentWorldName, this.m_currentCharacterName));
        }
    }
    
    public void loadInstance() {
        if (this.m_currentAccountName != null && this.m_currentWorldName != null && this.m_currentCharacterName != null && this.m_currentInstanceId != -1) {
            this.load(this.m_instanceProperties, this.getInstanceDirectory(), String.format(this.m_instanceFilePattern, this.m_currentAccountName, this.m_currentWorldName, this.m_currentCharacterName, this.m_currentInstanceId));
        }
    }
    
    public void saveInstance() {
        if (this.m_currentAccountName != null && this.m_currentWorldName != null && this.m_currentCharacterName != null && this.m_currentInstanceId != -1) {
            this.save(this.m_instanceProperties, String.format(this.m_instanceFilePattern, this.m_currentAccountName, this.m_currentWorldName, this.m_currentCharacterName, this.m_currentInstanceId));
        }
    }
    
    public String getAccountDirectory() {
        return String.format(this.m_accountDirectoryPattern, this.m_currentAccountName);
    }
    
    public String getWorldDirectory() {
        return String.format(this.m_worldDirectoryPattern, this.m_currentAccountName, this.m_currentWorldName);
    }
    
    public String getCharacterDirectory() {
        return String.format(this.m_characterDirectoryPattern, this.m_currentAccountName, this.m_currentWorldName, this.m_currentCharacterName);
    }
    
    public String getInstanceDirectory() {
        return this.getInstanceDirectory(this.m_currentInstanceId);
    }
    
    public String getInstanceDirectory(final short worldId) {
        return String.format(this.m_instanceDirectoryPattern, this.m_currentAccountName, this.m_currentWorldName, this.m_currentCharacterName, worldId);
    }
    
    private Properties getProperties(final byte domain) {
        switch (domain) {
            case 1: {
                return this.m_accountProperties;
            }
            case 2: {
                return this.m_worldProperties;
            }
            case 3: {
                return this.m_characterProperties;
            }
            case 4: {
                return this.m_instanceProperties;
            }
            default: {
                WakfuClientConfigurationManager.m_logger.warn((Object)("Domaine invalide : " + domain));
                return null;
            }
        }
    }
    
    public void setValue(final byte domain, final String key, final String value) {
        final Properties p = this.getProperties(domain);
        if (p == null) {
            return;
        }
        p.setProperty(key, value);
        this.saveAll();
    }
    
    public void setValue(final byte domain, final String key, final byte value) {
        this.setValue(domain, key, String.valueOf(value));
    }
    
    public void setValue(final byte domain, final String key, final int value) {
        this.setValue(domain, key, String.valueOf(value));
    }
    
    public void setValue(final byte domain, final String key, final short value) {
        this.setValue(domain, key, String.valueOf(value));
    }
    
    public void setValue(final byte domain, final String key, final float value) {
        this.setValue(domain, key, String.valueOf(value));
    }
    
    public void setValue(final byte domain, final String key, final double value) {
        this.setValue(domain, key, String.valueOf(value));
    }
    
    public void setValue(final byte domain, final String key, final long value) {
        this.setValue(domain, key, String.valueOf(value));
    }
    
    public void setValue(final byte domain, final String key, final boolean value) {
        this.setValue(domain, key, String.valueOf(value));
    }
    
    public String getStringValue(final byte domain, final String key) {
        final Properties p = this.getProperties(domain);
        if (p == null) {
            return null;
        }
        return p.getProperty(key);
    }
    
    public boolean getBooleanValue(final byte domain, final String key) {
        return PrimitiveConverter.getBoolean(this.getStringValue(domain, key));
    }
    
    public byte getByteValue(final byte domain, final String key) {
        return PrimitiveConverter.getByte(this.getStringValue(domain, key));
    }
    
    public short getShortValue(final byte domain, final String key) {
        return PrimitiveConverter.getShort(this.getStringValue(domain, key));
    }
    
    public int getIntValue(final byte domain, final String key) {
        return PrimitiveConverter.getInteger(this.getStringValue(domain, key));
    }
    
    public long getLongValue(final byte domain, final String key) {
        return PrimitiveConverter.getLong(this.getStringValue(domain, key));
    }
    
    public float getFloatValue(final byte domain, final String key) {
        return PrimitiveConverter.getFloat(this.getStringValue(domain, key));
    }
    
    public double getDoubleValue(final byte domain, final String key) {
        return PrimitiveConverter.getDouble(this.getStringValue(domain, key));
    }
    
    public boolean containsKey(final byte domain, final String key) {
        final Properties properties = this.getProperties(domain);
        return properties != null && properties.containsKey(key);
    }
    
    public void removeKey(final byte domain, final String key) {
        final Properties properties = this.getProperties(domain);
        if (properties == null) {
            return;
        }
        properties.remove(key);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuClientConfigurationManager.class);
        WakfuClientConfigurationManager.m_instance = new WakfuClientConfigurationManager();
    }
}
