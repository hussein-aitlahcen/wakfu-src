package com.ankamagames.baseImpl.graphics.alea.display.lights.world;

import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class LightningMapManager implements LitSceneModifier
{
    private static final Logger m_logger;
    private final AsyncMapLoader m_asyncLoader;
    private final IntObjectLightWeightMap<LightningMap> m_maps;
    private final Object m_mutex;
    private int m_worldId;
    private String m_pathURL;
    private boolean m_newLightMapLoaded;
    private static final LightningMapManager m_instance;
    
    public static LightningMapManager getInstance() {
        return LightningMapManager.m_instance;
    }
    
    private LightningMapManager() {
        super();
        this.m_asyncLoader = new AsyncMapLoader("LightMap loader");
        this.m_maps = new IntObjectLightWeightMap<LightningMap>();
        this.m_mutex = new Object();
        this.m_newLightMapLoaded = true;
    }
    
    public void setPath(final String path) {
        try {
            this.m_pathURL = FileHelper.getURL(path).toString();
            if (!this.m_pathURL.endsWith("/")) {
                this.m_pathURL += "/";
            }
        }
        catch (IOException e) {
            LightningMapManager.m_logger.error((Object)("Invalid path : " + path), (Throwable)e);
        }
    }
    
    public void setWorldId(final int worldId) {
        this.m_worldId = worldId;
    }
    
    public int getWorldId() {
        return this.m_worldId;
    }
    
    public CellLightDef getLightInfo(final int x, final int y, final int layer) {
        final LightningMap map = this.getMapsFromCell(x, y);
        return (map == null) ? null : map.getLightInfo(x, y, layer);
    }
    
    private LightningMap getMapsFromCell(final int x, final int y) {
        final int mapX = MathHelper.fastFloor(x / 18.0f);
        final int mapY = MathHelper.fastFloor(y / 18.0f);
        return this.m_maps.get(MathHelper.getIntFromTwoInt(mapX, mapY));
    }
    
    public final void loadMap(final short x, final short y) throws IOException {
        final LightningMap map = new LightningMap();
        final String filename = createMapPath(this.m_pathURL, this.m_worldId, x, y);
        final ExtendedDataInputStream stream = ExtendedDataInputStream.wrap(WorldMapFileHelper.readFile(filename));
        map.load(stream);
        assert MapConstants.getMapCoordFromCellX(map.m_x) == x && MapConstants.getMapCoordFromCellY(map.m_y) == y;
        synchronized (this.m_mutex) {
            this.m_maps.put(MathHelper.getIntFromTwoShort(x, y), map);
        }
        this.m_newLightMapLoaded = true;
    }
    
    public void clean() {
        synchronized (this.m_mutex) {
            this.m_maps.clear();
        }
        this.m_asyncLoader.clear();
    }
    
    public final void updateShadow(final float shadowIntensity) {
        synchronized (this.m_mutex) {
            for (int i = this.m_maps.size() - 1; i >= 0; --i) {
                this.m_maps.getQuickValue(i).updateShadow(shadowIntensity);
            }
        }
    }
    
    public final void updateNightLight(final float nightLightIntensity) {
        synchronized (this.m_mutex) {
            for (int i = this.m_maps.size() - 1; i >= 0; --i) {
                this.m_maps.getQuickValue(i).updateNightLight(nightLightIntensity);
            }
        }
    }
    
    public void removeMap(final short x, final short y) {
        final int key = MathHelper.getIntFromTwoShort(x, y);
        synchronized (this.m_mutex) {
            this.m_maps.remove(key);
        }
        this.m_asyncLoader.remove(key);
    }
    
    public boolean isNewLightMapLoaded() {
        return this.m_newLightMapLoaded;
    }
    
    public void setNewLightMapLoaded(final boolean newLightMapLoaded) {
        this.m_newLightMapLoaded = newLightMapLoaded;
    }
    
    private static String createMapPath(final String path, final int worldId, final short x, final short y) {
        assert path != null && path.contains("%d") && path.endsWith("/");
        return String.format(path, worldId) + x + '_' + y;
    }
    
    @Override
    public final int getPriority() {
        return 400;
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] color) {
        final CellLightDef cellLightDef = this.getLightInfo(x, y, layerId);
        if (cellLightDef != null) {
            final float[] c = cellLightDef.getColor();
            final int n = 0;
            color[n] *= c[0];
            final int n2 = 1;
            color[n2] *= c[1];
            final int n3 = 2;
            color[n3] *= c[2];
            final float[] nightLight = cellLightDef.getNightLight();
            if (nightLight != null) {
                final int n4 = 0;
                color[n4] += nightLight[0];
                final int n5 = 1;
                color[n5] += nightLight[1];
                final int n6 = 2;
                color[n6] += nightLight[2];
            }
        }
    }
    
    @Override
    public void update(final int deltaTime) {
    }
    
    @Override
    public boolean useless() {
        return false;
    }
    
    public void loadMapAsync(final short mapCoordX, final short mapCoordY) throws IOException {
        final int key = MathHelper.getIntFromTwoShort(mapCoordX, mapCoordY);
        this.m_asyncLoader.submit(key, new Runnable() {
            @Override
            public void run() {
                try {
                    LightningMapManager.this.loadMap(mapCoordX, mapCoordY);
                }
                catch (IOException e) {
                    LightningMapManager.m_logger.error((Object)"", (Throwable)e);
                }
            }
        });
    }
    
    public void insertMapPatch(final short mapCoordX, final short mapCoordY, final LightningMap lightMap) {
        final int key = MathHelper.getIntFromTwoShort(mapCoordX, mapCoordY);
        synchronized (this.m_mutex) {
            this.m_maps.put(key, lightMap);
        }
        this.setNewLightMapLoaded(true);
    }
    
    static {
        m_logger = Logger.getLogger((Class)LightningMapManager.class);
        m_instance = new LightningMapManager();
    }
}
