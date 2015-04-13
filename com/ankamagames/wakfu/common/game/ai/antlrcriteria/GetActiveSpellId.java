package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class GetActiveSpellId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetActiveSpellId.signatures;
    }
    
    public GetActiveSpellId(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1L;
        }
        if (fight.getSpellCaster() == null) {
            return -1L;
        }
        return fight.getSpellCaster().getSpellId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_ACTIVE_SPELL_ID;
    }
    
    static {
        (GetActiveSpellId.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
