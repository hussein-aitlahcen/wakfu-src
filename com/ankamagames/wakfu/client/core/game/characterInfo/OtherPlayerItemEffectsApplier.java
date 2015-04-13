package com.ankamagames.wakfu.client.core.game.characterInfo;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import gnu.trove.*;

final class OtherPlayerItemEffectsApplier
{
    private static final Logger m_logger;
    private final PlayerCharacter m_player;
    private final WakfuEffectContainerBuilder m_containerBuilder;
    
    OtherPlayerItemEffectsApplier(@NotNull final PlayerCharacter player) {
        super();
        this.m_containerBuilder = new WakfuEffectContainerBuilder();
        this.m_player = player;
    }
    
    void reloadItemsEffectsForPlayer() {
        final TByteIntHashMap equipmentAppearance = this.m_player.getEquipmentAppearance();
        if (equipmentAppearance == null) {
            return;
        }
        this.m_containerBuilder.setContainerType(12);
        final int[] equipments = equipmentAppearance.getValues();
        final TShortByteHashMap setsIds = new TShortByteHashMap();
        this.m_player.getRunningEffectManager().removeLinkedToContainerType(12);
        for (int i = 0; i < equipments.length; ++i) {
            this.reapplyEffectsWithHMIActions(setsIds, equipments[i]);
        }
        this.reapplySetEffectsWithHMIActions(setsIds);
    }
    
    private void reapplyEffectsWithHMIActions(final TShortByteHashMap setsIds, final int equipmentId) {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(equipmentId);
        if (referenceItem == null) {
            OtherPlayerItemEffectsApplier.m_logger.warn((Object)("L'item " + equipmentId + " n'existe pas"));
            return;
        }
        if (referenceItem.getSetId() > 0) {
            setsIds.adjustOrPutValue(referenceItem.getSetId(), (byte)1, (byte)1);
        }
        final Iterator<WakfuEffect> effects = (Iterator<WakfuEffect>)referenceItem.getEffectsIterator();
        if (!effects.hasNext()) {
            return;
        }
        this.executeEffectsWithHMIActions(equipmentId, effects);
    }
    
    private void reapplySetEffectsWithHMIActions(final TShortByteHashMap setsIds) {
        this.m_player.getRunningEffectManager().removeLinkedToContainerType(14);
        this.m_containerBuilder.setContainerType(14);
        final TShortByteIterator it = setsIds.iterator();
        while (it.hasNext()) {
            it.advance();
            final ItemSet set = ItemSetManager.getInstance().getItemSet(it.key());
            if (set == null) {
                continue;
            }
            final ItemSetLevel setLevel = set.getEffectsByNbElements(it.value());
            if (setLevel == null) {
                continue;
            }
            final Iterator<WakfuEffect> effects = setLevel.iterator();
            this.executeEffectsWithHMIActions(set.getId(), effects);
        }
    }
    
    private void executeEffectsWithHMIActions(final int equipmentId, final Iterator<WakfuEffect> effects) {
        this.m_containerBuilder.setContainerId(equipmentId);
        final WakfuEffectContainer container = this.m_containerBuilder.build();
        while (effects.hasNext()) {
            final WakfuEffect effect = effects.next();
            this.executeEffectIfItHasHMIActions(equipmentId, container, effect);
        }
    }
    
    private void executeEffectIfItHasHMIActions(final int equipmentId, final WakfuEffectContainer container, final WakfuEffect effect) {
        if (this.executeEffect(container, effect)) {
            return;
        }
        if (this.hasHMIActions(effect)) {
            OtherPlayerItemEffectsApplier.m_logger.warn((Object)("On a un HMI sur autre chose que sur un NullEffect sur un item " + equipmentId));
        }
    }
    
    private boolean executeEffect(final WakfuEffectContainer container, final WakfuEffect effect) {
        final SimpleCriterion criterion = effect.getConditions();
        final WakfuRunningEffect runningEffect = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
        final RunningEffect<WakfuEffect, WakfuEffectContainer> effectForCriterion = runningEffect.newInstance();
        boolean valid;
        try {
            valid = (criterion != null && !criterion.isValid(this.m_player, this.m_player, effectForCriterion, this.m_player.getAppropriateContext()));
        }
        catch (Exception e) {
            OtherPlayerItemEffectsApplier.m_logger.error((Object)"Exception levee", (Throwable)e);
            valid = false;
        }
        finally {
            effectForCriterion.release();
        }
        if (valid) {
            return false;
        }
        if (effect.getActionId() == RunningEffectConstants.NULL_EFFECT.getId()) {
            if (this.hasHMIActions(effect)) {
                effect.execute(container, this.m_player, this.m_player.getEffectContext(), RunningEffectConstants.getInstance(), this.m_player.getWorldCellX(), this.m_player.getWorldCellY(), this.m_player.getWorldCellAltitude(), this.m_player, null, false);
            }
            return true;
        }
        if (effect.getActionId() == RunningEffectConstants.STATE_APPLY.getId()) {
            final StateClient state = (StateClient)StateManager.getInstance().getState((int)effect.getParam(0));
            if (state != null) {
                for (final WakfuEffect subEffect : state) {
                    this.executeEffect(container, subEffect);
                }
            }
            else {
                OtherPlayerItemEffectsApplier.m_logger.error((Object)("etat inconnu:" + effect.getParam(0)));
            }
            return true;
        }
        if (RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.contains(effect.getActionId())) {
            final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(effect.getEffectId());
            if (effectGroup != null) {
                for (final WakfuEffect subEffect : effectGroup) {
                    this.executeEffect(container, subEffect);
                }
            }
            else {
                OtherPlayerItemEffectsApplier.m_logger.error((Object)("Group d'effet inconnu:" + effect.getEffectId()));
            }
            return true;
        }
        return false;
    }
    
    private boolean hasHMIActions(final WakfuEffect effect) {
        return effect.getActionsToExecuteOnApplication().hasNext() || effect.getActionsToExecuteOnExecution().hasNext();
    }
    
    static {
        m_logger = Logger.getLogger((Class)OtherPlayerItemEffectsApplier.class);
    }
}
