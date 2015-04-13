package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class MessageChange implements GuildChange
{
    private static final Logger m_logger;
    private String m_message;
    
    MessageChange() {
        super();
    }
    
    MessageChange(final String message) {
        super();
        this.m_message = message;
    }
    
    @Override
    public byte[] serialize() {
        final byte[] utf = StringUtils.toUTF8(this.m_message);
        final ByteBuffer bb = ByteBuffer.allocate(4 + utf.length);
        bb.putInt(utf.length);
        bb.put(utf);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        final byte[] utf = new byte[bb.getInt()];
        bb.get(utf);
        this.m_message = StringUtils.fromUTF8(utf);
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.changeMessage(this.m_message);
        }
        catch (GuildException e) {
            MessageChange.m_logger.error((Object)"Impossible de changer le message", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.MESSAGE;
    }
    
    @Override
    public String toString() {
        return "MessageChange{m_message='" + this.m_message + '\'' + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)MessageChange.class);
    }
}
