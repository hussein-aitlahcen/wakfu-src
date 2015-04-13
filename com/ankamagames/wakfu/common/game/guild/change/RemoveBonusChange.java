package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class RemoveBonusChange implements GuildChange
{
    private static final Logger m_logger;
    private int m_bonusId;
    
    RemoveBonusChange() {
        super();
    }
    
    RemoveBonusChange(final GuildBonus bonus) {
        super();
        this.m_bonusId = bonus.getBonusId();
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_bonusId);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_bonusId = bb.getInt();
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.removeBonus(this.m_bonusId);
        }
        catch (GuildException e) {
            RemoveBonusChange.m_logger.error((Object)"Impossible de retirer le bonus", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.REMOVE_BONUS;
    }
    
    @Override
    public String toString() {
        return "RemoveBonusChange{m_bonusId=" + this.m_bonusId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemoveBonusChange.class);
    }
}
