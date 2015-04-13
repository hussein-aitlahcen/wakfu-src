package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class HasWeaponType extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private List<NumericalValue> m_types;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasWeaponType.signatures;
    }
    
    public HasWeaponType(final ArrayList<ParserObject> args) {
        super();
        if (this.checkType(args) == 0) {
            this.m_types = new ArrayList<NumericalValue>();
            for (final ParserObject obj : args) {
                final NumericalValue temp = (NumericalValue)obj;
                this.m_types.add(temp);
            }
        }
    }
    
    public HasWeaponType() {
        super();
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null) {
            return -1;
        }
        if (!(criterionUser instanceof BasicCharacterInfo)) {
            return -1;
        }
        final ItemEquipment equip = ((BasicCharacterInfo)criterionUser).getEquipmentInventory();
        if (equip == null) {
            return -1;
        }
        try {
            if (this.checkAtPosition(criterionUser, criterionTarget, criterionContent, criterionContext, equip, EquipmentPosition.FIRST_WEAPON.getId())) {
                return 0;
            }
            if (this.checkAtPosition(criterionUser, criterionTarget, criterionContent, criterionContext, equip, EquipmentPosition.SECOND_WEAPON.getId())) {
                return 0;
            }
        }
        catch (Exception e) {
            return -1;
        }
        return -1;
    }
    
    private boolean checkAtPosition(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext, final ItemEquipment equip, final byte position) {
        final Item itemAtPos = ((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(position);
        return itemAtPos != null && itemAtPos.getReferenceItem() != null && this.paramsContainsType(itemAtPos.getReferenceItem().getItemType(), criterionUser, criterionTarget, criterionContent, criterionContext);
    }
    
    private boolean paramsContainsType(final AbstractItemType type, final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        for (AbstractItemType testedType = type; testedType != null; testedType = testedType.getParentType()) {
            for (byte i = 0; i < this.m_types.size(); ++i) {
                if ((short)this.m_types.get(i).getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) == testedType.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_WEAPON_TYPE;
    }
    
    static {
        HasWeaponType.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBERLIST };
        HasWeaponType.signatures.add(sig);
    }
}
