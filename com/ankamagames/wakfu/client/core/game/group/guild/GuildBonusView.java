package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.guild.bonus.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.datas.guild.level.*;
import com.ankamagames.wakfu.common.game.guild.constant.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.guild.bonus.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.time.*;

public class GuildBonusView extends ImmutableFieldProvider implements TimeTickListener
{
    public static final String NAME = "name";
    public static final String LEARN_DURATION = "learnDuration";
    public static final String DURATION = "duration";
    public static final String COST = "cost";
    public static final String REQUIREMENT = "requirement";
    public static final String EFFECTS = "effects";
    public static final String LEARN_REMAINING_TIME = "learnRemainingTime";
    public static final String REMAINING_TIME = "remainingTime";
    public static final String CAN_BE_PURCHASED = "canBePurchased";
    public static final String IS_ACTIVE = "isActive";
    private final GuildBonusDefinition m_bonus;
    
    public GuildBonusView(final GuildBonusDefinition bonus) {
        super();
        this.m_bonus = bonus;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(138, this.m_bonus.getId(), new Object[0]);
        }
        if (fieldName.equals("learnDuration")) {
            if (!this.m_bonus.hasLearningDuration()) {
                return null;
            }
            return TimeUtils.getShortDescription(this.m_bonus.getLearningDuration());
        }
        else if (fieldName.equals("duration")) {
            if (!this.m_bonus.hasDuration()) {
                return null;
            }
            return TimeUtils.getShortDescription(this.m_bonus.getDuration());
        }
        else {
            if (fieldName.equals("cost")) {
                final TextWidgetFormater sb = new TextWidgetFormater();
                if (!this.hasEnoughGuildPoints()) {
                    sb.openText().addColor(new Color(0.6f, 0.0f, 0.0f, 1.0f).getRGBtoHex());
                }
                sb.append(this.m_bonus.getCost());
                return sb.finishAndToString();
            }
            if (fieldName.equals("requirement")) {
                final TextWidgetFormater sb = new TextWidgetFormater();
                if (!this.isBonusUnlocked()) {
                    sb.openText().addColor(new Color(0.6f, 0.0f, 0.0f, 1.0f).getRGBtoHex());
                }
                sb.append(WakfuTranslator.getInstance().getString("required.level.custom", GuildLevelManager.INSTANCE.getBonusUnlockLevel(this.m_bonus.getId())));
                return sb.finishAndToString();
            }
            if (fieldName.equals("effects")) {
                return this.getEffectDesc();
            }
            if (fieldName.equals("canBePurchased")) {
                return this.canBePurchased();
            }
            if (fieldName.equals("learnRemainingTime")) {
                if (!this.m_bonus.hasLearningDuration()) {
                    return null;
                }
                final GameDate endDate = GuildBonusHelper.getStartActivationDate(this.getGuildBonusInfo(), this.m_bonus, this.getGuild());
                return this.getRemainingTimeDescription(endDate);
            }
            else if (fieldName.equals("remainingTime")) {
                if (!this.m_bonus.hasDuration()) {
                    return null;
                }
                final GameDate endDate = GuildBonusHelper.getDeactivationDate(this.getGuildBonusInfo(), this.m_bonus, this.getGuild());
                return this.getRemainingTimeDescription(endDate);
            }
            else {
                if (fieldName.equals("isActive")) {
                    return GuildTimeHelper.isActive(this.getGuildBonusInfo());
                }
                return null;
            }
        }
    }
    
    public String getEffectDesc() {
        return GuildBonusHelper.getEffectDescription(this.m_bonus);
    }
    
    private String getRemainingTimeDescription(final GameDate endDate) {
        final GameIntervalConst remainingTime = WakfuGameCalendar.getInstance().getDate().timeTo(endDate);
        return TimeUtils.getShortDescription(remainingTime.greaterThan(GameIntervalConst.MINUTE_INTERVAL) ? remainingTime : GameIntervalConst.MINUTE_INTERVAL);
    }
    
    public int getId() {
        return this.m_bonus.getId();
    }
    
    public GuildBuffEffect getEffect() {
        return this.m_bonus.getEffect();
    }
    
    private boolean canBePurchased() {
        return this.hasEnoughGuildPoints() && this.isBonusUnlocked();
    }
    
    private boolean hasEnoughGuildPoints() {
        return this.getGuildHandler().getCurrentGuildPoints() >= this.m_bonus.getCost();
    }
    
    private boolean isBonusUnlocked() {
        return GuildLevelManager.INSTANCE.isBonusUnlocked(this.m_bonus.getId(), this.getGuildHandler().getLevel());
    }
    
    private GuildLocalInformationHandler getGuildHandler() {
        return (GuildLocalInformationHandler)WakfuGameEntity.getInstance().getLocalPlayer().getGuildHandler();
    }
    
    private GuildBonus getGuildBonusInfo() {
        return this.getGuild().getBonus(this.m_bonus.getId());
    }
    
    private Guild getGuild() {
        return this.getGuildHandler().getGuild();
    }
    
    public void updatePurchaseCapability() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "canBePurchased", "requirement", "cost");
    }
    
    public void update() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "canBePurchased");
    }
    
    public void addToTimeManager() {
        TimeManager.INSTANCE.addListener(this);
    }
    
    public void removeFromTimeManager() {
        TimeManager.INSTANCE.removeListener(this);
    }
    
    @Override
    public void tick() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remainingTime", "learnRemainingTime", "isActive");
    }
}
