package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

public class IsEquippedWithSet extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_setId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsEquippedWithSet.signatures;
    }
    
    public IsEquippedWithSet(final ArrayList<ParserObject> args) {
        super();
        final byte idx = this.checkType(args);
        if (idx != 0) {
            IsEquippedWithSet.m_logger.error((Object)("Param\u00e9trage de crit\u00e8re inconnu : " + this));
            return;
        }
        this.m_setId = args.get(0);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            throw new CriteriaExecutionException("User null");
        }
        final long setId = this.m_setId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final AbstractItemSet set = user.getItemSetManager().getItemSet((short)setId);
        final ItemEquipment equipment = user.getEquipmentInventory();
        boolean ok = true;
        final Iterator it = set.iterator();
        while (it.hasNext()) {
            if (!equipment.containsReferenceId(it.next().getId())) {
                ok = false;
                break;
            }
        }
        return ok ? 0 : 1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_EQUIPPED_WITH_SET;
    }
    
    static {
        (IsEquippedWithSet.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER });
    }
}
