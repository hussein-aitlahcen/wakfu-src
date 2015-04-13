package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;

public class AnmShapeDefinition
{
    final short m_id;
    final short m_textureIndex;
    final float m_top;
    final float m_left;
    final float m_bottom;
    final float m_right;
    final short m_width;
    final short m_height;
    final float m_offsetX;
    final float m_offsetY;
    
    AnmShapeDefinition(final ExtendedDataInputStream bitStream) {
        super();
        this.m_id = bitStream.readShort();
        this.m_textureIndex = bitStream.readShort();
        this.m_top = (bitStream.readShort() & 0xFFFF) / 65535.0f;
        this.m_left = (bitStream.readShort() & 0xFFFF) / 65535.0f;
        this.m_bottom = (bitStream.readShort() & 0xFFFF) / 65535.0f;
        this.m_right = (bitStream.readShort() & 0xFFFF) / 65535.0f;
        this.m_width = bitStream.readShort();
        this.m_height = bitStream.readShort();
        this.m_offsetX = bitStream.readFloat();
        this.m_offsetY = bitStream.readFloat();
    }
}
