package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetCraftLevel extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_craftId;
    
    public GetCraftLevel() {
        super();
    }
    
    public GetCraftLevel(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_craftId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCraftLevel.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CRAFT_LEVEL;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public int getCraftId() {
        if (this.m_craftId.isConstant() && this.m_craftId.isInteger()) {
            return (int)this.m_craftId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof CraftUser)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        final int craftId = (int)this.m_craftId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        return ((CraftUser)user).getCraftHandler().getLevel(craftId);
    }
    
    static {
        (GetCraftLevel.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER });
    }
}
