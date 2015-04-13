package com.ankamagames.framework.graphics.image.io.TGA;

import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class TGAHeader
{
    public static final int HEADER_SIZE = 18;
    public byte m_idLength;
    public byte m_colorMapType;
    public byte m_imageType;
    public short m_colorMapStart;
    public short m_colorMapLength;
    public byte m_colorMapBitDepth;
    public short m_startX;
    public short m_startY;
    public short m_width;
    public short m_height;
    public byte m_bitDepth;
    public byte m_desc;
    
    public void setFromStream(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_idLength = bitStream.readByte();
        this.m_colorMapType = bitStream.readByte();
        this.m_imageType = bitStream.readByte();
        this.m_colorMapStart = bitStream.readShort();
        this.m_colorMapLength = bitStream.readShort();
        this.m_colorMapBitDepth = bitStream.readByte();
        this.m_startX = bitStream.readShort();
        this.m_startY = bitStream.readShort();
        this.m_width = bitStream.readShort();
        this.m_height = bitStream.readShort();
        this.m_bitDepth = bitStream.readByte();
        this.m_desc = bitStream.readByte();
    }
    
    public void toStream(final OutputBitStream stream) throws IOException {
        stream.writeByte(this.m_idLength);
        stream.writeByte(this.m_colorMapType);
        stream.writeByte(this.m_imageType);
        stream.writeShort(this.m_colorMapStart);
        stream.writeShort(this.m_colorMapLength);
        stream.writeByte(this.m_colorMapBitDepth);
        stream.writeShort(this.m_startX);
        stream.writeShort(this.m_startY);
        stream.writeShort(this.m_width);
        stream.writeShort(this.m_height);
        stream.writeByte(this.m_bitDepth);
        stream.writeByte(this.m_desc);
    }
}
