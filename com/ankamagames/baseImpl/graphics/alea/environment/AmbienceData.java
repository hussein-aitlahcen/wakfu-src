package com.ankamagames.baseImpl.graphics.alea.environment;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.utils.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class AmbienceData
{
    public int m_zoneId;
    public String m_name;
    public int m_musicPlayListId;
    public boolean m_useReverb;
    public int m_soundPreset;
    public byte m_zoneTypeId;
    public int m_graphicAmbienceId;
    public short m_entryGfxId;
    
    public AmbienceData() {
        super();
    }
    
    public AmbienceData(final int zoneId, final String name, final int musicPlayListId, final boolean useReverb, final int soundPreset, final byte ambianceZoneType, final int graphicAmibenceId, final short entryGfxId) {
        super();
        this.m_zoneId = zoneId;
        this.m_name = name;
        this.m_musicPlayListId = musicPlayListId;
        this.m_useReverb = useReverb;
        this.m_soundPreset = soundPreset;
        this.m_zoneTypeId = ambianceZoneType;
        this.m_graphicAmbienceId = graphicAmibenceId;
        this.m_entryGfxId = entryGfxId;
    }
    
    final void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_zoneId = istream.readInt();
        final short nameLength = istream.readShort();
        final byte[] bytes = istream.readBytes(nameLength);
        this.m_name = StringUtils.fromUTF8(bytes);
        this.m_musicPlayListId = istream.readInt();
        this.m_useReverb = istream.readBooleanBit();
        this.m_soundPreset = istream.readInt();
        this.m_zoneTypeId = istream.readByte();
        this.m_graphicAmbienceId = istream.readInt();
        this.m_entryGfxId = istream.readShort();
    }
    
    final void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_zoneId);
        final byte[] bytes = StringUtils.toUTF8(this.m_name);
        ostream.writeShort((short)bytes.length);
        ostream.writeBytes(bytes);
        ostream.writeInt(this.m_musicPlayListId);
        ostream.writeBooleanBit(this.m_useReverb);
        ostream.writeInt(this.m_soundPreset);
        ostream.writeByte(this.m_zoneTypeId);
        ostream.writeInt(this.m_graphicAmbienceId);
        ostream.writeShort(this.m_entryGfxId);
    }
}
