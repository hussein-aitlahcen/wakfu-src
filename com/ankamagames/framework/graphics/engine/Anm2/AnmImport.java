package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.*;
import java.io.*;

public class AnmImport
{
    short m_id;
    String m_name;
    int m_crc;
    
    public final void load(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_id = bitStream.readShort();
        this.m_name = bitStream.readString();
        bitStream.readInt();
        this.m_crc = Engine.getPartName(this.m_name);
    }
}
