package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class NationChange implements GuildChange
{
    private static final Logger m_logger;
    private int m_nationId;
    
    NationChange() {
        super();
    }
    
    NationChange(final int nationId) {
        super();
        this.m_nationId = nationId;
    }
    
    @Override
    public byte[] serialize() {
        final ByteArray bb = new ByteArray();
        bb.putInt(this.m_nationId);
        return bb.toArray();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_nationId = bb.getInt();
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.changeNation(this.m_nationId);
        }
        catch (GuildException e) {
            NationChange.m_logger.error((Object)"Impossible de changer le message", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.NATION;
    }
    
    @Override
    public String toString() {
        return "NationChange{m_nationId=" + this.m_nationId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationChange.class);
    }
}
