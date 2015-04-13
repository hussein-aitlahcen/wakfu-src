package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetTerritoryId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean target;
    
    public GetTerritoryId(final ArrayList<ParserObject> args) {
        super();
        this.target = (this.checkType(args) == 0 && args.get(0).getValue().equalsIgnoreCase("target"));
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTerritoryId.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(this.target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            return -1L;
        }
        return super.getSign() * user.getCurrentTerritoryId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TERRITORY_ID;
    }
    
    static {
        GetTerritoryId.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetTerritoryId.signatures.add(sig);
        sig = new ParserType[0];
        GetTerritoryId.signatures.add(sig);
    }
}
