package com.ankamagames.wakfu.client.core.game.item.data;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;

public final class ReferenceItemUniqueInstanceLoader implements BinaryTransformer<BinaryData, AbstractReferenceItem>
{
    private static final Logger m_logger;
    
    @Override
    public AbstractReferenceItem<ItemType> loadFromBinaryForm(final BinaryData data) {
        assert data != null;
        final ItemBinaryData bs = (ItemBinaryData)data;
        AbstractReferenceItem<ItemType> item;
        if (bs.isMetaItem()) {
            item = loadMetaItem(bs);
        }
        else if (bs.isSubMetaItem()) {
            final MetaItem parent = MetaItemManager.INSTANCE.get(bs.getMetaId());
            if (parent == null) {
                ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Un submetaItem sans parent !!! Id=" + bs.getId()));
                return null;
            }
            item = loadSubMetaItem(bs, parent);
        }
        else {
            item = loadReferenceItem(bs);
        }
        MetaItemHelper.INSTANCE.addToAppropriateManager(item);
        return item;
    }
    
    private static MetaItem loadMetaItem(final ItemBinaryData bs) {
        final EnumMap<ActionsOnItem, SimpleCriterion> criterion = loadCriterion(bs);
        final MetaItem item = ReferenceItemFactory.createMetaItem(bs, criterion);
        loadItemProperties(bs, item);
        loadItemActions(bs, item);
        loadEffects(bs, item);
        return item;
    }
    
    private static SubMetaItem loadSubMetaItem(final ItemBinaryData bs, final MetaItem meta) {
        final EnumMap<ActionsOnItem, SimpleCriterion> criterion = loadCriterion(bs);
        final SubMetaItem item = ReferenceItemFactory.createSubMetaItem(meta, bs, criterion);
        loadItemProperties(bs, item);
        loadItemActions(bs, item);
        loadEffects(bs, item);
        return item;
    }
    
    private static ReferenceItem loadReferenceItem(final ItemBinaryData bs) {
        final EnumMap<ActionsOnItem, SimpleCriterion> criterion = loadCriterion(bs);
        final ReferenceItem item = ReferenceItemFactory.createReferenceItem(bs, criterion);
        loadItemProperties(bs, item);
        loadItemActions(bs, item);
        loadEffects(bs, item);
        return item;
    }
    
    private static void loadItemActions(final ItemBinaryData bs, final AbstractReferenceItem<ItemType> referenceItem) {
        final int itemId = bs.getId();
        for (final ItemBinaryData.ItemAction bsaction : bs.getActions()) {
            final ItemActionConstants actionConstants = ItemActionConstants.getFromId(bsaction.getActionTypeId());
            final AbstractClientItemAction action = ItemActionFactory.newAction(bsaction.getActionId(), actionConstants);
            if (action != null) {
                SimpleCriterion criterion = null;
                try {
                    criterion = CriteriaCompiler.compileBoolean(bsaction.getCriteria());
                }
                catch (Exception e) {
                    ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Erreur de compilation du crit\u00e8rre sur l'action id=" + bsaction.getActionTypeId() + " de l'item id=" + itemId), (Throwable)e);
                }
                action.parseParameters(bsaction.getActionParams());
                action.setCriterion(criterion);
                action.setMustConsumeItem(bsaction.isConsumeItemOnAction());
                action.setClientOnly(bsaction.isClientOnly());
                action.setStopMovement(bsaction.isStopMovement());
                action.setCanUseDuringOccupation(actionConstants.canBeUsedDuringOccupation());
                action.setHasScript(bsaction.hasScript());
                referenceItem.addAction(action);
                referenceItem.setUsableInWorld(true);
            }
            else {
                ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Chargement d'une action d'un type inconnu sur un item actionId=" + bsaction.getActionTypeId() + " referenceItemId=" + referenceItem.getId()));
            }
        }
    }
    
    private static void loadEffects(final ItemBinaryData bs, final AbstractReferenceItem<ItemType> referenceItem) {
        final TIntArrayList variablesEffects = (referenceItem.getMetaType() == ItemMetaType.META_ITEM) ? new TIntArrayList() : null;
        final StaticEffectBinaryData effectData = new StaticEffectBinaryData();
        for (final int effectId : bs.getEffectIds()) {
            try {
                if (BinaryDocumentManager.getInstance().getId(effectId, effectData)) {
                    if (AbstractReferenceItem.isVariableEffect(bs.isMetaItem(), effectData.isActiveParentType(), effectData.getActionId())) {
                        variablesEffects.add(effectId);
                    }
                    else {
                        final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
                        if (effect != null) {
                            referenceItem.addEffect(effect);
                        }
                        else {
                            ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Probl\u00e8me de chargement du ReferenceItem " + bs.getId()));
                        }
                    }
                }
            }
            catch (Exception e) {
                ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Exception au chargement de l'effet " + effectId), (Throwable)e);
            }
        }
        if (variablesEffects != null) {
            ((MetaItem)referenceItem).setVariablesEffects(variablesEffects.toNativeArray());
        }
    }
    
    private static void loadItemProperties(final ItemBinaryData bs, final AbstractReferenceItem<ItemType> referenceItem) {
        final int itemId = bs.getId();
        final int[] itemPropertyIds = bs.getItemProperties();
        for (int i = 0, size = itemPropertyIds.length; i < size; ++i) {
            final int propertyId = itemPropertyIds[i];
            final ItemProperty prop = ItemProperty.getProperty(propertyId);
            if (prop == null) {
                ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Impossible de trouver la propri\u00e9t\u00e9 " + propertyId + " d\u00e9finie pour l'item " + itemId));
            }
            else {
                referenceItem.addItemProperty(prop);
            }
        }
    }
    
    private static EnumMap<ActionsOnItem, SimpleCriterion> loadCriterion(final ItemBinaryData bs) {
        final int itemId = bs.getId();
        final EnumMap<ActionsOnItem, SimpleCriterion> actionCriterion = new EnumMap<ActionsOnItem, SimpleCriterion>(ActionsOnItem.class);
        final String[] criteriaString = bs.getCriteria();
        if (criteriaString != null) {
            for (int n = criteriaString.length / 2, i = 0; i < n; ++i) {
                final String act = criteriaString[2 * i];
                final String criteria = criteriaString[2 * i + 1];
                ActionsOnItem action;
                try {
                    action = ActionsOnItem.valueOf(act);
                }
                catch (IllegalArgumentException e) {
                    ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration d'une action : type d'action inconnu : '" + act + "' Item : " + itemId), (Throwable)e);
                    continue;
                }
                SimpleCriterion criterion;
                try {
                    criterion = CriteriaCompiler.compileBoolean(criteria);
                }
                catch (Exception e2) {
                    ReferenceItemUniqueInstanceLoader.m_logger.error((Object)("Erreur lors de la compilation du crit\u00e8re sur l'action " + action + " de l'item " + itemId), (Throwable)e2);
                    continue;
                }
                actionCriterion.put(action, criterion);
            }
        }
        return actionCriterion;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceItemUniqueInstanceLoader.class);
    }
}
