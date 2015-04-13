package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.common.game.guild.bonus.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.guild.constant.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.guild.bonus.effect.*;

public class GuildBonusHelper
{
    public static String getEffectDescription(final GuildBonusDefinition bonus) {
        final GuildBuffEffect effect = bonus.getEffect();
        switch (effect.getType()) {
            case STORAGE_COMPARTMENT: {
                final TextWidgetFormater sb = new TextWidgetFormater();
                final UnlockGuildStorageCompartment unlockGuildStorageCompartment = (UnlockGuildStorageCompartment)effect;
                final GuildStorageCompartmentType compartmentType = unlockGuildStorageCompartment.getCompartmentType();
                sb.append(WakfuTranslator.getInstance().getString("storageCompartmentBonusDesc", compartmentType.m_size));
                return sb.finishAndToString();
            }
            case MEMBER_EFFECT: {
                final TextWidgetFormater sb = new TextWidgetFormater();
                final MemberEffect memberEffect = (MemberEffect)effect;
                final int effectId = memberEffect.getEffectId();
                final WakfuEffect wakfuEffect = EffectManager.getInstance().getEffect(effectId);
                final DummyEffectContainerWriter writer = new DummyEffectContainerWriter(Collections.singletonList(wakfuEffect), 0, (short)0);
                final ArrayList<String> effectsDesc = writer.writeContainer();
                for (int i = 0, size = effectsDesc.size(); i < size; ++i) {
                    sb.append(effectsDesc.get(i));
                }
                return sb.finishAndToString();
            }
            case CHANGE_NATION: {
                return WakfuTranslator.getInstance().getString("guildBonus.changeNation");
            }
            case INCREASE_MAX_AUTHORIZED_EVOLUTION: {
                return WakfuTranslator.getInstance().getString("guildBonus.increaseMaxAuthorizedEvolution");
            }
            case REDUCE_LEARNING_DURATION: {
                return WakfuTranslator.getInstance().getString("guildBonus.reduceLearningDuration");
            }
            case SET_WEEKLY_POINTS_LIMIT: {
                return WakfuTranslator.getInstance().getString("guildBonus.setWeeklyPointsLimit");
            }
            case SET_POINTS_EARNED_FACTOR: {
                return WakfuTranslator.getInstance().getString("guildBonus.setPointsEarnedFactor");
            }
            case CRITERION_BONUS: {
                return WakfuTranslator.getInstance().getString(138, bonus.getId(), new Object[0]);
            }
            default: {
                return null;
            }
        }
    }
    
    public static boolean isLoading(final GuildBonus bonus, final GuildBonusDefinition def, final Guild guild) {
        final double factor = guild.getConstantManager().getLearningDurationFactor();
        final ClientGuildInformationHandler guildHandler = WakfuGameEntity.getInstance().getLocalPlayer().getGuildHandler();
        final float moderationFactor = ((GuildLocalInformationHandler)guildHandler).getModerationBonusLearningFactor();
        return GuildTimeHelper.isLearning(bonus, def, factor * moderationFactor);
    }
    
    public static GameDate getStartActivationDate(final GuildBonus bonus, final GuildBonusDefinition def, final Guild guild) {
        final double factor = guild.getConstantManager().getLearningDurationFactor();
        final ClientGuildInformationHandler guildHandler = WakfuGameEntity.getInstance().getLocalPlayer().getGuildHandler();
        final float moderationFactor = ((GuildLocalInformationHandler)guildHandler).getModerationBonusLearningFactor();
        return GuildTimeHelper.getActivationDate(bonus, def, factor * moderationFactor);
    }
    
    public static GameDate getDeactivationDate(final GuildBonus bonus, final GuildBonusDefinition def, final Guild guild) {
        return GuildTimeHelper.getDeactivationDate(bonus, def, guild.getConstantManager().getDurationFactor());
    }
    
    public static boolean isBonusTimed(final GuildBonusDefinition def) {
        return def.hasLearningDuration() || def.hasDuration();
    }
}
