package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;

public class CharacterHasSummons extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    private boolean targetId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return CharacterHasSummons.signatures;
    }
    
    public CharacterHasSummons(final ArrayList<ParserObject> args) {
        super();
        if (this.checkType(args) == 0) {
            final StringObject temp = args.get(0);
            if (temp.getValue().trim().equalsIgnoreCase("target")) {
                this.targetId = true;
            }
            else {
                this.targetId = false;
            }
        }
        else {
            this.targetId = false;
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.targetId, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (!(targetCharacter instanceof SymbioticCharacter)) {
            return -1;
        }
        final AbstractSymbiot symb = ((SymbioticCharacter)targetCharacter).getSymbiot();
        if (symb == null) {
            return -1;
        }
        for (byte i = 0; i < symb.getMaximumCreatures(); ++i) {
            if (symb.getCreatureParametersFromIndex(i) != null) {
                return 0;
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HASSUMMONS;
    }
    
    static {
        signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        CharacterHasSummons.signatures.add(sig);
        sig = new ParserType[0];
        CharacterHasSummons.signatures.add(sig);
    }
}
