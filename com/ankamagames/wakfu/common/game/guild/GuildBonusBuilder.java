package com.ankamagames.wakfu.common.game.guild;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;

public class GuildBonusBuilder
{
    private int m_bonusId;
    private final GameDate m_buyDate;
    private final GameDate m_activationDate;
    
    public GuildBonusBuilder() {
        super();
        this.m_buyDate = new GameDate(GameDate.NULL_DATE);
        this.m_activationDate = new GameDate(GameDate.NULL_DATE);
    }
    
    public GuildBonusBuilder setBonusId(final int bonusId) {
        this.m_bonusId = bonusId;
        return this;
    }
    
    public GuildBonusBuilder setBuyDate(final GameDateConst buyDate) {
        this.m_buyDate.set(buyDate);
        return this;
    }
    
    public GuildBonusBuilder setActivationDate(final GameDateConst activationDate) {
        this.m_activationDate.set(activationDate);
        return this;
    }
    
    public GuildBonus createGuildBonus() {
        final GuildBonusModel bonus = new GuildBonusModel(this.m_bonusId);
        bonus.setBuyDate(this.m_buyDate);
        bonus.setActivationDate(this.m_activationDate);
        return bonus;
    }
    
    @Override
    public String toString() {
        return "GuildBonusBuilder{m_bonusId=" + this.m_bonusId + ", m_buyDate=" + this.m_buyDate + ", m_activationDate=" + this.m_activationDate + '}';
    }
}
