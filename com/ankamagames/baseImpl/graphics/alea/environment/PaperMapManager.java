package com.ankamagames.baseImpl.graphics.alea.environment;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.net.*;
import gnu.trove.*;

public class PaperMapManager
{
    private static final Logger m_logger;
    private static final PaperMapManager m_instance;
    private String m_path;
    private final TIntHashSet m_ambiencesAvailable;
    private final AmbienceMap m_map;
    
    private PaperMapManager() {
        super();
        this.m_ambiencesAvailable = new TIntHashSet();
        this.m_map = new AmbienceMap();
    }
    
    public static PaperMapManager getInstance() {
        return PaperMapManager.m_instance;
    }
    
    public void reset() {
        this.m_map.reset();
        this.m_ambiencesAvailable.clear();
    }
    
    public String getPath() {
        return this.m_path;
    }
    
    public void setPath(final String path) {
        this.m_path = path;
    }
    
    public boolean addAmbiences(final int[] ambiences) {
        return this.m_ambiencesAvailable.addAll(ambiences);
    }
    
    public boolean addAmbience(final int ambience) {
        return this.m_ambiencesAvailable.add(ambience);
    }
    
    public void load(final int worldId) {
        assert this.m_path != null : "PaperMapManager : Path undefined";
        final String dataPath = String.format(this.m_path, worldId);
        try {
            final URL url = WorldMapFileHelper.getURL(dataPath);
            this.m_map.load(ExtendedDataInputStream.wrapFullAndClose(new BufferedInputStream(url.openStream())));
        }
        catch (MalformedURLException e2) {
            PaperMapManager.m_logger.warn((Object)("Problem during PaperMapManager Load : invalid URL " + dataPath));
        }
        catch (IOException e) {
            PaperMapManager.m_logger.warn((Object)"Exception during PaperMapManager Load : ", (Throwable)e);
        }
    }
    
    public TIntHashSet getMapsFromAmbienceId(final int ambienceId) {
        return this.m_map.getMapsFromAmbienceId(ambienceId);
    }
    
    public TIntArrayList getAvailableMapCoordsHash() {
        final TIntArrayList list = new TIntArrayList();
        this.forEachAvailableMap(new AvailablePaperMapsHashProcedure() {
            @Override
            public void addAvailableMap(final int coordsHash) {
                list.add(coordsHash);
            }
        });
        return list;
    }
    
    public void forEachAvailableMap(final AvailablePaperMapsProcedure p) {
        if (this.m_ambiencesAvailable == null) {
            return;
        }
        if (!this.m_ambiencesAvailable.isEmpty()) {
            this.m_ambiencesAvailable.forEach(new TIntProcedure() {
                @Override
                public boolean execute(final int value) {
                    final TIntHashSet maps = PaperMapManager.this.m_map.getMapsFromAmbienceId(value);
                    if (maps != null) {
                        for (final int map : maps) {
                            p.addAvailableMap(AmbienceMap.getX(map), AmbienceMap.getY(map));
                        }
                    }
                    return true;
                }
            });
        }
    }
    
    public void forEachAvailableMap(final AvailablePaperMapsHashProcedure p) {
        if (this.m_ambiencesAvailable == null) {
            return;
        }
        if (!this.m_ambiencesAvailable.isEmpty()) {
            this.m_ambiencesAvailable.forEach(new TIntProcedure() {
                @Override
                public boolean execute(final int value) {
                    final TIntHashSet maps = PaperMapManager.this.m_map.getMapsFromAmbienceId(value);
                    if (maps != null) {
                        final TIntIterator it = maps.iterator();
                        while (it.hasNext()) {
                            p.addAvailableMap(it.next());
                        }
                    }
                    return true;
                }
            });
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PaperMapManager.class);
        m_instance = new PaperMapManager();
    }
    
    public interface UnavailablePaperMapHashsProcedure
    {
        void addUnavailableMap(int p0);
    }
    
    public interface UnavailablePaperMapsProcedure
    {
        void addUnavailableMap(short p0, short p1);
    }
    
    public interface AvailablePaperMapsHashProcedure
    {
        void addAvailableMap(int p0);
    }
    
    public interface AvailablePaperMapsProcedure
    {
        void addAvailableMap(short p0, short p1);
    }
}
