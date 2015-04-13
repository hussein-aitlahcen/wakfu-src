package com.ankamagames.baseImpl.graphics.alea.environment;

import org.apache.log4j.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.baseImpl.graphics.alea.sound.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.sound.group.field.*;
import gnu.trove.*;

public class EnvironmentMapManager
{
    private static final boolean DEBUG = false;
    private static final Logger m_logger;
    private int m_worldId;
    private SoundManager m_soundManager;
    private IsoParticleSystemManager m_particleManager;
    protected EnvironmentMapLoadedListener m_listener;
    protected String m_path;
    protected MapFactory m_mapFactory;
    private final AsyncMapLoader m_asyncLoader;
    protected final TLongObjectHashMap<ClientEnvironmentMap> m_maps;
    private static final EnvironmentMapManager m_instance;
    
    private EnvironmentMapManager() {
        super();
        this.m_worldId = Integer.MIN_VALUE;
        this.m_asyncLoader = new AsyncMapLoader("EnvMap loader");
        this.m_maps = new TLongObjectHashMap<ClientEnvironmentMap>();
    }
    
    public static EnvironmentMapManager getInstance() {
        return EnvironmentMapManager.m_instance;
    }
    
    public final void setPath(final String path) {
        this.m_path = path;
    }
    
    public final void setMapFactory(final MapFactory mapFactory) {
        this.m_mapFactory = mapFactory;
    }
    
    @Nullable
    public final ClientEnvironmentMap getMap(final short x, final short y, final int instanceId) {
        final long hashCode = getHashCode(x, y, instanceId);
        this.m_asyncLoader.waitFor(hashCode);
        synchronized (this.m_maps) {
            return this.m_maps.get(hashCode);
        }
    }
    
    @Nullable
    public final ClientEnvironmentMap getMapFromCell(final int cellX, final int cellY, final int instanceId) {
        final short x = (short)MapConstants.getMapCoordFromCellX(cellX);
        final short y = (short)MapConstants.getMapCoordFromCellY(cellY);
        return this.getMap(x, y, instanceId);
    }
    
    protected static long getHashCode(long x, long y, final int worldId) {
        assert x > -32768L && x < 32767L : "faut pas d\u00e9conner non plus un short \u00e7a suffit pour la taille du monde";
        assert y > -32768L && y < 32767L : "faut pas d\u00e9conner non plus un short \u00e7a suffit pour la taille du monde";
        x += 32767L;
        y += 32767L;
        return x << 48 | y << 32 | worldId;
    }
    
    private static String createMapPath(final String path, final int worldId, final short x, final short y) {
        assert path != null && path.contains("%d") && path.endsWith("/");
        return String.format(path, worldId) + x + '_' + y;
    }
    
    public final void loadMap(final short x, final short y, final int worldId) throws IOException {
        assert this.m_mapFactory != null : "Il faut d'abord appeler setMapFactory";
        final long hashCode = getHashCode(x, y, worldId);
        final boolean containsMap;
        synchronized (this.m_maps) {
            containsMap = this.m_maps.contains(hashCode);
        }
        if (containsMap) {
            this.loadDynamicData(hashCode);
            return;
        }
        final String fileName = createMapPath(this.m_path, worldId, x, y);
        final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(WorldMapFileHelper.readFile(fileName));
        final byte header = bitStream.readByte();
        final ClientEnvironmentMap environmentMap = this.m_mapFactory.createMap();
        if (header != environmentMap.getVersion()) {
            EnvironmentMapManager.m_logger.warn((Object)"version de map d'enviornenemt client incorrect");
        }
        this.loadMap(environmentMap, bitStream);
        synchronized (this.m_maps) {
            this.m_maps.put(hashCode, environmentMap);
        }
    }
    
    public void setSoundManager(@Nullable final SoundManager soundManager) {
        this.m_soundManager = soundManager;
    }
    
    public void setParticleManager(@Nullable final IsoParticleSystemManager particleManager) {
        this.m_particleManager = particleManager;
    }
    
    public void setWorldId(final int instanceId) {
        this.m_worldId = instanceId;
    }
    
