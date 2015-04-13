package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;

public class HavenWorldManager
{
    private static final Logger m_logger;
    public static final HavenWorldManager INSTANCE;
    private HavenWorldTopology m_havenWorld;
    private HavenWorldMapLoader m_mapLoader;
    private byte[] m_rawTopology;
    private byte[] m_rawBuildings;
    private GuildInfo m_guildInfo;
    private static boolean m_librariesLoaded;
    
    public boolean onEnterWorld(final short worldId) {
        if (this.m_rawTopology == null) {
            return false;
        }
        loadLibraries();
        this.initializeHavenWorld(worldId);
        this.cleanUpRaws();
        return true;
    }
    
    private void initializeHavenWorld(final short worldId) {
        (this.m_havenWorld = new HavenWorldTopology(worldId)).fromRawTopology(this.m_rawTopology);
        this.m_havenWorld.fromRawBuildings(this.m_rawBuildings, true);
        MapManagerHelper.setWorldId(worldId, this.m_mapLoader = this.createMapLoader(this.m_havenWorld));
    }
    
    public static void loadLibraries() {
        if (HavenWorldManager.m_librariesLoaded) {
            return;
        }
        try {
            ClientPartitionPatchLibrary.INSTANCE.load(WakfuConfiguration.getContentPath("partitionPatchFile"));
            EditorGroupMapLibrary.INSTANCE.load(WakfuConfiguration.getContentPath("buildingFile"));
        }
        catch (Exception e) {
            HavenWorldManager.m_logger.error((Object)"", (Throwable)e);
        }
        HavenWorldManager.m_librariesLoaded = true;
    }
    
    private void cleanUp() {
        this.m_havenWorld = null;
        this.m_guildInfo = null;
        this.cleanUpRaws();
    }
    
    private void cleanUpRaws() {
        this.m_rawTopology = null;
        this.m_rawBuildings = null;
    }
    
    public void onLeaveWorld() {
        this.cleanUp();
    }
    
    public void setRawTopology(final byte[] rawTopology) {
        this.m_rawTopology = rawTopology;
    }
    
    public void setRawBuildings(final byte[] rawBuildings) {
        this.m_rawBuildings = rawBuildings;
    }
    
    public void setGuildInfo(final GuildInfo guildInfo) {
        this.m_guildInfo = guildInfo;
    }
    
    private HavenWorldMapLoader createMapLoader(final HavenWorldTopology havenWorld) {
        return new HavenWorldMapLoader(havenWorld);
    }
    
    public HavenWorldTopology getHavenWorld() {
        return this.m_havenWorld;
    }
    
    public HavenWorldMapLoader getMapLoader() {
        return this.m_mapLoader;
    }
    
    public boolean hasHavenWorld() {
        return this.getHavenWorld() != null;
    }
    
    public GuildInfo getGuildInfo() {
        return this.m_guildInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldManager.class);
        INSTANCE = new HavenWorldManager();
        HavenWorldManager.m_librariesLoaded = false;
    }
}
