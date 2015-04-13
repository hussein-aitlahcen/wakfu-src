package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class ModifyBonusChange implements GuildChange
{
    private static final Logger m_logger;
    private GuildBonus m_bonus;
    
    ModifyBonusChange() {
        super();
    }
    
    ModifyBonusChange(final GuildBonus bonus) {
        super();
        this.m_bonus = bonus;
    }
    
    @Override
    public byte[] serialize() {
        return GuildSerializer.serializeBonus(this.m_bonus);
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_bonus = GuildSerializer.unserializeBonus(bb);
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.changeBonusActivationDate(this.m_bonus.getBonusId(), this.m_bonus.getActivationDate());
        }
        catch (GuildException e) {
            ModifyBonusChange.m_logger.error((Object)"Impossible de modifier le bonus", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.MODIFY_BONUS;
    }
    
    @Override
    public String toString() {
        return "ModifyBonusChange{m_bonus=" + this.m_bonus + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ModifyBonusChange.class);
    }
}
