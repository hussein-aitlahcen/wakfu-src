package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasCraft extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_playerId;
    private NumericalValue m_craftId;
    
    public HasCraft() {
        super();
    }
    
    public HasCraft(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_craftId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasCraft.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_CRAFT;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof CraftUser)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        final int craftId = (int)this.m_craftId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        return ((CraftUser)user).getCraftHandler().contains(craftId) ? 0 : -1;
    }
    
    public int getCraftId() {
        if (this.m_craftId.isInteger()) {
            return (int)this.m_craftId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    static {
        HasCraft.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        HasCraft.signatures.add(sig);
    }
}
