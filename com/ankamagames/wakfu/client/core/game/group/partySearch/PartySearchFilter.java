package com.ankamagames.wakfu.client.core.game.group.partySearch;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class PartySearchFilter
{
    final short m_minLevel;
    final short m_maxLevel;
    final String m_name;
    
    public PartySearchFilter(final short min, final short max, final String name) {
        super();
        this.m_minLevel = min;
        this.m_maxLevel = max;
        this.m_name = name;
    }
    
    public PartySearchFilter(final byte[] buffer) {
        super();
        final ByteBuffer bb = ByteBuffer.wrap(buffer);
        this.m_minLevel = bb.getShort();
        this.m_maxLevel = bb.getShort();
        final byte[] description = new byte[bb.getInt()];
        bb.get(description);
        this.m_name = StringUtils.fromUTF8(description);
    }
    
    public byte[] serialize() {
        final ByteArray bb = new ByteArray();
        bb.putShort(this.m_minLevel);
        bb.putShort(this.m_maxLevel);
        final byte[] description = StringUtils.toUTF8(this.m_name);
        bb.putInt(description.length);
        bb.put(description);
        return bb.toArray();
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public String toString() {
        return "PartySearchFilter{m_minLevel=" + this.m_minLevel + ", m_maxLevel=" + this.m_maxLevel + ", m_name='" + this.m_name + '\'' + '}';
    }
}
