package com.ankamagames.baseImpl.client.core.world;

import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class ParallaxInfo
{
    public static final short NO_WORLD = -1;
    public final short m_worldId;
    public final boolean m_isBackground;
    public final float m_moveSpeed;
    public final float m_zoom;
    public final float m_zoomSpeed;
    
    public ParallaxInfo() {
        this((short)(-1));
    }
    
    public ParallaxInfo(final short worldId) {
        this(worldId, true, 0.01f, 1.0f, 0.01f);
    }
    
    public ParallaxInfo(final short worldId, final boolean isBackground, final float moveSpeed, final float zoom, final float zoomSpeed) {
        super();
        this.m_worldId = worldId;
        this.m_isBackground = isBackground;
        this.m_moveSpeed = moveSpeed;
        this.m_zoom = zoom;
        this.m_zoomSpeed = zoomSpeed;
    }
    
    public ParallaxInfo withWorldId(final short worldId) {
        return new ParallaxInfo(worldId, this.m_isBackground, this.m_moveSpeed, this.m_zoom, this.m_zoomSpeed);
    }
    
    public ParallaxInfo(final ByteBuffer buffer) {
        super();
        this.m_worldId = buffer.getShort();
        this.m_isBackground = (buffer.get() != 0);
        this.m_moveSpeed = buffer.getFloat();
        this.m_zoom = buffer.getFloat();
        this.m_zoomSpeed = buffer.getFloat();
    }
    
    public void write(final OutputBitStream stream) throws IOException {
        stream.writeShort(this.m_worldId);
        stream.writeByte((byte)(this.m_isBackground ? 1 : 0));
        stream.writeFloat(this.m_moveSpeed);
        stream.writeFloat(this.m_zoom);
        stream.writeFloat(this.m_zoomSpeed);
    }
}
