package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class HasAnotherSameEquipment extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public HasAnotherSameEquipment(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasAnotherSameEquipment.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionContent instanceof Item)) {
            return -1;
        }
        if (!(criterionUser instanceof BasicCharacterInfo)) {
            return -1;
        }
        final Item item = (Item)criterionContent;
        final BasicCharacterInfo character = (BasicCharacterInfo)criterionUser;
        final int referenceId = item.getReferenceId();
        final long uniqueId = item.getUniqueId();
        boolean hasSameItem = this.hasSameItem(character, referenceId, uniqueId);
        if (hasSameItem) {
            return 0;
        }
        final AbstractReferenceItem refItem = item.getReferenceItem();
        if (refItem.getMetaType() == ItemMetaType.SUB_META_ITEM) {
            hasSameItem = this.hasSameMetaItem(character, uniqueId, refItem);
        }
        if (hasSameItem) {
            return 0;
        }
        return -1;
    }
    
    private boolean hasSameMetaItem(final BasicCharacterInfo character, final long uniqueId, final AbstractReferenceItem refItem) {
        final int metaId = refItem.getMetaId();
        final IMetaItem metaItem = MetaItemManager.INSTANCE.get(metaId);
        if (metaItem == null) {
            return false;
        }
        final int[] subIds = metaItem.getSubIds();
        for (int i = 0; i < subIds.length; ++i) {
            final int subId = subIds[i];
            if (this.hasSameItem(character, subId, uniqueId)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasSameItem(final BasicCharacterInfo character, final int referenceId, final long uniqueId) {
        boolean hasSameItem = false;
        final ArrayList<Item> sameItems = ((ArrayInventoryWithoutCheck<Item, R>)character.getEquipmentInventory()).getAllWithReferenceId(referenceId);
        for (final Item sameItem : sameItems) {
            if (sameItem.getUniqueId() != uniqueId) {
                hasSameItem = true;
            }
        }
        return hasSameItem;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_ANOTHER_SAME_EQUIPMENT;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
