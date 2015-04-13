package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.util.*;
import java.io.*;

public class GraphicalElement
{
    private static final Logger m_logger;
    public static final byte[] DEFAULT_TEINT;
    private final byte m_cellX;
    private final byte m_cellY;
    private final byte m_cellZ;
    private final byte m_height;
    private final boolean m_occluder;
    private final byte m_zOrder;
    private final ElementProperties m_element;
    private final byte[] m_teint;
    private final int m_groupInstanceId;
    private final short m_groupLayer;
    
    GraphicalElement(final int elementId, final byte cellX, final byte cellY, final byte cellZ, final byte height, final boolean occluder, final int groupInstanceId, final short groupLayer, final byte zOrder, final byte[] teint) {
        super();
        this.m_cellX = cellX;
        this.m_cellY = cellY;
        this.m_cellZ = cellZ;
        this.m_height = height;
        this.m_occluder = occluder;
        this.m_groupInstanceId = groupInstanceId;
        this.m_groupLayer = groupLayer;
        this.m_element = ElementPropertiesLibrary.getElement(elementId);
        if (this.m_element == null) {
            GraphicalElement.m_logger.error((Object)("l'element " + elementId + " n'a pas \u00e9t\u00e9 export\u00e9"));
        }
        this.m_zOrder = zOrder;
        this.m_teint = teint;
    }
    
    public static GraphicalElement fromStream(final ExtendedDataInputStream stream) {
        final int elementId = stream.readInt();
        final byte cellX = stream.readByte();
        final byte cellY = stream.readByte();
        final byte cellZ = stream.readByte();
        final byte height = stream.readByte();
        final boolean occluder = stream.readByte() != 0;
        final int groupInstanceId = stream.readInt();
        final short groupLayer = stream.readShort();
        final byte altitudeOrder = stream.readByte();
        final boolean hasTeint = stream.readByte() != 0;
        final byte[] teint = hasTeint ? stream.readBytes(3) : GraphicalElement.DEFAULT_TEINT;
        return new GraphicalElement(elementId, cellX, cellY, cellZ, height, occluder, groupInstanceId, groupLayer, altitudeOrder, teint);
    }
    
    public static byte[] serialize(final int elementId, final byte cellX, final byte cellY, final byte altitude, final byte height, final boolean occluder, final int groupInstanceId, final short groupLayer, final byte zOrder, final byte[] teint) throws IOException {
        final OutputBitStream stream = new OutputBitStream();
        stream.writeInt(elementId);
        stream.writeByte(cellX);
        stream.writeByte(cellY);
        stream.writeByte(altitude);
        stream.writeByte(height);
        stream.writeByte((byte)(occluder ? 1 : 0));
        stream.writeInt(groupInstanceId);
        stream.writeShort(groupLayer);
        stream.writeByte(zOrder);
        if (Arrays.equals(teint, GraphicalElement.DEFAULT_TEINT)) {
            stream.writeByte((byte)0);
        }
        else {
            stream.writeByte((byte)1);
            stream.writeBytes(teint);
        }
        return stream.getData();
    }
    
    public ElementProperties getProperties() {
        return this.m_element;
    }
    
    public byte getCellX() {
        return this.m_cellX;
    }
    
    public byte getCellY() {
        return this.m_cellY;
    }
    
    public byte getCellZ() {
        return this.m_cellZ;
    }
    
    public byte[] getTeint() {
        return this.m_teint;
    }
    
    public byte getzOrder() {
        return this.m_zOrder;
    }
    
    public byte getHeight() {
        return this.m_height;
    }
    
    public boolean isOccluder() {
        return this.m_occluder;
    }
    
    public int getGroupInstanceId() {
        return this.m_groupInstanceId;
    }
    
    public short getGroupLayer() {
        return this.m_groupLayer;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GraphicalElement.class);
        DEFAULT_TEINT = null;
    }
}
