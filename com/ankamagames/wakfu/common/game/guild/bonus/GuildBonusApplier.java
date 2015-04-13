package com.ankamagames.wakfu.common.game.guild.bonus;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.guild.bonus.*;
import com.ankamagames.wakfu.common.datas.guild.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.guild.bonus.effect.*;

public abstract class GuildBonusApplier implements GuildListener
{
    private static final Logger m_logger;
    protected final Guild m_guild;
    
    protected GuildBonusApplier(final Guild guild) {
        super();
        this.m_guild = guild;
    }
    
    protected void setLearningDurationFactor(final double factor) {
        this.m_guild.getConstantManager().setLearningDurationFactor(factor);
    }
    
    protected void setMaxAuthorizedEvolution(final int count) {
        this.m_guild.getConstantManager().setMaxSimultaneous(count);
    }
    
    protected void setCanChangeNation(final boolean activate) {
        this.m_guild.getConstantManager().setCanChangeNation(activate);
    }
    
    @Override
    public void nationIdChanged(final int nationId) {
    }
    
    @Override
    public void bonusActivated(final GuildBonus bonus) {
        final GuildBonusDefinition b = GuildBonusManager.INSTANCE.getBonus(bonus.getBonusId());
        switch (b.getEffect().getType()) {
            case CHANGE_NATION: {
                this.setCanChangeNation(true);
            }
            case INCREASE_MAX_AUTHORIZED_EVOLUTION: {
                this.setMaxAuthorizedEvolution(GuildDataManager.INSTANCE.getBonusMaxSimultaneousBonus());
            }
            case STORAGE_COMPARTMENT: {
                this.addStorageCompartment((UnlockGuildStorageCompartment)b.getEffect());
            }
            case UNLOCK_LEVEL: {
                this.unlockLevel((UnlockGuildLevel)b.getEffect());
            }
            case REDUCE_LEARNING_DURATION: {
                this.setLearningDurationFactor(GuildDataManager.INSTANCE.getBonusLearningFactor());
            }
            case SET_WEEKLY_POINTS_LIMIT: {
                this.setWeeklyPointsLimit((SetWeeklyPointsLimit)b.getEffect());
            }
            case SET_POINTS_EARNED_FACTOR: {
                this.setPointsEarnedFactor((SetPointsEarnedFactor)b.getEffect());
            }
            case MEMBER_EFFECT: {
                break;
            }
            case CRITERION_BONUS: {
                break;
            }
            default: {
                GuildBonusApplier.m_logger.warn((Object)("type d'effet non trait\u00e9 " + b.getEffect().getType()));
                break;
            }
        }
    }
    
    @Override
    public void earnedPointsWeeklyChanged(final int earnedPoints) {
    }
    
    @Override
    public void lastEarningPointWeekChanged(final int week) {
    }
    
    private void setPointsEarnedFactor(final SetPointsEarnedFactor effect) {
        this.m_guild.getConstantManager().setBonusPointEarnedFactor(effect.getFactor());
    }
    
    private void setWeeklyPointsLimit(final SetWeeklyPointsLimit effect) {
        final GuildController guildController = new GuildController(this.m_guild);
        guildController.setWeeklyPointsLimit(effect.getNewLimit());
    }
    
    protected void addStorageCompartment(final UnlockGuildStorageCompartment unlockGuildStorageCompartment) {
    }
    
    protected void unlockLevel(final UnlockGuildLevel unlockGuildLevel) {
    }
    
    @Override
    public void bonusRemoved(final GuildBonus bonus) {
        final GuildBonusDefinition b = GuildBonusManager.INSTANCE.getBonus(bonus.getBonusId());
        switch (b.getEffect().getType()) {
            case CHANGE_NATION: {
                this.setCanChangeNation(false);
            }
            case INCREASE_MAX_AUTHORIZED_EVOLUTION: {
                this.setMaxAuthorizedEvolution(GuildDataManager.INSTANCE.getDefaultMaxSimultaneousBonus());
            }
            case REDUCE_LEARNING_DURATION: {
                this.setLearningDurationFactor(1.0);
            }
            case SET_WEEKLY_POINTS_LIMIT: {
                this.setWeeklyPointsLimitToDefault();
            }
            case SET_POINTS_EARNED_FACTOR: {
                this.setPointsEarnedFactorToDefault();
            }
            default: {
                GuildBonusApplier.m_logger.warn((Object)("type d'effet non trait\u00e9 " + b.getEffect().getType()));
            }
        }
    }
    
    private void setPointsEarnedFactorToDefault() {
        this.m_guild.getConstantManager().setBonusPointEarnedFactor(1.0);
    }
    
    private void setWeeklyPointsLimitToDefault() {
        final GuildController guildController = new GuildController(this.m_guild);
        guildController.setWeeklyPointsLimit(7500);
    }
    
    @Override
    public void nameChanged() {
    }
    
    @Override
    public void blazonChanged() {
    }
    
    @Override
    public void descriptionChanged() {
    }
    
    @Override
    public void messageChanged() {
    }
    
    @Override
    public void levelChanged(final short level) {
    }
    
    @Override
    public void currentGuildPointsChanged(final int deltaPoints) {
    }
    
    @Override
    public void totalGuildPointsChanged(final int deltaPoints) {
    }
    
    @Override
    public void rankAdded(final GuildRank rank) {
    }
    
    @Override
    public void rankRemoved(final GuildRank rank) {
    }
    
    @Override
    public void memberAdded(final GuildMember member) {
    }
    
    @Override
    public void memberRemoved(final GuildMember member) {
    }
    
    @Override
    public void bonusAdded(final GuildBonus bonus) {
        if (bonus.getActivationDate() != null && !bonus.getActivationDate().isNull() && bonus.getActivationDate().beforeOrEquals(WakfuGameCalendar.getInstance().getNewDate())) {
            this.bonusActivated(bonus);
        }
    }
    
    @Override
    public void rankMoved(final GuildRank rank) {
    }
    
    @Override
    public void rankChanged(final GuildRank rank) {
    }
    
    @Override
    public void memberChanged(final GuildMember member) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildBonusApplier.class);
    }
}
