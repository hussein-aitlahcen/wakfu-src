package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetSatisfactionLevel extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetSatisfactionLevel.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetSatisfactionLevel(final ArrayList<ParserObject> args) {
        super();
        this.target = (this.checkType(args) == 0 && args.get(0).getValue().equalsIgnoreCase("target"));
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContent instanceof SatisfactionManager) {
            return this.getSign() * ((SatisfactionManager)criterionContent).getGlobalSatisfaction().getId();
        }
        ProtectorBase protector;
        if (criterionUser instanceof ProtectorBase) {
            protector = (ProtectorBase)criterionUser;
        }
        else {
            final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(this.target, criterionUser, criterionTarget, criterionContent, criterionContext);
            if (user == null) {
                throw new CriteriaExecutionException("On essaie de r\u00e9cup\u00e9rer la satisfaction d'une cible qui n'est pas un protecteur");
            }
            protector = ((user instanceof BasicCharacterInfo) ? ((BasicCharacterInfo)user).getProtector() : user);
        }
        final int satisfactionLevel = (protector != null) ? protector.getSatisfactionLevel().getId() : 0;
        return this.getSign() * satisfactionLevel;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_SATISFACTION_LEVEL;
    }
    
    static {
        GetSatisfactionLevel.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetSatisfactionLevel.signatures.add(sig);
        sig = new ParserType[0];
        GetSatisfactionLevel.signatures.add(sig);
    }
}
