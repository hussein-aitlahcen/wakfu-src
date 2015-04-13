package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class IsAbandonning extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsAbandonning.signatures;
    }
    
    public IsAbandonning(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        final byte type = this.checkType(args);
        if (type == 1) {
            this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        return (targetCharacter != null && targetCharacter.isAbandonning()) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ABANDONNING;
    }
    
    static {
        IsAbandonning.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        IsAbandonning.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        IsAbandonning.signatures.add(sig);
    }
}