    private void loadDynamicData(final long hashCode) {
        final ClientEnvironmentMap environmentMap;
        synchronized (this.m_maps) {
            environmentMap = this.m_maps.get(hashCode);
        }
        if (environmentMap != null && !environmentMap.m_loaded) {
            this.loadDynamicDataFrom(environmentMap);
        }
    }
    
    protected void loadMap(@NotNull final ClientEnvironmentMap clientEnvironmentMap, @NotNull final ExtendedDataInputStream istream) throws IOException {
        clientEnvironmentMap.load(istream);
        this.loadDynamicDataFrom(clientEnvironmentMap);
    }
    
    private void loadDynamicDataFrom(@NotNull final ClientEnvironmentMap environmentMap) {
        if (environmentMap.m_loaded) {
            EnvironmentMapManager.m_logger.error((Object)("chargement d'une map d\u00e9j\u00e0 charg\u00e9e  " + this.mapCoordString(environmentMap)));
            return;
        }
        this.loadParticles(environmentMap);
        this.loadSounds(environmentMap);
        environmentMap.m_loaded = true;
        if (this.m_listener != null) {
            this.m_listener.onEnvironmentMapLoaded(environmentMap);
        }
    }
    
    private void loadSounds(final ClientEnvironmentMap environmentMap) {
        final SoundDef[] sounds = environmentMap.getSoundData();
        if (sounds == null || this.m_soundManager == null) {
            return;
        }
        for (int i = 0; i < sounds.length; ++i) {
            try {
                final SoundDef s = sounds[i];
                final SoundData soundData = SoundBank.getInstance().get(s.m_soundId);
                final int x = environmentMap.convertToWorldX(s.m_x);
                final int y = environmentMap.convertToWorldY(s.m_y);
                assert s.m_positionedSound == null;
                s.m_positionedSound = this.m_soundManager.addWorldSound(soundData, x, y, s.m_z);
            }
            catch (Exception e) {
                EnvironmentMapManager.m_logger.error((Object)"", (Throwable)e);
            }
        }
    }
    
    private void loadParticles(final ClientEnvironmentMap environmentMap) {
        final ParticleDef[] particles = environmentMap.getParticleData();
        if (particles == null || this.m_particleManager == null) {
            return;
        }
        for (int i = 0; i < particles.length; ++i) {
            try {
                final ParticleDef p = particles[i];
                final CellParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getCellParticleSystem(p.m_systemId, p.m_level);
                if (particleSystem == null) {
                    EnvironmentMapManager.m_logger.warn((Object)("Erreur de cr\u00e9ation du systeme de particule " + p));
                }
                else {
                    final float x = environmentMap.convertToWorldX(p.m_x) + p.m_offsetX / 100.0f;
                    final float y = environmentMap.convertToWorldY(p.m_y) + p.m_offsetY / 100.0f;
                    final float z = p.m_z + p.m_offsetZ / 10.0f;
                    particleSystem.setPosition(x, y, z);
                    particleSystem.setLod(p.m_lod);
                    assert p.m_positionedParticles == null;
                    p.m_positionedParticles = particleSystem;
                    this.m_particleManager.addCellParticleSystem(particleSystem);
                }
            }
            catch (Exception e) {
                EnvironmentMapManager.m_logger.error((Object)"", (Throwable)e);
            }
        }
    }
    
    public void loadMapAsync(final short mapCoordX, final short mapCoordY) throws IOException {
        final long key = getHashCode(mapCoordX, mapCoordY, this.m_worldId);
        this.m_asyncLoader.submit(key, new Runnable() {
            @Override
            public void run() {
                try {
                    EnvironmentMapManager.this.loadMap(mapCoordX, mapCoordY);
                }
                catch (IOException e) {
                    EnvironmentMapManager.m_logger.error((Object)"", (Throwable)e);
                }
            }
        });
    }
    
    public void loadMap(final short x, final short y) throws IOException {
        assert this.m_worldId != Integer.MIN_VALUE : "il faut d'abord appler setWorldId";
        this.loadMap(x, y, this.m_worldId);
    }
    
    public void unload(final short x, final short y) {
        final long hashCode = getHashCode(x, y, this.m_worldId);
        this.unloadDynamicDataFromMap(x, y);
        synchronized (this.m_maps) {
            this.m_maps.remove(hashCode);
            this.m_asyncLoader.remove(hashCode);
        }
    }
    
