package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;

public class ReferenceItemFactory
{
    private static GemType getGemType(final ItemBinaryData bs) {
        final byte[] gemSlotType = bs.getGemSlotType();
        final GemType type = GemType.getById(gemSlotType[0]);
        return (type == null) ? GemType.NONE : type;
    }
    
    private static GemElementType getGemElementType(final ItemBinaryData bs) {
        final GemElementType gemstype = GemElementType.getById(bs.getGemElementType());
        return (gemstype == null) ? GemElementType.NONE : gemstype;
    }
    
    private static GemSlotsDefinition getGemSlotsDefinition(final ItemBinaryData bs) {
        final byte[] gemSlots = bs.getGemSlots();
        final int slotsCount = gemSlots.length;
        if (slotsCount == 0) {
            return GemSlotsDefinition.EMPTY;
        }
        final GemSlotDefinition[] slots = new GemSlotDefinition[slotsCount];
        for (int i = 0; i < slotsCount; ++i) {
            slots[i] = new GemSlotDefinition(GemType.getById(gemSlots[i]));
        }
        return new GemSlotsDefinition(slots);
    }
    
    private static ItemActionVisual getItemActionVisual(final ItemBinaryData bs) {
        return ItemActionVisual.getFromId(bs.getItemActionVisual());
    }
    
    private static ItemWorldUsageTarget getUsageTarget(final ItemBinaryData bs) {
        return ItemWorldUsageTarget.getFromId(bs.getWorldUsageTarget());
    }
    
    private static void fillItemBuilder(final AbstractReferenceItemBuilder builder, final ItemBinaryData bs, final Map<ActionsOnItem, SimpleCriterion> criterion) {
        builder.setId(bs.getId()).setSetId(bs.getItemSetId()).setGfxId(bs.getGfxId()).setFemaleGfxId((bs.getFemaleGfxId() == 0) ? bs.getGfxId() : bs.getFemaleGfxId()).setFloorGfxId(bs.getFloorGfxId()).setLevel(bs.getLevel()).setCriteria(criterion).setItemType(getItemType(bs)).setStackMaximumHeight(bs.getMaxStackHeight()).setUseCostAP(bs.getUseCostAP()).setUseCostMP(bs.getUseCostMP()).setUseCostWP(bs.getUseCostFP()).setUseRangeMin(bs.getUseRangeMin()).setUseRangeMax(bs.getUseRangeMax()).setUseTestFreeCell(bs.isUseTestFreeCell()).setUseTestNotBorderCell(bs.isUseTestNotBorderCell()).setUseTestLOS(bs.isUseTestLos()).setUseOnlyInLine(bs.isUseTestOnlyLine()).setRarity(getItemRarity(bs)).setBindType(getItemBindType(bs)).setType(getType(bs)).setGemType(getGemType(bs)).setGemElementType(getGemElementType(bs)).setGemsNum(bs.getGemNum());
    }
    
    private static AbstractItemType getItemType(final ItemBinaryData bs) {
        return ItemTypeManager.getInstance().getItemType(bs.getItemTypeId());
    }
    
    private static ItemType getType(final ItemBinaryData bs) {
        return ItemType.valueOf(bs.getGenerationType());
    }
    
    private static ItemRarity getItemRarity(final ItemBinaryData bs) {
        return ItemRarity.fromId(bs.getItemRarity());
    }
    
    private static ItemBindType getItemBindType(final ItemBinaryData bs) {
        final ItemBindType type = ItemBindType.getFromId(bs.getItemBindType());
        if (type != ItemBindType.NOT_BOUND && SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.FORCE_BIND_ON_PICKUP)) {
            return ItemBindType.fromParameters(true, type.isCharacter(), type.isShop());
        }
        return type;
    }
    
    public static MetaItem createMetaItem(final ItemBinaryData bs, final Map<ActionsOnItem, SimpleCriterion> criterion) {
        final MetaItemBuilder<MetaItemBuilder, MetaItem> builder = new MetaItemBuilder<MetaItemBuilder, MetaItem>();
        fillItemBuilder(builder, bs, criterion);
        builder.setSubs(bs.getSubMetaIds()).setActionVisual(getItemActionVisual(bs)).setUsageTarget(getUsageTarget(bs));
        return builder.getItem();
    }
    
    public static ReferenceItem createReferenceItem(final ItemBinaryData bs, final Map<ActionsOnItem, SimpleCriterion> criterion) {
        final ReferenceItemBuilder<ReferenceItemBuilder, ReferenceItem> builder = new ReferenceItemBuilder<ReferenceItemBuilder, ReferenceItem>();
        fillItemBuilder(builder, bs, criterion);
        builder.setActionVisual(getItemActionVisual(bs)).setUsageTarget(getUsageTarget(bs));
        return builder.getItem();
    }
    
    public static SubMetaItem createSubMetaItem(final MetaItem meta, final ItemBinaryData bs, final Map<ActionsOnItem, SimpleCriterion> criterion) {
        final SubMetaItemBuilder builder = new SubMetaItemBuilder();
        fillItemBuilder(builder, bs, criterion);
        ((ReferenceItemBuilder<SubMetaItemBuilder, I>)((ReferenceItemBuilder<SubMetaItemBuilder, I>)builder.setMetaParent(meta)).setActionVisual(getItemActionVisual(bs))).setUsageTarget(getUsageTarget(bs)).setMetaId(meta.getId());
        return ((AbstractReferenceItemBuilder<T, SubMetaItem>)builder).getItem();
    }
}
