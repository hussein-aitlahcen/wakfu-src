package com.ankamagames.wakfu.common.game.guild.bonus;

import com.ankamagames.wakfu.common.game.guild.bonus.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class GuildBonusDefinition
{
    private final int m_id;
    private final GameIntervalConst m_learningDuration;
    private final GameIntervalConst m_duration;
    private final int m_cost;
    private final GuildBuffEffect m_effect;
    private final BonusType m_type;
    
    public GuildBonusDefinition(final int id, final long learningDuration, final long duration, final int cost, final GuildBuffEffect effect, final BonusType type) {
        super();
        this.m_id = id;
        this.m_learningDuration = GameInterval.fromLong(learningDuration);
        this.m_duration = GameInterval.fromLong(duration);
        this.m_cost = cost;
        this.m_effect = effect;
        this.m_type = type;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public GameIntervalConst getLearningDuration() {
        return this.m_learningDuration;
    }
    
    public boolean hasLearningDuration() {
        return this.getLearningDuration().isPositive();
    }
    
    public GameIntervalConst getDuration() {
        return this.m_duration;
    }
    
    public boolean hasDuration() {
        return this.getDuration().isPositive();
    }
    
    public GuildBuffEffect getEffect() {
        return this.m_effect;
    }
    
    public int getCost() {
        return this.m_cost;
    }
    
    public BonusType getType() {
        return this.m_type;
    }
    
    @Override
    public String toString() {
        return "GuildBonusDefinition{m_id=" + this.m_id + ", m_learningDuration=" + this.m_learningDuration + ", m_duration=" + this.m_duration + ", m_cost=" + this.m_cost + ", m_effect=" + this.m_effect + ", m_type=" + this.m_type + '}';
    }
}