    protected void unloadDynamicDataFrom(@NotNull final ClientEnvironmentMap environmentMap) {
        if (!environmentMap.m_loaded) {
            return;
        }
        final ParticleDef[] particles = environmentMap.getParticleData();
        if (particles != null && this.m_particleManager != null) {
            for (int i = 0; i < particles.length; ++i) {
                final CellParticleSystem positionnedParticle = particles[i].m_positionedParticles;
                if (positionnedParticle != null) {
                    positionnedParticle.kill();
                    particles[i].m_positionedParticles = null;
                }
            }
        }
        final SoundDef[] sounds = environmentMap.getSoundData();
        if (sounds != null && this.m_soundManager != null) {
            for (int j = 0; j < sounds.length; ++j) {
                final PositionedSound positionedSound = sounds[j].m_positionedSound;
                if (positionedSound != null) {
                    positionedSound.release();
                    sounds[j].m_positionedSound = null;
                }
            }
        }
        environmentMap.m_loaded = false;
        if (this.m_listener != null) {
            this.m_listener.onEnvironmentMapUnloaded(environmentMap);
        }
    }
    
    private void unloadDynamicDataFromMap(final short x, final short y) {
        final ClientEnvironmentMap map = this.getMap(x, y);
        if (map == null) {
            EnvironmentMapManager.m_logger.warn((Object)("D\u00e9chargement d'une map non charg\u00e9e (" + x + ' ' + y + ')'));
            return;
        }
        this.unloadDynamicDataFrom(map);
    }
    
    public final void unloadDynamicDataFromAllMaps() {
        synchronized (this.m_maps) {
            this.m_maps.forEachValue(new TObjectProcedure<ClientEnvironmentMap>() {
                @Override
                public boolean execute(final ClientEnvironmentMap map) {
                    try {
                        EnvironmentMapManager.this.unloadDynamicDataFrom(map);
                    }
                    catch (Exception e) {
                        EnvironmentMapManager.m_logger.error((Object)"", (Throwable)e);
                    }
                    return true;
                }
            });
        }
    }
    
    @Nullable
    public final ClientEnvironmentMap getMap(final short x, final short y) {
        assert this.m_worldId != Integer.MIN_VALUE : "il faut d'abord appeler setWorldId";
        return this.getMap(x, y, this.m_worldId);
    }
    
    @Nullable
    public final ClientEnvironmentMap getMapFromCell(final int cellX, final int cellY) {
        assert this.m_worldId != Integer.MIN_VALUE : "il faut d'abord appeler setWorldId";
        return this.getMapFromCell(cellX, cellY, this.m_worldId);
    }
    
    public void reset() {
        synchronized (this.m_maps) {
            this.m_maps.forEachValue(new TObjectProcedure<ClientEnvironmentMap>() {
                @Override
                public boolean execute(final ClientEnvironmentMap map) {
                    if (map.m_loaded) {
                        EnvironmentMapManager.m_logger.error((Object)"Map non d\u00e9charg\u00e9e. Il faut appeler unloadDynamicDataFromAllMaps");
                    }
                    return true;
                }
            });
            this.m_maps.clear();
        }
        this.m_worldId = Integer.MIN_VALUE;
    }
    
    private String mapCoordString(@NotNull final ClientEnvironmentMap map) {
        return "(" + map.getX() + ' ' + map.getY() + " @" + this.m_worldId + ')';
    }
    
    public void setListener(final EnvironmentMapLoadedListener listener) {
        this.m_listener = listener;
    }
    
    public void insertMapPatch(final short mapCoordX, final short mapCoordY, final ClientEnvironmentMap envMap) {
        final long key = getHashCode(mapCoordX, mapCoordY, this.m_worldId);
        synchronized (this.m_maps) {
            this.m_maps.put(key, envMap);
            this.loadDynamicDataFrom(envMap);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)EnvironmentMapManager.class);
        m_instance = new EnvironmentMapManager();
    }
    
    public interface MapFactory
    {
        @NotNull
        ClientEnvironmentMap createMap();
    }
}
