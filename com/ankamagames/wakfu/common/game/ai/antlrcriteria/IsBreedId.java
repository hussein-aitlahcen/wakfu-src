package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import gnu.trove.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public class IsBreedId extends FunctionCriterion
{
    private final TShortArrayList m_breedIds;
    private String m_targetType;
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsBreedId.SIGNATURES;
    }
    
    public TShortArrayList getBreedIds() {
        return this.m_breedIds;
    }
    
    public boolean isTarget() {
        return "target".equals(this.m_targetType);
    }
    
    public IsBreedId(final ArrayList<ParserObject> args) {
        super();
        this.m_breedIds = new TShortArrayList();
        this.checkType(args);
        boolean targetOrCasterDefined = false;
        for (final ParserObject arg : args) {
            if (!targetOrCasterDefined) {
                this.m_targetType = ((StringObject)arg).getValue();
                targetOrCasterDefined = true;
            }
            else {
                final short id = (short)((NumericalValue)arg).getLongValue(null, null, null, null);
                this.m_breedIds.add(id);
            }
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (this.m_breedIds.contains(targetCharacter.getBreed().getBreedId())) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_BREED_ID;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBERLIST });
    }
}
