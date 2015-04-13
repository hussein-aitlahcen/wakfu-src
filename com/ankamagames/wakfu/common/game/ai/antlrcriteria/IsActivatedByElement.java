package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class IsActivatedByElement extends FunctionCriterion
{
    private int m_elementId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsActivatedByElement.signatures;
    }
    
    public IsActivatedByElement() {
        super();
    }
    
    public IsActivatedByElement(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_elementId = (int)args.get(0).getLongValue(null, null, null, null);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1;
        }
        if (fight.getSpellCaster() == null) {
            return -1;
        }
        if (fight.getSpellCaster().getSpellElement() == this.m_elementId) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ACTIVATED_BY_ELEMENT;
    }
    
    static {
        IsActivatedByElement.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsActivatedByElement.signatures.add(sig);
    }
}
