package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class AddBonusChange implements GuildChange
{
    private static final Logger m_logger;
    private GuildBonus m_bonus;
    
    AddBonusChange() {
        super();
    }
    
    AddBonusChange(final GuildBonus bonus) {
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
            controller.addBonus(this.m_bonus);
        }
        catch (GuildException e) {
            AddBonusChange.m_logger.error((Object)"Impossible d'ajouter le bonus", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.ADD_BONUS;
    }
    
    @Override
    public String toString() {
        return "AddBonusChange{m_bonus=" + this.m_bonus + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddBonusChange.class);
    }
}
