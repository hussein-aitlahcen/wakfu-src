package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public class StatueView extends WakfuClientInteractiveAnimatedElementSceneView
{
    private EquipmentHandler m_equipmentHandler;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_equipmentHandler = new EquipmentHandler((AnimatedElement)this);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_equipmentHandler = null;
    }
    
    @Override
    public void setInteractiveElement(@NotNull final ClientMapInteractiveElement element) {
        super.setInteractiveElement(element);
        ((EquipableDummy)element).setView(this);
    }
    
    private void updateAnm() {
        if (this.m_interactiveElement == null) {
            super.update();
            return;
        }
        final EquipableDummy equipableDummy = (EquipableDummy)this.m_interactiveElement;
        final int gfxId = this.getViewGfxId();
        super.setViewGfxId(gfxId);
        this.setAnimation(equipableDummy.getAnimName());
        this.setStaticAnimationKey(equipableDummy.getAnimName());
        final int itemAttachedRefId = equipableDummy.getItemAttachedRefId();
        final AbstractReferenceItem itemAttached = ReferenceItemManager.getInstance().getReferenceItem(itemAttachedRefId);
        if (itemAttached == null) {
            super.update();
            return;
        }
        if (this.updateAnmForItemSet(equipableDummy, itemAttached)) {
            super.update();
            return;
        }
        if (this.updateAnmForBadge(itemAttached)) {
            super.update();
        }
    }
    
    private boolean updateAnmForBadge(final AbstractReferenceItem setPack) {
        final GrowingArray<WakfuEffect> effects = (GrowingArray<WakfuEffect>)setPack.getEffects();
        for (final WakfuEffect effect : effects) {
            if (effect.getActionId() != RunningEffectConstants.STATE_APPLY.getId()) {
                continue;
            }
            final int stateId = effect.getParam(0, (short)0, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final State state = StateManager.getInstance().getState(stateId);
            if (state == null) {
                continue;
            }
            this.applyStateEffectsHmi(state);
        }
        return false;
    }
    
    private void applyStateEffectsHmi(final State state) {
        final ArrayList<WakfuEffect> stateEffects = state.getEffectsForLevelAsList((short)0);
        for (final WakfuEffect stateEffect : stateEffects) {
            final byte sex = ((EquipableDummy)this.getInteractiveElement()).getSex();
            final SimpleCriterion criterion = stateEffect.getConditions();
            if (criterion == null) {
                this.applyEffectHmi(stateEffect);
            }
            else {
                if (!(criterion instanceof IsSex) || ((IsSex)criterion).getSex() != sex) {
                    continue;
                }
                this.applyEffectHmi(stateEffect);
            }
        }
    }
    
    private void applyEffectHmi(final WakfuEffect stateEffect) {
        final List<HMIAction> actionsOrder = stateEffect.getActionsOrder();
        for (final HMIAction hmiAction : actionsOrder) {
            this.applyHmi(stateEffect, hmiAction);
        }
    }
    
    private boolean applyHmi(final WakfuEffect stateEffect, final HMIAction hmiAction) {
        if (hmiAction.getType() == HMIActionType.SKIN_PART_OTHER_CHANGE) {
            final HMIChangeSkinPartOtherAction changePartSkin = (HMIChangeSkinPartOtherAction)hmiAction;
            final ChangePartsList.Data data = new ChangePartsList.Data(stateEffect, changePartSkin.getAppearanceId(), changePartSkin.getWeight(), changePartSkin.getPartsToChange());
            data.apply(this, false);
            return true;
        }
        if (hmiAction.getType() == HMIActionType.COSTUME) {
            final HMICostumeAction action = (HMICostumeAction)hmiAction;
            final CostumeListData data2 = new CostumeListData(stateEffect, action.getAppearances(), action.getWeight(), action.getParticleId());
            data2.apply(this, false);
            return true;
        }
        return false;
    }
    
    private boolean updateAnmForItemSet(final EquipableDummy equipableDummy, final AbstractReferenceItem setPack) {
        final AbstractItemAction action = setPack.getItemAction();
        if (action == null) {
            super.update();
            return false;
        }
        if (action.getType() != ItemActionConstants.SPLIT_ITEM_SET) {
            super.update();
            return false;
        }
        final short itemSetId = ((SplitItemSetItemAction)action).getItemSetId();
        final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(itemSetId);
        if (itemSet == null) {
            StatueView.m_logger.error((Object)("Panoplie inconnue " + itemSetId));
            super.update();
            return false;
        }
        for (final ReferenceItem next : itemSet) {
            final AbstractItemType<ItemType> itemType = next.getItemType();
            final byte pos = itemType.getEquipmentPositions()[0].getId();
            this.m_equipmentHandler.applyEquipment(next, pos, equipableDummy.getSex());
        }
        return true;
    }
    
    @Override
    protected void selectAnimation(final byte state, final boolean useSpecificTransition, final TransitionModel transitionModel, final Direction8 direction) {
        this.setDirection(direction);
    }
    
    @Override
    public void update() {
        this.updateAnm();
    }
    
    @Override
    public void setViewGfxId(final int id) {
        if (this.m_interactiveElement == null) {
            super.setViewGfxId(id);
            return;
        }
        this.updateAnm();
    }
    
    public static class StatueViewFactory extends ObjectFactory<ClientInteractiveElementView>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public StatueView makeObject() {
            StatueView dimensionalBagView;
            try {
                dimensionalBagView = (StatueView)StatueViewFactory.m_pool.borrowObject();
                dimensionalBagView.setPool(StatueViewFactory.m_pool);
            }
            catch (Exception e) {
                StatueView.m_logger.error((Object)"Erreur lors de l'extraction d'un CharacterStatueViewFactory du pool", (Throwable)e);
                dimensionalBagView = new StatueView();
            }
            return dimensionalBagView;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<StatueView>() {
                @Override
                public StatueView makeObject() {
                    return new StatueView();
                }
            });
        }
    }
    
    private static class EquipmentHandler
    {
        private final TShortObjectHashMap<Anm> m_equipedPositions;
        private final AnimatedElement m_animatedElement;
        
        private EquipmentHandler(final AnimatedElement animatedElement) {
            super();
            this.m_equipedPositions = new TShortObjectHashMap<Anm>(EquipmentType.values().length);
            this.m_animatedElement = animatedElement;
        }
        
        public void unapplyEquipment(final short position) {
            final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(position);
            if (equipmentType == null) {
                return;
            }
            final Anm anm = this.m_equipedPositions.remove(position);
            if (anm != null) {
                this.unapplyEquipments(equipmentType, anm);
            }
        }
        
        private void unapplyEquipments(final EquipmentType equipmentType, final Anm value) {
            this.m_animatedElement.removeParts(value, equipmentType.m_linkageCrc);
        }
        
        public void applyEquipment(final int gfxId, final short position, final boolean hasGfx) {
            final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(position);
            if (equipmentType == null) {
                return;
            }
            this.unapplyEquipment(position);
            if (!hasGfx) {
                return;
            }
            try {
                final String equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
                final Anm equipment = AnimatedElement.loadEquipment(String.format(equipmentFileName, gfxId));
                this.m_animatedElement.applyParts(equipment, equipmentType.m_linkageCrc);
                this.m_equipedPositions.put(position, equipment);
            }
            catch (Exception e) {
                StatueView.m_logger.error((Object)("Erreur au chargement de l'\u00e9quipment : " + gfxId), (Throwable)e);
            }
        }
        
        public void applyEquipment(final AbstractReferenceItem item, final short position, final byte sex) {
            final int gfxId = (sex == 0) ? item.getGfxId() : item.getFemaleGfxId();
            this.applyEquipment(gfxId, position, item.getItemType().isVisibleInAnimations());
        }
        
        public void unapplyAllEquipments() {
            final TShortObjectIterator<Anm> iter = this.m_equipedPositions.iterator();
            while (iter.hasNext()) {
                iter.advance();
                final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(iter.key());
                this.unapplyEquipments(equipmentType, iter.value());
            }
            this.m_equipedPositions.clear();
        }
    }
}
