package com.ankamagames.wakfu.client.core.game.achievements;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import org.apache.commons.lang3.builder.*;
import gnu.trove.*;

public class QuestConfigManager
{
    public static final QuestConfigManager INSTANCE;
    private static final Logger m_logger;
    private static final int VERSION = 1;
    private final TIntObjectHashMap<QuestConfig> m_configs;
    
    private QuestConfigManager() {
        super();
        this.m_configs = new TIntObjectHashMap<QuestConfig>();
    }
    
    private static String getFile() {
        return WakfuClientConfigurationManager.getInstance().getCharacterDirectory() + "/questConfig.dat";
    }
    
    public void loadConfig() {
        this.m_configs.clear();
        final String path = getFile();
        try {
            final ExtendedDataInputStream istream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(path));
            final int version = istream.readInt();
            for (int numConfig = istream.readInt(), i = 0; i < numConfig; ++i) {
                final QuestConfig config = new QuestConfig();
                final int id = istream.readInt();
                config.load(istream, version);
                this.m_configs.put(id, config);
            }
        }
        catch (IOException e) {
            QuestConfigManager.m_logger.warn((Object)("probl\u00e8me \u00e0 l'ouverture des configs de qu\u00eates: " + e.getMessage()));
        }
    }
    
    public void saveConfig() {
        final String path = getFile();
        try {
            final FileOutputStream fileOutputStream = FileHelper.createFileOutputStream(path);
            final OutputBitStream ostream = new OutputBitStream(fileOutputStream);
            ostream.writeInt(1);
            ostream.writeInt(this.m_configs.size());
            final TIntObjectIterator<QuestConfig> it = this.m_configs.iterator();
            while (it.hasNext()) {
                it.advance();
                ostream.writeInt(it.key());
                it.value().save(ostream, 1);
            }
            ostream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            QuestConfigManager.m_logger.warn((Object)("probl\u00e8me \u00e0 la sauvegarde des configs de qu\u00eates :" + e.getMessage()));
        }
    }
    
    public void setOpened(final int questId, final boolean opened) {
        QuestConfig config = this.m_configs.get(questId);
        if (config == null) {
            config = new QuestConfig();
            this.m_configs.put(questId, config);
        }
        config.setOpened(opened);
        this.saveConfig();
    }
    
    public boolean isOpened(final int questId) {
        final QuestConfig config = this.m_configs.get(questId);
        return config == null || config.isOpened();
    }
    
    public void removeConfig(final int questId) {
        this.m_configs.remove(questId);
        this.saveConfig();
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("m_configs", this.m_configs).toString();
    }
    
    static {
        INSTANCE = new QuestConfigManager();
        m_logger = Logger.getLogger((Class)QuestConfigManager.class);
    }
    
    private static class QuestConfig
    {
        private boolean m_opened;
        
        public boolean isOpened() {
            return this.m_opened;
        }
        
        public void setOpened(final boolean opened) {
            this.m_opened = opened;
        }
        
        public void load(final ExtendedDataInputStream istream, final int version) {
            if (version >= 1) {
                this.m_opened = istream.readBooleanBit();
            }
        }
        
        public void save(final OutputBitStream ostream, final int version) throws IOException {
            if (version >= 1) {
                ostream.writeBooleanBit(this.m_opened);
            }
        }
        
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("m_opened", this.m_opened).toString();
        }
    }
}
