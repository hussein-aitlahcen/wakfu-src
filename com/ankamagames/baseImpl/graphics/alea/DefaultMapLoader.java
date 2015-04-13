package com.ankamagames.baseImpl.graphics.alea;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;

public class DefaultMapLoader implements MapLoader
{
    private static final Logger m_logger;
    private EnvironmentMapManager m_environmentMapMgr;
    private DisplayedScreenWorld m_screenWorld;
    private LightningMapManager m_lightningMapManager;
    private String m_validTplgMapsCoordFile;
    private String m_validGfxMapsCoordFile;
    private final PartitionExistValidator m_tplgCoordinates;
    private final PartitionExistValidator m_gfxCoordinates;
    private static final DefaultMapLoader m_instance;
    
    public static DefaultMapLoader getInstance() {
        return DefaultMapLoader.m_instance;
    }
    
    private DefaultMapLoader() {
        super();
        this.m_tplgCoordinates = new PartitionExistValidator();
        this.m_gfxCoordinates = new PartitionExistValidator();
    }
    
    @Override
    public void initialize(final EnvironmentMapManager environmentMapMgr, final DisplayedScreenWorld screenWorld, final LightningMapManager lightningMapManager) {
        this.m_environmentMapMgr = environmentMapMgr;
        this.m_screenWorld = screenWorld;
        this.m_lightningMapManager = lightningMapManager;
    }
    
    @Override
    public boolean acceptMap(final short mapCoordX, final short mapCoordY) {
        return this.m_tplgCoordinates.partitionExists(mapCoordX, mapCoordY);
    }
    
    @Override
    public void loadMaps(final short mapCoordX, final short mapCoordY) {
        TopologyMapManager.loadMapAsync(mapCoordX, mapCoordY);
        try {
            this.m_lightningMapManager.loadMapAsync(mapCoordX, mapCoordY);
        }
        catch (IOException e) {
            DefaultMapLoader.m_logger.warn((Object)("Light map " + mapCoordX + ' ' + mapCoordY), (Throwable)e);
        }
        try {
            this.m_environmentMapMgr.loadMapAsync(mapCoordX, mapCoordY);
        }
        catch (IOException e) {
            DefaultMapLoader.m_logger.warn((Object)("Environnement map doesn't exists " + mapCoordX + ' ' + mapCoordY), (Throwable)e);
        }
    }
    
    @Override
    public void prepare(final short worldId) {
        assert this.m_validTplgMapsCoordFile != null : "D'abord appler setValidMapsCoordFile";
        this.m_tplgCoordinates.loadFromFile(this.m_validTplgMapsCoordFile, worldId);
        this.m_gfxCoordinates.loadFromFile(this.m_validGfxMapsCoordFile, worldId);
        this.m_screenWorld.setWorld(new ScreenWorld(), this.m_gfxCoordinates);
        WorldGroupManager.getInstance().loadForWorld(worldId);
    }
    
    @Override
    public void clear() {
        this.m_tplgCoordinates.clear();
        this.m_gfxCoordinates.clear();
    }
    
    public PartitionExistValidator loadValidGfxMapsCoordinates(final int worldId) {
        final PartitionExistValidator validator = new PartitionExistValidator();
        validator.loadFromFile(this.m_validGfxMapsCoordFile, worldId);
        return validator;
    }
    
    public void setValidMapsCoordFile(@NotNull final String validTplgMapsCoordFile, @NotNull final String validGfxMapsCoordFile) {
        this.m_validTplgMapsCoordFile = validTplgMapsCoordFile;
        this.m_validGfxMapsCoordFile = validGfxMapsCoordFile;
        this.m_screenWorld.setMapsCoordinates(this.m_gfxCoordinates);
    }
    
    @Override
    public void prepareAmbianceZone(final short worldId) {
        AmbienceZoneBank.getInstance().setWorldId(worldId);
        AmbienceZoneBank.getInstance().load();
    }
    
    static {
        m_logger = Logger.getLogger((Class)DefaultMapLoader.class);
        m_instance = new DefaultMapLoader();
    }
}
