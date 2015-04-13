package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;

public class IsSelectedCreatureAvailable extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    
    public IsSelectedCreatureAvailable(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsSelectedCreatureAvailable.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null) {
            throw new CriteriaExecutionException("On cherche les summons d'un user null");
        }
        if (!(criterionUser instanceof SymbioticCharacter)) {
            throw new CriteriaExecutionException("On cherche les summons d'un user qui n'a pas de symbiote");
        }
        final AbstractSymbiot symb = ((SymbioticCharacter)criterionUser).getSymbiot();
        if (symb == null) {
            throw new CriteriaExecutionException("On cherche les summons d'un user qui n'a pas de symbiote");
        }
        if (symb.getCurrentCreatureParameters() == null) {
            return -1;
        }
        if (symb.isAvailable(symb.getCurrentCreatureIndex())) {
            return 0;
        }
        return -2;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_SELECTED_CREATURE_SUMMONED;
    }
    
    static {
        (signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
