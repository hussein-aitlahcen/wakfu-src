package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class HasEquipmentId extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private ArrayList<NumericalValue> m_ids;
    private NumberList m_idsAlt;
    private ArrayList<EquipmentPosition> m_slot;
    private boolean m_checkInventory;
    private boolean m_checkOnTarget;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasEquipmentId.signatures;
    }
    
    private EquipmentPosition getPositionFromString(final String string) {
        for (final EquipmentPosition pos : EquipmentPosition.values()) {
            if (pos.getEnumLabel().equalsIgnoreCase(string)) {
                return pos;
            }
        }
        return null;
    }
    
    public HasEquipmentId(final ArrayList<ParserObject> args) {
        super();
        this.m_slot = null;
        this.m_checkInventory = false;
        this.m_checkOnTarget = false;
        final byte b = this.checkType(args);
        int offset;
        if (args.get(0).getType() == ParserType.NUMBERLIST) {
            this.m_ids = null;
            this.m_idsAlt = args.get(0);
            offset = 1;
        }
        else {
            this.m_ids = new ArrayList<NumericalValue>();
            int i;
            for (i = 0; i < args.size() && args.get(i).getType() == ParserType.NUMBER; ++i) {
                final NumericalValue temp = args.get(i);
                this.m_ids.add(temp);
            }
            offset = i;
        }
        if (b == 0) {
            this.m_slot = null;
        }
        else if (b == 1) {
            for (int i = offset; i < args.size() && args.get(i).getType() == ParserType.STRING; ++i) {
                final StringObject stringo = args.get(i);
                if (stringo.getValue().equalsIgnoreCase("inventory")) {
                    this.m_checkInventory = true;
                }
                else if (stringo.getValue().equalsIgnoreCase("target")) {
                    this.m_checkOnTarget = true;
                }
                else {
                    if (this.m_slot == null) {
                        this.m_slot = new ArrayList<EquipmentPosition>();
                    }
                    final EquipmentPosition slot = this.getPositionFromString(stringo.getValue());
                    if (slot == null) {
                        throw new IllegalArgumentException("Nom de slot qui ne correspond a rien " + stringo.getValue());
                    }
                    this.m_slot.add(slot);
                }
            }
        }
    }
    
    public HasEquipmentId() {
        super();
        this.m_slot = null;
        this.m_checkInventory = false;
        this.m_checkOnTarget = false;
    }
    
    public TIntArrayList getIds() {
        final TIntArrayList ids = new TIntArrayList();
        for (int i = 0, size = this.m_ids.size(); i < size; ++i) {
            final NumericalValue value = this.m_ids.get(i);
            if (value.isInteger()) {
                ids.add((int)value.getLongValue(null, null, null, null));
            }
        }
        return ids;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
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
            if (this.m_checkOnTarget && criterionTarget instanceof BasicCharacterInfo) {
                user = (BasicCharacterInfo)criterionTarget;
            }
            else {
                user = (BasicCharacterInfo)criterionUser;
            }
        }
        final ItemEquipment equip = user.getEquipmentInventory();
        if (equip == null) {
            return -2;
        }
        final AbstractBagContainer bags = user.getBags();
        TLongArrayList ids;
        if (this.m_ids != null) {
            ids = new TLongArrayList();
            for (byte i = 0; i < this.m_ids.size(); ++i) {
                ids.add(this.m_ids.get(i).getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
            }
        }
        else {
            ids = this.m_idsAlt.getValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        }
        if (this.m_checkInventory) {
            final TLongObjectIterator<AbstractBag> bagsIterator = bags.getBagsIterator();
            while (bagsIterator.hasNext()) {
                bagsIterator.advance();
                final AbstractBag bag = bagsIterator.value();
                for (final Item item : bag.getInventory()) {
                    if (item != null && ids.contains(item.getReferenceId())) {
                        return 0;
                    }
                }
            }
        }
        if (this.m_slot == null) {
            for (final EquipmentPosition pos : EquipmentPosition.values()) {
                if (((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos.getId()) != null && ids.contains(((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos.getId()).getReferenceItem().getId())) {
                    return 0;
                }
            }
        }
        else {
            for (final EquipmentPosition pos2 : this.m_slot) {
                if (((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos2.getId()) != null && ids.contains(((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos2.getId()).getReferenceItem().getId())) {
                    return 0;
                }
            }
        }
        return -2;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HASEQUIPMENTID;
    }
    
    static {
        HasEquipmentId.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBERLIST };
        HasEquipmentId.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBERLIST, ParserType.STRINGLIST };
        HasEquipmentId.signatures.add(sig);
    }
}
