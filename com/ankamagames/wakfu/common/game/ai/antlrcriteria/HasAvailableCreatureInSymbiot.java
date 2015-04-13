package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;

public class HasAvailableCreatureInSymbiot extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    
    public HasAvailableCreatureInSymbiot(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasAvailableCreatureInSymbiot.signatures;
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
        final int creaturesCount = symb.getMaximumCreatures();
        for (byte i = 0; i < creaturesCount; ++i) {
            if (symb.isAvailable(i)) {
                return 0;
            }
        }
        return -2;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_AVAILABLE_CREATURE_IN_SYMBIOT;
    }
    
    static {
        signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        HasAvailableCreatureInSymbiot.signatures.add(sig);
    }
}
