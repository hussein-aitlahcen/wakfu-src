package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class IsActivatedBySpell extends FunctionCriterion
{
    private int m_spellId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsActivatedBySpell.signatures;
    }
    
    public IsActivatedBySpell() {
        super();
    }
    
    public IsActivatedBySpell(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_spellId = (int)args.get(0).getLongValue(null, null, null, null);
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
        if (fight.getSpellCaster().getSpellId() == this.m_spellId) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ACTIVATED_BY_SPELL;
    }
    
    static {
        IsActivatedBySpell.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsActivatedBySpell.signatures.add(sig);
    }
}
