package com.ankamagames.framework.graphics.engine.Anm2.Index;

import com.ankamagames.framework.graphics.engine.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public final class AnmAnimationFileRecord
{
    public static final short LOCAL = -1;
    public short m_fileIndex;
    public String m_name;
    public int m_crc;
    
    public final void load(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_name = bitStream.readString();
        bitStream.readInt();
        this.m_crc = Engine.getPartName(this.m_name);
        this.m_fileIndex = bitStream.readShort();
    }
    
    public final void save(final OutputBitStream bitStream) throws IOException {
        bitStream.writeString(this.m_name);
        bitStream.writeInt(this.m_crc);
        bitStream.writeShort(this.m_fileIndex);
    }
}
