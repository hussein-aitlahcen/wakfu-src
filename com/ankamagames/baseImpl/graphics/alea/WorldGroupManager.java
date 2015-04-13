package com.ankamagames.baseImpl.graphics.alea;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.nio.*;
import gnu.trove.*;

public class WorldGroupManager
{
    private static final Logger m_logger;
    private static final WorldGroupManager m_instance;
    private ByteArrayBitSet m_layers;
    private int m_layerCount;
    private String m_path;
    
    public WorldGroupManager() {
        super();
        this.m_layerCount = 0;
        this.m_path = "./";
    }
    
    public static WorldGroupManager getInstance() {
        return WorldGroupManager.m_instance;
    }
    
    public final void clear() {
        this.m_layers = null;
        this.m_layerCount = 0;
    }
    
    public final void setPath(final String path) {
        this.m_path = path;
    }
    
    public final void loadForWorld(final int worldId) {
        this.clear();
        final String fileName = String.format(this.m_path, worldId);
        try {
            this.load(fileName);
        }
        catch (IOException e) {
            WorldGroupManager.m_logger.error((Object)("Probl\u00ef¿½me lors du chargement des infos de group " + fileName), (Throwable)e);
        }
    }
    
    private void load(final String filename) throws IOException {
        final ExtendedDataInputStream buffer = ExtendedDataInputStream.wrap(WorldMapFileHelper.readFile(filename));
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.m_layerCount = 1 + buffer.readUnsignedShort();
        this.m_layers = new ByteArrayBitSet(this.m_layerCount * this.m_layerCount);
        for (int count = buffer.readUnsignedShort(), i = 0; i < count; ++i) {
            final int id = buffer.readShort();
            final int n = buffer.readShort();
            final short[] layersVisible = buffer.readShorts(n);
            this.setLayerVisibility(id, layersVisible, true);
        }
    }
    
    private void setLayerVisibility(final int from, final short[] layersVisible, final boolean visible) {
        final int offset = Math.abs(from) * this.m_layerCount;
        for (int i = 0; i < layersVisible.length; ++i) {
            this.m_layers.set(offset + Math.abs(layersVisible[i]), visible);
        }
    }
    
    public final boolean layerVisible(final int from, final int layer) {
        if (from == 0) {
            return layer <= 0;
        }
        final int a = (from < 0) ? (-from) : from;
        final int b = (layer < 0) ? (-layer) : layer;
        return this.m_layers.get(a * this.m_layerCount + b);
    }
    
    public final int getLayerCount() {
        return this.m_layerCount;
    }
    
    public void insertLayersVisibility(final int layerCount, final TShortObjectHashMap<short[]> layersByGroup) {
        this.m_layerCount = layerCount + 1;
        (this.m_layers = new ByteArrayBitSet(this.m_layerCount * this.m_layerCount)).set(0, true);
        layersByGroup.forEachEntry(new TShortObjectProcedure<short[]>() {
            @Override
            public boolean execute(final short from, final short[] layerVisible) {
                WorldGroupManager.this.setLayerVisibility(from, layerVisible, true);
                return true;
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldGroupManager.class);
        m_instance = new WorldGroupManager();
    }
}
