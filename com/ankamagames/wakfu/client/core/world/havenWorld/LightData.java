package com.ankamagames.wakfu.client.core.world.havenWorld;

import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;

public class LightData
{
    private final byte m_width;
    private final byte m_height;
    private CellLightDef[] m_layerColors;
    private final ArrayList<CellLightDef> m_cellLightDef;
    
    public LightData(final byte width, final byte height) {
        super();
        this.m_cellLightDef = new ArrayList<CellLightDef>(10);
        this.m_width = width;
        this.m_height = height;
    }
    
    public LightData(final LightData data) {
        super();
        this.m_cellLightDef = new ArrayList<CellLightDef>(10);
        this.m_width = data.m_width;
        this.m_height = data.m_height;
        for (final CellLightDef def : data.m_cellLightDef) {
            this.m_cellLightDef.add(new CellLightDef(def));
        }
        this.m_layerColors = data.m_layerColors;
    }
    
    public int getLightLayerCount() {
        return this.m_layerColors.length / (this.m_width * this.m_height);
    }
    
    public byte getWidth() {
        return this.m_width;
    }
    
    public byte getHeight() {
        return this.m_height;
    }
    
    ArrayList<CellLightDef> getCellLightDef() {
        return this.m_cellLightDef;
    }
    
    CellLightDef getLayerColors(final int x, final int y, final int layer) {
        final int index = x + (y + layer * this.m_height) * this.m_width;
        return this.m_layerColors[index];
    }
    
    public void read(final ExtendedDataInputStream stream) {
        final int numToRead = this.m_width * this.m_height;
        this.m_layerColors = LightningMap.loadLayers(this.m_cellLightDef, numToRead, stream);
    }
}
