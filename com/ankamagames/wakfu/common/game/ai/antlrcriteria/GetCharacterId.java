package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class GetCharacterId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private Target m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCharacterId.signatures;
    }
    
    public GetCharacterId(final ArrayList<ParserObject> args) {
        super();
        if (this.checkType(args) == 0) {
            this.m_target = ((args.size() == 0) ? Target.USER : Target.valueOf(args.get(0).getValue().toUpperCase()));
        }
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        long id = 0L;
        switch (this.m_target) {
            case USER: {
                if (criterionUser == null || !(criterionUser instanceof CriterionUser)) {
                    throw new CriteriaExecutionException("Le User du crit\u00e8re n'est pas un CriterionUser");
                }
                id = ((CriterionUser)criterionUser).getId();
                break;
            }
            case TARGET: {
                if (criterionTarget == null || !(criterionTarget instanceof CriterionUser)) {
                    throw new CriteriaExecutionException("La target du crit\u00e8re n'est pas un CriterionUser");
                }
                id = ((CriterionUser)criterionTarget).getId();
                break;
            }
            default: {
                throw new CriteriaExecutionException("Impossible de d\u00e9finir la cible du crit\u00e8re");
            }
        }
        return super.getSign() * id;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETCHARACTERID;
    }
    
    static {
        GetCharacterId.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetCharacterId.signatures.add(sig);
        sig = new ParserType[0];
        GetCharacterId.signatures.add(sig);
    }
    
    public enum Target
    {
        USER, 
        TARGET;
    }
}
