package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class HasEquipmentType extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private ArrayList<NumericalValue> m_types;
    private ArrayList<EquipmentPosition> m_slot;
    private boolean m_checkInventory;
    private boolean m_checkOnTarget;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasEquipmentType.signatures;
    }
    
    private EquipmentPosition getPositionFromString(final String string) {
        for (final EquipmentPosition pos : EquipmentPosition.values()) {
            if (pos.getEnumLabel().equalsIgnoreCase(string)) {
                return pos;
            }
        }
        return null;
    }
    
    public HasEquipmentType(final ArrayList<ParserObject> args) {
        super();
        this.m_checkOnTarget = false;
        if (this.checkType(args) == 0) {
            this.m_types = new ArrayList<NumericalValue>();
            for (final ParserObject obj : args) {
                final NumericalValue temp = (NumericalValue)obj;
                this.m_types.add(temp);
            }
            this.m_slot = null;
        }
        if (this.checkType(args) == 1) {
            this.m_types = new ArrayList<NumericalValue>();
            for (final ParserObject obj2 : args) {
                if (obj2.getType() == ParserType.NUMBER) {
                    final NumericalValue temp = (NumericalValue)obj2;
                    this.m_types.add(temp);
                }
                if (obj2.getType() == ParserType.STRING) {
                    final StringObject stringo = (StringObject)obj2;
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
                        this.m_slot.add(this.getPositionFromString(stringo.getValue()));
                    }
                }
            }
        }
    }
    
    private boolean isContained(final AbstractItemType type, final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        for (AbstractItemType testedType = type; testedType != null; testedType = testedType.getParentType()) {
            for (byte i = 0; i < this.m_types.size(); ++i) {
                if ((short)this.m_types.get(i).getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) == testedType.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isNotLinked(final EquipmentPosition pos, final AbstractItemType typ) {
        if (typ != null && pos != null) {
            final EquipmentPosition[] equipmentPositions = typ.getLinkedPositions();
            if (equipmentPositions != null && equipmentPositions.length > 0) {
                for (int i = 0; i < equipmentPositions.length; ++i) {
                    if (pos == equipmentPositions[i]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public TIntArrayList getTypes() {
        final TIntArrayList temp = new TIntArrayList();
        for (final NumericalValue type : this.m_types) {
            if (type.isConstant()) {
                temp.add((int)type.getLongValue(null, null, null, null));
            }
        }
        return temp;
    }
    
    public ArrayList<EquipmentPosition> getPos() {
        return this.m_slot;
    }
    
    public HasEquipmentType() {
        super();
        this.m_checkOnTarget = false;
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
        if (this.m_checkInventory) {
            final TLongObjectIterator<AbstractBag> bagsIterator = bags.getBagsIterator();
            while (bagsIterator.hasNext()) {
                bagsIterator.advance();
                final AbstractBag bag = bagsIterator.value();
                for (final Item item : bag.getInventory()) {
                    if (this.isContained(item.getReferenceItem().getItemType(), criterionUser, criterionTarget, criterionContent, criterionContext)) {
                        return 0;
                    }
                }
            }
        }
        if (this.m_slot == null) {
            for (final EquipmentPosition pos : EquipmentPosition.values()) {
                if (((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos.getId()) != null && this.isContained(((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos.getId()).getReferenceItem().getItemType(), criterionUser, criterionTarget, criterionContent, criterionContext)) {
                    return 0;
                }
            }
        }
        else {
            for (final EquipmentPosition pos2 : this.m_slot) {
                if (((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos2.getId()) != null && this.isNotLinked(pos2, ((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos2.getId()).getReferenceItem().getItemType()) && this.isContained(((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos2.getId()).getReferenceItem().getItemType(), criterionUser, criterionTarget, criterionContent, criterionContext)) {
                    return 0;
                }
            }
        }
        return -2;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HASEQUIPMENTTYPE;
    }
    
    static {
        HasEquipmentType.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBERLIST };
        HasEquipmentType.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBERLIST, ParserType.STRINGLIST };
        HasEquipmentType.signatures.add(sig);
    }
}
