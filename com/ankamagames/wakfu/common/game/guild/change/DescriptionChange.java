package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class DescriptionChange implements GuildChange
{
    private static final Logger m_logger;
    private String m_description;
    
    DescriptionChange() {
        super();
    }
    
    DescriptionChange(final String description) {
        super();
        this.m_description = description;
    }
    
    @Override
    public byte[] serialize() {
        final byte[] descUtf = StringUtils.toUTF8(this.m_description);
        final ByteBuffer bb = ByteBuffer.allocate(4 + descUtf.length);
        bb.putInt(descUtf.length);
        bb.put(descUtf);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        final byte[] descUtf = new byte[bb.getInt()];
        bb.get(descUtf);
        this.m_description = StringUtils.fromUTF8(descUtf);
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.changeDescription(this.m_description);
        }
        catch (GuildException e) {
            DescriptionChange.m_logger.error((Object)"Impossible de changer la description", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.DESCRIPTION;
    }
    
    @Override
    public String toString() {
        return "DescriptionChange{m_description='" + this.m_description + '\'' + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)DescriptionChange.class);
    }
}
