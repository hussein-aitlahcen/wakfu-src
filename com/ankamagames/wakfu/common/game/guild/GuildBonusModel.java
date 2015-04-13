package com.ankamagames.wakfu.common.game.guild;

import java.util.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

final class GuildBonusModel implements GuildBonus
{
    private final ArrayList<GuildBonusListener> m_listeners;
    private final int m_bonusId;
    private final GameDate m_buyDate;
    private final GameDate m_activationDate;
    
    GuildBonusModel(final int bonusId) {
        super();
        this.m_listeners = new ArrayList<GuildBonusListener>();
        this.m_buyDate = new GameDate(GameDate.NULL_DATE);
        this.m_activationDate = new GameDate(GameDate.NULL_DATE);
        this.m_bonusId = bonusId;
    }
    
    @Override
    public int getBonusId() {
        return this.m_bonusId;
    }
    
    @Override
    public GameDateConst getBuyDate() {
        return this.m_buyDate;
    }
    
    @Override
    public GameDateConst getActivationDate() {
        return this.m_activationDate;
    }
    
    void setBuyDate(final GameDateConst buyDate) {
        this.m_buyDate.set(buyDate);
    }
    
    void setActivationDate(final GameDateConst activationDate) {
        this.m_activationDate.set(activationDate);
        this.fireBonusActivated();
    }
    
    private void fireBonusActivated() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).bonusActivated(this);
        }
    }
    
    @Override
    public boolean addListener(final GuildBonusListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final GuildBonusListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "GuildBonusModel{m_activationDate=" + this.m_activationDate + ", m_bonusId=" + this.m_bonusId + ", m_buyDate=" + this.m_buyDate + '}';
    }
}
