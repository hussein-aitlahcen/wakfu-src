package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;

public class GetSpaceInSymbiot extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetSpaceInSymbiot.signatures;
    }
    
    public GetSpaceInSymbiot(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        long value = 0L;
        final BasicCharacterInfo casterCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(TargetType.CASTER, criterionUser, criterionTarget, criterionContext, criterionContent);
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(TargetType.TARGET, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (casterCharacter == null) {
            return -1L;
        }
        if (!(casterCharacter instanceof SymbioticCharacter)) {
            return -1L;
        }
        final AbstractSymbiot symb = casterCharacter.getSymbiot();
        value = symb.getSpaceForCreature(targetCharacter.getBreedId());
        return this.getSign() * value;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETSPACEINSYMBIOT;
    }
    
    static {
        GetSpaceInSymbiot.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        GetSpaceInSymbiot.signatures.add(sig);
    }
}
