package com.ankamagames.baseImpl.graphics.alea.display.lights.world;

import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LightningMap
{
    protected CellLightDef[] m_layerColors;
    protected final ArrayList<CellLightDef> m_cellLightDef;
    int m_x;
    int m_y;
    
    public LightningMap() {
        super();
        this.m_cellLightDef = new ArrayList<CellLightDef>(300);
    }
    
    public final void setMapCoordinates(final int mapX, final int mapY) {
        this.m_x = mapX * 18;
        this.m_y = mapY * 18;
    }
    
    protected final int getCellCoordX() {
        return this.m_x;
    }
    
    protected final int getCellCoordY() {
        return this.m_y;
    }
    
    final CellLightDef getLightInfo(final int x, final int y, final int layer) {
        try {
            return this.m_layerColors[this.getHashCode(x, y, layer)];
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public final int getHashCode(final int x, final int y, final int k) {
        return x - this.m_x + (y - this.m_y + k * 18) * 18;
    }
    
    final void load(final ExtendedDataInputStream stream) {
        this.m_x = stream.readShort() * 18;
        this.m_y = stream.readShort() * 18;
        this.m_layerColors = loadLayers(this.m_cellLightDef, 324, stream);
    }
    
    final void updateShadow(final float shadowIntensity) {
        for (int i = 0, size = this.m_cellLightDef.size(); i < size; ++i) {
            this.m_cellLightDef.get(i).updateShadow(shadowIntensity);
        }
    }
    
    final void updateNightLight(final float nightLightIntensity) {
        for (int i = 0, size = this.m_cellLightDef.size(); i < size; ++i) {
            this.m_cellLightDef.get(i).updateNightLight(nightLightIntensity);
        }
    }
    
    public static CellLightDef[] loadLayers(final ArrayList<CellLightDef> cellLightDef, final int numCells, final ExtendedDataInputStream stream) {
        cellLightDef.clear();
        readDefinition(cellLightDef, stream);
        return readLayers(cellLightDef, numCells, stream);
    }
    
    private static void readDefinition(final ArrayList<CellLightDef> cellLightDef, final ExtendedDataInputStream stream) {
        for (int defCount = stream.readShort() & 0xFFFF, i = 0; i < defCount; ++i) {
            final boolean allowOutDoorLighting = stream.readBooleanBit();
            final int ambiance = stream.readInt();
            final int shadow = stream.readInt();
            final int light = stream.readInt();
            final CellLightDef def = new CellLightDef(ambiance, shadow, light, allowOutDoorLighting);
            cellLightDef.add(def);
        }
    }
    
    private static CellLightDef[] readLayers(final ArrayList<CellLightDef> cellLightDef, final int numCells, final ExtendedDataInputStream stream) {
        final int layerCount = stream.readShort();
        final int count = stream.readShort();
        final CellLightDef[] layerColors = new CellLightDef[numCells * layerCount];
        for (int i = 0; i < count; ++i) {
            final int k = stream.readUnsignedShort();
            final int idx = stream.readUnsignedShort();
            layerColors[k] = cellLightDef.get(idx);
        }
        return layerColors;
    }
    
    public static class Indexer
    {
        final int m_x;
        final int m_y;
        final int m_width;
        final int m_height;
        
        public Indexer(final int x, final int y, final int width, final int height) {
            super();
            this.m_x = x * 18;
            this.m_y = y * 18;
            this.m_width = width;
            this.m_height = height;
        }
        
        public final int getHashCode(final int x, final int y, final int k) {
            return x - this.m_x + (y - this.m_y + k * this.m_height) * this.m_width;
        }
    }
}
