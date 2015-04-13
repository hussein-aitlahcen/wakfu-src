package com.ankamagames.wakfu.client.core.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class SpellEffectChatLogger
{
    public static final String SE_TAG = "[se]";
    public static final String TARGET_TAG = "[target]";
    public static final String CASTER_TAG = "[casterName]";
    public static final String SE_REGEX = "\\[se\\]";
    public static final String TARGET_REGEX = "\\[target\\]";
    public static final String CASTER_REGEX = "\\[casterName\\]";
    private final SpellEffectAction m_spellEffectAction;
    private static final Logger m_logger;
    private static final int[] IGNORED_EFFECTS;
    
    public SpellEffectChatLogger(final SpellEffectAction spellEffectAction) {
        super();
        this.m_spellEffectAction = spellEffectAction;
    }
    
    void displayChatMessage() {
        final FightInfo fight = this.m_spellEffectAction.getFight();
        final LocalPlayerCharacter info = WakfuGameEntity.getInstance().getLocalPlayer();
        if (fight == null || info == null || !fight.equals(info.getCurrentOrObservedFight())) {
            return;
        }
        if (!this.checkIgnoredEffectsList()) {
            return;
        }
        if (this.m_spellEffectAction.getRunningEffect() instanceof StateRunningEffect) {
            return;
        }
        if (!this.checkNotifyInChat()) {
            return;
        }
        final EffectUser caster = this.m_spellEffectAction.getRunningEffect().getCaster();
        if (caster == null) {
            return;
        }
        if (caster instanceof CharacterInfo && HoodedMonsterFightEventListener.isVisuallyHooded((CharacterInfo)caster)) {
            return;
        }
        final long concernedTargetId = this.m_spellEffectAction.getTargetId();
        if (!checkTargetValidity(fight, concernedTargetId)) {
            return;
        }
        final String finalText = this.decribeEffectAction();
        if (finalText.length() > 0) {
            SpellEffectAction.m_fightLogger.info(finalText);
        }
    }
    
    private static boolean checkTargetValidity(final FightInfo fight, final long concernedTargetId) {
        return concernedTargetId == Long.MIN_VALUE || fight.getFighterFromId(concernedTargetId) != null || fight.getEffectAreaManager().getActiveEffectAreaWithId(concernedTargetId) != null || (fight instanceof Fight && ((Fight)fight).getAdditionalTargetWithId(concernedTargetId) != null);
    }
    
    private boolean checkNotifyInChat() {
        return ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().notifyInChat();
    }
    
    private boolean checkIgnoredEffectsList() {
        return !ArrayUtils.contains(SpellEffectChatLogger.IGNORED_EFFECTS, this.m_spellEffectAction.getRunningEffect().getId());
    }
    
    private String getTargetName(final long targetId) {
        final FightInfo fight = this.m_spellEffectAction.getFight();
        final CharacterInfo fighter = fight.getFighterFromId(targetId);
        if (fighter != null) {
            return fighter.getName();
        }
        final AbstractEffectArea area = (AbstractEffectArea)fight.getEffectAreaManager().getActiveEffectAreaWithId(targetId);
        if (area != null) {
            return area.getName();
        }
        if (fight instanceof Fight) {
            final EffectUser target = ((Fight)fight).getAdditionalTargetWithId(targetId);
            if (target instanceof WakfuClientMapInteractiveElement) {
                return ((WakfuClientMapInteractiveElement)target).getName();
            }
        }
        return "";
    }
    
    @NotNull
    private String decribeEffectAction() {
        final TextWidgetFormater formattedMessage = new TextWidgetFormater();
        final boolean useCasterName = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().notifyInChatWithCasterName() || this.m_spellEffectAction.getTargetId() == Long.MIN_VALUE;
        final long fighterId = useCasterName ? this.m_spellEffectAction.getCasterId() : this.m_spellEffectAction.getTargetId();
        formattedMessage.openText().addColor(ChatConstants.CHAT_FIGHT_TARGET_COLOR).append(this.getTargetName(fighterId));
        formattedMessage.append(WakfuTranslator.getInstance().getString("colon"));
        formattedMessage.closeText();
        formattedMessage.openText().addColor(ChatConstants.CHAT_FIGHT_EFFECT_COLOR);
        final String s = this.getEffectText(formattedMessage, false);
        return (s == null) ? "" : s;
    }
    
    private String getEffectText(final TextWidgetFormater formattedMessage, final boolean ignoreCustom) {
        final int id = this.m_spellEffectAction.getRunningEffect().getId();
        final ArrayList<String> values = new ArrayList<String>();
        final WakfuEffect genericEffect = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect();
        final int effectId = genericEffect.getEffectId();
        String finalText = "";
        if (effectId != -1 && !ignoreCustom && WakfuTranslator.getInstance().containsContentKey(13, effectId)) {
            final boolean useGenericValue = id == RunningEffectConstants.NULL_EFFECT.getId() && genericEffect.getParamsCount() > 0;
            final int value = useGenericValue ? genericEffect.getParam(0, ((RunningEffect<FX, WakfuEffectContainer>)this.m_spellEffectAction.getRunningEffect()).getEffectContainer().getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) : this.m_spellEffectAction.getRunningEffect().getValue();
            if (id == RunningEffectConstants.STATE_FORCE_UNAPPLY.getId() || id == RunningEffectConstants.APPLY_STATE_PERCENT_FUNCTION_AREA_HP.getId()) {
                final StateClient state = (StateClient)StateManager.getInstance().getState(this.m_spellEffectAction.getRunningEffect().getValue());
                finalText = WakfuTranslator.getInstance().getString(13, effectId, state.getName());
            }
            else if (id == RunningEffectConstants.STATE_APPLY.getId()) {
                final ApplyState applyState = (ApplyState)this.m_spellEffectAction.getRunningEffect();
                int genericEffectParam2 = 0;
                if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 2) {
                    genericEffectParam2 = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(1, applyState.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                }
                final StateClient state2 = (StateClient)StateManager.getInstance().getState(applyState.getStateId());
                finalText = WakfuTranslator.getInstance().getString(13, effectId, CastableDescriptionGenerator.getStateNameLink(state2, ((ApplyState)this.m_spellEffectAction.getRunningEffect()).getStateLevel(), state2.getMaxlevel(), true), genericEffectParam2);
            }
            else if (id == RunningEffectConstants.APPLY_STATE_CAPED_BY_ANOTHER_STATE.getId()) {
                final ApplyState applyState = (ApplyState)this.m_spellEffectAction.getRunningEffect();
                int genericEffectParam2 = 0;
                if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 2) {
                    genericEffectParam2 = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(1, applyState.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                }
                final StateClient state2 = (StateClient)StateManager.getInstance().getState(applyState.getStateId());
                finalText = WakfuTranslator.getInstance().getString(13, effectId, CastableDescriptionGenerator.getStateNameLink(state2, ((ApplyState)this.m_spellEffectAction.getRunningEffect()).getStateLevel(), state2.getMaxlevel(), true), genericEffectParam2);
            }
            else if (id == RunningEffectConstants.SET_EFFECT_AREA.getId() || id == RunningEffectConstants.SET_FECA_GLYPH.getId() || id == RunningEffectConstants.SET_BOMB.getId()) {
                int areaTypeId = 0;
                if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 1) {
                    areaTypeId = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(0, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                }
                final String areaName = WakfuTranslator.getInstance().getString(6, areaTypeId, new Object[0]);
                finalText = WakfuTranslator.getInstance().getString(13, effectId, areaName);
            }
            else if (id == RunningEffectConstants.REMOVE_AREA_USING_TARGET.getId() || id == RunningEffectConstants.REMOVE_AREA_USING_TARGET_CELL.getId()) {
                int areaTypeId = 0;
                if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 1) {
                    areaTypeId = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(0, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                }
                final String areaName = WakfuTranslator.getInstance().getString(6, areaTypeId, new Object[0]);
                finalText = WakfuTranslator.getInstance().getString(13, effectId, areaName);
            }
            else if (id == RunningEffectConstants.REPLACE_AREA_BY_ANOTHER.getId() || id == RunningEffectConstants.REPLACE_AREA_BY_ANOTHER_USING_TARGET.getId()) {
                int areaTypeId = 0;
                if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 2) {
                    areaTypeId = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(1, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                }
                final String areaName = WakfuTranslator.getInstance().getString(6, areaTypeId, new Object[0]);
                finalText = WakfuTranslator.getInstance().getString(13, effectId, areaName);
            }
            else if (id == RunningEffectConstants.SUMMON.getId()) {
                if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 1) {
                    final int summonId = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(0, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    values.add(WakfuTranslator.getInstance().getString(7, summonId, new Object[0]));
                }
            }
            else {
                finalText = WakfuTranslator.getInstance().getString(13, effectId, WakfuTranslator.getInstance().formatNumber(value));
            }
        }
        if (finalText.contains("[se]")) {
            final String effectText = this.getEffectText(formattedMessage, true);
            finalText = finalText.replaceAll("\\[se\\]", effectText);
        }
        else if (ignoreCustom || finalText.length() == 0 || effectId == EfficencyGenericEffect.getInstance().getEffectId()) {
            String effectText = WakfuTranslator.getInstance().getStringWithoutFormat(30, id);
            if (effectText.length() > 0) {
                if (id == RunningEffectConstants.STATE_APPLY.getId() || id == RunningEffectConstants.APPLY_DEATHTAG.getId() || id == RunningEffectConstants.APPLY_STATE_FOR_FECA_ARMOR.getId() || id == RunningEffectConstants.APPLY_STATE_PERCENT_FUNCTION_AREA_HP.getId() || id == RunningEffectConstants.APPLY_STATE_CAPED_BY_ANOTHER_STATE.getId()) {
                    final ApplyState applyState2 = (ApplyState)this.m_spellEffectAction.getRunningEffect();
                    final String s1 = String.valueOf(applyState2.getStateLevel());
                    final StateClient state3 = (StateClient)StateManager.getInstance().getState(applyState2.getStateId());
                    switch (applyState2.getExecutionStatus()) {
                        case 3:
                        case 6: {
                            return null;
                        }
                        case 2: {
                            effectText = WakfuTranslator.getInstance().getString("effect.immuned", state3.getName());
                            break;
                        }
                        default: {
                            final StringBuilder sb = new StringBuilder(CastableDescriptionGenerator.getStateNameLink(state3, ((ApplyState)this.m_spellEffectAction.getRunningEffect()).getStateLevel(), state3.getMaxlevel(), true));
                            this.addStatus(sb);
                            values.add(sb.toString());
                            values.add(s1);
                            break;
                        }
                    }
                }
                else if (id == RunningEffectConstants.STATE_APPLICATION_BONUS.getId()) {
                    final StateClient state4 = (StateClient)StateManager.getInstance().getState(genericEffect.getParam(0, ((RunningEffect<FX, WakfuEffectContainer>)this.m_spellEffectAction.getRunningEffect()).getEffectContainer().getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
                    final StringBuilder sb2 = new StringBuilder(state4.getName());
                    values.add(sb2.toString());
                    values.add(String.valueOf(genericEffect.getParam(1, ((RunningEffect<FX, WakfuEffectContainer>)this.m_spellEffectAction.getRunningEffect()).getEffectContainer().getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL)));
                }
                else if (id == RunningEffectConstants.STATE_FORCE_UNAPPLY.getId()) {
                    final StateClient state4 = (StateClient)StateManager.getInstance().getState(this.m_spellEffectAction.getRunningEffect().getValue());
                    final StringBuilder sb2 = new StringBuilder(state4.getName());
                    this.addStatus(sb2);
                    values.add(sb2.toString());
                }
                else if (id == RunningEffectConstants.SET_AURA.getId() || id == RunningEffectConstants.SET_AURA_ON_TARGET.getId() || id == RunningEffectConstants.SET_LOOT_EFFECT_AREA.getId()) {
                    final StringBuilder sb3 = new StringBuilder().append(WakfuTranslator.getInstance().getString(6, this.m_spellEffectAction.getRunningEffect().getValue(), new Object[0]));
                    this.addStatus(sb3);
                    values.add(sb3.toString());
                }
                else if (id == RunningEffectConstants.HP_LOSS_FROM_LOOT_AREA.getId() || id == RunningEffectConstants.DROP_FROM_LOOT_AREA.getId() || id == RunningEffectConstants.HP_LOSS_FUNCTION_LOOT.getId()) {
                    final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_spellEffectAction.getRunningEffect().getValue());
                    StringBuilder sb2;
                    if (referenceItem != null) {
                        sb2 = new StringBuilder(referenceItem.getName());
                    }
                    else {
                        sb2 = new StringBuilder("");
                    }
                    this.addStatus(sb2);
                    values.add(sb2.toString());
                }
                else if (id == RunningEffectConstants.SEDUCE_MONSTER.getId()) {
                    final StringBuilder sb3 = new StringBuilder();
                    this.addStatus(sb3);
                    values.add(sb3.toString());
                }
                else if (id == RunningEffectConstants.STATE_RESISTANCE.getId()) {
                    final StateClient state4 = (StateClient)StateManager.getInstance().getState(((StateResistance)this.m_spellEffectAction.getRunningEffect()).getStateId());
                    values.add(state4.getName());
                    try {
                        values.add(Integer.toString(this.m_spellEffectAction.getRunningEffect().getValue()));
                    }
                    catch (Exception e2) {
                        SpellEffectChatLogger.m_logger.error((Object)"probl\u00e8me \u00e0 la recup\u00e9ration du % de resistance d'\u00e9tat");
                    }
                }
                else if (id == RunningEffectConstants.SET_FECA_GLYPH.getId() || id == RunningEffectConstants.SET_EFFECT_AREA.getId() || id == RunningEffectConstants.SET_GLYPH.getId() || id == RunningEffectConstants.SET_TRAP.getId() || id == RunningEffectConstants.SET_BOMB.getId()) {
                    if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 1) {
                        final int areaTypeId2 = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(0, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                        values.add(WakfuTranslator.getInstance().getString(6, areaTypeId2, new Object[0]));
                    }
                }
                else if (id == RunningEffectConstants.REMOVE_AREA_USING_TARGET.getId() || id == RunningEffectConstants.REMOVE_AREA_USING_TARGET_CELL.getId()) {
                    if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 1) {
                        final int areaTypeId2 = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(0, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                        values.add(WakfuTranslator.getInstance().getString(6, areaTypeId2, new Object[0]));
                    }
                }
                else if (id == RunningEffectConstants.REPLACE_AREA_BY_ANOTHER.getId() || id == RunningEffectConstants.REPLACE_AREA_BY_ANOTHER_USING_TARGET.getId()) {
                    if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 2) {
                        final int areaTypeId2 = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(1, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                        values.add(WakfuTranslator.getInstance().getString(6, areaTypeId2, new Object[0]));
                    }
                }
                else if (id == RunningEffectConstants.SUMMON.getId()) {
                    if (((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParamsCount() >= 1) {
                        final int summonId2 = ((RunningEffect<WakfuEffect, EC>)this.m_spellEffectAction.getRunningEffect()).getGenericEffect().getParam(0, this.m_spellEffectAction.getRunningEffect().getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                        values.add(WakfuTranslator.getInstance().getString(7, summonId2, new Object[0]));
                    }
                }
                else {
                    final StringBuilder sb3 = new StringBuilder().append(WakfuTranslator.getInstance().formatNumber(this.m_spellEffectAction.getRunningEffect().getValue()));
                    this.addStatus(sb3);
                    values.add(sb3.toString());
                }
                effectText = this.checkTextTags(effectText);
                formattedMessage.append(effectText);
                this.addBlockExecutionFeedback(formattedMessage);
                formattedMessage.closeText();
                try {
                    finalText = StringFormatter.format(formattedMessage.finishAndToString(), values.toArray());
                }
                catch (Exception e) {
                    SpellEffectChatLogger.m_logger.error((Object)("probl\u00e8me dans le formattage de la chaine " + formattedMessage), (Throwable)e);
                }
            }
        }
        else if (finalText != null) {
            formattedMessage.append(this.checkTextTags(finalText));
            formattedMessage.closeText();
            finalText = formattedMessage.finishAndToString();
        }
        return finalText;
    }
    
    private void addBlockExecutionFeedback(final TextWidgetFormater formattedMessage) {
        switch (this.m_spellEffectAction.getRunningEffect().getExecutionStatus()) {
            case 7: {
                formattedMessage.append(" (").append(WakfuTranslator.getInstance().getString("exec.block.chat")).append(")");
                break;
            }
        }
    }
    
    private String checkTextTags(final String effectText2) {
        String effectText3 = DefaultEffectWriter.replaceStaticPatterns(effectText2);
        if (!effectText3.contains("[target]") && !effectText3.contains("[casterName]")) {
            return effectText3;
        }
        final FightInfo fight = this.m_spellEffectAction.getFight();
        final String targetName = fight.getFighterFromId(this.m_spellEffectAction.getTargetId()).getName();
        effectText3 = effectText3.replaceAll("\\[target\\]", targetName);
        final EffectUser caster = this.m_spellEffectAction.getRunningEffect().getCaster();
        if (caster != null) {
            final CharacterInfo fighter = fight.getFighterFromId(caster.getId());
            if (fighter != null) {
                effectText3 = effectText3.replaceAll("\\[casterName\\]", fighter.getName());
            }
        }
        return effectText3;
    }
    
    private void addStatus(final StringBuilder sb) {
        switch (this.m_spellEffectAction.getRunningEffect().getExecutionStatus()) {
            case 4: {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("exec.absorb")).append(")");
                break;
            }
            case 1: {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("exec.failed")).append(")");
                break;
            }
            case 2: {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("exec.immune")).append(")");
                break;
            }
            case 13: {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("exec.seduction.immunity")).append(")");
                break;
            }
            case 3:
            case 6: {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("exec.resist")).append(")");
                break;
            }
            case 12: {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("exec.level.too.high")).append(")");
                break;
            }
            case 14: {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("exec.seduction.success")).append(")");
                break;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellEffectChatLogger.class);
        IGNORED_EFFECTS = new int[] { 30, 40, 190 };
    }
}
