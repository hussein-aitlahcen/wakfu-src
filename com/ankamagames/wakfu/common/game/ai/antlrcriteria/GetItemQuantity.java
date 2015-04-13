package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class GetItemQuantity extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    private ArrayList<NumericalValue> m_ids;
    private NumberList m_idsalt;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetItemQuantity.SIGNATURES;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetItemQuantity(final List<ParserObject> args) {
        super();
        if (this.checkType(args) == 0) {
            if (args.get(0).getType() == ParserType.NUMBERLIST) {
                this.m_ids = null;
                this.m_idsalt = args.get(0);
            }
            else {
                this.m_ids = new ArrayList<NumericalValue>();
                this.m_idsalt = null;
                for (final ParserObject obj : args) {
                    final NumericalValue temp = (NumericalValue)obj;
                    this.m_ids.add(temp);
                }
            }
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicCharacterInfo user;
        if (criterionUser == null) {
            if (!(criterionContent instanceof PlayerTriggered)) {
                throw new CriteriaExecutionException("on cherche l'\u00e9quipement d'un user null");
            }
            user = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        else {
            if (!(criterionUser instanceof BasicCharacterInfo)) {
                throw new CriteriaExecutionException("on cherche l'\u00e9quipement d'autre chose qu'un character");
            }
            user = (BasicCharacterInfo)criterionUser;
        }
        if (user == null) {
            return 0L;
        }
        final ItemEquipment equip = user.getEquipmentInventory();
        final AbstractBagContainer bags = user.getBags();
        if (bags == null) {
            return 0L;
        }
        final TLongArrayList itemIds = this.getItemIds(criterionUser, criterionTarget, criterionContent, criterionContext);
        int count = 0;
        for (int i = 0; i < itemIds.size(); ++i) {
            final int refId = (int)itemIds.get(i);
            final TLongObjectIterator<AbstractBag> bagsIterator = bags.getBagsIterator();
            while (bagsIterator.hasNext()) {
                bagsIterator.advance();
                final AbstractBag bag = bagsIterator.value();
                final ArrayList<Item> ids = bag.getInventory().getAllWithReferenceId(refId);
                if (ids != null) {
                    for (final Item item : ids) {
                        count += item.getQuantity();
                    }
                }
            }
            final ArrayList<Item> ids = ((ArrayInventoryWithoutCheck<Item, R>)equip).getAllWithReferenceId(refId);
            if (ids != null) {
                for (final Item item2 : ids) {
                    count += item2.getQuantity();
                }
            }
        }
        if (user instanceof InventoryUser) {
            final QuestInventory inventory = (QuestInventory)((InventoryUser)user).getInventory(InventoryType.QUEST);
            for (int j = 0; j < itemIds.size(); ++j) {
                final QuestItem item3 = inventory.getItem((int)itemIds.get(j));
                if (item3 != null) {
                    count += item3.getQuantity();
                }
            }
        }
        return count;
    }
    
    public TLongArrayList getItemIds(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        TLongArrayList itemIds;
        if (this.m_idsalt != null) {
            itemIds = this.m_idsalt.getValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        }
        else {
            itemIds = new TLongArrayList();
            for (int i = 0; i < this.m_ids.size(); ++i) {
                final long refId = this.m_ids.get(i).getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
                itemIds.add(refId);
            }
        }
        return itemIds;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETITEMQUANTITY;
    }
    
    static {
        SIGNATURES = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBERLIST };
        GetItemQuantity.SIGNATURES.add(sig);
    }
}
